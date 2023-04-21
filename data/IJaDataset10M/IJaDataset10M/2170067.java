package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "replaceChild(newChild,oldChild)" method causes the 
 *     DOMException NO_MODIFICATION_ALLOWED_ERR to be raised
 *     if the node is readonly.
 *     
 *     Obtain the children of the THIRD "gender" element.   The elements
 *     content is an entity reference.   Get the FIRST item 
 *     from the entity reference and execute the "replaceChild(newChild,oldChild)" method.
 *     This causes a NO_MODIFICATION_ALLOWED_ERR DOMException to be thrown.
* @author NIST
* @author Mary Brady
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-785887307">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-785887307</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-785887307')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-785887307')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-785887307">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-785887307</a>
*/
public final class nodereplacechildnomodificationallowederr extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodereplacechildnomodificationallowederr(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        NodeList genderList;
        Node genderNode;
        Node entRef;
        Node entElement;
        Node createdNode;
        Node replacedChild;
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
        createdNode = doc.createElement("newChild");
        {
            boolean success = false;
            try {
                replacedChild = entRef.replaceChild(createdNode, entElement);
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
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodereplacechildnomodificationallowederr";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodereplacechildnomodificationallowederr.class, args);
    }
}
