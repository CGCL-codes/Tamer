package org.jcvi.common.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * An <code>HttpPOSTStream</code> represents a stream of data to be sent 
 * along with an HTTP POST request.  This uses the default method declared by 
 * the CGI 1.1 spec (<code>application/x-www-form-urlencoded</code>).
 *
 * @author jsitz@jcvi.org
 */
public class HttpPOSTStream {

    /** The default character encoding for text data */
    private static final Charset ASCII = Charset.forName("ASCII");

    /** The separator string to place between variables */
    private static final byte[] VAR_SEPARATOR = "&".getBytes(HttpPOSTStream.ASCII);

    /** The separator string to place between variable names and their values */
    private static final byte[] VALUE_SEPARATOR = "=".getBytes(HttpPOSTStream.ASCII);

    /** The stream to write data to. */
    private final OutputStream out;

    /** The number of variables written to the stream. */
    private final int varCount;

    /** The name of the character encoding used. */
    private final String encodingName;

    /**
     * Creates a new <code>HttpPOSTStream</code> attached to an existing 
     * {@link URLConnection}.  This will initialize the connection and use the
     * connection's {@link OutputStream} as the destination for all data.
     * 
     * @param connection The {@link URLConnection} to attach to.
     * @param dataCharset The {@link Charset} the data was generated in.
     * @throws IOException If there is an error writing to the stream.
     */
    public HttpPOSTStream(URLConnection connection, Charset dataCharset) throws IOException {
        super();
        this.out = connection.getOutputStream();
        this.encodingName = dataCharset.name();
        this.varCount = 0;
    }

    /**
     * Writes a single variable with its optional value to the output stream.
     * The variable will be encoded as necessary.
     * 
     * @param var The name of the variable to write.
     * @param value The value of the variable or <code>null</code> if no 
     * value exists for this variable.
     * @throws IOException If there is an error writing to the stream.
     */
    public void writeVariable(String var, String value) throws IOException {
        if (this.varCount > 0) {
            this.out.write(HttpPOSTStream.VAR_SEPARATOR);
        }
        this.writeURLEncoded(var);
        if (value != null) {
            this.out.write(HttpPOSTStream.VALUE_SEPARATOR);
            this.writeURLEncoded(value);
        }
    }

    /**
     * Writes a single unvalued variable to the output stream.
     * The variable will be encoded as necessary.  This is actually a simple
     * delegation to {@link #writeVariable(String, String)} with a 
     * <code>null</code> value.
     * 
     * @param var The name of the variable to write.
     * @throws IOException If there is an error writing to the stream.
     * @see #writeVariable(String, String)
     */
    public void writeVariable(String var) throws IOException {
        this.writeVariable(var, null);
    }

    /**
     * Writes the given string as a URL encoded string.
     * 
     * @param data The data to write.
     * @throws IOException  If there is an error writing to the stream.
     */
    private void writeURLEncoded(String data) throws IOException {
        this.out.write(URLEncoder.encode(data, this.encodingName).getBytes(HttpPOSTStream.ASCII));
    }

    /**
     * Closes the output stream and signals that there is no more data to be
     * written.  When attached to a {@link URLConnection}, this may trigger 
     * additional events on the connection.
     * 
     * @throws IOException If there is an error flushing or closing the stream.
     */
    public void close() throws IOException {
        this.out.flush();
        this.out.close();
    }
}
