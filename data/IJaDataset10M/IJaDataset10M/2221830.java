package org.restlet.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.restlet.data.MediaType;
import org.restlet.util.ByteUtils;

/**
 * Transient representation based on a BIO input stream.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class InputRepresentation extends StreamRepresentation {

    /** The representation's stream. */
    private InputStream inputStream;

    /**
     * Constructor.
     * 
     * @param inputStream
     *            The representation's stream.
     * @param mediaType
     *            The representation's media type.
     */
    public InputRepresentation(InputStream inputStream, MediaType mediaType) {
        this(inputStream, mediaType, UNKNOWN_SIZE);
    }

    /**
     * Constructor.
     * 
     * @param inputStream
     *            The representation's stream.
     * @param mediaType
     *            The representation's media type.
     * @param expectedSize
     *            The expected input stream size.
     */
    public InputRepresentation(InputStream inputStream, MediaType mediaType, long expectedSize) {
        super(mediaType);
        setSize(expectedSize);
        this.inputStream = inputStream;
        setAvailable(inputStream != null);
        setTransient(true);
    }

    /**
     * Returns a stream with the representation's content.
     * 
     * @return A stream with the representation's content.
     */
    @Override
    public synchronized InputStream getStream() throws IOException {
        InputStream result = this.inputStream;
        this.inputStream = null;
        setAvailable(false);
        return result;
    }

    /**
     * Converts the representation to a string value. Be careful when using this
     * method as the conversion of large content to a string fully stored in
     * memory can result in OutOfMemoryErrors being thrown.
     * 
     * @return The representation as a string value.
     */
    @Override
    public String getText() throws IOException {
        return ByteUtils.toString(getStream(), this.getCharacterSet());
    }

    /**
     * Writes the representation to a byte stream.
     * 
     * @param outputStream
     *            The output stream.
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {
        ByteUtils.write(getStream(), outputStream);
    }
}
