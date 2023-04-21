package org.melati.template.velocity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides an interface for objects that output from melati.
 */
public class MelatiBufferedVelocityWriter extends MelatiVelocityWriter {

    private OutputStream underlying;

    private ByteArrayOutputStream buffer;

    public MelatiBufferedVelocityWriter(OutputStream output, String encoding) throws IOException {
        super(new ByteArrayOutputStream(), encoding);
        buffer = (ByteArrayOutputStream) outputStream;
        underlying = output;
    }

    public MelatiBufferedVelocityWriter(HttpServletResponse response) throws IOException {
        this(response.getOutputStream(), response.getCharacterEncoding());
    }

    /**
   * Not sure if this is used now.
   * <p>
   * It was used like a <code>StringWriter</code> which is pointless.
   * If it is used to convert characters to bytes (and not back again)
   * then that might make sense.
   *
   * @todo Check if used and deprecate/delete.
   */
    public MelatiBufferedVelocityWriter(String encoding) throws IOException {
        this(new ByteArrayOutputStream(), encoding);
    }

    public void close() throws IOException {
        super.close();
        buffer.writeTo(underlying);
        buffer.close();
        underlying.flush();
        underlying.close();
    }

    public void flush() throws IOException {
        out.flush();
        buffer.writeTo(underlying);
        buffer.reset();
        underlying.flush();
    }

    public void reset() throws IOException {
        out.flush();
        buffer.reset();
    }

    /**
   * @deprecated Use {@link org.melati.util.MelatiStringWriter#toString()}.
   * @todo Fix or delete as this uses the default platform charset to decode.
   */
    public String asString() throws IOException {
        out.flush();
        return buffer.toString();
    }
}
