package com.jaeksoft.searchlib.parser.htmlParser;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.jaeksoft.searchlib.streamlimiter.LimitException;
import com.jaeksoft.searchlib.streamlimiter.StreamLimiter;
import com.jaeksoft.searchlib.util.DomUtils;

public class StrictXhtmlParser extends HtmlDocumentProvider {

    public StrictXhtmlParser(String charset, StreamLimiter streamLimiter) throws LimitException {
        super(charset, streamLimiter);
    }

    @Override
    public String getName() {
        return "StrictXml";
    }

    @Override
    protected DomHtmlNode getDocument(String charset, InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilder builder = DomUtils.getNewDocumentBuilder(false, true);
        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding(charset);
        return new DomHtmlNode(builder.parse(inputSource));
    }
}
