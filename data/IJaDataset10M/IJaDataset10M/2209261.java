package joeq.Scheduler;

import joeq.Memory.CodeAddress;
import joeq.Memory.StackAddress;

public abstract class jq_RegisterState {

    public abstract static class Factory {

        public abstract jq_RegisterState create();
    }

    public static Factory factory;

    public static jq_RegisterState create() {
        return factory.create();
    }

    public abstract CodeAddress getEip();

    public abstract void setEip(CodeAddress a);

    public abstract StackAddress getEsp();

    public abstract void setEsp(StackAddress a);

    public abstract StackAddress getEbp();

    public abstract void setEbp(StackAddress a);

    public abstract void setControlWord(int x);

    public abstract void setStatusWord(int x);

    public abstract void setTagWord(int x);

    public abstract void setContextFlags(int x);

    public static final int CONTEXT_i386 = 0x00010000;

    public static final int CONTEXT_CONTROL = (CONTEXT_i386 | 0x00000001);

    public static final int CONTEXT_INTEGER = (CONTEXT_i386 | 0x00000002);

    public static final int CONTEXT_SEGMENTS = (CONTEXT_i386 | 0x00000004);

    public static final int CONTEXT_FLOATING_POINT = (CONTEXT_i386 | 0x00000008);

    public static final int CONTEXT_DEBUG_REGISTERS = (CONTEXT_i386 | 0x00000010);

    public static final int CONTEXT_EXTENDED_REGISTERS = (CONTEXT_i386 | 0x00000020);

    public static final int CONTEXT_FULL = (CONTEXT_CONTROL | CONTEXT_INTEGER | CONTEXT_SEGMENTS);
}
