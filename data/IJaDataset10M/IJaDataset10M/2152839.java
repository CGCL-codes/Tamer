package com.dukesoftware.utils.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class DOMParseModule implements XMLParseHelper {

    private final DocumentBuilder builder;

    private final DOMHandler handler;

    public DOMParseModule(DOMHandler handler) throws ParserConfigurationException {
        this.handler = handler;
        this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public void pasre(File file) throws SAXException, IOException {
        handler.parse(builder.parse(file));
    }
}
