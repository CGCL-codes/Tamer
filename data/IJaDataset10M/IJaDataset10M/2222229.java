package com.meterware.httpunit.parsing;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLDocument;
import java.net.URL;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 * @author <a href="mailto:bw@xmlizer.biz">Bernhard Wagner</a>
 * @author <a href="mailto:Artashes.Aghajanyan@lycos-europe.com">Artashes Aghajanyan</a>
 **/
class NekoHTMLParser implements HTMLParser {

    /**
	 * parse the given URL with the given pageText using the given document adapter
	 * @param pageURL
	 * @param pageText
	 * @param adapter
	 */
    public void parse(URL pageURL, String pageText, DocumentAdapter adapter) throws IOException, SAXException {
        try {
            NekoDOMParser parser = NekoDOMParser.newParser(adapter, pageURL);
            parser.parse(new InputSource(new StringReader(pageText)));
            Document doc = parser.getDocument();
            adapter.setDocument((HTMLDocument) doc);
        } catch (NekoDOMParser.ScriptException e) {
            throw e.getException();
        }
    }

    public String getCleanedText(String string) {
        return (string == null) ? "" : string.replace(NBSP, ' ');
    }

    public boolean supportsPreserveTagCase() {
        return false;
    }

    public boolean supportsForceTagCase() {
        return false;
    }

    public boolean supportsReturnHTMLDocument() {
        return true;
    }

    public boolean supportsParserWarnings() {
        return true;
    }

    private static final char NBSP = (char) 160;
}
