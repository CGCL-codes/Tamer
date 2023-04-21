package org.jjsip.sip.message;

import java.util.Vector;
import org.jjsip.net.UdpPacket;
import org.jjsip.sip.header.ContentLengthHeader;
import org.jjsip.sip.header.ContentTypeHeader;
import org.jjsip.sip.header.Header;
import org.jjsip.sip.header.MultipleHeader;
import org.jjsip.sip.header.RequestLine;
import org.jjsip.sip.header.SipHeaderType;
import org.jjsip.sip.header.StatusLine;
import org.jjsip.sip.provider.SipParser;

/**
 * Class BaseMessageOtp implements a generic SIP Message. It extends class
 * BaseMessage adding one-time-parsing functionality (it parses the entire
 * Message just when it is constructed).
 * <p/>
 * At the contrary, class BaseMessage works in a just-in-time manner (it parses
 * the message each time a particular header field is requested).
 */
public abstract class BaseMessageOtp extends BaseMessage {

    protected RequestLine request_line;

    protected StatusLine status_line;

    protected Vector<Header> headers;

    protected String body;

    /** Inits empty Message */
    private void init() {
        request_line = null;
        status_line = null;
        headers = null;
        body = null;
    }

    /** Constructs a new empty Message */
    public BaseMessageOtp() {
        init();
        headers = new Vector<Header>();
    }

    /** Constructs a new Message */
    public BaseMessageOtp(byte[] data, int offset, int len) {
        init();
        parseIt(new String(data, offset, len));
    }

    /** Constructs a new Message */
    public BaseMessageOtp(UdpPacket packet) {
        init();
        parseIt(new String(packet.getData(), packet.getOffset(), packet.getLength()));
    }

    /** Constructs a new Message */
    public BaseMessageOtp(String str) {
        init();
        parseIt(str);
    }

    /** Constructs a new Message */
    public BaseMessageOtp(BaseMessageOtp msg) {
        init();
        remote_addr = msg.remote_addr;
        remote_port = msg.remote_port;
        transport_proto = msg.transport_proto;
        connection_id = msg.connection_id;
        request_line = msg.request_line;
        status_line = msg.status_line;
        headers = new Vector<Header>();
        for (int i = 0; i < msg.headers.size(); i++) headers.addElement(msg.headers.elementAt(i));
        body = msg.body;
    }

    /** Sets the entire message */
    @Override
    public void setMessage(String str) {
        parseIt(str);
    }

    /**
	 * @param str
	 */
    private void parseIt(String str) {
        SipParser par = new SipParser(str);
        String version = str.substring(0, 4);
        if (version.equalsIgnoreCase("SIP/")) status_line = par.getStatusLine(); else request_line = par.getRequestLine();
        headers = new Vector<Header>();
        Header h = par.getHeader();
        while (h != null) {
            headers.addElement(h);
            h = par.getHeader();
        }
        ContentLengthHeader clh = getContentLengthHeader();
        if (clh != null) {
            int len = clh.getContentLength();
            body = par.getString(len);
        } else if (getContentTypeHeader() != null) {
            body = par.getRemainingString();
            if (body.length() == 0) body = null;
        }
    }

