package org.radeox.macro;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IsbnMacroTest extends MacroTestSupport {

    public IsbnMacroTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(IsbnMacroTest.class);
    }

    public void testIsbn() {
        String result = engine.render("{isbn:0201615630}", context);
        assertEquals("(<a href=\"http://www.amazon.com/exec/obidos/ASIN/0201615630\">Amazon.com</a>)", result);
    }
}
