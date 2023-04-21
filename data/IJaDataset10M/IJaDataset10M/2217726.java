package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     Retrieve the second "p" and append a "br" Element
 *     node to the list of children.   The last node in the list
 *     is then retrieved and its NodeName examined.   The
 *     "getNodeName()" method should return "br".
* @author Curt Arnold
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-184E7107">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-184E7107</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=247">http://www.w3.org/Bugs/Public/show_bug.cgi?id=247</a>
*/
public final class hc_nodeappendchild extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public hc_nodeappendchild(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Node employeeNode;
        NodeList childList;
        Node createdNode;
        Node lchild;
        String childName;
        Node appendedChild;
        doc = (Document) load("hc_staff", true);
        elementList = doc.getElementsByTagName("p");
        employeeNode = elementList.item(1);
        childList = employeeNode.getChildNodes();
        createdNode = doc.createElement("br");
        appendedChild = employeeNode.appendChild(createdNode);
        lchild = employeeNode.getLastChild();
        childName = lchild.getNodeName();
        assertEqualsAutoCase("element", "nodeName", "br", childName);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/hc_nodeappendchild";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(hc_nodeappendchild.class, args);
    }
}
