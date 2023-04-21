package edu.uga.galileo.slash.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {

    private byte[] data;

    private String type;

    /**
	 * Creates a new <code>ByteArrayDataSource</code> for retrieving byte
	 * information from an <code>InputStream</code>.
	 * 
	 * @param is
	 *            The <code>InputStream</code> to use.
	 * @param type
	 *            The type of message to send ("text/html," e.g.)
	 */
    public ByteArrayDataSource(InputStream is, String type) {
        this.type = type;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) os.write(ch);
            data = os.toByteArray();
        } catch (IOException ioex) {
        }
    }

    /**
	 * Public constructor for use with a byte array of data.
	 * 
	 * @param data
	 *            The data.
	 * @param type
	 *            The type of message to send ("text/html," e.g.)
	 */
    public ByteArrayDataSource(byte[] data, String type) {
        this.data = data;
        this.type = type;
    }

    /**
	 * Public constructor for use with <code>String</code> data.
	 * 
	 * @param data
	 *            The data.
	 * @param type
	 *            The type of message to send ("text/html," e.g.)
	 */
    public ByteArrayDataSource(String data, String type) {
        try {
            this.data = data.getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException uex) {
        }
        this.type = type;
    }

    /**
	 * Return an InputStream for the data. Note - a new stream must be returned
	 * each time.
	 * 
	 * @return an <code>InputStream</code> based on the data.
	 */
    public InputStream getInputStream() throws IOException {
        if (data == null) throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }

    /**
	 * This shouldn't be called with this class, and will throw an exception if
	 * an attempt is made to do so.
	 */
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot do this");
    }

    /**
	 * Get the content type.
	 */
    public String getContentType() {
        return type;
    }

    /**
	 * Get the name.
	 */
    public String getName() {
        return "dummy";
    }
}
