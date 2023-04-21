package eu.annocultor.triple;

import junit.framework.TestCase;
import org.junit.Test;
import eu.annocultor.context.Concepts;

public class TripleTest extends TestCase {

    @Test
    public void testToString() throws Exception {
        Triple triple = new Triple("http://x", Concepts.DC.TITLE, new LiteralValue("y", "nl"), null);
        assertEquals("<http://x,http://purl.org/dc/elements/1.1/title@nl,y@nl>", triple.toString());
    }
}
