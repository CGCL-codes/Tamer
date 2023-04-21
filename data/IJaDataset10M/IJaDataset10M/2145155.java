package com.phloc.commons.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import org.junit.Test;
import org.w3c.dom.Document;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.commons.xml.schema.XMLSchemaCache;

/**
 * Test class for class {@link XMLFactory}.
 * 
 * @author philip
 */
public final class XMLFactoryTest extends AbstractPhlocTestCase {

    @Test
    public void testCreateDefaultDocumentBuilderFactory() {
        final DocumentBuilderFactory dbf = XMLFactory.createDefaultDocumentBuilderFactory();
        assertNotNull(dbf);
        assertTrue(dbf.isCoalescing());
        assertTrue(dbf.isIgnoringComments());
        assertTrue(dbf.isNamespaceAware());
    }

    @Test
    public void testGetDocumentBuilder() {
        assertNotNull(XMLFactory.getDocumentBuilder());
        assertSame(XMLFactory.getDocumentBuilder(), XMLFactory.getDocumentBuilder());
    }

    @Test
    public void testGetDOMImplementation() {
        assertNotNull(XMLFactory.getDOMImplementation());
    }

    @Test
    public void testCreateDocumentBuilder() {
        final Schema sch = XMLSchemaCache.getInstance().getSchema(new ClassPathResource("xml/schema1.xsd"));
        assertNotNull(sch);
        DocumentBuilder db = XMLFactory.createDocumentBuilder(sch);
        assertNotNull(db);
        assertNotNull(db.getSchema());
        try {
            XMLFactory.createDocumentBuilder(null);
            fail();
        } catch (final NullPointerException ex) {
        }
        db = XMLFactory.createDocumentBuilder();
        assertNotNull(db);
        assertNull(db.getSchema());
    }

    @Test
    public void testGetSaxParserFactory() {
        assertNotNull(XMLFactory.getSaxParserFactory(false));
        assertNotNull(XMLFactory.getSaxParserFactory(true));
        assertSame(XMLFactory.getSaxParserFactory(false), XMLFactory.getSaxParserFactory(false));
        assertSame(XMLFactory.getSaxParserFactory(true), XMLFactory.getSaxParserFactory(true));
        assertTrue(XMLFactory.getSaxParserFactory(false) != XMLFactory.getSaxParserFactory(true));
    }

    @Test
    public void testCreateSaxParser() {
        assertNotNull(XMLFactory.createSaxParser(false));
        assertNotNull(XMLFactory.createSaxParser(true));
    }

    @Test
    public void testNewDocument() {
        Document doc = XMLFactory.newDocument();
        assertNotNull(doc);
        assertNull(doc.getDoctype());
        assertNull(doc.getDocumentElement());
        assertEquals(EXMLVersion.DEFAULT.getVersion(), doc.getXmlVersion());
        doc = XMLFactory.newDocument(EXMLVersion.XML_11);
        assertNotNull(doc);
        assertNull(doc.getDoctype());
        assertNull(doc.getDocumentElement());
        assertEquals(EXMLVersion.XML_11.getVersion(), doc.getXmlVersion());
        doc = XMLFactory.newDocument("qname", null, null);
        assertNotNull(doc);
        assertNotNull(doc.getDoctype());
        assertEquals("qname", doc.getDoctype().getName());
        assertNull(doc.getDoctype().getPublicId());
        assertNull(doc.getDoctype().getSystemId());
        assertNotNull(doc.getDocumentElement());
        assertEquals("qname", doc.getDocumentElement().getTagName());
        assertEquals(EXMLVersion.DEFAULT.getVersion(), doc.getXmlVersion());
        doc = XMLFactory.newDocument(EXMLVersion.XML_11, "qname", "pubid", "sysid");
        assertNotNull(doc);
        assertNotNull(doc.getDoctype());
        assertEquals("qname", doc.getDoctype().getName());
        assertEquals("pubid", doc.getDoctype().getPublicId());
        assertEquals("sysid", doc.getDoctype().getSystemId());
        assertNotNull(doc.getDocumentElement());
        assertEquals("qname", doc.getDocumentElement().getTagName());
        assertEquals(EXMLVersion.XML_11.getVersion(), doc.getXmlVersion());
    }
}
