package javax.mail.internet;

import javax.mail.*;
import javax.activation.*;
import java.io.*;
import java.net.UnknownServiceException;

/**
 * A utility class that implements a DataSource out of
 * a MimePart. This class is primarily meant for service providers.
 *
 * @see		javax.mail.internet.MimePart
 * @see		javax.activation.DataSource
 * @author 	John Mani
 */
public class MimePartDataSource implements DataSource, MessageAware {

    /**
     * The MimePart that provides the data for this DataSource.
     *
     * @since	JavaMail 1.4
     */
    protected MimePart part;

    private MessageContext context;

    private static boolean ignoreMultipartEncoding = true;

    static {
        try {
            String s = System.getProperty("mail.mime.ignoremultipartencoding");
            ignoreMultipartEncoding = s == null || !s.equalsIgnoreCase("false");
        } catch (SecurityException sex) {
        }
    }

    /**
     * Constructor, that constructs a DataSource from a MimePart.
     */
    public MimePartDataSource(MimePart part) {
        this.part = part;
    }

    /**
     * Returns an input stream from this  MimePart. <p>
     *
     * This method applies the appropriate transfer-decoding, based 
     * on the Content-Transfer-Encoding attribute of this MimePart.
     * Thus the returned input stream is a decoded stream of bytes.<p>
     *
     * This implementation obtains the raw content from the Part
     * using the <code>getContentStream()</code> method and decodes
     * it using the <code>MimeUtility.decode()</code> method.
     *
     * @see	javax.mail.internet.MimeMessage#getContentStream
     * @see	javax.mail.internet.MimeBodyPart#getContentStream
     * @see	javax.mail.internet.MimeUtility#decode
     * @return 	decoded input stream
     */
    public InputStream getInputStream() throws IOException {
        InputStream is;
        try {
            if (part instanceof MimeBodyPart) is = ((MimeBodyPart) part).getContentStream(); else if (part instanceof MimeMessage) is = ((MimeMessage) part).getContentStream(); else throw new MessagingException("Unknown part");
            String encoding = restrictEncoding(part.getEncoding(), part);
            if (encoding != null) return MimeUtility.decode(is, encoding); else return is;
        } catch (MessagingException mex) {
            throw new IOException(mex.getMessage());
        }
    }

    /**
     * Restrict the encoding to values allowed for the
     * Content-Type of the specified MimePart.  Returns
     * either the original encoding or null.
     */
    private static String restrictEncoding(String encoding, MimePart part) throws MessagingException {
        if (!ignoreMultipartEncoding || encoding == null) return encoding;
        if (encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit") || encoding.equalsIgnoreCase("binary")) return encoding;
        String type = part.getContentType();
        if (type == null) return encoding;
        try {
            ContentType cType = new ContentType(type);
            if (cType.match("multipart/*") || cType.match("message/*")) return null;
        } catch (ParseException pex) {
        }
        return encoding;
    }

    /**
     * DataSource method to return an output stream. <p>
     *
     * This implementation throws the UnknownServiceException.
     */
    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException();
    }

    /**
     * Returns the content-type of this DataSource. <p>
     *
     * This implementation just invokes the <code>getContentType</code>
     * method on the MimePart.
     */
    public String getContentType() {
        try {
            return part.getContentType();
        } catch (MessagingException mex) {
            return "application/octet-stream";
        }
    }

    /**
     * DataSource method to return a name.  <p>
     *
     * This implementation just returns an empty string.
     */
    public String getName() {
        try {
            if (part instanceof MimeBodyPart) return ((MimeBodyPart) part).getFileName();
        } catch (MessagingException mex) {
        }
        return "";
    }

    /**
     * Return the <code>MessageContext</code> for the current part.
     * @since JavaMail 1.1
     */
    public synchronized MessageContext getMessageContext() {
        if (context == null) context = new MessageContext(part);
        return context;
    }
}
