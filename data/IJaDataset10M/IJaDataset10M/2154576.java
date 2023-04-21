package org.zoolu.sip.header;

/** Header is the base Class for all SIP Headers
 */
public class Header {

    /** The header type */
    protected String m_strName;

    /** The header string, without terminating CRLF */
    protected String m_strValue;

    /** Creates a void Header. */
    protected Header() {
        m_strName = null;
        m_strValue = null;
    }

    /** Creates a new Header. */
    public Header(String hname, String hvalue) {
        m_strName = hname;
        m_strValue = hvalue;
    }

    /** Creates a new Header. */
    public Header(Header header) {
        m_strName = header.getName();
        m_strValue = header.getValue();
    }

    /** Creates and returns a copy of the Header */
    public Object clone() {
        return new Header(getName(), getValue());
    }

    /** Whether the Header is equal to Object <i>obj</i> */
    public boolean equals(Object obj) {
        try {
            Header header = (Header) obj;
            return (header.getName().equals(this.getName()) && header.getValue().equals(this.getValue()));
        } catch (Exception e) {
            return false;
        }
    }

    /** Gets m_strName of Header */
    public String getName() {
        return m_strName;
    }

    /** Gets m_strValue of Header */
    public String getValue() {
        return m_strValue;
    }

    /** Sets m_strValue of Header */
    public void setValue(String strHvalue) {
        m_strValue = strHvalue;
    }

    /** Gets string representation of Header */
    public String toString() {
        return m_strName + ": " + m_strValue + "\r\n";
    }
}
