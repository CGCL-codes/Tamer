package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The string returned by the "getNodeValue()" method for a 
 *     Text Node is the content of the Text node.
 *     
 *     Retrieve the Text node from the last child of the first 
 *     employee and check the string returned by the 
 *     "getNodeValue()" method.   It should be equal to 
 *     "1230 North Ave. Dallas, Texas 98551". 
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68D080">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68D080</a>
*/
public final class hc_nodetextnodevalue extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_nodetextnodevalue(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "hc_staff", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList elementList;
        Element testAddr;
        Node textNode;
        String textValue;
        doc = (Document) load("hc_staff", false);
        elementList = doc.getElementsByTagName("acronym");
        testAddr = (Element) elementList.item(0);
        textNode = testAddr.getFirstChild();
        textValue = textNode.getNodeValue();
        assertEquals("textNodeValue", "1230 North Ave. Dallas, Texas 98551", textValue);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_nodetextnodevalue";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_nodetextnodevalue.class, args);
    }
}
