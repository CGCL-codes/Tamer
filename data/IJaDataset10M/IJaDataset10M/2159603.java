package se.kb.oai.ore;

import static org.junit.Assert.*;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ORETest {

    @Test
    public void testConstructor() throws URISyntaxException {
        ResourceMap map = new ResourceMap(new URI("http://test.kb.se/rem/"));
        assertEquals("http://test.kb.se/rem/#aggregation", map.getAggregation().getId().toString());
    }
}
