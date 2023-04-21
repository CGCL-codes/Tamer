package com.flagstone.transform.shape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

public final class DefineShape3Test {

    private transient DefineShape3 fixture;

    private final transient byte[] encoded = new byte[] { 0x06, 0x01, 0x01, 0x00, 0x02, 0x00, 0x06, 0x50 };

    private final transient byte[] extended = new byte[] { 0x7F, 0x01, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x06, 0x50 };

    @Test
    @Ignore
    public void checkCopy() {
        final DefineShape3 copy = fixture.copy();
        assertNotSame(fixture, copy);
    }

    @Test
    @Ignore
    public void encodeCoordTransform() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final SWFEncoder encoder = new SWFEncoder(stream);
        final Context context = new Context();
        assertEquals(encoded.length, fixture.prepareToEncode(context));
        fixture.encode(encoder, context);
    }

    @Test
    @Ignore
    public void decode() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(encoded);
        final SWFDecoder decoder = new SWFDecoder(stream);
        final Context context = new Context();
        fixture = new DefineShape3(decoder, context);
        assertTrue(true);
    }

    @Test
    @Ignore
    public void decodeExtended() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(extended);
        final SWFDecoder decoder = new SWFDecoder(stream);
        final Context context = new Context();
        fixture = new DefineShape3(decoder, context);
        assertTrue(true);
    }
}
