package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The "removeAttribute(name)" removes an attribute by name.
 *    If the attribute has a default value, it is immediately
 *    replaced.
 *    
 *    Retrieve the attribute named "street" from the last child
 *    of the fourth employee, then remove the "street" 
 *    attribute by invoking the "removeAttribute(name)" method.
 *    The "street" attribute has a default value defined in the
 *    DTD file, that value should immediately replace the old
 *    value.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-6D6AC0F9</a>
* @see <a href="http://lists.w3.org/Archives/Public/www-dom-ts/2002Mar/0002.html">http://lists.w3.org/Archives/Public/www-dom-ts/2002Mar/0002.html</a>
*/
public final class elementremoveattribute extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public elementremoveattribute(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.validating };
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
        NodeList elementList;
        Element testEmployee;
        String attrValue;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagName("address");
        testEmployee = (Element) elementList.item(3);
        testEmployee.removeAttribute("street");
        attrValue = testEmployee.getAttribute("street");
        assertEquals("streetYes", "Yes", attrValue);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/elementremoveattribute";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(elementremoveattribute.class, args);
    }
}
