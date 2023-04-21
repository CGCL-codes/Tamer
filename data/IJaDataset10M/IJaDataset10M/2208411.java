package org.zoolu.sip.header;

import org.zoolu.sip.provider.SipParser;

/** SIP Header Content-Length */
public class ContentLengthHeader extends Header {

    public ContentLengthHeader(int len) {
        super(SipHeaders.Content_Length, String.valueOf(len));
    }

    public ContentLengthHeader(Header hd) {
        super(hd);
    }

    /** Gets content-length of ContentLengthHeader */
    public int getContentLength() {
        return (new SipParser(m_strValue)).getInt();
    }

    /** Set content-length of ContentLengthHeader */
    public void setContentLength(int cLength) {
        m_strValue = String.valueOf(cLength);
    }
}
