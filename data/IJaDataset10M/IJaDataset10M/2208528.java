package com.jaeksoft.searchlib.parser.htmlParser;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jaeksoft.searchlib.streamlimiter.LimitException;
import com.jaeksoft.searchlib.streamlimiter.StreamLimiter;

public class HtmlCleanerParser extends HtmlDocumentProvider {

    public HtmlCleanerParser(String charset, StreamLimiter streamLimiter) throws LimitException {
        super(charset, streamLimiter);
    }

    @Override
    public String getName() {
        return "HtmlCleaner";
    }

    @Override
    protected DomHtmlNode getDocument(String charset, InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        HtmlCleaner cleaner = new HtmlCleaner();
        CleanerProperties props = cleaner.getProperties();
        props.setNamespacesAware(true);
        TagNode node = cleaner.clean(inputStream, charset);
        Document document = new DomSerializer(props, true).createDOM(node);
        return new DomHtmlNode(document);
    }
}
