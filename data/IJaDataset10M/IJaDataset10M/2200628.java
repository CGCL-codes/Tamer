package org.acs.elated.test.commons.parser;

import junit.framework.*;
import org.acs.elated.commons.parser.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class TestTextParser extends TestCase {

    private TextParser textParser = null;

    protected void setUp() throws Exception {
        super.setUp();
        textParser = new TextParser();
    }

    protected void tearDown() throws Exception {
        textParser = null;
        super.tearDown();
    }

    public void testParse() {
        String str = "my String";
        str = textParser.parse(str.getBytes());
        assertEquals(str, "my String");
    }
}
