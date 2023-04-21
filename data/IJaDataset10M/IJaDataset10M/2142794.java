package com.explosion.datastream.search;

import junit.framework.TestCase;
import com.explosion.expfmodules.search.LineReplacer;

/**
 * @author Stephen
 * Created on 28-Jun-2004
 */
public class LineReplacerTest extends TestCase {

    /**
     * 
     */
    public LineReplacerTest() {
        super();
    }

    /**
     * @param arg0
     */
    public LineReplacerTest(String arg0) {
        super(arg0);
    }

    public void testLineReplacer() throws Exception {
        LineReplacer replace = new LineReplacer("hello", "goodbye");
        replace.open();
        String replacement = replace.processLine("hello me hearties", 1, "filename");
        assertEquals("goodbye me hearties", replacement);
        replace.close();
        replace = new LineReplacer("leigh.mccullough@maven.aniteps.com", "steve.cowx@gnasher.aniteps.com");
        replace.open();
        replacement = replace.processLine(":pserver:leigh.mccullough@maven.aniteps.com:/cvsroot", 1, "filename");
        assertEquals(":pserver:steve.cowx@gnasher.aniteps.com:/cvsroot", replacement);
    }
}
