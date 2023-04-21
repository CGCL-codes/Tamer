package com.meterware.httpunit;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import org.w3c.dom.*;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

/**
 * This class represents an HTML page returned from a request.
 **/
class ReceivedPage extends ParsedHTML {

    public ReceivedPage(URL url, String parentTarget, String pageText, String characterSet) throws SAXException {
        super(url, parentTarget, getDOM(pageText), characterSet);
        setBaseAttributes();
    }

    /**
     * Returns the title of the page.
     **/
    public String getTitle() throws SAXException {
        NodeList nl = ((Document) getDOM()).getElementsByTagName("title");
        if (nl.getLength() == 0) return "";
        if (!nl.item(0).hasChildNodes()) return "";
        return nl.item(0).getFirstChild().getNodeValue();
    }

    private static Node getDOM(String pageText) throws SAXException {
        try {
            return getParser().parseDOM(new ByteArrayInputStream(pageText.getBytes(getUTFEncodingName())), null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding failed");
        }
    }

    private static String _utfEncodingName;

    private static String getUTFEncodingName() {
        if (_utfEncodingName == null) {
            String versionNum = System.getProperty("java.version");
            if (versionNum.startsWith("1.1")) _utfEncodingName = "UTF8"; else _utfEncodingName = "UTF-8";
        }
        return _utfEncodingName;
    }

    private void setBaseAttributes() throws SAXException {
        NodeList nl = ((Document) getDOM()).getElementsByTagName("base");
        if (nl.getLength() == 0) return;
        try {
            applyBaseAttributes(NodeUtils.getNodeAttribute(nl.item(0), "href"), NodeUtils.getNodeAttribute(nl.item(0), "target"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to set document base: " + e);
        }
    }

    private void applyBaseAttributes(String baseURLString, String baseTarget) throws MalformedURLException {
        if (baseURLString.length() > 0) {
            this.setBaseURL(new URL(baseURLString));
        }
        if (baseTarget.length() > 0) {
            this.setBaseTarget(baseTarget);
        }
    }

    private static Tidy getParser() {
        Tidy tidy = new Tidy();
        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
        tidy.setQuiet(true);
        tidy.setShowWarnings(HttpUnitOptions.getParserWarningsEnabled());
        return tidy;
    }
}
