package com.siemens.ct.exi.values;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class QNameValue extends AbstractValue {

    private static final long serialVersionUID = -6092774558055449492L;

    protected final String namespaceUri;

    protected final String localName;

    protected final String prefix;

    protected char[] characters;

    protected String sValue;

    public QNameValue(String namespaceUri, String localName, String prefix) {
        super(ValueType.QNAME);
        this.namespaceUri = namespaceUri;
        this.localName = localName;
        this.prefix = prefix;
        if (prefix == null || prefix.length() == 0) {
            sValue = localName;
        } else {
            sValue = prefix + ":" + localName;
        }
    }

    public String getNamespaceUri() {
        return this.namespaceUri;
    }

    public String getLocalName() {
        return this.localName;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getCharactersLength() {
        return sValue.length();
    }

    public char[] toCharacters(char[] cbuffer, int offset) {
        for (int i = 0; i < sValue.length(); i++) {
            cbuffer[i + offset] = sValue.charAt(i);
        }
        return cbuffer;
    }

    @Override
    public String toString() {
        return sValue;
    }

    @Override
    public String toString(char[] cbuffer, int offset) {
        return sValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof QNameValue) {
            QNameValue other = (QNameValue) o;
            return namespaceUri.equals(other.namespaceUri) && localName.equals(other.localName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return namespaceUri.hashCode() ^ localName.hashCode();
    }
}
