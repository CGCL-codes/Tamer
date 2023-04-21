package org.xmlpull.v1.xni2xmlpull1;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.apache.xerces.xni.XMLString;

/**
 * This is pointer to internal state of X2Iterator.
 * Intention is to extract state form XNI callback and make it accessible later.
 *
 * <p><b>NOTE:</b> this object is invalid after call to iterator nextState() method!!!!
 */
public class X2State {

    private static class X2Attribute {

        String name;

        String prefix;

        String namespace;

        String value;

        boolean isDefault;

        String type;
    }

    private static class X2Namespace {

        String prefix;

        String uri;
    }

    protected int lineNumber;

    protected int columnNumber;

    protected Exception savedException;

    protected X2StateType stateType;

    protected char[] buf = new char[0];

    protected int off;

    protected int len;

    protected boolean empty;

    protected String namespace;

    protected String prefix;

    protected String name;

    private X2Attribute[] attrs = new X2Attribute[0];

    private int attrsEnd = 0;

    private X2Namespace[] ns = new X2Namespace[0];

    private int nsEnd = 0;

    protected X2Iterator iter;

    X2State() {
    }

    public void reset(X2StateType stateType, X2Iterator iter) {
        this.stateType = stateType;
        savedException = null;
        off = len = 0;
        empty = false;
        prefix = name = namespace = null;
        attrsEnd = 0;
        nsEnd = 0;
        if (iter == null) throw new NullPointerException();
        this.iter = iter;
        lineNumber = iter.locator != null ? iter.locator.getLineNumber() : -1;
        columnNumber = iter.locator != null ? iter.locator.getColumnNumber() : -1;
    }

    public X2StateType getStateType() {
        return stateType;
    }

    public void setName(String value) {
        name = value;
    }

    public String getName() {
        return name;
    }

    public void setPrefix(String value) {
        prefix = value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setNamespace(String value) {
        namespace = value;
    }

    public String getNamespace() {
        return namespace;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public boolean isEmptyElement() {
        if (stateType != X2StateType.START_TAG) {
            throw new IllegalArgumentException();
        }
        return empty;
    }

    public void setEmptyElement(boolean value) {
        if (stateType != X2StateType.START_TAG) {
            throw new IllegalArgumentException();
        }
        empty = value;
    }

    public void setString(XMLString string) {
        if (stateType != X2StateType.TEXT && stateType != X2StateType.CDSECT && stateType != X2StateType.ENTITY_REF && stateType != X2StateType.IGNORABLE_WHITESPACE && stateType != X2StateType.PROCESSING_INSTRUCTION && stateType != X2StateType.COMMENT && stateType != X2StateType.DOCDECL) {
            throw new IllegalArgumentException();
        }
        if (buf.length < string.length) {
            buf = new char[string.length];
        }
        len = string.length;
        off = 0;
        System.arraycopy(string.ch, string.offset, buf, 0, len);
    }

    public void appendText(char[] ch, int coff, int clen) {
        if ((off + len + clen) >= buf.length) {
            char[] newBuf = new char[off + len + clen + 100];
            if (len > 0) {
                System.arraycopy(buf, off, newBuf, 0, len);
            }
            off = 0;
            buf = newBuf;
        }
        System.arraycopy(ch, coff, buf, off + len, clen);
        len += clen;
    }

    public char[] getBuf() {
        if (stateType != X2StateType.TEXT && stateType != X2StateType.CDSECT && stateType != X2StateType.ENTITY_REF && stateType != X2StateType.IGNORABLE_WHITESPACE && stateType != X2StateType.PROCESSING_INSTRUCTION && stateType != X2StateType.COMMENT && stateType != X2StateType.DOCDECL) {
            throw new IllegalArgumentException();
        }
        return buf;
    }

    public int getOff() {
        if (stateType != X2StateType.TEXT && stateType != X2StateType.CDSECT && stateType != X2StateType.ENTITY_REF && stateType != X2StateType.IGNORABLE_WHITESPACE && stateType != X2StateType.PROCESSING_INSTRUCTION && stateType != X2StateType.COMMENT && stateType != X2StateType.DOCDECL) {
            throw new IllegalArgumentException();
        }
        return off;
    }

    public int getLen() {
        if (stateType != X2StateType.TEXT && stateType != X2StateType.CDSECT && stateType != X2StateType.ENTITY_REF && stateType != X2StateType.IGNORABLE_WHITESPACE && stateType != X2StateType.PROCESSING_INSTRUCTION && stateType != X2StateType.COMMENT && stateType != X2StateType.DOCDECL) {
            throw new IllegalArgumentException();
        }
        return len;
    }

    public int getNamespacesLength() {
        return nsEnd;
    }

    public void addNamespaceDeclaration(String prefix, String uri) {
        if (stateType != X2StateType.START_TAG) {
            throw new IllegalStateException();
        }
        if (nsEnd >= ns.length) {
            int length = 2 * nsEnd + 10;
            X2Namespace[] newNs = new X2Namespace[length];
            System.arraycopy(ns, 0, newNs, 0, ns.length);
            for (int i = ns.length; i < newNs.length; i++) {
                newNs[i] = new X2Namespace();
            }
            ns = newNs;
        }
        X2Namespace n = ns[nsEnd];
        n.prefix = prefix;
        n.uri = uri;
        ++nsEnd;
    }

    public String getNamespacesPrefix(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IllegalStateException();
        }
        if (i < 0 || i >= nsEnd) {
            throw new IllegalArgumentException();
        }
        return ns[i].prefix;
    }

    public String getNamespacesUri(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IllegalStateException();
        }
        if (i < 0 || i >= nsEnd) {
            throw new IllegalArgumentException();
        }
        return ns[i].uri;
    }

    public int getAttributesLength() {
        return attrsEnd;
    }

    public void ensureAttributesLength(int length) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (length > attrs.length) {
            X2Attribute[] newAttrs = new X2Attribute[length];
            System.arraycopy(attrs, 0, newAttrs, 0, attrs.length);
            for (int i = attrs.length; i < newAttrs.length; i++) {
                newAttrs[i] = new X2Attribute();
            }
            attrs = newAttrs;
        }
    }

    public void addAttribute(String namespace, String prefix, String name, String value, boolean specified, String type) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (attrsEnd >= attrs.length) {
            ensureAttributesLength(attrs.length + 10);
        }
        X2Attribute attr = attrs[attrsEnd];
        attr.namespace = namespace;
        attr.prefix = prefix;
        attr.name = name;
        attr.value = value;
        attr.isDefault = !specified;
        attr.type = type;
        ++attrsEnd;
    }

    public String getAttributeNamespace(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].namespace;
    }

    public String getAttributePrefix(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].prefix;
    }

    public String getAttributeName(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].name;
    }

    public String getAttributeValue(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].value;
    }

    public boolean isAttributeDefault(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].isDefault;
    }

    public String getAttributeType(int i) {
        if (stateType != X2StateType.START_TAG) {
            throw new IndexOutOfBoundsException();
        }
        if (i < 0 || i >= attrsEnd) {
            throw new IndexOutOfBoundsException();
        }
        return attrs[i].type;
    }

    public String toString() {
        return "[STATE type=" + stateType + "]";
    }
}
