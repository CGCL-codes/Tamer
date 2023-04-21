package kanjidic.parser;

import org.xml.sax.SAXException;

class KanjidicDTDHandler implements org.xml.sax.DTDHandler {

    @Override
    public void notationDecl(String name, String publicID, String systemID) throws SAXException {
    }

    @Override
    public void unparsedEntityDecl(String name, String publicID, String systemID, String notationName) throws SAXException {
    }
}
