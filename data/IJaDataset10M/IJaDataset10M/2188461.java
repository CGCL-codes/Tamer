package logahawk.formatters;

import java.io.*;
import java.util.*;
import org.apache.xerces.parsers.*;
import org.testng.*;
import org.testng.annotations.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XmlNodeParentArgFormatterTest {

    @Test
    public void document() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root />")));
        Node n = parser.getDocument();
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "");
    }

    @Test
    public void root() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root />")));
        Node n = parser.getDocument().getFirstChild();
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root>");
    }

    @Test
    public void element() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root><element /></root>")));
        Node n = parser.getDocument().getFirstChild().getFirstChild();
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root><element>");
    }

    @Test
    public void nestedElement() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root><elem1><elem2 /></elem1></root>")));
        Node n = parser.getDocument().getFirstChild().getFirstChild().getFirstChild();
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root><elem1><elem2>");
    }

    @Test
    public void attribute() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root key=\"value\"/>")));
        Node n = parser.getDocument().getFirstChild().getAttributes().item(0);
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root>key=\"value\"");
    }

    @Test
    public void nestedAttribute() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root><element key=\"value\"/></root>")));
        Node n = parser.getDocument().getFirstChild().getFirstChild().getAttributes().item(0);
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root><element>key=\"value\"");
    }

    @Test
    public void text() throws Exception {
        XmlNodeParentArgFormatter f = new XmlNodeParentArgFormatter();
        Collection<ArgumentFormatter> childFormatters = Arrays.<ArgumentFormatter>asList(new StringArgFormatter());
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader("<root>text</root>")));
        Node n = parser.getDocument().getFirstChild().getFirstChild();
        Assert.assertTrue(f.canFormat(n));
        Assert.assertEquals(f.format(n, childFormatters, 0), "<root>text");
    }
}
