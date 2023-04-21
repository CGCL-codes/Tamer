package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "setAttribute(name,value)" method adds a new attribute
 *    to the Element.  If the "name" is already present, then
 *    its value should be changed to the new one that is in
 *    the "value" parameter. 
 *    
 *    Retrieve the last child of the fourth employee, then add 
 *    an attribute to it by invoking the 
 *    "setAttribute(name,value)" method.  Since the name of the
 *    used attribute("street") is already present in this     
 *    element, then its value should be changed to the new one
 *    of the "value" parameter.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68F082">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-F68F082</a>
*/
public final class elementchangeattributevalue extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public elementchangeattributevalue(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Element testEmployee;
        String attrValue;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagName("address");
        testEmployee = (Element) elementList.item(3);
        testEmployee.setAttribute("street", "Neither");
        attrValue = testEmployee.getAttribute("street");
        assertEquals("elementChangeAttributeValueAssert", "Neither", attrValue);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/elementchangeattributevalue";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(elementchangeattributevalue.class, args);
    }
}
