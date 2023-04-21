package com.hyk.proxy.common.secure;

import java.nio.ByteBuffer;

/**
 *
 */
public class NoneSecurityService implements SecurityService {

    @Override
    public String getName() {
        return NAME;
    }

    public static final String NAME = "none";

    @Override
    public byte[] decrypt(byte[] value) {
        return value;
    }

    @Override
    public byte[] encrypt(byte[] value) {
        return value;
    }

    @Override
    public ByteBuffer decrypt(ByteBuffer value) {
        return value;
    }

    @Override
    public ByteBuffer encrypt(ByteBuffer value) {
        return value;
    }

    @Override
    public ByteBuffer[] decrypt(ByteBuffer[] value) {
        return value;
    }

    @Override
    public ByteBuffer[] encrypt(ByteBuffer[] value) {
        return value;
    }
}
