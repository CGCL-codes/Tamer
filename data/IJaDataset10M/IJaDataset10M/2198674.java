package net.sf.uitags.js;

import java.util.List;
import junit.framework.TestCase;

public class NamedSuitesTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSuiteNamesMayBeInterspersedWithWhiteSpaces() {
        NamedSuites suites = (NamedSuites) Suites.getInstance(" core , shift ");
        List fileNames = suites.getFileNames();
        assertEquals("util/Object.js", fileNames.get(0));
        assertEquals("util/Exception.js", fileNames.get(1));
        assertEquals("shift/Suite.js", fileNames.get(22));
        assertEquals("shift/Driver.js", fileNames.get(23));
    }
}
