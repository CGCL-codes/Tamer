package edu.asu.gios.tool;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseInjectedNodeXML extends DefaultHandler {

    String path;

    String filename;

    Map<String, Set<String>> parentChild;

    int level;

    ArrayList<String> parent;

    public ParseInjectedNodeXML(String path, String filename, Map<String, Set<String>> parentChild) throws Exception {
        super();
        this.path = path;
        this.filename = filename;
        this.parentChild = parentChild;
        level = 0;
        parent = new ArrayList<String>();
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        FileReader r = new FileReader(path + "\\" + filename);
        xr.parse(new InputSource(r));
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        if (0 < parent.size()) {
            String parentName = parent.get(parent.size() - 1);
            Set set = parentChild.get(parentName);
            if (null == set) {
                set = new HashSet<String>();
                set.add(name);
                parentChild.put(parentName, set);
            } else {
                parentChild.get(parentName).add(name);
            }
        }
        ++level;
        parent.add(name);
    }

    public void endElement(String uri, String name, String qName) {
        --level;
        if (0 < parent.size()) {
            parent.remove(parent.size() - 1);
        } else {
            int a = 1;
        }
    }
}
