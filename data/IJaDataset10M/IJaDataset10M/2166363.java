package org.furthurnet.xmlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import org.furthurnet.datastructures.supporting.Common;

public class XmlObject {

    private String name = null;

    private String value = null;

    private Vector attributes = null;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int numAttributes() {
        return attributes == null ? 0 : attributes.size();
    }

    public XmlObject getAttribute(int i) {
        return (XmlObject) attributes.elementAt(i);
    }

    public Vector getAttributeVector() {
        return new Vector(attributes);
    }

    public void echo(int tab) {
        for (int i = 0; i < tab; i++) System.out.print(" ");
        System.out.println(name + ":" + value);
        if (attributes != null) for (int i = 0; i < attributes.size(); i++) ((XmlObject) attributes.elementAt(i)).echo(tab + 4);
    }

    public String getXml() {
        try {
            StringBuffer enc = new StringBuffer();
            enc.append('<').append(name);
            if (attributes != null) {
                for (int i = 0; i < attributes.size(); i++) {
                    XmlObject obj = ((XmlObject) attributes.elementAt(i));
                    if ((obj.attributes == null) || (obj.attributes.size() == 0)) {
                        enc.append(' ').append(obj.name).append('=').append('\"').append(obj.value).append('\"');
                    }
                }
            }
            enc.append('>');
            if (value != null) enc.append(value);
            if (attributes != null) {
                for (int i = 0; i < attributes.size(); i++) {
                    XmlObject obj = ((XmlObject) attributes.elementAt(i));
                    if ((obj.attributes != null) && (obj.attributes.size() > 0)) enc.append(obj.getXml());
                }
            }
            enc.append("</").append(name).append('>');
            String s = Common.replaceAll(enc.toString(), "\n", " ");
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public XmlObject getAttribute(String attr) throws XmlException {
        try {
            for (int i = 0; i < attributes.size(); i++) {
                if (((XmlObject) attributes.elementAt(i)).name.equals(attr)) return (XmlObject) attributes.elementAt(i);
            }
        } catch (Exception e) {
        }
        throw new XmlException("Error locating attribute " + attr);
    }

    public static Vector parse(File f) throws XmlException {
        try {
            StringBuffer input = new StringBuffer(4096);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(f));
                while (br.ready()) input.append((char) br.read());
                br.close();
            } catch (Exception e) {
            }
            return parse(input.toString());
        } catch (XmlException e) {
            throw e;
        } catch (Exception e) {
            throw new XmlException("Error parsing XML Data : " + e.getMessage());
        }
    }

    public static Vector parse(String xmlData) throws XmlException {
        try {
            Vector objects = new Vector();
            do {
                XmlObject obj = new XmlObject();
                int startDef = xmlData.indexOf("<");
                int endDef = getClosingPos(xmlData, startDef);
                int endName = xmlData.indexOf(" ", startDef);
                if ((endName < startDef) || (endName > endDef)) endName = endDef; else obj.attributes = getEmbeddedAttributes(xmlData.substring(endName + 1, endDef));
                obj.name = xmlData.substring(startDef + 1, endName);
                if (xmlData.charAt(endDef - 1) != '/') {
                    String endTag = "</" + obj.name + ">";
                    int endPos = xmlData.indexOf(endTag);
                    String subStr = xmlData.substring(endDef + 1, endPos);
                    endDef = endPos + endTag.length() - 1;
                    if (subStr.indexOf("<") >= 0 && subStr.indexOf(">") >= 1) {
                        if (obj.attributes == null) obj.attributes = parse(subStr); else obj.attributes.addAll(parse(subStr));
                    } else obj.value = Common.replaceAll(subStr, "\"", "'");
                }
                objects.add(obj);
                if (endDef + 1 >= xmlData.length()) {
                    xmlData = new String("");
                } else {
                    xmlData = xmlData.substring(endDef + 1).trim();
                }
            } while (xmlData.length() > 3);
            return objects;
        } catch (XmlException e) {
            throw e;
        } catch (Exception e) {
            throw new XmlException("Error parsing XML Data : " + e.getMessage());
        }
    }

    private static Vector getEmbeddedAttributes(String attr) throws XmlException {
        try {
            Vector attributes = new Vector();
            boolean done = false;
            do {
                XmlObject obj = new XmlObject();
                attr = attr.trim();
                int endName = attr.indexOf(" ");
                if ((endName == -1) || (attr.indexOf("=") < endName)) endName = attr.indexOf("=");
                obj.name = attr.substring(0, endName);
                int equalSignPos = attr.indexOf("=", endName);
                int startValue = attr.indexOf("\"", equalSignPos + 1);
                int endValue = attr.indexOf("\"", startValue + 1);
                if ((startValue == -1) && (endValue == -1)) {
                    startValue = attr.indexOf("'", equalSignPos + 1);
                    endValue = attr.indexOf("'", startValue + 1);
                }
                obj.value = Common.replaceAll(attr.substring(startValue + 1, endValue), "\"", "'");
                attributes.add(obj);
                if (endValue + 1 >= attr.length()) done = true; else {
                    attr = attr.substring(endValue + 1).trim();
                    if (attr.length() < 3) done = true;
                }
            } while (!done);
            return attributes;
        } catch (Exception e) {
        }
        return new Vector();
    }

    public static SearchParamSet getSearchParamSet(String searchStr) throws XmlException {
        XmlObject obj = (XmlObject) parse(searchStr).elementAt(0);
        return new SearchParamSet(obj);
    }

    private static int getClosingPos(String s, int startPos) {
        int found = -1;
        int pos = startPos;
        do {
            found = s.indexOf(">", pos);
            if (found == -1) return -1;
            if (countQuotes(s, startPos, found) % 2 == 0) return found; else pos = found + 1;
        } while (true);
    }

    private static int countQuotes(String s, int start, int end) {
        int count = 0;
        do {
            int pos = s.indexOf("\"", start);
            if ((pos == -1) || (pos >= end)) return count; else {
                count++;
                start = pos + 1;
            }
        } while (true);
    }
}
