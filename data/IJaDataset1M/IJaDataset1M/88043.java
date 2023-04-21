package com.dukesoftware.utils.data;

public final class BitSet {

    private static final int MAXLEN = 32;

    private int bits = 0;

    public void setAllFalse() {
        bits = 0;
    }

    public boolean isTrue(int i) {
        return (bits >> i & 1) != 0;
    }

    public void setTrue(int i) {
        bits = bits | 1 << i;
    }

    public boolean acceptable(int i) {
        return i < MAXLEN && i >= 0;
    }
}
