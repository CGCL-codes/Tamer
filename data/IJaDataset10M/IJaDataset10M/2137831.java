package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "removeAttribute(name)" method for an attribute causes the 
 *    DOMException NO_MODIFICATION_ALLOWED_ERR to be raised
 *    if the node is readonly.
 *    
 *    Obtain the children of the THIRD "gender" element.  The elements
 *    content is an entity reference.  Try to remove the "domestic" attribute
 *    from the entity reference by executing the "removeAttribute(name)" method.
 *    This causes a NO_MODIFICATION_ALLOWED_ERR DOMException to be thrown.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-258A00AF')/constant[@name='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-6D6AC0F9')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#xpointer(id('ID-6D6AC0F9')/raises/exception[@name='DOMException']/descr/p[substring-before(.,':')='NO_MODIFICATION_ALLOWED_ERR'])</a>
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9</a>
*/
public final class elementremoveattributenomodificationallowederr extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public elementremoveattributenomodificationallowederr(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Node gender;
        NodeList genList;
        Node gen;
        NodeList gList;
        int nodeType;
        Element genElement;
        doc = (Document) load("staff", true);
        genderList = doc.getElementsByTagName("gender");
        gender = genderList.item(2);
        genList = gender.getChildNodes();
        gen = genList.item(0);
        assertNotNull("genNotNull", gen);
        nodeType = (int) gen.getNodeType();
        if (equals(1, nodeType)) {
            gen = doc.createEntityReference("ent4");
            assertNotNull("createdEntRefNotNull", gen);
        }
        gList = gen.getChildNodes();
        genElement = (Element) gList.item(0);
        assertNotNull("genElementNotNull", genElement);
        {
            boolean success = false;
            try {
                genElement.removeAttribute("domestic");
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
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/elementremoveattributenomodificationallowederr";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(elementremoveattributenomodificationallowederr.class, args);
    }
}
