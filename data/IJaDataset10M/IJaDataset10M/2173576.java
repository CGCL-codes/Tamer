package de.cologneintelligence.fitgoodies.parsers;

import de.cologneintelligence.fitgoodies.FitGoodiesTestCase;
import de.cologneintelligence.fitgoodies.ScientificDouble;
import de.cologneintelligence.fitgoodies.parsers.ScientificDoubleParser;

/**
 * $Id: ScientificDoubleParserTest.java 46 2011-09-04 14:59:16Z jochen_wierum $
 * @author jwierum
 */
public final class ScientificDoubleParserTest extends FitGoodiesTestCase {

    public void testParser() throws Exception {
        ScientificDoubleParser parser = new ScientificDoubleParser();
        assertTrue(parser.parse("1.3", null).equals(new ScientificDouble(1.326)));
        assertFalse(parser.parse("1.3", null).equals(new ScientificDouble(1.396)));
        assertTrue(parser.parse("1.5e1", null).equals(new ScientificDouble(14.5)));
        assertFalse(parser.parse("1.5e1", null).equals(new ScientificDouble(15.8)));
        assertTrue(parser.parse("2", null).equals(new ScientificDouble(2.3)));
        assertFalse(parser.parse("2", null).equals(new ScientificDouble(1.3)));
        assertTrue(parser.parse("1.5e-1", null).equals(new ScientificDouble(0.148)));
        assertFalse(parser.parse("1.5e-1", null).equals(new ScientificDouble(0.158)));
    }
}
