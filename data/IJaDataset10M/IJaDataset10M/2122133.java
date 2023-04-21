package com.flagstone.transform.linestyle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;
import com.flagstone.transform.datatype.Color;

public final class MorphLineStyle2Test {

    private final transient int startWidth = 1;

    private final transient Color startColor = new Color(2, 3, 4, 5);

    private final transient int endWidth = 6;

    private final transient Color endColor = new Color(7, 8, 9, 10);

    private transient MorphLineStyle2 fixture;

    private final transient byte[] encoded = new byte[] { 0x01, 0x00, 0x06, 0x00, 0x02, 0x03, 0x04, 0x05, 0x07, 0x08, 0x09, 0x0A };

    @Test
    @Ignore
    public void checkCopy() {
        fixture = new MorphLineStyle2(startWidth, endWidth, startColor, endColor);
        final MorphLineStyle2 copy = fixture.copy();
        assertNotSame(fixture, copy);
        assertSame(fixture.getStartColor(), copy.getStartColor());
        assertSame(fixture.getEndColor(), copy.getEndColor());
        assertEquals(fixture.toString(), copy.toString());
    }

    @Test
    @Ignore
    public void encode() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final SWFEncoder encoder = new SWFEncoder(stream);
        final Context context = new Context();
        context.put(Context.TRANSPARENT, 1);
        fixture = new MorphLineStyle2(startWidth, endWidth, startColor, endColor);
        assertEquals(encoded.length, fixture.prepareToEncode(context));
        fixture.encode(encoder, context);
        assertArrayEquals(encoded, stream.toByteArray());
    }

    @Test
    @Ignore
    public void decode() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(encoded);
        final SWFDecoder decoder = new SWFDecoder(stream);
        final Context context = new Context();
        context.put(Context.TRANSPARENT, 1);
        fixture = new MorphLineStyle2(decoder, context);
        assertTrue(true);
        assertEquals(startWidth, fixture.getStartWidth());
        assertEquals(endWidth, fixture.getEndWidth());
        assertEquals(startColor.getRed(), fixture.getStartColor().getRed());
        assertEquals(startColor.getGreen(), fixture.getStartColor().getGreen());
        assertEquals(startColor.getBlue(), fixture.getStartColor().getBlue());
        assertEquals(startColor.getAlpha(), fixture.getStartColor().getAlpha());
        assertEquals(endColor.getRed(), fixture.getEndColor().getRed());
        assertEquals(endColor.getGreen(), fixture.getEndColor().getGreen());
        assertEquals(endColor.getBlue(), fixture.getEndColor().getBlue());
        assertEquals(endColor.getAlpha(), fixture.getEndColor().getAlpha());
    }
}
