package org.zoolu.sdp;

import org.zoolu.tools.Parser;

/** SDP connection field.
  * <p>
  * <BLOCKQUOTE><PRE>
  *    connection-field = "c=" nettype SP addrtype SP connection-address CRLF
  *                       ;a connection field must be present
  *                       ;in every media description or at the
  *                       ;session-level
  * </PRE></BLOCKQUOTE>
  */
public class ConnectionField extends SdpField {

    /** Creates a new ConnectionField. */
    public ConnectionField(String connection_field) {
        super('c', connection_field);
    }

    /** Creates a new ConnectionField. */
    public ConnectionField(String address_type, String address, int ttl, int num) {
        super('c', null);
        m_strValue = "IN " + address_type + " " + address;
        if (ttl > 0) m_strValue += "/" + ttl;
        if (num > 0) m_strValue += "/" + num;
    }

    /** Creates a new ConnectionField. */
    public ConnectionField(String address_type, String address) {
        super('c', "IN " + address_type + " " + address);
    }

    /** Creates a new ConnectionField. */
    public ConnectionField(SdpField sf) {
        super(sf);
    }

    /** Gets the connection address. */
    public String getAddressType() {
        String type = (new Parser(m_strValue)).skipString().getString();
        return type;
    }

    /** Gets the connection address. */
    public String getAddress() {
        String address = (new Parser(m_strValue)).skipString().skipString().getString();
        int i = address.indexOf("/");
        if (i < 0) return address; else return address.substring(0, i);
    }

    /** Gets the TTL. */
    public int getTTL() {
        String address = (new Parser(m_strValue)).skipString().skipString().getString();
        int i = address.indexOf("/");
        if (i < 0) return 0;
        int j = address.indexOf("/", i);
        if (j < 0) return Integer.parseInt(address.substring(i)); else return Integer.parseInt(address.substring(i, j));
    }

    /** Gets the number of addresses. */
    public int getNum() {
        String address = (new Parser(m_strValue)).skipString().skipString().getString();
        int i = address.indexOf("/");
        if (i < 0) return 0;
        int j = address.indexOf("/", i);
        if (j < 0) return 0;
        return Integer.parseInt(address.substring(j));
    }
}
