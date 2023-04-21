package org.apache.solr.analysis;

import junit.framework.TestCase;

public class TestMappingCharFilterFactory extends TestCase {

    public void testParseString() throws Exception {
        MappingCharFilterFactory f = new MappingCharFilterFactory();
        try {
            f.parseString("\\");
            fail("escape character cannot be alone.");
        } catch (RuntimeException expected) {
        }
        assertEquals("unexpected escaped characters", "\\\"\n\t\r\b\f", f.parseString("\\\\\\\"\\n\\t\\r\\b\\f"));
        assertEquals("unexpected escaped characters", "A", f.parseString("\\u0041"));
        assertEquals("unexpected escaped characters", "AB", f.parseString("\\u0041\\u0042"));
        try {
            f.parseString("\\u000");
            fail("invalid length check.");
        } catch (RuntimeException expected) {
        }
        try {
            f.parseString("\\u123x");
            fail("invalid hex number check.");
        } catch (NumberFormatException expected) {
        }
    }
}
