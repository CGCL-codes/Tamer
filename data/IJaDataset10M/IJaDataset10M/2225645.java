package net.sf.jabref.export.xml;

import static org.junit.Assert.assertNotNull;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.stream.StreamSource;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests {@link XslFormatter}
 * 
 * @author kariem
 * @author coezbek
 */
public class XslFormatterTest {

    private static final String XSL_LOCATION = "jar:file:lib/bibtexconverter-20070816.jar!/net/sourceforge/bibtexml/xslt/bibxml2docbook.xsl";

    /**
	 * Test {@link XslFormatter#prepare(Document)}
	 * 
	 * @throws MalformedURLException if the URI could not be parsed
	 */
    @Test
    public void testPrepareDocumentDocument() throws MalformedURLException {
        XslFormatter f = new XslFormatter("test", "test", "test", new URL(XSL_LOCATION));
        StreamSource source = new StreamSource(getClass().getResourceAsStream("/test.xml"));
        Document result = f.prepare(source);
        assertNotNull(result);
    }
}
