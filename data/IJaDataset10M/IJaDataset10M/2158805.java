package com.google.gxp.compiler.parser;

import com.google.common.collect.Iterables;
import com.google.gxp.compiler.alerts.Alert;
import com.google.gxp.compiler.alerts.AlertSet;
import com.google.gxp.compiler.alerts.common.SaxAlert;
import com.google.gxp.compiler.base.Node;
import com.google.gxp.compiler.fs.FileRef;
import com.google.gxp.compiler.fs.FileSystem;
import com.google.gxp.compiler.fs.InMemoryFileSystem;
import com.google.gxp.compiler.schema.BuiltinSchemaFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import junit.framework.TestCase;
import org.xml.sax.SAXParseException;

/**
 * Tests for {@link Parser}.
 */
public class ParserTest extends TestCase {

    private FileSystem sourcePathFs;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        sourcePathFs = new InMemoryFileSystem();
        addFileToSourceFs("/foo", "[file /foo]");
        addFileToSourceFs("/bar", "[file /bar]");
    }

    @Override
    public void tearDown() throws Exception {
        sourcePathFs = null;
        super.tearDown();
    }

    private String createSourceFileName() {
        String packageName = getClass().getPackage().getName();
        return "/" + packageName.replace('.', '/') + "/" + getName() + ".gxp";
    }

    private ParseTree parse(String s) throws Exception {
        SourceEntityResolver entityResolver = new FileSystemEntityResolver(sourcePathFs);
        Parser parser = new Parser(BuiltinSchemaFactory.INSTANCE, SaxXmlParser.INSTANCE, entityResolver);
        FileRef fileRef = addFileToSourceFs(createSourceFileName(), s);
        return parser.parse(fileRef);
    }

    public void testEmpty() throws Exception {
        ParseTree tree = parse("");
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertTrue(alert instanceof SaxAlert);
    }

    public void testUnknownElementNamespace() throws Exception {
        ParseTree tree = parse("<gxp:template " + "xmlns:gxp='http://google.com/i/dont/really/exist'/>");
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertTrue(alert instanceof UnknownNamespaceError);
    }

    public void testUnknownElement() throws Exception {
        ParseTree tree = parse("<gxp:idontexist xmlns:gxp='http://google.com/2001/gxp'/>");
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertTrue(alert instanceof UnknownElementError);
    }

    public void testNoNamespaceElement() throws Exception {
        ParseTree tree = parse("<idonthaveanamespace/>");
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertTrue(alert instanceof NoNamespaceError);
    }

    public void testBaseCase() throws Exception {
        ParseTree tree = parse("<gxp:template " + "xmlns:gxp='http://google.com/2001/gxp'/>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertTrue(tree.getChildren().get(0).getChildren().isEmpty());
        assertTrue(tree.getChildren().get(0).getAttributes().isEmpty());
    }

    public void testAttribute() throws Exception {
        ParseTree tree = parse("<gxp:template " + "xmlns:gxp='http://google.com/2001/gxp' name='foo.Bar'/>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertTrue(tree.getChildren().get(0).getChildren().isEmpty());
        assertEquals(1, tree.getChildren().get(0).getAttributes().size());
        ParsedAttribute attr = tree.getChildren().get(0).getAttributes().get(0);
        assertEquals(NullNamespace.INSTANCE, attr.getNamespace());
        assertEquals("name", attr.getName());
        assertEquals("'name' attribute", attr.getDisplayName());
        assertEquals("foo.Bar", attr.getValue());
    }

    /**
   * Tests that attribute ordering is preserved for a specific ordering.
   * (happens to use 'name' and 'gxp:ispace' attributes)
   *
   * @param nameFirst if true, then the name attribute is first, otherwise it's
   * last.
   */
    public void attributeOrderingHelper(boolean nameFirst) throws Exception {
        String nameAttr = " name='MyName'";
        String iSpaceAttr = " gxp:ispace='preserve'";
        ParseTree tree = parse("<gxp:param" + " xmlns:gxp='http://google.com/2001/gxp'" + (nameFirst ? (nameAttr + iSpaceAttr) : (iSpaceAttr + nameAttr)) + "/>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertTrue(tree.getChildren().get(0).getChildren().isEmpty());
        assertEquals(2, tree.getChildren().get(0).getAttributes().size());
        ParsedAttribute attr = tree.getChildren().get(0).getAttributes().get(nameFirst ? 0 : 1);
        assertEquals(NullNamespace.INSTANCE, attr.getNamespace());
        assertEquals("name", attr.getName());
        assertEquals("'name' attribute", attr.getDisplayName());
        assertEquals("MyName", attr.getValue());
        attr = tree.getChildren().get(0).getAttributes().get(nameFirst ? 1 : 0);
        assertEquals(GxpNamespace.INSTANCE, attr.getNamespace());
        assertEquals("ispace", attr.getName());
        assertEquals("'gxp:ispace' attribute", attr.getDisplayName());
        assertEquals("preserve", attr.getValue());
    }

    /**
   * Tests that attribute ordering is preserved.
   */
    public void testAttributeOrdering() throws Exception {
        attributeOrderingHelper(true);
        attributeOrderingHelper(false);
    }

    public void testWithDoctype() throws Exception {
        ParseTree tree = parse("<!DOCTYPE gxp:template SYSTEM " + "\"http://gxp.googlecode.com/svn/trunk/resources/xhtml.ent\">\n" + "<gxp:template " + "xmlns:gxp='http://google.com/2001/gxp'/>");
        assertEquals(1, tree.getAlerts().size());
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertEquals("Resolved entity `http://gxp.googlecode.com/svn/trunk/resources/xhtml.ent`" + " to `/com/google/gxp/compiler/parser/xhtml.ent`", alert.getMessage());
    }

    public void testStartEnd() throws Exception {
        ParseTree tree = parse("<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + "</gxp:template>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertTrue(tree.getChildren().get(0).getChildren().isEmpty());
    }

    public void testMismatchedStartEnd() throws Exception {
        ParseTree tree = parse("<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + "</gxp:if>");
        assertFalse(tree.getAlerts().isEmpty());
    }

    public void testTextChild() throws Exception {
        ParseTree tree = parse("<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + "   " + "</gxp:template>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertEquals(1, tree.getChildren().get(0).getChildren().size());
        Node textNode = tree.getChildren().get(0).getChildren().get(0);
        assertTrue(textNode instanceof TextElement);
        assertEquals("   ", ((TextElement) textNode).getText());
    }

    public void testCharRefInChild() throws Exception {
        ParseTree tree = parse("<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + " &lt; " + "</gxp:template>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertEquals(1, tree.getChildren().get(0).getChildren().size());
        assertTrue(tree.getChildren().get(0).getChildren().get(0) instanceof TextElement);
        TextElement textNode = ((TextElement) tree.getChildren().get(0).getChildren().get(0));
        assertEquals(" < ", textNode.getText());
    }

    public void testNestedElements() throws Exception {
        ParseTree tree = parse("<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + "<gxp:if/>" + " &#128; " + "<gxp:eval/>" + "</gxp:template>");
        assertEquals(AlertSet.EMPTY, tree.getAlerts());
        assertEquals(3, tree.getChildren().get(0).getChildren().size());
        Node ifElement = tree.getChildren().get(0).getChildren().get(0);
        assertTrue(ifElement instanceof GxpNamespace.GxpElement);
        assertTrue(((GxpNamespace.GxpElement) ifElement).getElementType() == GxpNamespace.ElementType.IF);
        Node textElement = tree.getChildren().get(0).getChildren().get(1);
        assertTrue(textElement instanceof TextElement);
        assertEquals(" \200 ", ((TextElement) textElement).getText());
        Node exprElement = tree.getChildren().get(0).getChildren().get(2);
        assertTrue(exprElement instanceof GxpNamespace.GxpElement);
        assertTrue(((GxpNamespace.GxpElement) exprElement).getElementType() == GxpNamespace.ElementType.EVAL);
    }

    public void testEntityResolution() throws Exception {
        ParseTree tree = parse("<!DOCTYPE gxp:template [" + "  <!ENTITY foo PUBLIC '//foo' 'http://www/~someone/foo'>\n" + "  <!ENTITY bar PUBLIC '//bar' 'http://www/~someone/bar'>\n" + "]>" + "<gxp:template xmlns:gxp='http://google.com/2001/gxp'>" + "  &foo;" + "  &bar;" + "</gxp:template>");
        Iterator<Alert> alerts = tree.getAlerts().iterator();
        Alert alert1 = alerts.next();
        assertEquals("Resolved entity `//foo` to `/foo`", alert1.getMessage());
        Alert alert2 = alerts.next();
        assertFalse(alerts.hasNext());
        assertEquals("Resolved entity `//bar` to `/bar`", alert2.getMessage());
        Node textElement = tree.getChildren().get(0).getChildren().get(0);
        assertEquals("  [file /foo]  [file /bar]", ((TextElement) textElement).getText());
    }

    public void testUndeclaredEntityResolution() throws Exception {
        ParseTree tree = parse("<!DOCTYPE gxp:template [\n" + "  <!ENTITY foo PUBLIC '//foo' 'http://www/~someone/foo'>\n" + "  <!ENTITY bar PUBLIC '//bar' 'http://www/~someone/bar'>\n" + "]>\n" + "<gxp:template xmlns:gxp='http://google.com/2001/gxp'>\n" + "  start\n" + "  &baz;\n" + "  end\n" + "</gxp:template>");
        assertEquals(1, tree.getAlerts().size());
        Alert alert = Iterables.getOnlyElement(tree.getAlerts());
        assertEquals(SAXParseException.class.getName() + ": The entity \"baz\" was referenced, but not declared.", alert.getMessage());
        assertEquals("/com/google/gxp/compiler/parser/testUndeclaredEntityResolution.gxp:7:3:7:3", alert.getSourcePosition().toString());
    }

    private void assertElementExists(String ns, String name) throws Exception {
        ParseTree tree = parse(String.format("<%s xmlns='%s'/>", name, ns));
        assertTrue(String.format("No such element '%s' in '%s'.", name, ns), tree.getAlerts().isEmpty());
    }

    private static final String GXP_NS = "http://google.com/2001/gxp";

    private static final String CALL_NS = "http://google.com/2001/gxp/call";

    private static final String HTML_NS = "http://www.w3.org/1999/xhtml";

    public void testElementsExist() throws Exception {
        assertElementExists(GXP_NS, "attr");
        assertElementExists(GXP_NS, "eval");
        assertElementExists(GXP_NS, "if");
        assertElementExists(GXP_NS, "elif");
        assertElementExists(GXP_NS, "else");
        assertElementExists(GXP_NS, "import");
        assertElementExists(GXP_NS, "abbr");
        assertElementExists(GXP_NS, "loop");
        assertElementExists(GXP_NS, "param");
        assertElementExists(GXP_NS, "template");
        assertElementExists(GXP_NS, "throws");
        assertElementExists(GXP_NS, "typeparam");
        assertElementExists(GXP_NS, "msg");
        assertElementExists(GXP_NS, "nomsg");
        assertElementExists(GXP_NS, "ph");
        assertElementExists(GXP_NS, "eph");
        assertElementExists(CALL_NS, "Whatever");
        assertElementExists(CALL_NS, "com.google.foo.Bar");
        assertElementExists(HTML_NS, "img");
    }

    private FileRef addFileToSourceFs(String path, String content) throws IOException {
        FileRef fileRef = sourcePathFs.parseFilename(path);
        OutputStream out = fileRef.openOutputStream();
        try {
            Writer outW = new OutputStreamWriter(out, "UTF-8");
            outW.write(content);
            outW.flush();
        } finally {
            out.close();
        }
        return fileRef;
    }
}
