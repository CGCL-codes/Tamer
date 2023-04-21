package org.nexopenframework.xml;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.nexopenframework.xml.XmlUtils;
import org.w3c.dom.Document;
import junit.framework.TestCase;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class XmlUtilsTest extends TestCase {

    public void testCreateDocumentBuilderFactory() {
        DocumentBuilderFactory dbf = XmlUtils.createDocumentBuilderFactory(true, false);
        assertNotNull(dbf);
        assertEquals(true, dbf.isNamespaceAware());
        assertEquals(false, dbf.isValidating());
    }

    public void testCreateDocument() {
        final String xml = "<example><id>12345-894546</id><operation>urn:operation:some</operation></example>";
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        Document doc = XmlUtils.createDocument(bais);
        assertNotNull(doc);
    }
}
