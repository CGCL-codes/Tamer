package org.apache.axiom.attachments.impl;

import org.apache.axiom.attachments.Part;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.activation.DataHandler;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.HeaderTokenizer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * AbstractPart is a base class for the actual 
 * Part implementations.  The primary purpose of AbstractPart is
 * to define some of the common methods to promote code reuse.
 */
abstract class AbstractPart implements Part {

    private static Log log = LogFactory.getLog(AbstractPart.class);

    private Hashtable headers;

    /**
     * The actual parts are constructed with the PartFactory.
     * @see org.apache.axiom.attachments.impl.PartFactory
     * @param headers
     */
    AbstractPart(Hashtable in) {
        headers = in;
        if (headers == null) {
            headers = new Hashtable();
        }
    }

    public void addHeader(String name, String value) {
        if (log.isDebugEnabled()) {
            log.debug("addHeader: (" + name + ") value=(" + value + ")");
        }
        Header headerObj = new Header(name, value);
        String key = name.toLowerCase();
        headers.put(key, headerObj);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        if (log.isDebugEnabled()) {
            log.debug("getAllHeaders");
        }
        return headers.elements();
    }

    public String getHeader(String name) {
        String key = name.toLowerCase();
        Header header = (Header) headers.get(key);
        String value = header == null ? null : header.getValue();
        if (log.isDebugEnabled()) {
            log.debug("getHeader name=(" + name + ") value=(" + value + ")");
        }
        return value;
    }

    public String getContentID() throws MessagingException {
        return getHeader("content-id");
    }

    public String getContentType() throws MessagingException {
        return getHeader("content-type");
    }

    /**
     * @return contentTransferEncoding
     * @throws MessagingException
     */
    public String getContentTransferEncoding() throws MessagingException {
        if (log.isDebugEnabled()) {
            log.debug("getContentTransferEncoding()");
        }
        String cte = getHeader("content-transfer-encoding");
        if (log.isDebugEnabled()) {
            log.debug(" CTE =" + cte);
        }
        if (cte != null) {
            cte = cte.trim();
            if (cte.equalsIgnoreCase("7bit") || cte.equalsIgnoreCase("8bit") || cte.equalsIgnoreCase("quoted-printable") || cte.equalsIgnoreCase("base64")) {
                return cte;
            }
            HeaderTokenizer ht = new HeaderTokenizer(cte, HeaderTokenizer.MIME);
            boolean done = false;
            while (!done) {
                HeaderTokenizer.Token token = ht.next();
                switch(token.getType()) {
                    case HeaderTokenizer.Token.EOF:
                        if (log.isDebugEnabled()) {
                            log.debug("HeaderTokenizer EOF");
                        }
                        done = true;
                        break;
                    case HeaderTokenizer.Token.ATOM:
                        return token.getValue();
                }
            }
            return cte;
        }
        return null;
    }

    public abstract DataHandler getDataHandler() throws MessagingException;

    public abstract String getFileName() throws MessagingException;

    public abstract InputStream getInputStream() throws IOException, MessagingException;

    public abstract long getSize() throws MessagingException;
}
