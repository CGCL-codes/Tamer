package edu.kds.asm.asmProgram;

import edu.kds.asm.asmProgram.trace.*;

public class MIPSRegisters {

    public static final int REGISTER_AMOUNT = 32;

    private long[] registers = new long[REGISTER_AMOUNT];

    public TraceAction writeRegister(int register, long value) {
        if (trace != null) trace.performingAction(new RegWriteAction(register, value));
        registers[register] = value;
        return new RegWriteAction(register, value);
    }

    public long readRegister(int register) {
        return registers[register];
    }

    public long getRegister(int register) {
        return registers[register];
    }

    public void reset() {
        for (int r = 0; r < REGISTER_AMOUNT; r++) {
            registers[r] = 0;
        }
    }

    private Trace trace;

    void setTrace(Trace t) {
        trace = t;
    }

    public static int registerNumber(String reg) {
        for (int i = 0; i < regNames.length; i++) {
            if (reg.equalsIgnoreCase(regNames[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String registerName(int reg) {
        return "$" + regNames[reg];
    }

    private static final String[] regNames = { "zero", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "k0", "k1", "gp", "sp", "fp", "ra" };
}
