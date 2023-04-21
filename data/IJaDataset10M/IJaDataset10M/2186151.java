package tests.org.w3c.dom;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import javax.xml.parsers.DocumentBuilder;

/**
 *     The method getInternalSubset() returns the public identifier of the external subset.
 *   
 *     Create a new DocumentType node with the value "PUB" for its publicId.
 *     Check the value of the publicId attribute using getPublicId().
* @author IBM
* @author Neil Delima
* @see <a href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-Core-DocType-publicId">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-Core-DocType-publicId</a>
* @see <a href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=259">http://www.w3.org/Bugs/Public/show_bug.cgi?id=259</a>
*/
@TestTargetClass(DocumentType.class)
public final class DocumentTypePublicId extends DOMTestCase {

    DOMDocumentBuilderFactory factory;

    DocumentBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        try {
            factory = new DOMDocumentBuilderFactory(DOMDocumentBuilderFactory.getConfiguration1());
            builder = factory.getBuilder();
        } catch (Exception e) {
            fail("Unexpected exception" + e.getMessage());
        }
    }

    protected void tearDown() throws Exception {
        factory = null;
        builder = null;
        super.tearDown();
    }

    /**
    * Runs the test case.
    * @throws Throwable Any uncaught exception causes test to fail
    */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getPublicId", args = {  })
    public void testGetPublicId() throws Throwable {
        Document doc;
        DocumentType docType;
        DOMImplementation domImpl;
        String publicId;
        String nullNS = null;
        doc = (Document) load("staffNS", builder);
        domImpl = doc.getImplementation();
        docType = domImpl.createDocumentType("l2:root", "PUB", nullNS);
        publicId = docType.getPublicId();
        assertEquals("documenttypepublicid01", "PUB", publicId);
    }
}
