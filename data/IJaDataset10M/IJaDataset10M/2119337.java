package com.mindbright.jce.crypto.spec;

import java.math.BigInteger;
import com.mindbright.jca.security.spec.KeySpec;

public class DHPrivateKeySpec extends DHParamsImpl implements KeySpec {

    protected BigInteger x;

    public DHPrivateKeySpec(BigInteger x, BigInteger p, BigInteger g) {
        super(p, g);
        this.x = x;
    }

    public BigInteger getX() {
        return x;
    }
}
