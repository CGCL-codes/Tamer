package net.sf.practicalxml;

import java.util.List;
import javax.xml.namespace.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *  Tests all DomUtil methods except <code>getPath()</code> and
 *  <code>getAbsolutePath()</code>.
 */
public class TestDomUtil extends AbstractTestCase {

    public void testNewDocument() throws Exception {
        Document doc1 = DomUtil.newDocument();
        assertNotNull(doc1);
        assertNull(doc1.getFirstChild());
        Element doc2root = DomUtil.newDocument("bar");
        assertNotNull(doc2root);
        assertNotNull(doc2root.getOwnerDocument());
        assertSame(doc2root.getOwnerDocument(), doc2root.getParentNode());
        assertNull(doc2root.getNamespaceURI());
        assertNull(doc2root.getPrefix());
        assertEquals("bar", doc2root.getLocalName());
        assertNull(doc2root.getFirstChild());
        Element doc3root = DomUtil.newDocument("foo", "bar");
        assertNotNull(doc3root);
        assertNotNull(doc3root.getOwnerDocument());
        assertSame(doc3root.getOwnerDocument(), doc3root.getParentNode());
        assertEquals("foo", doc3root.getNamespaceURI());
        assertNull(doc3root.getPrefix());
        assertEquals("bar", doc3root.getLocalName());
        assertNull(doc3root.getFirstChild());
        Element doc4root = DomUtil.newDocument(new QName("bar"));
        assertNotNull(doc4root);
        assertNotNull(doc4root.getOwnerDocument());
        assertSame(doc4root.getOwnerDocument(), doc4root.getParentNode());
        assertNull(doc4root.getNamespaceURI());
        assertNull(doc4root.getPrefix());
        assertEquals("bar", doc4root.getLocalName());
        assertNull(doc4root.getFirstChild());
        Element doc5root = DomUtil.newDocument(new QName("foo", "bar"));
        assertNotNull(doc5root);
        assertNotNull(doc5root.getOwnerDocument());
        assertSame(doc5root.getOwnerDocument(), doc5root.getParentNode());
        assertEquals("foo", doc5root.getNamespaceURI());
        assertNull(doc5root.getPrefix());
        assertEquals("bar", doc5root.getLocalName());
        assertNull(doc5root.getFirstChild());
        Element doc6root = DomUtil.newDocument(new QName("foo", "bar", "baz"));
        assertNotNull(doc6root);
        assertNotNull(doc6root.getOwnerDocument());
        assertSame(doc6root.getOwnerDocument(), doc6root.getParentNode());
        assertEquals("foo", doc6root.getNamespaceURI());
        assertEquals("baz", doc6root.getPrefix());
        assertEquals("bar", doc6root.getLocalName());
        assertEquals("baz:bar", doc6root.getNodeName());
        assertNull(doc6root.getFirstChild());
    }

    public void testAppendChild() throws Exception {
        Element root = DomUtil.newDocument("foo", "bar");
        Element child1 = DomUtil.appendChild(root, "baz");
        assertNotNull(child1);
        assertSame(root, child1.getParentNode());
        assertNull(child1.getNamespaceURI());
        assertNull(child1.getPrefix());
        assertEquals("baz", child1.getLocalName());
        Element child2 = DomUtil.appendChild(root, "argle", "bargle");
        assertNotNull(child2);
        assertSame(root, child2.getParentNode());
        assertEquals("argle", child2.getNamespaceURI());
        assertNull(child2.getPrefix());
        assertEquals("bargle", child2.getLocalName());
        Element child3 = DomUtil.appendChild(root, "argle", "w:wargle");
        assertNotNull(child3);
        assertSame(root, child3.getParentNode());
        assertEquals("argle", child3.getNamespaceURI());
        assertEquals("w", child3.getPrefix());
        assertEquals("wargle", child3.getLocalName());
        Element grandchild1 = DomUtil.appendChildInheritNamespace(child1, "qwe");
        assertNotNull(grandchild1);
        assertSame(child1, grandchild1.getParentNode());
        assertEquals(child1.getNamespaceURI(), grandchild1.getNamespaceURI());
        assertEquals(child1.getPrefix(), grandchild1.getPrefix());
        assertEquals("qwe", grandchild1.getLocalName());
        Element grandchild2 = DomUtil.appendChildInheritNamespace(child2, "asd");
        assertNotNull(grandchild2);
        assertSame(child2, grandchild2.getParentNode());
        assertEquals(child2.getNamespaceURI(), grandchild2.getNamespaceURI());
        assertEquals(child2.getPrefix(), grandchild2.getPrefix());
        assertEquals("asd", grandchild2.getLocalName());
        Element grandchild3 = DomUtil.appendChildInheritNamespace(child3, "zxc");
        assertNotNull(grandchild3);
        assertSame(child3, grandchild3.getParentNode());
        assertEquals(child3.getNamespaceURI(), grandchild3.getNamespaceURI());
        assertEquals(child3.getPrefix(), grandchild3.getPrefix());
        assertEquals("zxc", grandchild3.getLocalName());
    }

