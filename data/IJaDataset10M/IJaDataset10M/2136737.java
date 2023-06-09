package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 * If the "setAttributeNode(newAttr)" method replaces an
 * existing Attr node with the same name, then it should
 * return the previously existing Attr node.
 * Retrieve the last child of the third employee and add a
 * new attribute node.  The new attribute node is "class",
 * which is already present in this Element.  The method
 * should return the existing Attr node(old "class" Attr).
 * This test uses the "createAttribute(name)" method
 * from the Document interface.
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-887236154">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-887236154</a>
*/
public final class hc_elementreplaceexistingattributegevalue extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_elementreplaceexistingattributegevalue(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        super(factory);
        String contentType = getContentType();
        preload(contentType, "hc_staff", true);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        NodeList elementList;
        Element testEmployee;
        Attr newAttribute;
        Attr streetAttr;
        String value;
        doc = (Document) load("hc_staff", true);
        elementList = doc.getElementsByTagName("acronym");
        testEmployee = (Element) elementList.item(2);
        newAttribute = doc.createAttribute("class");
        streetAttr = testEmployee.setAttributeNode(newAttribute);
        assertNotNull("previousAttrNotNull", streetAttr);
        value = streetAttr.getValue();
        assertEquals("previousAttrValue", "No", value);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_elementreplaceexistingattributegevalue";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_elementreplaceexistingattributegevalue.class, args);
    }
}
