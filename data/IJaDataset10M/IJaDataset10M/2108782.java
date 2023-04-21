package com.flagstone.transform.fillstyle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;
import com.flagstone.transform.datatype.Color;

public final class FocalGradientFillTest {

    private static transient List<Gradient> list = new ArrayList<Gradient>();

    static {
        list.add(new Gradient(1, new Color(2, 3, 4)));
        list.add(new Gradient(5, new Color(6, 7, 8)));
    }

    private transient FocalGradientFill fixture;

    private final transient byte[] encoded = new byte[] {};

    @Test
    @Ignore
    public void checkCopy() {
        final FocalGradientFill copy = fixture.copy();
        assertNotSame(fixture.getGradients(), copy.getGradients());
        assertEquals(fixture.toString(), copy.toString());
    }

    @Test
    @Ignore
    public void encode() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final SWFEncoder encoder = new SWFEncoder(stream);
        final Context context = new Context();
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
        decoder.readByte();
        fixture = new FocalGradientFill(decoder, context);
        assertTrue(true);
    }
}
