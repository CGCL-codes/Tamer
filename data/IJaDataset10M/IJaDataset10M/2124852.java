package iwork.cmx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.w3c.dom.*;
import swig.util.Debug;
import swig.util.XMLHelper;
import swig.util.XMLException;

public class Mem {

    /** Variable for an Unique id. */
    String uid = null;

    /**  Variable for the Mem's Attributes. */
    HashMap attributes;

    /** Expiration time of this Mem unit, expressed in XQL format.*/
    String expire;

    /** Raw xml content of the Mem. */
    String content = null;

    /**  Construct an empty Mem. */
    public Mem() {
        attributes = new HashMap();
    }

    /** Construct a copy of the given Mem. */
    public Mem(Mem mem) {
        uid = mem.uid;
        attributes = new HashMap(mem.attributes);
        expire = mem.expire;
        content = mem.content;
    }

    /**
     * Set Mem's Unique ID. Currently not used (the unique ID is set to NULL).
     */
    public void setUID(String uid) {
        this.uid = null;
    }

    /** Get Mem's Unique ID.*/
    public String getUID() {
        return uid;
    }

    /**	Get the value of attribute "name". */
    public String getAttribute(String name) {
        return (String) attributes.get(name);
    }

    /**	Create an attribute "name" with value "value". */
    public void putAttribute(String name, String value) {
        attributes.put(name, value);
    }

    /** Remove the attribute "name".*/
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /** Set expiration. */
    public void setExpiration(String expire) {
        this.expire = expire;
    }

    /**	Get expiration. */
    public String getExpiration() {
        return expire;
    }

    /**Get raw content. */
    public String getContent() {
        return content;
    }

    /**	Set content.*/
    public void setContent(String content) {
        this.content = content;
    }

    /** Create a Mem from a String format XML. */
    public static Mem FromXML(String xml) throws XMLException {
        try {
            Debug.Print("dataheap", "Mem.FromXML: parsing: " + xml);
            Element emem = XMLHelper.GetDocumentElement(xml);
            return FromXML(emem);
        } catch (IOException e) {
            Debug.AssertNotReached();
            return null;
        }
    }

    /**	Create a Mem from an Element format XML. */
    public static Mem FromXML(Element emem) throws XMLException {
        Mem ret = new Mem();
        ret.uid = emem.getAttribute("uid");
        if ("".equals(ret.uid)) ret.uid = null;
        NamedNodeMap attrmap = emem.getAttributes();
        int length = attrmap.getLength();
        for (int i = 0; i < length; i++) {
            Attr attr = (Attr) attrmap.item(i);
            String name = attr.getName();
            String value = attr.getValue();
            Debug.Assert((name != null) && (value != null));
            if (!"uid".equals(name)) ret.putAttribute(name, value);
        }
        NodeList contentlist = emem.getChildNodes();
        length = contentlist.getLength();
        if (length > 0) ret.content = "";
        for (int i = 0; i < length; i++) {
            ret.content += XMLHelper.ToString(contentlist.item(i));
        }
        return ret;
    }

    /**
     * Create a copy of itself in String format XML. 
     */
    public String toXML() {
        StringBuffer ret = new StringBuffer("<mem");
        if (uid != null) {
            ret.append(" uid=\"" + uid + "\"");
        }
        Iterator iter = attributes.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            String value = (String) attributes.get(name);
            if (value != null) {
                ret.append(" " + name + "=\"" + value + "\"");
            }
        }
        if (content != null) {
            ret.append(">");
            ret.append(content);
            ret.append("</mem>");
        } else {
            ret.append(" />");
        }
        return ret.toString();
    }

    /**
     * Identical to toXML()
     */
    public String toString() {
        return toXML();
    }
}
