package com.emeraldjb.base.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.*;
import com.emeraldjb.base.*;

/**
 * <p>
 * Copyright (c) 2003, 2004 by Emeraldjb LLC
 * All Rights Reserved.
 * </p>
 */
public class XMLJavadocReader extends EmeraldjbXMLReader {

    private Javadoc javadoc = null;

    public static void main(String[] args) {
        try {
            XMLJavadocReader.testInputFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testInputFile() throws Exception {
        XMLJavadocReader xjr = new XMLJavadocReader();
        Javadoc javadoc = new Javadoc();
        xjr.setJavadoc(javadoc);
        InputStream is = xjr.getInputStream("testjavadoc.xml", FILE_STREAM);
        xjr.startParsing(is, xjr, EmeraldjbSchemaConst.ELEM_JAVADOC);
    }

    /**
   * Derived from base reader it allows passing of common elements.
   * For example patternSeqNum
   * @return
   */
    public EmeraldjbBean getCurrentBean() {
        return javadoc;
    }

    /**
	 * @return
	 */
    public Javadoc getJavadoc() {
        if (javadoc == null) throw new RuntimeException("No javadoc set.");
        return javadoc;
    }

    /**
	 * @param javadoc
	 */
    public void setJavadoc(Javadoc javadoc) {
        this.javadoc = javadoc;
    }

    /**
    * no kids!
    */
    public void childFinishedParsing(EmeraldjbXMLReader child, String el_name) throws SAXException {
        super.childFinishedParsing(child, el_name);
    }

    /**
    * Parser calls this for each element in a document
    */
    public void startElement(String name_space_URI, String local_name, String raw_name, Attributes atts) throws SAXException {
        setElementName(raw_name);
        if (EmeraldjbSchemaConst.ELEM_JAVADOC.equalsIgnoreCase(raw_name)) {
            getCdata().setLength(0);
        } else if (EmeraldjbSchemaConst.ELEM_DESCRIPTION.equalsIgnoreCase(raw_name)) {
            getCdata().setLength(0);
        } else if (EmeraldjbSchemaConst.ELEM_SEE.equalsIgnoreCase(raw_name)) {
            getCdata().setLength(0);
        } else if (EmeraldjbSchemaConst.ELEM_SINCE.equalsIgnoreCase(raw_name)) {
            getCdata().setLength(0);
        } else if (EmeraldjbSchemaConst.ELEM_DEPRECATED.equalsIgnoreCase(raw_name)) {
            getCdata().setLength(0);
        } else {
            super.startElement(name_space_URI, local_name, raw_name, atts);
        }
    }

    /**
    * Receive notification of the end of an element.
    * The SAX parser will invoke this method at the end of every element in the XML document; there will be a corresponding startElement event for every endElement event (even when the element is empty).
    */
    public void endElement(String namespaceURI, String local_name, String qName) throws SAXException {
        if (local_name == null || local_name.length() == 0) local_name = qName;
        if (local_name == null || local_name.length() == 0) local_name = qName;
        String raw_name = local_name;
        if (local_name.equals(getEndElement())) {
            if (getParentReader() != null) getParentReader().childFinishedParsing(this, getEndElement());
            return;
        }
        if (EmeraldjbSchemaConst.ELEM_DESCRIPTION.equalsIgnoreCase(raw_name)) {
            getJavadoc().setDescription(getCdataString());
        } else if (EmeraldjbSchemaConst.ELEM_DEPRECATED.equalsIgnoreCase(raw_name)) {
            getJavadoc().setDeprecated(getCdataString());
        } else if (EmeraldjbSchemaConst.ELEM_SEE.equalsIgnoreCase(raw_name)) {
            getJavadoc().setSee(getCdataString());
        } else if (EmeraldjbSchemaConst.ELEM_SINCE.equalsIgnoreCase(raw_name)) {
            getJavadoc().setSince(getCdataString());
        }
    }
}
