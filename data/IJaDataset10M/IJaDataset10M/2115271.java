package net.sourceforge.syncyoursecrets.model.pw;

import static org.junit.Assert.assertEquals;
import net.sourceforge.syncyoursecrets.util.XmlSerializeTool;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class PWTableEntryTest performs a round trip test for the PWTableEntry.
 * 
 * 
 * 
 * @author Jan Petranek
 */
public class PWTableEntryTest {

    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "Test Content";

    /** The Constant TEST_NAME. */
    private static final String TEST_NAME = "Entry Name";

    /**
	 * Create an XML document containing a single PWTableEntry.
	 * 
	 * @return the document
	 * 
	 * @throws Exception
	 *             the exception
	 */
    public Document testToXml() throws Exception {
        Document doc = XmlSerializeTool.createDocument();
        PWTableEntry tEntry = new PWTableEntry(TEST_NAME);
        tEntry.setContent(TEST_CONTENT);
        Element node = tEntry.toXml(doc);
        doc.appendChild(node);
        return doc;
    }

    /**
	 * Read the PWTableEntry in from xml and check its content.
	 * 
	 * @param doc
	 *            the document containing our PWTableEntry
	 * 
	 * @throws Exception
	 *             the exception
	 */
    public void fillFromXml(Document doc) throws Exception {
        Element root = doc.getDocumentElement();
        PWTableEntry tEntry = new PWTableEntry(root);
        assertEquals("Checking name", TEST_NAME, tEntry.getName());
        assertEquals("Checking content", TEST_CONTENT, tEntry.getContent());
    }

    /**
	 * Makes a round trip test.
	 * 
	 * @throws Exception
	 *             when the test fails.
	 */
    @Test
    public void testRoundTrip() throws Exception {
        Document doc = testToXml();
        fillFromXml(doc);
    }
}
