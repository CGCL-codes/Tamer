package org.privasphere.crypto;

public class FF extends Func3 {

    public long Func(long b, long c, long d) {
        return (d ^ (b & (c ^ d)));
    }
}
