package mobi.ilabs.markup;

import java.io.IOException;
import java.util.*;
import mobi.ilabs.common.Debug;
import mobi.ilabs.common.EntityMap;

/**
* The <b>MarkupElement</b> is a utility class for the <b>MarkupReader</b>.
* <p>
* Each <b>MarkupElement</b> has a type, identified by name, and MAY have a set of attribute specifications.
* Each attribute specification has a name and a value.
* <p>
* A <b>MarkupElement</b> may be contained in another, parent <b>MarkupElement</b> of which it is considered a child.
* <p>
* @see <a href="http://www.w3.org/TR/2004/REC-xml-20040204/#sec-logical-struct">XML Logical Structures</a>
* @author �ystein Myhre
*/
public class MarkupElement implements Debug.Constants {

    private static final int START = 0;

    private static final int END = 1;

    private static final int EMPTY = 2;

    private MarkupElement parent;

    private String name;

    private Hashtable attributes;

    private Vector content;

    /**
   * Creates a new <b>MarkupElement</b>.
   * <p>
   * @param name The element's name
   * @throws IOException If an I/O error occurs
   * @since 0.x
   */
    public MarkupElement(String name) {
        this(null, name, null);
    }

    /**
   * Creates a new <b>MarkupElement</b>.
   * <p>
   * @param parent The parent element or null
   * @param name The element's name
   * @param attributes The attributes or null
   * @throws IOException If an I/O error occurs
   * @since 0.x
   */
    public MarkupElement(MarkupElement parent, String name, Hashtable attributes) {
        this.parent = parent;
        this.name = name;
        this.attributes = attributes;
    }

    public MarkupElement getParent() {
        return (parent);
    }

    public String getName() {
        return (name);
    }

    public String getText() {
        StringBuffer all = getAllText();
        if (all == null) return (null);
        return (all.toString());
    }

    public void addChild(MarkupElement elt) {
        elt.parent = this;
        addContent(elt);
    }

    public void removeChild(MarkupElement elt) {
        elt.parent = null;
        removeContent(elt);
    }

    public void addText(String text) {
        addContent(text);
    }

    public void setAttribute(String name, String value) {
        if (name == null || value == null) return;
        if (attributes == null) attributes = new Hashtable();
        attributes.put(name, value);
    }

    public String getAttribute(String name) {
        if (attributes == null) return (null);
        String value = (String) attributes.get(name);
        if (value == null) return (null);
        if (value.length() == 0) return (null);
        return (value);
    }

    public String getAttribute(String name, String defaultValue) {
        String value = getAttribute(name);
        if (value == null) return (defaultValue);
        return (value);
    }

    public int getAttributeInt(String name, int defaultValue) {
        try {
            return (Integer.parseInt(getAttribute(name)));
        } catch (IllegalArgumentException iae) {
        } catch (NullPointerException npe) {
        }
        return (defaultValue);
    }

    public Vector getChildren(String tagname) {
        if (content == null) return (null);
        Vector res = null;
        for (Enumeration e = content.elements(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            if (obj instanceof MarkupElement) {
                if (res == null) res = new Vector();
                if (((MarkupElement) obj).getName().equals(tagname)) res.addElement(obj);
            }
        }
        return (res);
    }

    public MarkupElement getChild(String tagname) {
        if (content == null) return (null);
        for (Enumeration e = content.elements(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            if (obj instanceof MarkupElement) {
                if (((MarkupElement) obj).getName().equals(tagname)) return ((MarkupElement) obj);
            }
        }
        return (null);
    }

    public String getChildText(String tagname) {
        MarkupElement child = getChild(tagname);
        if (child != null) return (child.getText());
        return ("");
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        if (content != null) {
            appendTag(buf, START);
            for (Enumeration e = content.elements(); e.hasMoreElements(); ) {
                Object elt = e.nextElement();
                if (elt instanceof String) {
                    buf.append(EntityMap.escape(new StringBuffer((String) elt)));
                } else buf.append(elt.toString());
            }
            appendTag(buf, END);
        } else appendTag(buf, EMPTY);
        return (buf.toString());
    }

    public String toString() {
        return (toXML());
    }

    private void addContent(Object obj) {
        if (obj == null) return;
        if (content == null) content = new Vector();
        content.addElement(obj);
    }

    private void removeContent(Object obj) {
        if (obj == null) return;
        if (content != null) content.removeElement(obj);
    }

    private StringBuffer getAllText() {
        if (content == null) return (null);
        StringBuffer allText = null;
        for (Enumeration e = content.elements(); e.hasMoreElements(); ) {
            Object obj = e.nextElement();
            if (obj instanceof String) {
                if (allText == null) allText = new StringBuffer();
                allText.append((String) obj);
            }
        }
        return (allText);
    }

    private void appendAttributes(StringBuffer buf) {
        Enumeration e = attributes.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = (String) attributes.get(key);
            buf.append(' ');
            buf.append(key);
            buf.append("=\"");
            buf.append(value);
            buf.append('\"');
        }
    }

    private void appendTag(StringBuffer buf, int type) {
        buf.append('<');
        if (type == END) buf.append('/');
        buf.append(name);
        if (type != END && attributes != null) appendAttributes(buf);
        if (type == EMPTY) buf.append('/');
        buf.append('>');
    }
}
