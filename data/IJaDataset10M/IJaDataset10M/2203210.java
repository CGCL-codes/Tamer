package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 * The "deleteData(offset,count)" method removes a range of
 * characters from the node.  Delete data at the beginning
 * of the character data.
 * Retrieve the character data from the last child of the
 * first employee.  The "deleteData(offset,count)"
 * method is then called with offset=0 and count=16.
 * The method should delete the characters from position
 * 0 thru position 16.  The new value of the character data
 * should be "Dallas, Texas 98551".
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-7C603781">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-7C603781</a>
*/
public final class characterdatadeletedatabegining extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public characterdatadeletedatabegining(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        Node nameNode;
        CharacterData child;
        String childData;
        doc = (Document) load("staff", true);
        elementList = doc.getElementsByTagName("address");
        nameNode = elementList.item(0);
        child = (CharacterData) nameNode.getFirstChild();
        child.deleteData(0, 16);
        childData = child.getData();
        assertEquals("characterdataDeleteDataBeginingAssert", "Dallas, Texas 98551", childData);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/characterdatadeletedatabegining";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(characterdatadeletedatabegining.class, args);
    }
}
