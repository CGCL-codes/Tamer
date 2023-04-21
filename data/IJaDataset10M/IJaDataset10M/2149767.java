package edu.kds.asm.asmProgram;

import edu.kds.misc.BinaryNumber;

public abstract class RTypeInstruction extends MIPSInstruction {

    protected final int reg1, reg2, reg3;

    protected RTypeInstruction(String name, int reg1, int reg2, int reg3) {
        super(name);
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.reg3 = reg3;
    }

    protected abstract String getFunctField();

    public long getMachineCode() {
        String op = "000000";
        String rs = BinaryNumber.toBinString(reg2, 5);
        String rt = BinaryNumber.toBinString(reg3, 5);
        String rd = BinaryNumber.toBinString(reg1, 5);
        String shamt = "00000";
        String funct = getFunctField();
        try {
            return Long.parseLong(op + rs + rt + rd + shamt + funct, 2);
        } catch (NumberFormatException exc) {
            System.err.println("Error in generating machine code");
            exc.printStackTrace();
            return 0;
        }
    }

    public String toString() {
        return name + " " + MIPSRegisters.registerName(reg1) + ", " + MIPSRegisters.registerName(reg2) + ", " + MIPSRegisters.registerName(reg3);
    }
}
