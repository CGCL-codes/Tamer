package rpg.organiser.core.xml;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import rpg.organiser.api.ILocation;
import rpg.organiser.core.Location;
import rpg.organiser.test.TestHelper;

public class LocationXmlDecoderEncoderTest {

    private LocationXmlDecoder decoder;

    private LocationXmlEncoder encoder;

    private static final String TEMPLATE = "" + "<location>" + "  <id>0</id>" + "  <name>name</name>" + "  <classification>classification</classification>" + "</location>";

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalidXml() {
        decoder.decode("<asd>");
    }

    @Test
    public void testEncode() {
        ILocation location = new Location(0, "name", "classification");
        String encoded = encoder.encode(location);
        assertNotNull(encoded);
        TestHelper.xmlEquals(TEMPLATE, encoded);
    }

    @Test
    public void testDecoded() {
        ILocation decoded = decoder.decode(TEMPLATE);
        assertNotNull(decoded);
        assertEquals(0, decoded.getId());
        assertEquals("name", decoded.getName());
        assertEquals("classification", decoded.getClassification());
    }

    @Test
    public void testEncodedDecodedEncoded() {
        ILocation location = new Location(0, "name", "classification");
        String encoded = encoder.encode(location);
        ILocation decoded = decoder.decode(encoded);
        String encoded2 = encoder.encode(decoded);
        assertEquals(location, decoded);
        assertEquals(encoded, encoded2);
    }

    @Before
    public void setup() {
        encoder = new LocationXmlEncoder();
        decoder = new LocationXmlDecoder();
    }
}
