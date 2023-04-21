package org.acs.elated.test.commons.parser;

import junit.framework.*;
import org.acs.elated.commons.parser.*;

public class TestParsePDF extends TestCase {

    private ParsePDF parsePDF = null;

    protected void setUp() throws Exception {
        super.setUp();
        parsePDF = new ParsePDF();
    }

    protected void tearDown() throws Exception {
        parsePDF = null;
        super.tearDown();
    }
}
