package org.ezfusion.msgsrv;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.dom.DocumentImpl;
import org.ezfusion.exceptions.ArgumentException;
import org.ezfusion.exceptions.EZFusionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Message implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3438999895832515091L;

    private Document xmldoc;

    public Message(String from, String to, Vector<String> forward) throws EZFusionException {
        if (from == null) throw new ArgumentException("String from", "not null", "null");
        if (to == null) throw new ArgumentException("String to", "not null", "null");
        if (forward == null) throw new ArgumentException("Vector<String> forward", "not null", "null");
        xmldoc = new DocumentImpl();
        Element root = xmldoc.createElement("ezmessage");
        root.setAttribute("from", from);
        root.setAttribute("to", to);
        Element fwd = null;
        for (int i = 0; i < forward.size(); i++) {
            fwd = xmldoc.createElement("forward");
            fwd.setAttribute("from", forward.get(i).toString());
            root.appendChild(fwd);
        }
        xmldoc.appendChild(root);
    }

    public void addContent(String key, String value) throws EZFusionException {
        if (key == null) throw new ArgumentException("String key", "not null", "null");
        if (value == null) throw new ArgumentException("String value", "not null", "null");
        NodeList nl = xmldoc.getElementsByTagName("ezmessage");
        Element root = (Element) nl.item(0);
        NodeList nl2 = xmldoc.getElementsByTagName("content");
        for (int i = 0; i < nl2.getLength(); i++) {
            if (((Element) nl2.item(i)).getAttribute("key").equalsIgnoreCase(key)) {
                root.removeChild(nl2.item(i));
                System.out.println("node has been removed");
            }
        }
        Element content = xmldoc.createElementNS(null, "content");
        content.setAttribute("key", key);
        content.setTextContent(value);
        root.appendChild(content);
    }

    public String getContent(String key) throws EZFusionException {
        String content = null;
        if (key != null) {
            NodeList nl = xmldoc.getElementsByTagName("content");
            Element element = null;
            int i = 0;
            while ((i < nl.getLength()) && (content == null)) {
                element = (Element) nl.item(i);
                if (element.getAttribute("key").equalsIgnoreCase(key)) {
                    content = element.getTextContent();
                }
                i++;
            }
        } else throw new ArgumentException("String key", "not null", "null");
        return content;
    }

    public Hashtable<String, String> getContent() {
        Hashtable<String, String> result = new Hashtable<String, String>();
        NodeList nl = xmldoc.getElementsByTagName("content");
        Element element = null;
        int i = 0;
        while (i < nl.getLength()) {
            element = (Element) nl.item(i);
            result.put(element.getAttribute("key"), element.getTextContent());
            i++;
        }
        return result;
    }

    public String toString() {
        String str;
        try {
            str = "from=" + getFrom() + ", to=" + getTo() + ", content=" + getContent();
        } catch (Exception e) {
            str = "unable to read the message";
        }
        return str;
    }

    public String getTo() {
        NodeList nl = xmldoc.getElementsByTagName("ezmessage");
        Element root = (Element) nl.item(0);
        return root.getAttribute("to");
    }

    public String getFrom() {
        NodeList nl = xmldoc.getElementsByTagName("ezmessage");
        Element root = (Element) nl.item(0);
        return root.getAttribute("from");
    }

    public Vector<String> getFwd() {
        Vector<String> result = new Vector<String>();
        NodeList nl = xmldoc.getElementsByTagName("forward");
        for (int i = 0; i < nl.getLength(); i++) {
            result.add(((Element) nl.item(i)).getAttribute("from"));
        }
        return result;
    }

    public void addFwd(String aName) {
        NodeList nl = xmldoc.getElementsByTagName("ezmessage");
        Element root = (Element) nl.item(0);
        Element fwd = xmldoc.createElement("forward");
        fwd.setAttribute("from", aName);
        root.appendChild(fwd);
    }
}
