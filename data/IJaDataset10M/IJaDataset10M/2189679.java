package org.kubiki.xml;

import java.util.*;
import org.kubiki.base.*;

public class XMLElement {

    public Vector subElements;

    String content;

    String title;

    public Hashtable properties;

    XMLParser parser;

    public XMLElement(String tag, XMLParser parser) {
        this.parser = parser;
        subElements = new Vector();
        properties = new Hashtable();
        setProperties(tag);
    }

    public void setProperties(String tag) {
        String[] p = { "", "" };
        int c = 0;
        String name = "";
        String value = "";
        String[] parts = tag.split(" ", 2);
        if (parts.length == 2) {
            String elname = parts[0].replaceAll("<", "").trim();
            properties.put(elname, "");
            String[] props = parts[1].split(" ");
            for (int i = 0; i < props.length; i++) {
                String[] args = props[i].split("=");
                if (args.length == 2) {
                    value = "";
                    if (args[1].length() > 2) {
                        try {
                            value = args[1].replaceAll("\"", "").replaceAll(">", "");
                        } catch (java.lang.Exception e) {
                        }
                    }
                    properties.put(args[0], value);
                }
            }
        } else if (parts.length == 1) {
            String elname = parts[0].replaceAll("<", "").trim();
            properties.put(elname, "");
        }
        parts = null;
    }

    public void setProperties2(String tag) {
        String[] p = { "", "" };
        int c = 0;
        char a;
        for (int i = 0; i < tag.length(); i++) {
            a = tag.charAt(i);
            switch(a) {
                case '<':
                    break;
                case '>':
                    break;
                case '"':
                    break;
                case '=':
                    {
                        c++;
                        break;
                    }
                case ' ':
                    {
                        properties.put(p[0], p[1]);
                        c = 0;
                        p[0] = "";
                        p[1] = "";
                        break;
                    }
                default:
                    {
                        p[c] = p[c] + a;
                    }
            }
        }
        properties.put(p[0], p[1]);
    }

    public XMLElement addElement(String tag) {
        XMLElement xmlElement = new XMLElement(tag, parser);
        subElements.add(xmlElement);
        return xmlElement;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void getSubelements() {
        boolean parsed = parser.handleElement(this);
        if (parsed == false) {
            if (subElements.size() > 0) {
                for (int j = 0; j < subElements.size(); j++) {
                    XMLElement thisElement = (XMLElement) subElements.elementAt(j);
                    thisElement.getSubelements();
                    thisElement = null;
                }
            }
        }
    }

    public boolean hasChild(String name) {
        boolean hasChild = false;
        for (int j = 0; j < subElements.size(); j++) {
            XMLElement thisElement = (XMLElement) subElements.elementAt(j);
            if (thisElement.properties.containsKey(name)) hasChild = true;
        }
        return hasChild;
    }

    public XMLElement getChild(String name) {
        XMLElement child = null;
        for (int j = 0; j < subElements.size(); j++) {
            XMLElement thisElement = (XMLElement) subElements.elementAt(j);
            if (thisElement.properties.containsKey(name)) child = thisElement;
        }
        return child;
    }

    public Hashtable getProperties() {
        return properties;
    }

    public String getProperty(String name) {
        if (properties.get(name) != null) {
            return (String) properties.get(name);
        } else {
            return null;
        }
    }
}
