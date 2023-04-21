package net.sf.mogbox.chocobo;

import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ModelList {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private Entry[] list;

    public ModelList(String filename) {
        InputStream in = ModelList.class.getResourceAsStream(filename);
        if (in == null) {
            list = new Entry[0];
            return;
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        try {
            SAXParser parser = factory.newSAXParser();
            parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
            parser.setProperty(JAXP_SCHEMA_SOURCE, getClass().getResourceAsStream("list.xsd"));
            parser.parse(in, new ListContentHandeler());
        } catch (Throwable t) {
            RuntimeException exception = new RuntimeException(t.getMessage(), t.getCause());
            exception.setStackTrace(t.getStackTrace());
            throw exception;
        }
    }

    public String getName(int id, int race) {
        if (id < 0 || id >= list.length) return null;
        Entry entry = list[id];
        if (entry == null) return null;
        if (entry.alts != null && race >= 0 && race <= 7) {
            Entry alt = entry.alts[race];
            if (alt != null && alt.name != null) {
                return alt.name;
            }
        }
        return entry.name;
    }

    public boolean isHidden(int id, int race) {
        if (id < 0 || id >= list.length) return false;
        Entry entry = list[id];
        if (entry == null) return false;
        if (entry.alts != null && race >= 0 && race <= 7) {
            Entry alt = entry.alts[race];
            if (alt != null && alt.hide != null) {
                return alt.hide;
            }
        }
        return entry.hide != null ? entry.hide : false;
    }

    public int[] getOverrides(int id, int race) {
        if (id < 0 || id >= list.length) return null;
        Entry entry = list[id];
        if (entry == null) return null;
        if (entry.alts != null && race >= 0 && race <= 7) {
            Entry alt = entry.alts[race];
            if (alt != null && alt.overrides != null) {
                return alt.overrides;
            }
        }
        return entry.overrides;
    }

    private class Entry {

        public int id;

        public Boolean hide;

        public String name;

        public int[] overrides;

        public Entry[] alts;
    }

    private class ListContentHandeler extends DefaultHandler {

        private ArrayList<Entry> workingList;

        private Entry entry;

        private Entry alt;

        public void startDocument() throws SAXException {
            workingList = new ArrayList<Entry>();
        }

        @Override
        public void startElement(String ns, String name, String q, Attributes attrs) throws SAXException {
            if (ns.equals("urn:xmlns:mogbox:chocobo:1.0")) {
                if (name.equals("item")) {
                    entry = new Entry();
                    entry.id = Integer.parseInt(attrs.getValue("", "id"));
                    process(entry, attrs);
                } else if (name.equals("alt")) {
                    alt = new Entry();
                    String breed = attrs.getValue("", "breed");
                    if (breed.equals("R")) alt.id = 0; else if (breed.equals("D")) alt.id = 1; else if (breed.equals("P")) alt.id = 2; else if (breed.equals("C")) alt.id = 3; else if (breed.equals("J")) alt.id = 4;
                    process(alt, attrs);
                }
            }
        }

        private void process(Entry entry, Attributes attrs) {
            entry.name = attrs.getValue("", "name");
            String temp;
            if ((temp = attrs.getValue("", "hide")) != null) entry.hide = temp.equals("true");
            boolean set = false;
            int[] overrides = { -1, -1, -1, -1, -1 };
            if ((temp = attrs.getValue("", "body")) != null) {
                set = true;
                overrides[0] = Integer.parseInt(temp);
            }
            if ((temp = attrs.getValue("", "feet")) != null) {
                set = true;
                overrides[1] = Integer.parseInt(temp);
            }
            if ((temp = attrs.getValue("", "head")) != null) {
                set = true;
                overrides[2] = Integer.parseInt(temp);
            }
            if ((temp = attrs.getValue("", "tail")) != null) {
                set = true;
                overrides[3] = Integer.parseInt(temp);
            }
            if ((temp = attrs.getValue("", "armor")) != null) {
                set = true;
                overrides[4] = Integer.parseInt(temp);
            }
            if (set) entry.overrides = overrides;
        }

        @Override
        public void endElement(String ns, String name, String qName) throws SAXException {
            if (ns.equals("urn:xmlns:mogbox:chocobo:1.0")) {
                if (name.equals("item")) {
                    if (workingList.size() > entry.id) {
                        workingList.set(entry.id, entry);
                    } else {
                        for (int i = workingList.size(); i < entry.id; i++) workingList.add(i, null);
                        workingList.add(entry.id, entry);
                    }
                } else if (name.equals("alt")) {
                    if (entry.alts == null) entry.alts = new Entry[8];
                    entry.alts[alt.id] = alt;
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            Entry[] temp = new Entry[workingList.size()];
            list = workingList.toArray(temp);
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            e.printStackTrace();
        }
    }
}
