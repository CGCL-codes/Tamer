package net.sf.yaxdiff.sax;

import org.xml.sax.Attributes;

final class EscapedAttributes implements Attributes {

    static String escaped(String s) {
        for (int i = 0; i < s.length(); i++) {
            switch(s.charAt(i)) {
                case '\n':
                case '\r':
                case '\\':
                    return s.replace("\n", "\\n").replace("\r", "\\r").replace("\\", "\\\\");
                default:
            }
        }
        return s;
    }

    private Attributes wrapped = null;

    /**
     * @param uri
     * @param localName
     * @return
     * @see org.xml.sax.Attributes#getIndex(java.lang.String, java.lang.String)
     */
    public int getIndex(String uri, String localName) {
        return wrapped.getIndex(uri, localName);
    }

    public void wrap(Attributes atts) {
        this.wrapped = atts;
    }

    /**
     * @param name
     * @return
     * @see org.xml.sax.Attributes#getIndex(java.lang.String)
     */
    public int getIndex(String name) {
        return wrapped.getIndex(name);
    }

    /**
     * @return
     * @see org.xml.sax.Attributes#getLength()
     */
    public int getLength() {
        return wrapped.getLength();
    }

    /**
     * @param index
     * @return
     * @see org.xml.sax.Attributes#getLocalName(int)
     */
    public String getLocalName(int index) {
        return wrapped.getLocalName(index);
    }

    /**
     * @param index
     * @return
     * @see org.xml.sax.Attributes#getQName(int)
     */
    public String getQName(int index) {
        return wrapped.getQName(index);
    }

    /**
     * @param index
     * @return
     * @see org.xml.sax.Attributes#getType(int)
     */
    public String getType(int index) {
        return wrapped.getType(index);
    }

    /**
     * @param uri
     * @param localName
     * @return
     * @see org.xml.sax.Attributes#getType(java.lang.String, java.lang.String)
     */
    public String getType(String uri, String localName) {
        return wrapped.getType(uri, localName);
    }

    /**
     * @param name
     * @return
     * @see org.xml.sax.Attributes#getType(java.lang.String)
     */
    public String getType(String name) {
        return wrapped.getType(name);
    }

    /**
     * @param index
     * @return
     * @see org.xml.sax.Attributes#getURI(int)
     */
    public String getURI(int index) {
        return wrapped.getURI(index);
    }

    /**
     * @param index
     * @return
     * @see org.xml.sax.Attributes#getValue(int)
     */
    public String getValue(int index) {
        return escaped(wrapped.getValue(index));
    }

    /**
     * @param uri
     * @param localName
     * @return
     * @see org.xml.sax.Attributes#getValue(java.lang.String, java.lang.String)
     */
    public String getValue(String uri, String localName) {
        return escaped(wrapped.getValue(uri, localName));
    }

    /**
     * @param name
     * @return
     * @see org.xml.sax.Attributes#getValue(java.lang.String)
     */
    public String getValue(String name) {
        return escaped(wrapped.getValue(name));
    }
}
