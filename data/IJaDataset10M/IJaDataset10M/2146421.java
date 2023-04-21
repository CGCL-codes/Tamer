package com.aol.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ChangelistPatch {

    private String patchFile = null;

    public ChangelistPatch(String file) {
        patchFile = file;
    }

    public Iterator getChanges(String changelistName) {
        return parseFile(patchFile, changelistName);
    }

    private Iterator parseFile(String patchFile, String changelistName) {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            XMLHandler handler = new XMLHandler();
            handler.setChangelistName(changelistName);
            xr.setContentHandler(handler);
            xr.parse(new InputSource(patchFile));
            return handler.iterator();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final class ChangeLineRec {

        public String markup = null;

        public String dependencies = null;
    }

    private final class XMLHandler extends DefaultHandler {

        private String chgName = null;

        private HashMap changelist = null;

        private StringBuffer contents = new StringBuffer();

        private boolean processing = false;

        public XMLHandler() {
        }

        public void setChangelistName(String n) {
            chgName = n;
        }

        public void startElement(String ns, String localName, String qName, Attributes attr) throws SAXException {
            contents.delete(0, contents.length());
            if (!processing && "changelist".equals(localName)) {
                String name = attr.getValue("name");
                if (name != null && name.equals(chgName)) {
                    processing = true;
                    changelist = new HashMap();
                    changelist.put("changeLines", new LinkedList());
                    int num_attr = attr.getLength();
                    for (int i = 0; i < num_attr; i++) changelist.put(attr.getLocalName(i), attr.getValue(i));
                }
            }
            if (!processing) return;
            if ("markup".equals(localName)) {
                LinkedList changeLines = (LinkedList) changelist.get("changeLines");
                ChangeLineRec rec = new ChangeLineRec();
                rec.markup = attr.getValue("file");
                changeLines.add(rec);
            }
        }

        public void endElement(String ns, String localName, String qName) throws SAXException {
            if (!processing) return;
            if ("changelist".equals(localName)) processing = false;
            if ("dependencies".equals(localName)) {
                LinkedList changeLines = (LinkedList) changelist.get("changeLines");
                ChangeLineRec rec = (ChangeLineRec) changeLines.getLast();
                rec.dependencies = contents.toString();
                if (rec.dependencies != null) rec.dependencies = rec.dependencies.trim();
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            contents.append(ch, start, length);
        }

        public Iterator iterator() {
            if (changelist == null) return null;
            LinkedList l = (LinkedList) changelist.get("changeLines");
            if (l != null) return l.iterator(); else return null;
        }
    }
}
