package com.flagstone.transform.action;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

public final class TableTest {

    private static List<String> list;

    @BeforeClass
    public static void initialize() {
        list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
    }

    private static final transient int TYPE = ActionTypes.TABLE;

    private transient Table fixture;

    private final transient byte[] encoded = new byte[] { (byte) TYPE, 0x08, 0x00, 0x03, 0x00, 0x41, 0x00, 0x42, 0x00, 0x43, 0x00 };

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForStringWithNull() {
        fixture = new Table();
        fixture.add(null);
    }

    @Test
    public void checkCopy() {
        fixture = new Table(list);
        final Table copy = fixture.copy();
        assertNotSame(fixture.getValues(), copy.getValues());
        assertEquals(fixture.toString(), copy.toString());
    }

    @Test
    public void encode() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final SWFEncoder encoder = new SWFEncoder(stream);
        final Context context = new Context();
        fixture = new Table(list);
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
        fixture = new Table(decoder);
        assertTrue(true);
        assertEquals(list, fixture.getValues());
    }
}
