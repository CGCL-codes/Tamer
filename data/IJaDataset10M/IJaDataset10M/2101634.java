package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *   The "setPrefix(prefix)" method causes the 
 *   DOMException NO_MODIFICATION_ALLOWED_ERR to be raised
 *   if the node is readonly.
 *   
 *   Obtain the children of the THIRD "gender" element.  The elements
 *   content is an entity reference.  Get the FIRST item 
 *   from the entity reference and execute the "setPrefix(prefix)" method.
 *   This causes a NO_MODIFICATION_ALLOWED_ERR DOMException to be thrown.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/DOM-Level-2-Core/core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-NodeNSPrefix">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-NodeNSPrefix</a>
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#xpointer(id('ID-NodeNSPrefix')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/DOM-Level-2-Core/core#xpointer(id('ID-NodeNSPrefix')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])</a>
*/
public final class prefix08 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public prefix08(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.namespaceAware };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList genderList;
        Node genderNode;
        Node entRef;
        Node entElement;
        Node createdNode;
        int nodeType;
        doc = (Document) load("staff", true);
        genderList = doc.getElementsByTagName("gender");
        genderNode = genderList.item(2);
        entRef = genderNode.getFirstChild();
        nodeType = (int) entRef.getNodeType();
        if (equals(1, nodeType)) {
            entRef = doc.createEntityReference("ent4");
            assertNotNull("createdEntRefNotNull", entRef);
        }
        entElement = entRef.getFirstChild();
        assertNotNull("entElement", entElement);
        createdNode = doc.createElement("text3");
        {
            boolean success = false;
            try {
                entElement.setPrefix("newPrefix");
            } catch (DOMException ex) {
                success = (ex.code == DOMException.NO_MODIFICATION_ALLOWED_ERR);
            }
            assertTrue("throw_NO_MODIFICATION_ALLOWED_ERR", success);
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/prefix08";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(prefix08.class, args);
    }
}
