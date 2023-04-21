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
public class XMLForeignKeyReader extends EmeraldjbXMLReader {

    private ForeignKey foreignKey = null;

    public static void main(String[] args) {
        try {
            XMLForeignKeyReader.testInputFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testInputFile() throws Exception {
        XMLForeignKeyReader xfr = new XMLForeignKeyReader();
        ForeignKey foreignKey = new ForeignKey();
        xfr.setForeignKey(foreignKey);
        InputStream is = xfr.getInputStream("testforeignkey.xml", FILE_STREAM);
        xfr.startParsing(is, xfr, EmeraldjbSchemaConst.ELEM_FOREIGN_KEY);
    }

    /**
   * Derived from base reader it allows passing of common elements.
   * For example patternSeqNum
   * @return
   */
    public EmeraldjbBean getCurrentBean() {
        return foreignKey;
    }

    /**
	 * @return
	 */
    public ForeignKey getForeignKey() {
        if (foreignKey == null) throw new RuntimeException("No foreignKey set.");
        return foreignKey;
    }

    /**
	 * @param key
	 */
    public void setForeignKey(ForeignKey key) {
        foreignKey = key;
    }

    /**
    * Parser calls this for each element in a document
    */
    public void startElement(String name_space_URI, String local_name, String raw_name, Attributes atts) throws SAXException {
        setElementName(raw_name);
        String cols = null;
        try {
            if (EmeraldjbSchemaConst.ELEM_FOREIGN_KEY.equalsIgnoreCase(raw_name)) {
                String name = atts.getValue(EmeraldjbSchemaConst.ATTR_NAME);
                getForeignKey().setName(name);
                cols = atts.getValue(EmeraldjbSchemaConst.ATTR_COLS);
                getForeignKey().setRawCols(cols);
                String references = atts.getValue(EmeraldjbSchemaConst.ATTR_REFERENCES);
                int lpos = references.indexOf("(");
                int rpos = references.indexOf(")", lpos);
                if (lpos < 1 || rpos < lpos - 1) {
                    throw new RuntimeException("rawReferences must has bad format[" + references + "].");
                }
                String table_name = references.substring(0, lpos).trim();
                getForeignKey().setExternalTable(table_name);
                getForeignKey().setRawReferences(references.substring(lpos + 1, rpos));
            } else {
                super.startElement(name_space_URI, local_name, raw_name, atts);
            }
        } catch (EmeraldjbException ex) {
            throw new SAXException("Failed to locate all columns for the fkey with columns:" + cols);
        }
    }

    public void endElement(String namespaceURI, String local_name, String qName) throws SAXException {
        if (local_name == null || local_name.length() == 0) local_name = qName;
        String raw_name = local_name;
        if (local_name.equals(getEndElement())) {
            if (getParentReader() != null) getParentReader().childFinishedParsing(this, getEndElement());
            return;
        }
    }
}
