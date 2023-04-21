package ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.xml;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.beans.XMLEncoder;
import java.sql.Timestamp;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import org.junit.Before;
import org.junit.Test;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.history.HTMLResultDoc;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.structure.TableCell;

public class ConverterTest {

    TableCell cell;

    @Before
    public void setUp() throws Exception {
        cell = new TableCell();
        cell.set("Hello");
        cell.putAttribute("id", "hello");
        cell.putStyle("display", "block");
    }

    @Test
    public void whatGoesInComesOut() throws Exception {
        String xml = Converter.toXml(cell);
        TableCell conversion = (TableCell) Converter.fromXml(xml);
        assertEquals(cell, conversion);
    }

    @Test
    public void compansateForNonStandardAttributeFormat() throws Exception {
        String xml = "<td id=hello>hello</td>";
        TableCell cell = (TableCell) Converter.fromXml(xml);
        assertNotNull(cell);
    }

    @Test
    public void getsWholeContents() throws Exception {
        String xml = "<td id=hello>hello<hr /><span id=\"sp\" class=\"arr\"> </span>world</td>";
        TableCell cell = (TableCell) Converter.fromXml(xml);
        assertEquals("hello<hr /><span class=\"arr\" id=\"sp\"> </span>world", cell.getContents());
    }

    @Test
    public void testHTMLResultDocConvert() throws Exception {
        String doc = "<html><hello/><world/></html>";
        String xml = "<HTMLResultDoc startTime=\"1969-12-31 17:00:00.0\" endTime=\"1969-12-31 17:00:00.0\" id=\"hi\">" + "&lt;html&gt;&lt;hello/&gt;&lt;world/&gt;&lt;/html&gt;" + "</HTMLResultDoc>";
        Timestamp start = new Timestamp(69, 11, 31, 17, 0, 0, 0);
        Timestamp end = new Timestamp(69, 11, 31, 17, 0, 0, 0);
        HTMLResultDoc result = new HTMLResultDoc("hi", doc, start, end);
        String convertedToXml = Converter.toXml(result);
        assertEquals(xml, convertedToXml);
        Object obj = Converter.fromXml(convertedToXml);
        assertEquals(result, obj);
    }
}
