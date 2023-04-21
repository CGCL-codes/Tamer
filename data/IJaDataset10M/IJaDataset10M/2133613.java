package com.flagstone.transform;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

public final class FreeCodingTest extends AbstractCodingTest {

    @Test
    public void checkFreeLengthForEncoding() throws IOException {
        final Free object = new Free(1);
        final byte[] binary = new byte[] { (byte) 0xC2, 0x00, 0x01, 0x00 };
        assertEquals(CALCULATED_LENGTH, binary.length, prepare(object));
    }

    @Test
    public void checkFreeIsEncoded() throws IOException {
        final Free object = new Free(1);
        final byte[] binary = new byte[] { (byte) 0xC2, 0x00, 0x01, 0x00 };
        assertArrayEquals(NOT_ENCODED, binary, encode(object));
    }

    @Test
    public void checkFreeIsDecoded() throws IOException {
        final byte[] binary = new byte[] { (byte) 0xC2, 0x00, 0x01, 0x00 };
        Free object = (Free) decodeMovieTag(binary);
        assertEquals(NOT_DECODED, 1, object.getIdentifier());
    }

    @Test
    public void checkExtendedFreeIsDecoded() throws IOException {
        final byte[] binary = new byte[] { (byte) 0xFF, 0x00, 0x02, 0x00, 0x00, 0x00, 0x01, 0x00 };
        Free object = (Free) decodeMovieTag(binary);
        assertEquals(NOT_DECODED, 1, object.getIdentifier());
    }
}
