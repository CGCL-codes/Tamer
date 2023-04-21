package xcordion.lang.java;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;
import xcordion.util.DocumentFactory;

public class JDomTestDocumentTest {

    private static final String FOO_URI = "http://www.foo.int/";

    private static final String TEST_DOCUMENT = "<xml xmlns:foo=\"" + FOO_URI + "\">\n" + "\t<a>Apple</a>\n" + "\t<b color=\"red\">Ball</b>\n" + "\t<c>Cookie <x>(good enough for me)</x></c>\n" + "\t<d foo:bar=\"baz\" foo:answer=\"42\">Deoxyribonucleic Acid</d>\n" + "\t<e>  This  text\n" + "\t\tcontains rubbish whitespace!</e>\n" + "</xml>";

    private JDomTestDocument testDoc;

    private static final String SIMPLE_TEST_DOC = "<xml><z>One</z><z>Two</z><z>Three</z></xml>";

    private JDomTestDocument simpleTestDoc;

    @Before
    public void setUp() throws Exception {
        testDoc = new JDomTestDocument(DocumentFactory.document(TEST_DOCUMENT));
        simpleTestDoc = new JDomTestDocument(DocumentFactory.document(SIMPLE_TEST_DOC));
    }

    @Test
    public void testSimpleHappyPath() throws Exception {
        JDomTestDocument.JDomTestElement rootElement = testDoc.getRootElement();
        assertEquals("xml", rootElement.getLocalName());
        assertEquals(5, rootElement.getChildren().size());
        JDomTestDocument.JDomTestElement a = rootElement.getChildren().get(0);
        assertEquals("a", a.getLocalName());
        assertEquals("Apple", a.getValue());
        JDomTestDocument.JDomTestElement b = rootElement.getChildren().get(1);
        assertEquals("b", b.getLocalName());
        assertEquals("Ball", b.getValue());
        assertEquals("red", b.getAttribute(null, "color"));
        JDomTestDocument.JDomTestElement c = rootElement.getChildren().get(2);
        assertEquals("c", c.getLocalName());
        assertEquals("Cookie (good enough for me)", c.getValue());
        assertEquals(1, c.getChildren().size());
        JDomTestDocument.JDomTestElement x = c.getChildren().get(0);
        assertEquals("x", x.getLocalName());
        assertEquals("(good enough for me)", x.getValue());
        JDomTestDocument.JDomTestElement d = rootElement.getChildren().get(3);
        assertEquals("d", d.getLocalName());
        assertEquals("Deoxyribonucleic Acid", d.getValue());
        assertEquals("baz", d.getAttribute(FOO_URI, "bar"));
        assertEquals(42, d.getIntAttribute(FOO_URI, "answer"));
    }

    @Test
    public void testWhitespace() throws Exception {
        assertEquals("This text contains rubbish whitespace!", testDoc.getRootElement().getChildren().get(4).getValue());
    }

    @Test
    public void testSetText() throws Exception {
        JDomTestDocument.JDomTestElement c = testDoc.getRootElement().getChildren().get(2);
        c.getChildren().get(0).setText("(a sometimes food)");
        assertEquals("<c>Cookie <x>(a sometimes food)</x></c>", c.asXml());
        assertEquals("Cookie (good enough for me)", c.getValue());
    }

    @Test
    public void testInsertChildAfter() {
        JDomTestDocument.JDomTestElement b = testDoc.getRootElement().getChildren().get(1);
        JDomTestDocument.JDomTestElement newElement = testDoc.newElement("new");
        newElement.setText("this is new");
        testDoc.getRootElement().insertChildAfter(b, newElement);
        String testDocXml = testDoc.asXml();
        assertTrue(testDocXml, testDocXml.contains("</b><new>this is new</new>"));
    }

    @Test
    public void testRemove() {
        JDomTestDocument.JDomTestElement c = testDoc.getRootElement().getChildren().get(2);
        JDomTestDocument.JDomTestElement x = c.getChildren().get(0);
        c.remove(x);
        assertEquals("<c>Cookie </c>", c.asXml());
    }

    @Test
    public void testGetFirstChildNamed() throws JDOMException {
        assertEquals("One", simpleTestDoc.getRootElement().getFirstChildNamed("z").getValue());
    }

    @Test
    public void testPrependChild() {
        JDomTestDocument.JDomTestElement y = simpleTestDoc.newElement("y");
        y.setText("hello");
        simpleTestDoc.getRootElement().prependChild(y);
        assertEquals("<xml><y>hello</y><z>One</z><z>Two</z><z>Three</z></xml>", simpleTestDoc.getRootElement().asXml());
    }

    @Test
    public void testAppendChild() {
        JDomTestDocument.JDomTestElement y = simpleTestDoc.newElement("y");
        y.setText("hello");
        simpleTestDoc.getRootElement().appendChild(y);
        assertEquals("<xml><z>One</z><z>Two</z><z>Three</z><y>hello</y></xml>", simpleTestDoc.getRootElement().asXml());
    }

    @Test
    public void testGetIntAttribute() {
        assertEquals(42, testDoc.getRootElement().getChildren().get(3).getIntAttribute(FOO_URI, "answer"));
        assertEquals(1, testDoc.getRootElement().getChildren().get(3).getIntAttribute("answer"));
    }
}
