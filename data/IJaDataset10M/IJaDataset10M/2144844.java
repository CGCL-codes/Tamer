package org.w3c.domts.level1.core;

import org.w3c.dom.*;
import org.w3c.domts.DOMTestCase;
import org.w3c.domts.DOMTestDocumentBuilderFactory;

/**
 *     Duplicate entities are to be discarded. 
 *    Retrieve the Document Type for this document and create 
 *    a NamedNodeMap of all its entities.  The entity named 
 *    "ent1" is defined twice and therefore that last 
 *    occurrance should be discarded.
* @author NIST
* @author Mary Brady
* @see <a href="http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1788794630">http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001/level-one-core#ID-1788794630</a>
*/
public final class documenttypegetentitieslength extends DOMTestCase {

    /**
    * Constructor.
    * @param factory document factory, may not be null
    * @throws org.w3c.domts.DOMTestIncompatibleException Thrown if test is not compatible with parser configuration
    */
    public documenttypegetentitieslength(final DOMTestDocumentBuilderFactory factory) throws org.w3c.domts.DOMTestIncompatibleException {
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
        NamedNodeMap entityList;
        doc = (Document) load("staff", false);
        docType = doc.getDoctype();
        assertNotNull("docTypeNotNull", docType);
        entityList = docType.getEntities();
        assertNotNull("entitiesNotNull", entityList);
        if (("image/svg+xml".equals(getContentType()))) {
            assertSize("entitySizeSVG", 7, entityList);
        } else {
            assertSize("entitySize", 5, entityList);
        }
    }

    /**
    *  Gets URI that identifies the test.
    *  @return uri identifier of test
    */
    public String getTargetURI() {
        return "http://www.w3.org/2001/DOM-Test-Suite/level1/core/documenttypegetentitieslength";
    }

    /**
    * Runs this test from the command line.
    * @param args command line arguments
    */
    public static void main(final String[] args) {
        DOMTestCase.doMain(documenttypegetentitieslength.class, args);
    }
}
