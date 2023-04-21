package scratchcomp.util.xml;

import java.util.Collections;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import scratchcomp.util.Helper;

public class XMLElement implements Serializable, Comparable {

    private String name;

    private String value;

    private Vector content = null;

    private Map args = null;

    private Vector valuePool = new Vector();

    private int occurance = 1;

    private int parents = 0;

    private boolean mixed = false;

    public XMLElement(String n, String v, int p) {
        name = n;
        value = v;
        parents = p;
        if (content == null) content = new Vector();
    }

    public XMLElement(String n, Vector v, int p) {
        name = n;
        valuePool = v;
        parents = p;
        if (content == null) content = new Vector();
    }

    public void addElement(XMLElement xmle) {
        if (content == null) content = new Vector();
        content.add(xmle);
    }

    public void addTextValue(String s) {
        if (content == null) {
            content = new Vector();
            mixed = true;
        }
        content.add(s);
    }

    public synchronized XMLElement getElement(int i) {
        if (i < content.size()) {
            Object o = content.elementAt(i);
            if (o instanceof XMLElement) return (XMLElement) o; else return new XMLElement("" + o.hashCode(), (String) o, 0);
        } else return null;
    }

    public synchronized XMLElement getElement(String n) {
        XMLElement ret = null;
        XMLElement temp;
        for (int i = 0; i < content.size(); i++) {
            temp = getElement(i);
            if (n.equals(temp.getName())) {
                ret = temp;
            }
        }
        return ret;
    }

    public synchronized Vector getElements(String n) {
        Vector ret = new Vector();
        XMLElement temp;
        for (int i = 0; i < content.size(); i++) {
            temp = getElement(i);
            if (n.equals(temp.getName())) {
                ret.add(temp);
            }
        }
        return ret;
    }

    public synchronized XMLElement getElementRecursive(XMLElement search) {
        XMLElement ret = null;
        XMLElement temp = null;
        if (content != null) {
            for (int i = 0; i < content.size(); i++) {
                temp = getElement(i).getElementRecursive(search);
                if (temp != null && temp.equals(search)) {
                    ret = temp;
                }
            }
        }
        return ret;
    }

    public XMLElement lastElement() {
        Object o = content.lastElement();
        if (o instanceof XMLElement) return (XMLElement) o; else return new XMLElement("" + o.hashCode(), (String) o, 0);
    }

    public void set(int i, XMLElement xe) {
        if (content != null) {
            content.set(i, xe);
        }
    }

    public void setValue(String s) {
        value = s;
    }

    public void setValue(Vector v) {
        valuePool = v;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        if (valuePool.size() == 0) return value; else {
            String ret = "";
            for (int i = 0; i < valuePool.size(); i++) {
                ret += (String) valuePool.elementAt(i);
            }
            return ret;
        }
    }

    public synchronized String getValueOf(String n) {
        String ret = null;
        String temp = null;
        if (name.equals(n)) {
            if (valuePool.size() > 0) {
                ret = "";
                for (int i = 0; i < valuePool.size(); i++) {
                    ret += (String) valuePool.elementAt(i);
                }
            } else {
                ret = this.getValue();
            }
        } else {
            if (content != null) {
                for (int i = 0; i < content.size(); i++) {
                    temp = ((XMLElement) content.elementAt(i)).getValueOf(n);
                    if (temp != null) {
                        ret = temp;
                    }
                }
            }
        }
        return ret;
    }

    public synchronized String getValueOfContent(String n) {
        String ret = null;
        String temp = null;
        if (name.equals(n)) {
            if (valuePool.size() > 0) {
                ret = "";
                for (int i = 0; i < valuePool.size(); i++) {
                    ret += (String) valuePool.elementAt(i);
                }
            } else {
                ret = this.getValue();
            }
        } else {
            if (content != null) {
                for (int i = 0; i < content.size(); i++) {
                    temp = ((XMLElement) content.elementAt(i)).getValue();
                    if (n.equals(temp)) {
                        ret = temp;
                    }
                }
            }
        }
        return ret;
    }

    public void setValueOf(String n, String v) {
        if (getElement(n) == null) {
            XMLElement xmle = new XMLElement(n, v, 1);
            content.add(xmle);
        } else {
            XMLElement xmle = getElement(n);
            xmle.setValue(v);
            content.set(content.indexOf(getElement(n)), xmle);
        }
    }

    public Vector getContent() {
        return content;
    }

    public void setOccurance(int i) {
        occurance = i;
    }

    public int occured() {
        return occurance;
    }

    public int getParents() {
        return parents;
    }

    public int getContentSize() {
        if (content != null) return content.size(); else return 0;
    }

    public void setName(String s) {
        name = s;
    }

    public String toString() {
        return XMLWriter.printStruct(this, 0);
    }

    public synchronized void addArg(String name, String value) {
        if (args == null) args = Collections.synchronizedMap(new HashMap());
        Map m = Collections.synchronizedMap(args);
        synchronized (m) {
            m.put(name, value);
        }
        args = m;
    }

    public synchronized String getArgValue(String name) {
        if (args != null) {
            Map m = Collections.synchronizedMap(args);
            return (String) m.get(name);
        } else return null;
    }

    public Map getArgs() {
        if (args != null) return Collections.synchronizedMap(args); else return null;
    }

    public int compareTo(Object o) {
        XMLElement comp = (XMLElement) o;
        if (this.occured() < comp.occured()) return -1; else if (this.occured() > comp.occured()) return 1; else return 0;
    }

    public XMLElement last() {
        if (content == null) return null; else return (XMLElement) content.lastElement();
    }

    public void swapContent() {
        content = Helper.swapVector(content);
    }

    public void addAll(Vector v) {
        if (content == null) content = new Vector();
        content.addAll(v);
    }

    public void addAll(XMLElement xmle) {
        if (xmle != null) {
            if (content == null) content = new Vector();
            content.addAll(xmle.getContent());
        }
    }

    public void addAllReverse(Vector v) {
        if (v != null) {
            if (content == null) content = new Vector();
            for (int i = v.size() - 1; i >= 0; i--) content.add(v.elementAt(i));
        }
    }

    public void addAllReverse(XMLElement xmle) {
        if (xmle != null) {
            if (content == null) content = new Vector();
            Vector xmlec = xmle.getContent();
            for (int i = xmlec.size() - 1; i >= 0; i--) content.add(xmlec.elementAt(i));
        }
    }

    public synchronized void addArgs(String value) {
        if (value.endsWith("/")) value = value.substring(0, value.length() - 1);
        String name = "";
        String val = "";
        boolean nameread = true;
        boolean valueread = false;
        for (int i = 0; i < value.length(); i++) {
            if (nameread) {
                if (value.charAt(i) != '=') {
                    if (value.charAt(i) != ' ') name += value.charAt(i);
                } else nameread = false;
            } else {
                if (valueread) {
                    if (value.charAt(i) != '"') val += value.charAt(i); else {
                        this.addArg(name, val);
                        name = "";
                        val = "";
                        valueread = false;
                        nameread = true;
                    }
                }
                if (name.length() > 0 && value.charAt(i) == '"') {
                    valueread = true;
                }
            }
        }
        name = null;
        val = null;
    }
}