    public void testAppendChildWithInvalidQname() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child = DomUtil.appendChild(root, "MyNSURI", "argle:bargle");
        Element grandchild = DomUtil.appendChildInheritNamespace(child, "zippy:pinhead");
        assertEquals("zippy", grandchild.getPrefix());
    }

    public void testGetLocalName() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child1 = DomUtil.appendChild(root, "argle", "bargle");
        Element child2 = DomUtil.appendChild(root, "argle", "w:wargle");
        assertEquals("foo", DomUtil.getLocalName(root));
        assertEquals("bargle", DomUtil.getLocalName(child1));
        assertEquals("wargle", DomUtil.getLocalName(child2));
    }

    public void testGetSiblings() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child1 = DomUtil.appendChildInheritNamespace(root, "bargle");
        Element child2 = DomUtil.appendChild(root, "argle", "bargle");
        Element child3 = DomUtil.appendChild(root, "argle", "w:wargle");
        DomUtil.appendText(root, "should never be returned");
        List<Element> ret1 = DomUtil.getSiblings(root);
        assertEquals(1, ret1.size());
        assertSame(root, ret1.get(0));
        List<Element> ret2 = DomUtil.getSiblings(child1);
        assertEquals(3, ret2.size());
        assertSame(child1, ret2.get(0));
        assertSame(child2, ret2.get(1));
        assertSame(child3, ret2.get(2));
        List<Element> ret3 = DomUtil.getSiblings(child1, "bargle");
        assertEquals(2, ret3.size());
        assertSame(child1, ret3.get(0));
        assertSame(child2, ret3.get(1));
        List<Element> ret4 = DomUtil.getSiblings(child1, "wargle");
        assertEquals(1, ret4.size());
        assertSame(child3, ret4.get(0));
        List<Element> ret5 = DomUtil.getSiblings(child1, "argle", "bargle");
        assertEquals(1, ret5.size());
        assertSame(child2, ret5.get(0));
        List<Element> ret6 = DomUtil.getSiblings(child1, null, "bargle");
        assertEquals(1, ret6.size());
        assertSame(child1, ret6.get(0));
        List<Element> ret7 = DomUtil.getSiblings(child1, "fargle", "bargle");
        assertEquals(0, ret7.size());
    }

    public void testHasChildren() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element empty = DomUtil.appendChild(root, "empty");
        Element textOnly = DomUtil.appendChild(root, "textOnly");
        Element cdataOnly = DomUtil.appendChild(root, "cdataOnly");
        Element elemOnly = DomUtil.appendChild(root, "elemOnly");
        Element mixed = DomUtil.appendChild(root, "mixed");
        Element misc = DomUtil.appendChild(root, "nonElem");
        Document dom = root.getOwnerDocument();
        textOnly.appendChild(dom.createTextNode("blahblahblah"));
        cdataOnly.appendChild(dom.createCDATASection("blahblahblah"));
        elemOnly.appendChild(dom.createElement("foo"));
        elemOnly.appendChild(dom.createElement("bar"));
        mixed.appendChild(dom.createTextNode("blahblahblah"));
        mixed.appendChild(dom.createElement("foo"));
        mixed.appendChild(dom.createTextNode("blahblahblah"));
        mixed.appendChild(dom.createElement("bar"));
        misc.appendChild(dom.createComment("nothing to see here"));
        misc.appendChild(dom.createProcessingInstruction("blah", "blah"));
        assertFalse(DomUtil.hasChildren(empty));
        assertTrue(DomUtil.hasChildren(textOnly));
        assertTrue(DomUtil.hasChildren(cdataOnly));
        assertTrue(DomUtil.hasChildren(elemOnly));
        assertTrue(DomUtil.hasChildren(mixed));
        assertTrue(DomUtil.hasChildren(misc));
        assertFalse(DomUtil.hasElementChildren(empty));
        assertFalse(DomUtil.hasElementChildren(textOnly));
        assertFalse(DomUtil.hasElementChildren(cdataOnly));
        assertTrue(DomUtil.hasElementChildren(elemOnly));
        assertTrue(DomUtil.hasElementChildren(mixed));
        assertFalse(DomUtil.hasElementChildren(misc));
        assertFalse(DomUtil.hasTextChildren(empty));
        assertTrue(DomUtil.hasTextChildren(textOnly));
        assertTrue(DomUtil.hasTextChildren(cdataOnly));
        assertFalse(DomUtil.hasTextChildren(elemOnly));
        assertTrue(DomUtil.hasTextChildren(mixed));
        assertFalse(DomUtil.hasTextChildren(misc));
    }

    public void testGetChildrenOfElement() throws Exception {
        Element root = DomUtil.newDocument("foo");
        DomUtil.appendText(root, "bar");
        Element child1 = DomUtil.appendChild(root, "bargle");
        Element child2 = DomUtil.appendChild(root, "argle", "bargle");
        Element child3 = DomUtil.appendChild(root, "argle", "w:wargle");
        List<Element> ret1 = DomUtil.getChildren(root);
        assertEquals(3, ret1.size());
        assertSame(child1, ret1.get(0));
        assertSame(child2, ret1.get(1));
        assertSame(child3, ret1.get(2));
        List<Element> ret2 = DomUtil.getChildren(root, "bargle");
        assertEquals(2, ret2.size());
        assertSame(child1, ret2.get(0));
        assertSame(child2, ret2.get(1));
        List<Element> ret3 = DomUtil.getChildren(root, "argle", "bargle");
        assertEquals(1, ret3.size());
        assertSame(child2, ret3.get(0));
        List<Element> ret4 = DomUtil.getChildren(root, "argle", "wargle");
        assertEquals(1, ret4.size());
        assertSame(child3, ret4.get(0));
        assertNull(DomUtil.getChild(root, "bar"));
        assertNull(DomUtil.getChild(root, "fizzle", "bizzle"));
        assertSame(child1, DomUtil.getChild(root, "bargle"));
        assertSame(child2, DomUtil.getChild(root, "argle", "bargle"));
    }

    public void testGetChildrenOfDocument() throws Exception {
        Element root1 = DomUtil.newDocument("foo");
        Document dom1 = root1.getOwnerDocument();
        assertSame(root1, DomUtil.getChild(dom1, "foo"));
        assertNull(DomUtil.getChild(dom1, "bar"));
        List<Element> rslt1a = DomUtil.getChildren(dom1);
        assertEquals(1, rslt1a.size());
        assertSame(root1, rslt1a.get(0));
        List<Element> rslt1b = DomUtil.getChildren(dom1, "foo");
        assertEquals(1, rslt1b.size());
        assertSame(root1, rslt1b.get(0));
        List<Element> rslt1c = DomUtil.getChildren(dom1, "bar");
        assertEquals(0, rslt1c.size());
        Element root2 = DomUtil.newDocument("urn:bar", "foo");
        Document dom2 = root2.getOwnerDocument();
        assertSame(root2, DomUtil.getChild(dom2, "urn:bar", "foo"));
        assertNull(DomUtil.getChild(dom2, "urn:bar", "bar"));
        List<Element> rslt2a = DomUtil.getChildren(dom2);
        assertEquals(1, rslt2a.size());
        assertSame(root2, rslt2a.get(0));
        List<Element> rslt2b = DomUtil.getChildren(dom2, "urn:bar", "foo");
        assertEquals(1, rslt2b.size());
        assertSame(root2, rslt2b.get(0));
        List<Element> rslt2c = DomUtil.getChildren(dom2, "urn:bar", "bar");
        assertEquals(0, rslt2c.size());
        List<Element> rslt2d = DomUtil.getChildren(dom2, "bar");
        assertEquals(0, rslt2d.size());
    }

    public void testGetChildrenOfComment() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Comment comment = root.getOwnerDocument().createComment("blah blah blah");
        root.appendChild(comment);
        List<Element> result = DomUtil.getChildren(comment);
        assertEquals(0, result.size());
    }

    public void testGetAttributes() throws Exception {
        Element root = DomUtil.newDocument("foo");
        List<Attr> list1 = DomUtil.getAttributes(root);
        assertEquals(0, list1.size());
        root.setAttributeNS("foo", "bar", "baz");
        List<Attr> list2 = DomUtil.getAttributes(root);
        assertEquals(1, list2.size());
        Attr attr2 = list2.get(0);
        assertEquals("foo", attr2.getNamespaceURI());
        assertEquals("bar", attr2.getLocalName());
        assertEquals("baz", attr2.getValue());
        root.setAttribute("argle", "bargle");
        List<Attr> list3 = DomUtil.getAttributes(root);
        assertEquals(2, list3.size());
    }

    public void testGetAttributeLocalName() throws Exception {
        Element root = DomUtil.newDocument("root");
        Element child1 = DomUtil.appendChild(root, "child1");
        child1.setAttributeNS("baz", "foo", "bar");
        Attr attr1 = (Attr) child1.getAttributes().item(0);
        assertEquals("foo", DomUtil.getLocalName(attr1));
        Element child2 = DomUtil.appendChild(root, "child2");
        child2.setAttributeNS("baz", "baz:foo", "bar");
        Attr attr2 = (Attr) child1.getAttributes().item(0);
        assertEquals("foo", DomUtil.getLocalName(attr2));
        Element child3 = DomUtil.appendChild(root, "child3");
        child3.setAttribute("foo", "bar");
        Attr attr3 = (Attr) child3.getAttributes().item(0);
        assertEquals("foo", DomUtil.getLocalName(attr3));
    }

    public void testAppendText() throws Exception {
        Element root = DomUtil.newDocument("root");
        Text text = DomUtil.appendText(root, "blah blah");
        assertSame(text, root.getChildNodes().item(0));
    }

    public void testGetText() throws Exception {
        String t1 = "argle";
        String t2 = "bargle";
        String t3 = "wargle";
        Element root = DomUtil.newDocument("foo");
        assertNull(DomUtil.getText(root));
        DomUtil.appendText(root, t1);
        DomUtil.appendText(root, t2);
        assertEquals(t1 + t2, DomUtil.getText(root));
        Element child = DomUtil.appendChild(root, "bar");
        DomUtil.appendText(child, t3);
        assertEquals(t1 + t2 + t3, root.getTextContent());
        assertEquals(t1 + t2, DomUtil.getText(root));
        assertEquals(t3, DomUtil.getText(child));
    }

    public void testGetSetTextWithCData() throws Exception {
        String t1 = "argle";
        String t2 = "bargle";
        Element root = DomUtil.newDocument("root");
        Document dom = root.getOwnerDocument();
        CDATASection cdata = dom.createCDATASection(t1);
        root.appendChild(cdata);
        assertEquals(t1, DomUtil.getText(root));
        assertEquals(t1, root.getTextContent());
        DomUtil.setText(root, t2);
        assertEquals(t2, DomUtil.getText(root));
        assertEquals(t2, root.getTextContent());
    }

    public void testSetText() throws Exception {
        String t1 = "argle";
        String t2 = "bargle";
        String t3 = "wargle";
        Element root = DomUtil.newDocument("foo");
        Element child = DomUtil.appendChild(root, "bar");
        DomUtil.appendText(root, t1);
        DomUtil.appendText(child, t2);
        DomUtil.setText(root, t3);
        assertEquals(t3, DomUtil.getText(root));
        assertEquals(t2, DomUtil.getText(child));
        assertEquals(t2 + t3, root.getTextContent());
    }

    public void testTrimText() throws Exception {
        final String TEXT1 = "TrimMe";
        final String TEXT1_WS = "   " + TEXT1 + "   ";
        final String TEXT2_WS = "    ";
        Element root = DomUtil.newDocument("foo");
        Element child1 = DomUtil.appendChildInheritNamespace(root, "foo");
        Element child2 = DomUtil.appendChildInheritNamespace(root, "foo");
        DomUtil.setText(child1, TEXT1_WS);
        DomUtil.setText(child2, TEXT2_WS);
        assertEquals(TEXT1_WS, child1.getTextContent());
        assertEquals(TEXT2_WS, child2.getTextContent());
        DomUtil.trimTextRecursive(child1);
        assertEquals(TEXT1, child1.getTextContent());
        assertEquals(TEXT2_WS, child2.getTextContent());
        DomUtil.trimTextRecursive(root.getOwnerDocument());
        assertEquals(TEXT1, child1.getTextContent());
        assertEquals("", child2.getTextContent());
        assertEquals(0, child2.getChildNodes().getLength());
    }

    public void testRemoveEmptyText() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child1 = DomUtil.appendChildInheritNamespace(root, "foo");
        Element child2 = DomUtil.appendChildInheritNamespace(root, "foo");
        DomUtil.setText(child1, "foo");
        DomUtil.setText(child2, "   ");
        DomUtil.setText(root, "bar");
        assertEquals("foo   bar", root.getTextContent());
        DomUtil.removeEmptyTextRecursive(root);
        assertEquals("foobar", root.getTextContent());
    }

    public void testIsNamed() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child = DomUtil.appendChild(root, "bar", "argle:bargle");
        assertTrue(DomUtil.isNamed(root, null, "foo"));
        assertTrue(DomUtil.isNamed(child, "bar", "bargle"));
        assertFalse(DomUtil.isNamed(root, null, "blimey"));
        assertFalse(DomUtil.isNamed(child, "bar", "blimey"));
        assertFalse(DomUtil.isNamed(child, "bar", "argle:bargle"));
        assertFalse(DomUtil.isNamed(child, null, "bargle"));
        try {
            assertFalse(DomUtil.isNamed(root, null, null));
            fail("accepted null localName");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testToList() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Element child1 = DomUtil.appendChild(root, "foo");
        DomUtil.setText(root, "blah blah blah");
        Element child2 = DomUtil.appendChild(root, "foo");
        List<Element> result = DomUtil.asList(root.getElementsByTagName("*"), Element.class);
        assertEquals(2, result.size());
        assertSame(child1, result.get(0));
        assertSame(child2, result.get(1));
    }

    public void testFilterNodeList() throws Exception {
        Element root = DomUtil.newDocument("foo");
        Document dom = root.getOwnerDocument();
        Text child1 = dom.createTextNode("argle");
        root.appendChild(child1);
        Element child2 = dom.createElement("bar");
        root.appendChild(child2);
        Comment child3 = dom.createComment("blah blah blah");
        root.appendChild(child3);
        Text child4 = dom.createTextNode("wargle");
        root.appendChild(child4);
        Element child5 = dom.createElement("baz");
        root.appendChild(child5);
        NodeList list = root.getChildNodes();
        List<Element> elems = DomUtil.filter(list, Element.class);
        assertEquals(2, elems.size());
        assertSame(child2, elems.get(0));
        assertSame(child5, elems.get(1));
        List<Text> texts = DomUtil.filter(list, Text.class);
        assertEquals(2, texts.size());
        assertSame(child1, texts.get(0));
        assertSame(child4, texts.get(1));
        List<Comment> comments = DomUtil.filter(list, Comment.class);
        assertEquals(1, comments.size());
        assertSame(child3, comments.get(0));
        List<Document> docs = DomUtil.filter(list, Document.class);
        assertEquals(0, docs.size());
    }
}
