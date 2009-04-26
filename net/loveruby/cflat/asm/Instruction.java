package net.loveruby.cflat.asm;

public class Instruction extends Assembly {
    protected String mnemonic;
    protected String suffix;
    protected AsmOperand[] operands;
    protected boolean needRelocation;

    public Instruction(String mnemonic) {
        this(mnemonic, "", new AsmOperand[0], false);
    }

    public Instruction(String mnemonic, String suffix, AsmOperand a1) {
        this(mnemonic, suffix, new AsmOperand[] { a1 }, false);
    }

    public Instruction(String mnemonic, String suffix,
                       AsmOperand a1, AsmOperand a2) {
        this(mnemonic, suffix, new AsmOperand[] { a1, a2 }, false);
    }

    public Instruction(String mnemonic, String suffix,
                       AsmOperand a1, AsmOperand a2, boolean reloc) {
        this(mnemonic, suffix, new AsmOperand[] { a1, a2 }, reloc);
    }

    public Instruction(String mnemonic, String suffix, AsmOperand[] operands, boolean reloc) {
        this.mnemonic = mnemonic;
        this.suffix = suffix;
        this.operands = operands;
        this.needRelocation = reloc;
    }

    public Instruction build(String mnemonic, AsmOperand o1) {
        return new Instruction(mnemonic, this.suffix,
                new AsmOperand[] { o1 }, needRelocation);
    }

    public Instruction build(String mnemonic, AsmOperand o1, AsmOperand o2) {
        return new Instruction(mnemonic, this.suffix,
                new AsmOperand[] { o1, o2 }, needRelocation);
    }

    public boolean isInstruction() {
        return true;
    }

    public String mnemonic() {
        return this.mnemonic;
    }

    public boolean isJumpInstruction() {
        return mnemonic.equals("jmp")
                || mnemonic.equals("jz")
                || mnemonic.equals("jne")
                || mnemonic.equals("je")
                || mnemonic.equals("jne");
    }

    /**
     * Returns the number of operands.
     */
    public int numOperands() {
        return this.operands.length;
    }

    public AsmOperand operand1() {
        return this.operands[0];
    }

    public AsmOperand operand2() {
        return this.operands[1];
    }

    /**
     * Extract jump destination label from operands.
     */
    public Symbol jmpDestination() {
        DirectMemoryReference ref = (DirectMemoryReference)operands[0];
        return (Symbol)ref.value();
    }

    public void collectStatistics(AsmStatistics stats) {
        stats.instructionUsed(mnemonic);
        for (int i = 0; i < operands.length; i++) {
            operands[i].collectStatistics(stats);
        }
    }

    public void fixStackOffset(long diff) {
        if (!needRelocation) return;
        for (int i = 0; i < operands.length; i++) {
            operands[i].fixStackOffset(diff);
        }
    }

    public String toSource(SymbolTable table) {
        StringBuffer buf = new StringBuffer();
        buf.append("\t");
        buf.append(mnemonic + suffix);
        String sep = "\t";
        for (int i = 0; i < operands.length; i++) {
            buf.append(sep); sep = ", ";
            buf.append(operands[i].toSource(table));
        }
        return buf.toString();
    }

    public String toString() {
        return "#<Insn " + mnemonic + ">";
    }
}
