package tests.org.w3c.dom;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;

/**
 * Using the method getNamedItemNS, retreive the entity "ent1" and notation
 * "notation1" from a NamedNodeMap of this DocumentTypes entities and notations.
 * Both should be null since entities and notations are not namespaced.
 * 
 * @author IBM
 * @author Neil Delima
 * @see <a
 *      href="http://www.w3.org/TR/DOM-Level-2-Core/core#ID-getNamedItemNS">http://www.w3.org/TR/DOM-Level-2-Core/core#ID-getNamedItemNS</a>
 * @see <a
 *      href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=259">http://www.w3.org/Bugs/Public/show_bug.cgi?id=259</a>
 * @see <a
 *      href="http://www.w3.org/Bugs/Public/show_bug.cgi?id=407">http://www.w3.org/Bugs/Public/show_bug.cgi?id=407</a>
 * @see <a
 *      href="http://lists.w3.org/Archives/Member/w3c-dom-ig/2003Nov/0016.html">http://lists.w3.org/Archives/Member/w3c-dom-ig/2003Nov/0016.html</a>
 */
@TestTargetClass(NamedNodeMap.class)
public final class NamedNodeMapGetNamedItemNS extends DOMTestCase {

    DOMDocumentBuilderFactory factory;

    DocumentBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        try {
            factory = new DOMDocumentBuilderFactory(DOMDocumentBuilderFactory.getConfiguration2());
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

    @TestTargetNew(level = TestLevel.PARTIAL, notes = "Doesn't verify DOMException.", method = "getNamedItemNS", args = { java.lang.String.class, java.lang.String.class })
    public void testGetNamedItemNS2() throws Throwable {
        Document doc;
        NamedNodeMap attributes;
        Node element;
        Attr attribute;
        NodeList elementList;
        String attrName;
        doc = (Document) load("staffNS", builder);
        elementList = doc.getElementsByTagNameNS("http://www.nist.gov", "address");
        element = elementList.item(1);
        attributes = element.getAttributes();
        attribute = (Attr) attributes.getNamedItemNS("http://www.nist.gov", "domestic");
        attrName = attribute.getNodeName();
        assertEquals("namednodemapgetnameditemns02", "emp:domestic", attrName);
    }

    @TestTargetNew(level = TestLevel.PARTIAL, notes = "Doesn't verify DOMException.", method = "getNamedItemNS", args = { java.lang.String.class, java.lang.String.class })
    public void testGetNamedItemNS3() throws Throwable {
        Document doc;
        NamedNodeMap attributes;
        Node element;
        Attr attribute;
        Attr newAttr1;
        Attr newAttr2;
        String attrName;
        doc = (Document) load("staffNS", builder);
        element = doc.createElementNS("http://www.w3.org/DOM/Test", "root");
        newAttr1 = doc.createAttributeNS("http://www.w3.org/DOM/L1", "L1:att");
        ((Element) element).setAttributeNodeNS(newAttr1);
        newAttr2 = doc.createAttributeNS("http://www.w3.org/DOM/L2", "L2:att");
        ((Element) element).setAttributeNodeNS(newAttr2);
        attributes = element.getAttributes();
        attribute = (Attr) attributes.getNamedItemNS("http://www.w3.org/DOM/L2", "att");
        attrName = attribute.getNodeName();
        assertEquals("namednodemapgetnameditemns03", "L2:att", attrName);
    }

    @TestTargetNew(level = TestLevel.PARTIAL, notes = "Doesn't verify DOMException.", method = "getNamedItemNS", args = { java.lang.String.class, java.lang.String.class })
    public void testGetNamedItemNS4() throws Throwable {
        Document doc;
        NamedNodeMap attributes;
        Element element;
        Attr attribute;
        Attr newAttr1;
        NodeList elementList;
        String attrName;
        doc = (Document) load("staffNS", builder);
        elementList = doc.getElementsByTagNameNS("*", "address");
        element = (Element) elementList.item(1);
        newAttr1 = doc.createAttributeNS("http://www.w3.org/DOM/L1", "street");
        element.setAttributeNodeNS(newAttr1);
        attributes = element.getAttributes();
        attribute = (Attr) attributes.getNamedItemNS("http://www.w3.org/DOM/L1", "street");
        attrName = attribute.getNodeName();
        assertEquals("namednodemapgetnameditemns04", "street", attrName);
    }

    @TestTargetNew(level = TestLevel.PARTIAL, notes = "Doesn't verify DOMException.", method = "getNamedItemNS", args = { java.lang.String.class, java.lang.String.class })
    public void testGetNamedItemNS5() throws Throwable {
        Document doc;
        NamedNodeMap attributes;
        Node element;
        Attr attribute;
        NodeList elementList;
        doc = (Document) load("staffNS", builder);
        elementList = doc.getElementsByTagNameNS("*", "address");
        element = elementList.item(1);
        attributes = element.getAttributes();
        attribute = (Attr) attributes.getNamedItemNS("*", "street");
        assertNull("namednodemapgetnameditemns05", attribute);
    }
}
