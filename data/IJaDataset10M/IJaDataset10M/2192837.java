package fr.xebia.jms;

import java.io.IOException;
import java.io.InputStream;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import org.springframework.util.Assert;

/**
 * <p>
 * Small modification on
 * {@link org.springframework.ws.transport.jms.BytesMessageInputStream} :
 * <ul>
 * <li>make class <code>public</code> instead of <code>protected</code>,</li>
 * <li>
 * in {@link #read()} method inspired by
 * {@link java.io.ByteArrayInputStream#read()} : "
 * <code>return message.readByte() & 0xff;</code>" instead of "
 * <code>return message.readByte() </code>".</li>
 * </ul>
 * </p>
 * <p>
 * Initial javadoc:
 * </p>
 * <p>
 * Input stream that wraps a {@link BytesMessage}.
 * </p>
 * 
 * @author Arjen Poutsma
 * @since 1.5.0
 */
public class BytesMessageInputStream extends InputStream {

    private final BytesMessage message;

    public BytesMessageInputStream(BytesMessage message) {
        Assert.notNull(message, "'message' must not be null");
        this.message = message;
    }

    @Override
    public long skip(long n) throws IOException {
        throw new IOException("NOT supported skip(" + n + ")");
    }

    public int read(byte b[]) throws IOException {
        try {
            return message.readBytes(b);
        } catch (JMSException ex) {
            throw new IOException(ex);
        }
    }

    public int read(byte b[], int off, int len) throws IOException {
        if (off == 0) {
            try {
                return message.readBytes(b, len);
            } catch (JMSException ex) {
                throw new IOException(ex);
            }
        } else {
            return super.read(b, off, len);
        }
    }

    public int read() throws IOException {
        try {
            return message.readByte() & 0xff;
        } catch (MessageEOFException ex) {
            return -1;
        } catch (JMSException ex) {
            throw new IOException(ex);
        }
    }
}
