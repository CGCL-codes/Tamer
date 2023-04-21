package com.google.code.ebmlviewer.core;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.testng.annotations.Test;
import static oe.assertions.Assertions.assertThat;
import static oe.assertions.Predicates.isEqualTo;

public class UnsignedIntegerTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void decodeWithNullBufferFails() {
        EbmlDecoder decoder = new EbmlDecoder();
        decoder.decodeUnsignedInteger(null, 0);
    }

    @Test(expectedExceptions = BufferUnderflowException.class)
    public void decodeWithBufferUnderflow() {
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.limit(7);
        int position = buffer.position();
        try {
            decoder.decodeUnsignedInteger(buffer, 8);
        } finally {
            assertThat(buffer.position(), isEqualTo(position));
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void encodeWithNullBufferFails() {
        EbmlEncoder encoder = new EbmlEncoder();
        encoder.encodeUnsignedInteger(null, 0L, 8);
    }

    @Test(expectedExceptions = BufferOverflowException.class)
    public void encodeWithBufferOverflow() {
        EbmlEncoder encoder = new EbmlEncoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.limit(7);
        int position = buffer.position();
        try {
            encoder.encodeUnsignedInteger(buffer, 0L, 8);
        } finally {
            assertThat(buffer.position(), isEqualTo(position));
        }
    }

    @Test
    public void roundtripPositive() {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        long write = 0x1122334455667788L;
        for (int i = 8; i >= 0; i--) {
            buffer.clear();
            encoder.encodeUnsignedInteger(buffer, write, i);
            buffer.flip();
            long read = decoder.decodeUnsignedInteger(buffer, i);
            assertThat(read, isEqualTo(write));
            write >>= 8;
        }
    }

    @Test
    public void roundtripNegative() {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        long write = 0x8877665544332211L;
        for (int i = 8; i >= 0; i--) {
            buffer.clear();
            encoder.encodeUnsignedInteger(buffer, write, i);
            buffer.flip();
            long read = decoder.decodeUnsignedInteger(buffer, i);
            assertThat(read, isEqualTo(write));
            write >>>= 8;
        }
    }
}
