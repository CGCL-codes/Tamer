package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "replaceData(offset,count,arg)" method raises a NO_MODIFICATION_ALLOWED_ERR 
 *    DOMException if the node is readonly.
 *    
 *    Obtain the children of the THIRD "gender" element.  The elements
 *    content is an entity reference.  Get the FIRST item 
 *    from the entity reference and execute the "replaceData(offset,count,arg)" method.
 *    This causes a NO_MODIFICATION_ALLOWED_ERR DOMException to be thrown.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-E5CBA7FB')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-E5CBA7FB')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-E5CBA7FB</a>
*/
public final class characterdatareplacedatanomodificationallowederr extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public characterdatareplacedatanomodificationallowederr(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Node entElement;
        Node entElementContent;
        Node entReference;
        int nodeType;
        doc = (Document) load("staff", true);
        genderList = doc.getElementsByTagName("gender");
        genderNode = genderList.item(2);
        entReference = genderNode.getFirstChild();
        assertNotNull("entReferenceNotNull", entReference);
        nodeType = (int) entReference.getNodeType();
        if (equals(1, nodeType)) {
            entReference = doc.createEntityReference("ent4");
            assertNotNull("createdEntRefNotNull", entReference);
        }
        entElement = entReference.getFirstChild();
        assertNotNull("entElementNotNull", entElement);
        entElementContent = entElement.getFirstChild();
        assertNotNull("entElementContentNotNull", entElementContent);
        {
            boolean success = false;
            try {
                ((CharacterData) entElementContent).replaceData(1, 3, "newArg");
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
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/characterdatareplacedatanomodificationallowederr";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(characterdatareplacedatanomodificationallowederr.class, args);
    }
}
