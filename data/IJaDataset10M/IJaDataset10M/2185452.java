package com.dbxml.xml.sax;

import com.dbxml.util.UTF8;
import com.dbxml.xml.QName;
import com.dbxml.xml.dtsm.Constants;
import com.dbxml.xml.dtsm.TableBuilder;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * DTSMContentHandler
 */
public class DTSMContentHandler extends DefaultHandler implements LexicalHandler {

    private Stack stack = new Stack();

    private StringBuffer sb = new StringBuffer();

    private TableBuilder builder;

    private boolean cdata;

    private SAXException exception;

    public DTSMContentHandler(TableBuilder builder) {
        this.builder = builder;
    }

    public TableBuilder getTableBuilder() {
        return builder;
    }

    public SAXException getException() {
        return exception;
    }

    public void warning(SAXParseException e) throws SAXException {
        exception = e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        exception = e;
    }

    public void error(SAXParseException e) throws SAXException {
        exception = e;
    }

    public void complete() {
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (cdata) sb.append(ch, start, length); else {
            byte[] b = UTF8.toUTF8(ch, start, length);
            builder.addEntry(Constants.OBJ_TEXT, b);
        }
    }

    public void startDocument() {
        builder.addEntry(Constants.OBJ_BEGIN_DOCUMENT);
    }

    public void endDocument() {
        builder.addEntry(Constants.OBJ_END_DOCUMENT);
        complete();
    }

    public void ignorableWhitespace(char ch[], int start, int length) {
        byte[] b = UTF8.toUTF8(ch, start, length);
        builder.addEntry(Constants.OBJ_TEXT, b);
    }

    public void processingInstruction(String target, String data) {
        QName q = new QName(target);
        int symID = builder.addEntry(Constants.OBJ_BEGIN_PROCINST, q);
        byte[] b = UTF8.toUTF8(data);
        builder.addEntry(Constants.OBJ_TEXT, b);
        builder.addEntry(Constants.OBJ_END_PROCINST, symID);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        QName qname = new QName(qName, namespaceURI);
        int symID = builder.addEntry(Constants.OBJ_BEGIN_ELEMENT, qname);
        stack.push(new Integer(symID));
        int size = atts.getLength();
        for (int i = 0; i < size; i++) {
            QName aq = new QName(atts.getQName(i), atts.getURI(i));
            int as = builder.addEntry(Constants.OBJ_BEGIN_ATTRIBUTE, aq);
            builder.addEntry(Constants.OBJ_TEXT, UTF8.toUTF8(atts.getValue(i)));
            builder.addEntry(Constants.OBJ_END_ATTRIBUTE, as);
        }
    }

    public void startCDATA() {
        sb.setLength(0);
        cdata = true;
    }

    public void endCDATA() {
        byte[] b = UTF8.toUTF8(sb.toString());
        builder.addEntry(Constants.OBJ_CDATA, b);
        cdata = false;
    }

    public void comment(char[] ch, int start, int length) {
        byte[] b = UTF8.toUTF8(ch, start, length);
        builder.addEntry(Constants.OBJ_COMMENT, b);
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        Integer symID = (Integer) stack.pop();
        builder.addEntry(Constants.OBJ_END_ELEMENT, symID.intValue());
    }

    public void notationDecl(String name, String publicID, String systemID) throws SAXException {
        QName qname = new QName(name);
        int symID = builder.addEntry(Constants.OBJ_BEGIN_NOTATION, qname);
        byte[] b = UTF8.toUTF8(publicID);
        builder.addEntry(Constants.OBJ_TEXT, b);
        b = UTF8.toUTF8(systemID);
        builder.addEntry(Constants.OBJ_TEXT, b);
        builder.addEntry(Constants.OBJ_END_NOTATION, symID);
    }

    public void unparsedEntityDecl(String name, String publicID, String systemID, String notation) throws SAXException {
        QName qname = new QName(name);
        int symID = builder.addEntry(Constants.OBJ_BEGIN_ENTITY, qname);
        byte[] b = UTF8.toUTF8(publicID);
        builder.addEntry(Constants.OBJ_TEXT, b);
        b = UTF8.toUTF8(systemID);
        builder.addEntry(Constants.OBJ_TEXT, b);
        b = UTF8.toUTF8(notation);
        builder.addEntry(Constants.OBJ_TEXT, b);
        builder.addEntry(Constants.OBJ_END_ENTITY, symID);
    }

    public void skippedEntity(String name) {
    }

    public void startDTD(String name, String publicID, String systemID) throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
    }
}
