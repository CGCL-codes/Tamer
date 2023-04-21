package foa.preview;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributeListImpl;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

public class PreviewHandler extends HandlerBase {

    private PreviewRenderer preview;

    private String partialContent;

    private StringBuffer content;

    private Vector element;

    private Vector page;

    private Vector doc;

    public PreviewHandler(PreviewRenderer pr) {
        preview = pr;
        content = new StringBuffer(0);
    }

    public void startDocument() throws SAXException {
        doc = new Vector(0, 1);
    }

    public void endDocument() throws SAXException {
        preview.setDocument(doc);
        preview.displayFirstPage();
    }

    public void startElement(String tag, AttributeList attributes) throws SAXException {
        AttributeListImpl a = new AttributeListImpl(attributes);
        if (tag.equals("Page")) {
            page = new Vector(0, 1);
            page.add(a);
        } else if (tag.equals("Document")) {
        } else {
            String t = new String(tag);
            element = new Vector(2, 1);
            element.add(t);
            element.add(a);
            page.add(element);
        }
    }

    public void endElement(String tag) throws SAXException {
        if (tag.equals("Page")) {
            doc.add(page);
        }
        if (tag.equals("LineArea")) {
            String c = new String(content);
            element.add(c);
        }
        content.delete(0, content.length() + 1);
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        partialContent = new String(buf, offset, len);
        if (partialContent != null) {
            content.append(partialContent);
            StringBuffer temp = content;
            int j = 0;
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == 10) {
                    content.deleteCharAt(j);
                } else {
                    j = j + 1;
                }
            }
        }
    }
}
