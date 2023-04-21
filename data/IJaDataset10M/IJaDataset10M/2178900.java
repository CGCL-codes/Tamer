package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The string returned by the "getNodeName()" method for a 
 *     Text Node is "#text".
 *     
 *     Retrieve the Text Node from the last child of the
 *     first employee and check the string returned 
 *     by the "getNodeName()" method.   It should be equal to 
 *     "#text". 
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68D095">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68D095</a>
*/
public final class nodetextnodename extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodetextnodename(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "staff", false);
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
        String textName;
        doc = (Document) load("staff", false);
        elementList = doc.getElementsByTagName("address");
        testAddr = (Element) elementList.item(0);
        textNode = testAddr.getFirstChild();
        textName = textNode.getNodeName();
        assertEquals("nodeTextNodeNameAssert1", "#text", textName);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodetextnodename";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodetextnodename.class, args);
    }
}
