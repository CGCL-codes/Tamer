package org.w3c.domts.level2.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     The createDocument method with valid arguments, should create a DOM Document of 
 *     the specified type.  
 *     
 *     Call the createDocument on this DOMImplementation with 
 *     createDocument ("http://www.w3.org/DOMTest/L2",see the array below for valid QNames,null).  
 *     Check if the returned Document object is is empty with no Document Element. 
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#Level-2-Core-DOM-createDocument">http://www.w3.org/TR/DOM-Level-2-Core/core#Level-2-Core-DOM-createDocument</a>
*/
public final class domimplementationcreatedocument03 extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public domimplementationcreatedocument03(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
        org.w3c.domts.DocumentBuilderSetting[] settings = new org.w3c.domts.DocumentBuilderSetting[] { org.w3c.domts.DocumentBuilderSetting.namespaceAware };
        DOMTestDocumentBuilderFactory testFactory = factory.newInstance(settings);
        setFactory(testFactory);
        String contentType = getContentType();
        preload(contentType, "staffNS", false);
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    public void runTest() throws Throwable {
        Document doc;
        DOMImplementation domImpl;
        Document newDoc;
        DocumentType docType = null;
        String namespaceURI = "http://www.w3.org/DOMTest/L2";
        String qualifiedName;
        java.util.List qualifiedNames = new java.util.ArrayList();
        qualifiedNames.add("_:_");
        qualifiedNames.add("_:h0");
        qualifiedNames.add("_:test");
        qualifiedNames.add("l_:_");
        qualifiedNames.add("ns:_0");
        qualifiedNames.add("ns:a0");
        qualifiedNames.add("ns0:test");
        qualifiedNames.add("a.b:c");
        qualifiedNames.add("a-b:c");
        qualifiedNames.add("a-b:c");
        doc = (Document) load("staffNS", false);
        domImpl = doc.getImplementation();
        for (int indexN1006B = 0; indexN1006B < qualifiedNames.size(); indexN1006B++) {
            qualifiedName = (String) qualifiedNames.get(indexN1006B);
            newDoc = domImpl.createDocument(namespaceURI, qualifiedName, docType);
            assertNotNull("domimplementationcreatedocument03", newDoc);
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level2/core/domimplementationcreatedocument03";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(domimplementationcreatedocument03.class, args);
    }
}
