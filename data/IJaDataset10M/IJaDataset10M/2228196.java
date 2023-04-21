package com.amd.javalabs.tools.disasm.operands;

public class AbsoluteAddress implements Address {

    private long displacement;

    public long getDisplacement() {
        return displacement;
    }

    private long segment;

    public AbsoluteAddress(long segment, long disp) {
        this.displacement = disp;
        this.segment = segment;
    }

    public AbsoluteAddress(long disp) {
        this.displacement = disp;
        this.segment = 0;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (getSegment() != 0) {
            buf.append("0x");
            buf.append(Long.toHexString(getSegment()));
            buf.append(":");
        }
        buf.append("[");
        buf.append("0x");
        buf.append(Long.toHexString(getDisplacement()));
        buf.append("]");
        return buf.toString();
    }

    long getSegment() {
        return segment;
    }
}
