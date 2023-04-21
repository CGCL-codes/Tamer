package org.acs.elated.test.commons.parser;

import junit.framework.*;
import org.acs.elated.commons.parser.*;
import org.acs.elated.fed.*;

public class TestHTMLParser extends TestCase {

    private HTMLParser htmlParser = null;

    private FedoraInterface fedoraInterface = FedoraInterfaceFactory.getFedoraInterface();

    protected void setUp() throws Exception {
        super.setUp();
        htmlParser = new HTMLParser();
    }

    protected void tearDown() throws Exception {
        htmlParser = null;
        super.tearDown();
    }

    public void testParseHTML() throws Exception {
        byte[] b = fedoraInterface.getDatastreamData("demo:00000100000600000", "DS1");
        String actualValue = htmlParser.parse(b);
        assertEquals(19967, actualValue.length());
    }

    public void testParseXML() throws Exception {
        byte[] b = fedoraInterface.getDatastreamData("demo:00000100000600000", "DC");
        String actualValue = htmlParser.parse(b);
        String expectedValue = "AcsIntern demo:00000100000600000   ";
        assertEquals(expectedValue, actualValue);
    }
}
