package org.xmlvm.iphone;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlvm.XMLVMIgnore;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class NSXMLParserDelegate extends NSObject {

    @XMLVMIgnore
    private class DefaultHandlerInstance extends DefaultHandler {

        @Override
        @XMLVMIgnore
        public void startPrefixMapping(String prefix, String uri) {
            if (parser.shouldReportNamespacePrefixes()) {
                didStartMappingPrefix(parser, prefix, uri);
            }
        }

        @Override
        @XMLVMIgnore
        public void endPrefixMapping(String prefix) {
            if (parser.shouldReportNamespacePrefixes()) {
                didEndMappingPrefix(parser, prefix);
            }
        }

        @Override
        @XMLVMIgnore
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("".equals(localName)) {
                localName = qName;
            }
            didStartElement(parser, localName, uri, qName, convertAttributes(attributes));
        }

        @Override
        @XMLVMIgnore
        public void endElement(String uri, String localName, String qName) {
            if ("".equals(localName)) {
                localName = qName;
            }
            didEndElement(parser, localName, uri, qName);
        }

        @Override
        @XMLVMIgnore
        public void characters(char[] ch, int start, int length) {
            String characters = String.copyValueOf(ch, start, length);
            foundCharacters(parser, characters);
        }

        private Map<String, String> convertAttributes(Attributes attributes) {
            Map<String, String> attr = new HashMap<String, String>();
            for (int i = 0; i < attributes.getLength(); i++) {
                attr.put(attributes.getQName(i), attributes.getValue(i));
            }
            return attr;
        }
    }

    private NSXMLParser parser;

    @XMLVMIgnore
    DefaultHandler handler = new DefaultHandlerInstance();

    public void didStartMappingPrefix(NSXMLParser parser, String prefix, String namespaceURI) {
    }

    public void didEndMappingPrefix(NSXMLParser parser, String prefix) {
    }

    public void didStartElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName, Map<String, String> attributes) {
    }

    public void didEndElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName) {
    }

    public void foundCharacters(NSXMLParser parser, String characters) {
    }

    public void foundCDATA(NSXMLParser parser, NSData CDATABlock) {
    }

    void setParser(NSXMLParser parser) {
        this.parser = parser;
    }
}
