package de.huxhorn.sulky.codec;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlCodecTest {

    @Test
    public void test() {
        String obj = "Foo";
        XmlEncoder<String> encoder = new XmlEncoder<String>();
        XmlDecoder<String> decoder = new XmlDecoder<String>();
        byte[] encoded = encoder.encode(obj);
        String decoded = decoder.decode(encoded);
        assertEquals(obj, decoded);
    }
}