    /** Gets string representation of Message */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (request_line != null) str.append(request_line.toString()); else if (status_line != null) str.append(status_line.toString());
        for (int i = 0; i < headers.size(); i++) str.append(((Header) headers.elementAt(i)).toString());
        str.append("\r\n");
        if (body != null) str.append(body);
        return str.toString();
    }

    /** Gets message length */
    @Override
    public int getLength() {
        return toString().length();
    }

    /** Whether Message is a Request */
    @Override
    public boolean isRequest() {
        if (request_line != null) return true; else return false;
    }

    /** Whether Message is a <i>method</i> request */
    @Override
    public boolean isRequest(String method) {
        if (request_line != null && request_line.getMethod().equalsIgnoreCase(method)) return true; else return false;
    }

    /** Whether Message has Request-line */
    @Override
    protected boolean hasRequestLine() {
        return request_line != null;
    }

    /**
	 * Gets RequestLine in Message (Returns null if called for no request
	 * message)
	 */
    @Override
    public RequestLine getRequestLine() {
        return request_line;
    }

    /** Sets RequestLine of the Message */
    @Override
    public void setRequestLine(RequestLine rl) {
        request_line = rl;
    }

    /** Removes RequestLine of the Message */
    @Override
    public void removeRequestLine() {
        request_line = null;
    }

    /** Whether Message is a Response */
    @Override
    public boolean isResponse() throws NullPointerException {
        if (status_line != null) return true; else return false;
    }

    /** Whether Message has Status-line */
    @Override
    protected boolean hasStatusLine() {
        return status_line != null;
    }

    /**
	 * Gets StautsLine in Message (Returns null if called for no response
	 * message)
	 */
    @Override
    public StatusLine getStatusLine() {
        return status_line;
    }

    /** Sets StatusLine of the Message */
    @Override
    public void setStatusLine(StatusLine sl) {
        status_line = sl;
    }

    /** Removes StatusLine of the Message */
    @Override
    public void removeStatusLine() {
        status_line = null;
    }

    /** Removes Request\Status Line of the Message */
    @Override
    protected void removeFirstLine() {
        removeRequestLine();
        removeStatusLine();
    }

    /**
	 * @param hname
	 * @return
	 */
    protected int indexOfHeader(String hname) {
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.elementAt(i);
            if (hname.equalsIgnoreCase(h.getHeaderType().fullName)) return i;
        }
        return -1;
    }

    /**
	 * Gets the first Header of specified name (Returns null if no Header is
	 * found)
	 */
    @Override
    public Header getHeader(String hname) {
        int i = indexOfHeader(hname);
        if (i < 0) return null; else return (Header) headers.elementAt(i);
    }

    /**
	 * Gets a Vector of all Headers of specified name (Returns empty Vector if
	 * no Header is found)
	 */
    @Override
    public Vector<Header> getHeaders(String hname) {
        Vector<Header> v = new Vector<Header>();
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.elementAt(i);
            if (hname.equalsIgnoreCase(h.getHeaderType().fullName)) {
                v.addElement(h);
            }
        }
        return v;
    }

    /**
	 * Adds Header at the top/bottom. The bottom is considered before the
	 * Content-Length and Content-Type headers
	 */
    @Override
    public void addHeader(Header header, boolean top) {
        if (top) headers.insertElementAt(header, 0); else headers.addElement(header);
    }

    /** Adds a Vector of Headers at the top/bottom */
    @Override
    public void addHeaders(Vector<Header> headers, boolean top) {
        for (int i = 0; i < headers.size(); i++) if (top) this.headers.insertElementAt(headers.elementAt(i), i); else this.headers.addElement(headers.elementAt(i));
    }

    /** Adds MultipleHeader(s) <i>mheader</i> at the top/bottom */
    @Override
    public void addHeaders(MultipleHeader mheader, boolean top) {
        if (mheader.isCommaSeparated()) addHeader(mheader.toHeader(), top); else addHeaders(mheader.getHeaders(), top);
    }

    /**
	 * Adds Header before the first header <i>refer_hname</i> .
	 * <p>
	 * If there is no header of such type, it is added at top
	 */
    @Override
    public void addHeaderBefore(Header new_header, String refer_hname) {
        int i = indexOfHeader(refer_hname);
        if (i < 0) i = 0;
        headers.insertElementAt(new_header, i);
    }

    /**
	 * Adds MultipleHeader(s) before the first header <i>refer_hname</i> .
	 * <p>
	 * If there is no header of such type, they are added at top
	 */
    @Override
    public void addHeadersBefore(MultipleHeader mheader, String refer_hname) {
        if (mheader.isCommaSeparated()) addHeaderBefore(mheader.toHeader(), refer_hname); else {
            int index = indexOfHeader(refer_hname);
            if (index < 0) index = 0;
            Vector<Header> hs = mheader.getHeaders();
            for (int k = 0; k < hs.size(); k++) headers.insertElementAt(hs.elementAt(k), index + k);
        }
    }

    /**
	 * Adds Header after the first header <i>refer_hname</i> .
	 * <p>
	 * If there is no header of such type, it is added at bottom
	 */
    @Override
    public void addHeaderAfter(Header new_header, String refer_hname) {
        int i = indexOfHeader(refer_hname);
        if (i >= 0) i++; else i = headers.size();
        headers.insertElementAt(new_header, i);
    }

    /**
	 * Adds MultipleHeader(s) after the first header <i>refer_hname</i> .
	 * <p>
	 * If there is no header of such type, they are added at bottom
	 */
    @Override
    public void addHeadersAfter(MultipleHeader mheader, String refer_hname) {
        if (mheader.isCommaSeparated()) addHeaderAfter(mheader.toHeader(), refer_hname); else {
            int index = indexOfHeader(refer_hname);
            if (index >= 0) index++; else index = headers.size();
            Vector<Header> hs = mheader.getHeaders();
            for (int k = 0; k < hs.size(); k++) headers.insertElementAt(hs.elementAt(k), index + k);
        }
    }

    /** Removes first Header of specified name */
    @Override
    public boolean removeHeader(String hname) {
        return removeHeader(hname, true);
    }

    /** Removes first (or last) Header of specified name. */
    @Override
    public boolean removeHeader(String hname, boolean first) {
        int index = -1;
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.elementAt(i);
            if (hname.equalsIgnoreCase(h.getHeaderType().fullName)) {
                index = i;
                if (first) {
                    i = headers.size();
                }
            }
        }
        if (index >= 0) {
            headers.removeElementAt(index);
            return true;
        }
        return false;
    }

    /** Removes all Headers of specified name */
    @Override
    public void removeAllHeaders(String hname) {
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.elementAt(i);
            if (hname.equalsIgnoreCase(h.getHeaderType().fullName)) {
                headers.removeElementAt(i);
                i--;
            }
        }
    }

    /**
	 * Sets the Header <i>hd</i> removing any previous headers of the same type.
	 */
    @Override
    public void setHeader(Header hd) {
        boolean first = true;
        SipHeaderType headerType = hd.getHeaderType();
        for (int i = 0; i < headers.size(); i++) {
            Header h = (Header) headers.elementAt(i);
            if (headerType == h.getHeaderType()) {
                if (first) {
                    headers.setElementAt(h, i);
                    first = false;
                } else {
                    headers.removeElementAt(i);
                    i--;
                }
            }
        }
        if (first) headers.addElement(hd);
    }

    /** Sets MultipleHeader <i>mheader</i> */
    @Override
    public void setHeaders(MultipleHeader mheader) {
        if (mheader.isCommaSeparated()) setHeader(mheader.toHeader()); else {
            boolean first = true;
            SipHeaderType headerType = mheader.getHeaderType();
            for (int i = 0; i < headers.size(); i++) {
                Header h = (Header) headers.elementAt(i);
                if (headerType == h.getHeaderType()) {
                    if (first) {
                        Vector<Header> hs = mheader.getHeaders();
                        for (int k = 0; k < hs.size(); k++) headers.insertElementAt(hs.elementAt(k), i + k);
                        first = false;
                        i += hs.size() - 1;
                    } else {
                        headers.removeElementAt(i);
                        i--;
                    }
                }
            }
        }
    }

    /** Whether Message has Body */
    @Override
    public boolean hasBody() {
        return this.body != null;
    }

    /** Gets body(content) type */
    @Override
    public String getBodyType() {
        return getContentTypeHeader().getContentType();
    }

    /** Sets the message body */
    @Override
    public void setBody(String content_type, String body) {
        removeBody();
        if (body != null && body.length() > 0) {
            setContentTypeHeader(new ContentTypeHeader(content_type));
            setContentLengthHeader(new ContentLengthHeader(body.length()));
            this.body = body;
        } else {
            setContentLengthHeader(new ContentLengthHeader(0));
            this.body = null;
        }
    }

    /**
	 * Gets message body. The end of body is evaluated from the Content-Length
	 * header if present (SIP-RFC compliant), or from the end of message if no
	 * Content-Length header is present (non-SIP-RFC compliant)
	 */
    @Override
    public String getBody() {
        return this.body;
    }

    /** Removes the message body (if it exists) and the final empty line */
    @Override
    public void removeBody() {
        removeContentLengthHeader();
        removeContentTypeHeader();
        this.body = null;
    }
}
