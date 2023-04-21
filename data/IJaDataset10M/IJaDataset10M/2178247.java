package net.toolkit.xml;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestToStringVisitor {

    @Test
    public void testVisitXmlDocument() {
        XmlDocument d = new XmlDocument();
        ToStringVisitor v = new ToStringVisitor();
        v.visit(d);
        assertEquals(d.toString(), v.toString());
    }

    @Test
    public void testVisitXmlDocumentNamespaces() {
        XmlDocument d = new XmlDocument();
        d.namespace("xsd", "http://www.w3school.org");
        d.root("element");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><element xmlns:xsd=\"http://www.w3school.org\"/>", d.toString());
    }

    @Test
    public void testVisitXmlDocumentSchemaLocation() {
        XmlDocument d = new XmlDocument();
        d.schemaLocation("http://www.springframework.org/bean.xsd");
        d.root("element");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><element schemaLocations=\"http://www.springframework.org/bean.xsd \"/>", d.toString());
    }

    @Test
    public void testVisitXmlElement() {
        XmlElement e = (XmlElement) new XmlElement("ELEMENT").attribute("attribute", "attribute").attribute("attribute1", "attribute1");
        ToStringVisitor v = new ToStringVisitor();
        v.visit(e);
        assertEquals(e.toString(), v.toString());
    }

    @Test
    public void testVisitXmlElementNamespace() {
        XmlElement e = (XmlElement) new XmlElement("bean", "ELEMENT").attribute("attribute", "attribute").attribute("attribute1", "attribute1");
        ToStringVisitor v = new ToStringVisitor();
        v.visit(e);
        assertEquals(e.toString(), v.toString());
    }

    @Test
    public void testVisitTextCdata() {
        Text t = new Text("content", null, true);
        ToStringVisitor v = new ToStringVisitor();
        t.accept(v);
        assertEquals("<![CDATA[content]]>", v.toString());
    }

    @Test
    public void testVisitText() {
        Text t = new Text("content", null);
        ToStringVisitor v = new ToStringVisitor();
        t.accept(v);
        assertEquals("content", v.toString());
    }
}
