package net.sourceforge.htmlunit.htmlparser;

import java.io.StringReader;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class HTMLScannerDebugger {

    public static void main(final String[] args) throws Exception {
        String html = "<h1 hello=\"there\"></h1>\n";
        System.out.println("-------------- NekoHtml --------------");
        nekoHtml(html);
        System.out.println("------------- HtmlParser -------------");
        htmlParser(html);
        System.out.println("---------------- XML ----------------");
        xml(html);
    }

    private static void nekoHtml(String html) throws Exception {
        StringReader stringReader = new StringReader(html);
        XMLParserConfiguration parser = new HTMLConfiguration();
        parser.setDocumentHandler(new OutContentHandler());
        parser.parse(new XMLInputSource(null, null, null, stringReader, "UTF-8"));
    }

    private static void htmlParser(String html) throws Exception {
        StringReader stringReader = new StringReader(html);
        HTMLScanner reader = new HTMLScanner();
        reader.setContentHandler(new OutContentHandler());
        reader.parse(new InputSource(stringReader));
    }

    private static void xml(String html) throws Exception {
        StringReader stringReader = new StringReader(html);
        XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setContentHandler(new OutContentHandler());
        reader.parse(new InputSource(stringReader));
    }
}
