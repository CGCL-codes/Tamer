package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The string returned by the "getNodeValue()" method for a 
 *     DocumentType Node is null.
* @author NIST
* @author Mary Brady
*/
public final class nodedocumenttypenodevalue extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public nodedocumenttypenodevalue(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        DocumentType docType;
        NamedNodeMap attrList;
        doc = (Document) load("staff", false);
        docType = doc.getDoctype();
        assertNotNull("docTypeNotNull", docType);
        attrList = docType.getAttributes();
        assertNull("doctypeAttributesNull", attrList);
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/nodedocumenttypenodevalue";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(nodedocumenttypenodevalue.class, args);
    }
}
