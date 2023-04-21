package uk.ac.ebi.intact.application.dataConversion.psiUpload.parser.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.XrefTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.parser.XrefParser;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.parser.test.mock.MockDocumentBuilder;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.parser.test.mock.MockXmlContent;
import uk.ac.ebi.intact.util.test.mocks.MockInputStream;
import java.util.Collection;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: XrefParserTest.java 3917 2005-04-28 08:27:39Z skerrien $
 */
public class XrefParserTest extends TestCase {

    /**
     * Constructs a NewtServerProxyTest instance with the specified name.
     *
     * @param name the name of the test.
     */
    public XrefParserTest(final String name) {
        super(name);
    }

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(XrefParserTest.class);
    }

    public void testProcess() {
        final MockInputStream is = new MockInputStream();
        is.setBuffer(MockXmlContent.XREF_1);
        final Document document = MockDocumentBuilder.build(is);
        final Element element = document.getDocumentElement();
        XrefTag xref = null;
        Collection secondary = null;
        Collection allXrefs = null;
        xref = XrefParser.processPrimaryRef(element);
        secondary = XrefParser.processSecondaryRef(element);
        allXrefs = XrefParser.process(element);
        assertNotNull(xref);
        assertEquals("11805826", xref.getId());
        assertEquals(xref.getDb(), "pubmed");
        assertEquals("mySecondaryId", xref.getSecondary());
        assertEquals("version1", xref.getVersion());
        assertNotNull(secondary);
        assertEquals(2, secondary.size());
        xref = Utilities.getXrefByCvDatabase(secondary, "go");
        assertNotNull(xref);
        assertNotNull(xref.getDb());
        assertEquals("go", xref.getDb());
        assertEquals("GO:0005634", xref.getId());
        assertEquals("C:nucleus", xref.getSecondary());
        assertEquals("version2", xref.getVersion());
        xref = Utilities.getXrefByCvDatabase(secondary, "sgd");
        assertNotNull(xref);
        assertNotNull(xref.getDb());
        assertEquals("sgd", xref.getDb());
        assertEquals("S0004064", xref.getId());
        assertEquals("BUD20", xref.getSecondary());
        assertEquals("version2", xref.getVersion());
        assertNotNull(allXrefs);
        assertEquals(3, allXrefs.size());
        xref = Utilities.getXrefByCvDatabase(allXrefs, "pubmed");
        assertNotNull(xref);
        assertEquals("11805826", xref.getId());
        assertEquals("mySecondaryId", xref.getSecondary());
        assertEquals("version1", xref.getVersion());
        assertNotNull(xref.getDb());
        assertEquals("pubmed", xref.getDb());
        xref = Utilities.getXrefByCvDatabase(allXrefs, "go");
        assertNotNull(xref);
        assertNotNull(xref.getDb());
        assertEquals("go", xref.getDb());
        assertEquals("GO:0005634", xref.getId());
        assertEquals("C:nucleus", xref.getSecondary());
        assertEquals("version2", xref.getVersion());
        xref = Utilities.getXrefByCvDatabase(allXrefs, "sgd");
        assertNotNull(xref);
        assertNotNull(xref.getDb());
        assertEquals("sgd", xref.getDb());
        assertEquals("S0004064", xref.getId());
        assertEquals("BUD20", xref.getSecondary());
        assertEquals("version2", xref.getVersion());
    }
}
