package com.st.rrd.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

class CDPBlock implements Externalizable {

    private double value;

    private long unknownDataPoints;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getUnknownDataPoints() {
        return unknownDataPoints;
    }

    public void setUnknownDataPoints(long unknownDataPoints) {
        this.unknownDataPoints = unknownDataPoints;
    }

    @Override
    public String toString() {
        return "CDPBlock [unknownDataPoints=" + unknownDataPoints + ", value=" + value + "]";
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = in.readDouble();
        unknownDataPoints = in.readLong();
        int skippedBytes = in.skipBytes(80);
        if (skippedBytes != 64) {
            throw new IOException("invalid bytes were skipped. Expected: 64 Got: " + skippedBytes);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    }
}
