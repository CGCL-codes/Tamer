package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *  The method getNamedItemNS retrieves a node specified by local name and namespace URI. 
 *  
 *  Using the method getNamedItemNS, retreive an attribute node having namespaceURI=http://www.nist.gov
 *  and localName=domestic, from a NamedNodeMap of attribute nodes, for the second element 
 *  whose namespaceURI=http://www.nist.gov and localName=address.  Verify if the attr node 
 *  has been retreived successfully by checking its nodeName atttribute.
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-getNamedItemNS">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-getNamedItemNS</a>
*/
public final class namednodemapgetnameditemns02 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public namednodemapgetnameditemns02(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.namespaceAware };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staffNS", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NamedNodeMap attributes;
        Node element;
        Attr attribute;
        NodeList elementList;
        String attrName;
        doc = (Document) load("staffNS", false);
        elementList = doc.getElementsByTagNameNS("http://www.nist.gov", "address");
        element = elementList.item(1);
        attributes = element.getAttributes();
        attribute = (Attr) attributes.getNamedItemNS("http://www.nist.gov", "domestic");
        attrName = attribute.getNodeName();
        assertEquals("namednodemapgetnameditemns02", "emp:domestic", attrName);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/namednodemapgetnameditemns02";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(namednodemapgetnameditemns02.class, args);
    }
}
