package be.lassi.pdf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.n3.nanoxml.NonValidator;
import net.n3.nanoxml.StdXMLParser;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class HtmlToPdfTranslatorTestCase {

    private final List<Element> elements = new ArrayList<Element>();

    @BeforeMethod
    public void before() {
        elements.clear();
    }

    @Test
    public void testParagraph() throws Exception {
        parse("<p>one</p><p>two</p>");
        assertEquals(elements.size(), 2);
        assertTrue(elements.get(0) instanceof Paragraph);
        assertTrue(elements.get(1) instanceof Paragraph);
        Paragraph paragraph1 = (Paragraph) elements.get(0);
        Paragraph paragraph2 = (Paragraph) elements.get(1);
        assertEquals(paragraph1.spacingBefore(), 5f);
        assertEquals(paragraph1.spacingAfter(), 5f);
        assertEquals(paragraph1.getFont().getFamily(), Font.HELVETICA);
        assertEquals(paragraph1.getFont().getSize(), 10f);
        assertEquals(paragraph1.getChunks().size(), 1);
        Chunk chunk1 = (Chunk) paragraph1.getChunks().get(0);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(paragraph2.getChunks().size(), 1);
        Chunk chunk2 = (Chunk) paragraph2.getChunks().get(0);
        assertEquals(chunk2.getContent(), "two");
    }

    @Test
    public void testHeader() throws Exception {
        parse("<h1>one</h1>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getLeading(), 50f);
        assertEquals(paragraph.spacingAfter(), 30f);
        assertEquals(paragraph.getFont().getFamily(), Font.HELVETICA);
        assertEquals(paragraph.getFont().getSize(), 20f);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "1");
        assertEquals(chunk2.getContent(), "   ");
        assertEquals(chunk3.getContent(), "one");
    }

    @Test
    public void testPre() throws Exception {
        parse("<pre> one\ntwo\n&amp;three </pre>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph1 = (Paragraph) elements.get(0);
        assertEquals(paragraph1.getFont().getFamily(), Font.COURIER);
        assertEquals(paragraph1.getFont().getSize(), 10f);
        assertEquals(paragraph1.spacingBefore(), 5f);
        assertEquals(paragraph1.spacingAfter(), 5f);
        assertEquals(paragraph1.getChunks().size(), 1);
        Chunk chunk1 = (Chunk) paragraph1.getChunks().get(0);
        assertEquals(chunk1.getContent(), " one\ntwo\n&three ");
    }

    @Test
    public void testPreEliminateNewLines() throws Exception {
        testPreEliminateNewLines("<pre>\none\ntwo\nthree\n</pre>", "one\ntwo\nthree");
        testPreEliminateNewLines("<pre>one\ntwo\nthree\n</pre>", "one\ntwo\nthree");
        testPreEliminateNewLines("<pre>\none\ntwo\nthree</pre>", "one\ntwo\nthree");
        testPreEliminateNewLines("<pre>\n\none\ntwo\nthree\n\n</pre>", "\none\ntwo\nthree\n");
    }

    private void testPreEliminateNewLines(final String html, final String expected) throws Exception {
        elements.clear();
        parse(html);
        Paragraph paragraph = (Paragraph) elements.get(0);
        Chunk chunk = (Chunk) paragraph.getChunks().get(0);
        assertEquals(chunk.getContent(), expected);
    }

    @Test
    public void testBold() throws Exception {
        parse("<p>one<b>two</b>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
        assertEquals(chunk3.getContent(), "three");
        assertEquals(chunk1.getFont().getStyle(), Font.NORMAL);
        assertEquals(chunk2.getFont().getStyle(), Font.BOLD);
        assertEquals(chunk3.getFont().getStyle(), Font.NORMAL);
    }

    @Test
    public void testItalic() throws Exception {
        parse("<p>one<i>two</i>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
        assertEquals(chunk3.getContent(), "three");
        assertEquals(chunk1.getFont().getStyle(), Font.NORMAL);
        assertEquals(chunk2.getFont().getStyle(), Font.ITALIC);
        assertEquals(chunk3.getFont().getStyle(), Font.NORMAL);
    }

    @Test
    public void testBoldItalic() throws Exception {
        parse("<p>one<b><i>two</i></b>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
        assertEquals(chunk3.getContent(), "three");
        assertEquals(chunk1.getFont().getStyle(), Font.NORMAL);
        assertEquals(chunk2.getFont().getStyle(), Font.BOLD | Font.ITALIC);
        assertEquals(chunk3.getFont().getStyle(), Font.NORMAL);
    }

    @Test
    public void testAnchor() throws Exception {
        parse("<p>one<a href=\"#bla\">two</a>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
        assertEquals(chunk3.getContent(), "three");
    }

    @Test
    public void testAnchor2() throws Exception {
        parse("<p>one<a name=\"bla\">two</a>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 3);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        Chunk chunk3 = (Chunk) paragraph.getChunks().get(2);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
        assertEquals(chunk3.getContent(), "three");
    }

    @Test
    public void testAnchor3() throws Exception {
        parse("<p>one<a name=\"bla\"></a>three</p>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Paragraph);
        Paragraph paragraph = (Paragraph) elements.get(0);
        assertEquals(paragraph.getChunks().size(), 2);
        Chunk chunk1 = (Chunk) paragraph.getChunks().get(0);
        Chunk chunk2 = (Chunk) paragraph.getChunks().get(1);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "three");
    }

    @Test
    public void testList() throws Exception {
        parse("<ol><li>one</li><li>two</li></ol>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof com.lowagie.text.List);
        com.lowagie.text.List list = (com.lowagie.text.List) elements.get(0);
        assertEquals(list.getItems().size(), 2);
        ListItem item1 = (ListItem) list.getItems().get(0);
        ListItem item2 = (ListItem) list.getItems().get(1);
        assertEquals(item1.getFont().getFamily(), Font.HELVETICA);
        assertEquals(item1.getFont().getSize(), 10f);
        assertEquals(item1.getChunks().size(), 2);
        Chunk chunk1 = (Chunk) item1.getChunks().get(0);
        Chunk chunk2 = (Chunk) item2.getChunks().get(0);
        assertEquals(chunk1.getContent(), "one");
        assertEquals(chunk2.getContent(), "two");
    }

    @Test
    public void testTable() throws Exception {
        parse("<table><tr><td>11</td><td>12</td></tr><tr><td>21</td><td>22</td></tr></table>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof PdfPTable);
        PdfPTable table = (PdfPTable) elements.get(0);
        assertEquals(table.size(), 2);
        assertEquals(table.getNumberOfColumns(), 2);
        PdfPCell[] cells1 = table.getRow(0).getCells();
        PdfPCell[] cells2 = table.getRow(1).getCells();
        Paragraph cell11 = (Paragraph) cells1[0].getCompositeElements().get(0);
        Paragraph cell12 = (Paragraph) cells1[1].getCompositeElements().get(0);
        Paragraph cell21 = (Paragraph) cells2[0].getCompositeElements().get(0);
        Paragraph cell22 = (Paragraph) cells2[1].getCompositeElements().get(0);
        String string11 = cell11.getContent();
        String string12 = cell12.getContent();
        String string21 = cell21.getContent();
        String string22 = cell22.getContent();
        assertEquals(string11, "11");
        assertEquals(string12, "12");
        assertEquals(string21, "21");
        assertEquals(string22, "22");
    }

    @Test
    public void testTableParagraphs() throws Exception {
        parse("<table><tr><td><p>one</p><p>two</p></td></tr></table>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof PdfPTable);
        PdfPTable table = (PdfPTable) elements.get(0);
        assertEquals(table.size(), 1);
        assertEquals(table.getNumberOfColumns(), 1);
        List<Object> objects = cellElements(table, 0, 0);
        assertEquals(objects.size(), 2);
        Paragraph p1 = (Paragraph) objects.get(0);
        Paragraph p2 = (Paragraph) objects.get(1);
        assertEquals(p1.getContent(), "one");
        assertEquals(p2.getContent(), "two");
    }

    @Test
    public void testTableImage() throws Exception {
        parse("<table><tr><td><p>one</p><img src=\"audio.png\" alt=\"test\"/><p>two</p></td></tr></table>");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof PdfPTable);
        PdfPTable table = (PdfPTable) elements.get(0);
        assertEquals(table.size(), 1);
        assertEquals(table.getNumberOfColumns(), 1);
        List<Object> objects = cellElements(table, 0, 0);
        assertEquals(objects.size(), 3);
        Paragraph p1 = (Paragraph) objects.get(0);
        Paragraph p3 = (Paragraph) objects.get(2);
        assertEquals(p1.getContent(), "one");
        assertEquals(p3.getContent(), "two");
        PdfPTable table2 = (PdfPTable) objects.get(1);
        assertEquals(table2.size(), 1);
        assertEquals(table2.getNumberOfColumns(), 1);
        Image image = table2.getRow(0).getCells()[0].getImage();
        assertEquals(image.getAlt(), "test");
    }

    /**
     * Tests TableInfo.getColumnCount().
     */
    @Test
    public void testTableDetermineColumnCount() throws Exception {
        assertTableColumnCount("<table><tr><td>1</td></tr></table>", 1);
        assertTableColumnCount("<table><tr><td>1</td><td>2</td><td>3</td></tr></table>", 3);
        assertTableColumnCount("<table><tr><td colspan=\"3\">123</td></tr></table>", 3);
        assertTableColumnCount("<table><tr><td colspan=\"2\">12</td><td>3</td></tr></table>", 3);
    }

    private void assertTableColumnCount(final String html, final int expectedColumnCount) throws Exception {
        elements.clear();
        parse(html);
        PdfPTable table = (PdfPTable) elements.get(0);
        assertEquals(table.getNumberOfColumns(), expectedColumnCount);
    }

    @Test
    public void testSpaces() throws Exception {
        parse("<p>one two\nthree</p>");
        assertChunkContent("one two three");
    }

    @Test
    public void testNewLine() throws Exception {
        parse("<p>one\ntwo</p>");
        assertChunkContent("one two");
    }

    @Test
    public void testEntityNbsp() throws IOException, XMLException {
        parse("<p>one &nbsp; two</p>");
        assertChunkContent("one   two");
    }

    @Test
    public void testEntityAmp() throws Exception {
        parse("<p>one &amp; two</p>");
        assertChunkContent("one & two");
    }

    @Test
    public void testImage() throws Exception {
        parse("<img src=\"audio.png\" width=\"425\" height=\"255\" alt=\"test\" />");
        assertEquals(elements.size(), 1);
        assertTrue(elements.get(0) instanceof Image);
        Image image = (Image) elements.get(0);
        assertEquals(image.getWidth(), 425, 1f);
        assertEquals(image.getHeight(), 255, 1f);
        assertEquals(image.getPlainWidth(), 318.75f, 1f);
        assertEquals(image.getPlainHeight(), 191.25f, 1f);
    }

    @SuppressWarnings("unchecked")
    private List<Object> cellElements(final PdfPTable table, final int row, final int column) {
        return table.getRow(row).getCells()[column].getCompositeElements();
    }

    private void assertChunkContent(final String expected) {
        Paragraph paragraph = (Paragraph) elements.get(0);
        Chunk chunk = (Chunk) paragraph.getChunks().get(0);
        assertEquals(chunk.getContent(), expected);
    }

    private void parse(final String string) throws IOException, XMLException {
        MockDocListener document = new MockDocListener();
        TableOfContents toc = new TableOfContents();
        HtmlToPdfTranslator handler = new HtmlToPdfTranslator(document, toc);
        StdXMLParser parser = new StdXMLParser();
        parser.setValidator(new NonValidator());
        parser.setBuilder(handler);
        parser.setResolver(new EntityResolver());
        Reader reader = new StringReader(string);
        try {
            StdXMLReader xmlReader = new StdXMLReader(reader);
            xmlReader.setSystemID("filename");
            parser.setReader(xmlReader);
            parser.parse();
        } finally {
            reader.close();
        }
    }

    private class MockDocListener extends DefaultDocListener {

        @Override
        public boolean add(final Element element) throws DocumentException {
            return elements.add(element);
        }
    }
}
