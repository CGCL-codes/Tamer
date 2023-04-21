package com.flagstone.transform.action;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

public final class WaitForFrameTest {

    private static final transient int TYPE = ActionTypes.WAIT_FOR_FRAME;

    private static final transient int FRAME = 1;

    private static final transient int COUNT = 2;

    private transient WaitForFrame fixture;

    private final transient byte[] encoded = new byte[] { (byte) TYPE, 0x03, 0x00, 0x01, 0x00, 0x02 };

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForFrameNumberWithLowerBound() {
        fixture = new WaitForFrame(0, COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForFrameNumberWithUpperBound() {
        fixture = new WaitForFrame(65536, COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForActionCountWithLowerBound() {
        fixture = new WaitForFrame(FRAME, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForActionCountWithUpperBound() {
        fixture = new WaitForFrame(FRAME, 256);
    }

    @Test
    public void checkCopy() {
        fixture = new WaitForFrame(FRAME, COUNT);
        final WaitForFrame copy = fixture.copy();
        assertSame(fixture, copy);
        assertEquals(fixture.toString(), copy.toString());
    }

    @Test
    public void encode() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final SWFEncoder encoder = new SWFEncoder(stream);
        final Context context = new Context();
        fixture = new WaitForFrame(FRAME, COUNT);
        assertEquals(encoded.length, fixture.prepareToEncode(context));
        fixture.encode(encoder, context);
        encoder.flush();
        assertArrayEquals(encoded, stream.toByteArray());
    }

    @Test
    public void decode() throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(encoded);
        final SWFDecoder decoder = new SWFDecoder(stream);
        decoder.readByte();
        fixture = new WaitForFrame(decoder);
        assertTrue(true);
        assertEquals(FRAME, fixture.getFrameNumber());
        assertEquals(COUNT, fixture.getActionCount());
    }
}
