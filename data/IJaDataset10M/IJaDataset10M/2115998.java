package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "getAttributes()" method invoked on an EntityReference 
 *     Node returns null.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-84CF096">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-84CF096</a>
*/
public final class nodeentityreferencenodeattributes extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodeentityreferencenodeattributes(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList elementList;
        Element entRefAddr;
        Node entRefNode;
        NamedNodeMap attrList;
        int nodeType;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagName("address");
        entRefAddr = (Element) elementList.item(1);
        entRefNode = entRefAddr.getFirstChild();
        nodeType = (int) entRefNode.getNodeType();
        if (!equals(5, nodeType)) {
            entRefNode = doc.createEntityReference("ent2");
            assertNotNull("createdEntRefNotNull", entRefNode);
        }
        attrList = entRefNode.getAttributes();
        assertNull("attrList", attrList);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodeentityreferencenodeattributes";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodeentityreferencenodeattributes.class, args);
    }
}
