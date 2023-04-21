package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     If there is not a node immediately preceding this node the
 *     "getPreviousSibling()" method returns null.
 *     
 *     Retrieve the first child of the second employee and
 *     invoke the "getPreviousSibling()" method.   It should
 *     be set to null. 
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-640FB3C8">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-640FB3C8</a>
*/
public final class nodegetprevioussiblingnull extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodegetprevioussiblingnull(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Node employeeNode;
        Node fcNode;
        Node psNode;
        doc = (Document) load("staff", false);
        elementList = doc.getElementsByTagName("employee");
        employeeNode = elementList.item(2);
        fcNode = employeeNode.getFirstChild();
        psNode = fcNode.getPreviousSibling();
        assertNull("nodeGetPreviousSiblingNullAssert1", psNode);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodegetprevioussiblingnull";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodegetprevioussiblingnull.class, args);
    }
}
