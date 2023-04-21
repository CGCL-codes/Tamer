package org.apache.axiom.attachments.impl;

import org.apache.axiom.attachments.utils.BAAInputStream;
import org.apache.axiom.om.OMException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * PartOnMemoryEnhanced stores the attachment in memory (in non-contigous byte arrays)
 * This implementation is used for smaller attachments to enhance 
 * performance.
 * 
 * The PartOnMemoryEnhanced object is created by the PartFactory
 * @see org.apache.axiom.attachments.impl.PartFactory.
 */
public class PartOnMemoryEnhanced extends AbstractPart {

    private static Log log = LogFactory.getLog(PartOnMemoryEnhanced.class);

    ArrayList data;

    int length;

    /**
     * Construct a PartOnMemory
     * @param headers
     * @param data array list of 4K byte[]
     * @param length (length of data in bytes)
     */
    PartOnMemoryEnhanced(Hashtable headers, ArrayList data, int length) {
        super(headers);
        this.data = data;
        this.length = length;
    }

    public DataHandler getDataHandler() throws MessagingException {
        DataSource ds = new MyByteArrayDataSource();
        return new MyDataHandler(ds);
    }

    public String getFileName() throws MessagingException {
        return null;
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return new BAAInputStream(data, length);
    }

    public long getSize() throws MessagingException {
        return length;
    }

    class MyDataHandler extends DataHandler {

        DataSource ds;

        public MyDataHandler(DataSource ds) {
            super(ds);
            this.ds = ds;
        }

        public void writeTo(OutputStream os) throws IOException {
            InputStream is = ds.getInputStream();
            if (is instanceof BAAInputStream) {
                ((BAAInputStream) is).writeTo(os);
            } else {
                BufferUtils.inputStream2OutputStream(is, os);
            }
        }
    }

    /**
     * A DataSource that is backed by the byte[] and 
     * headers map.
     */
    class MyByteArrayDataSource implements DataSource {

        public String getContentType() {
            String ct = getHeader("content-type");
            return (ct == null) ? "application/octet-stream" : ct;
        }

        public InputStream getInputStream() throws IOException {
            InputStream is = new BAAInputStream(data, length);
            String cte = null;
            try {
                cte = getContentTransferEncoding();
                if (cte != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Start Decoding stream");
                    }
                    return MimeUtility.decode(is, cte);
                }
            } catch (MessagingException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Stream Failed decoding");
                }
                throw new OMException(e);
            }
            return is;
        }

        public String getName() {
            return "MyByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}
