package net.sf.saxon.dotnet;

import cli.System.IO.TextReader;
import java.io.IOException;

/**
 * An implementation of java.io.Reader that wraps a .NET System.IO.TextReader
 */
public class DotNetReader extends java.io.Reader {

    private TextReader reader;

    public DotNetReader(TextReader reader) {
        this.reader = reader;
    }

    /**
     * Get the underlying TextReader object
     */
    public TextReader getUnderlyingTextReader() {
        return reader;
    }

    /**
     * Close the stream.  Once a stream has been closed, further read(),
     * ready(), mark(), or reset() invocations will throw an IOException.
     * Closing a previously-closed stream, however, has no effect.
     *
     * @throws java.io.IOException If an I/O error occurs
     */
    public void close() throws IOException {
        reader.Close();
    }

    /**
     * Read characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start storing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     *         stream has been reached
     * @throws java.io.IOException If an I/O error occurs
     */
    public int read(char cbuf[], int off, int len) throws IOException {
        int n = reader.Read(cbuf, off, len);
        return (n == 0 ? -1 : n);
    }

    /**
     * Read a single character.  This method will block until a character is
     * available, an I/O error occurs, or the end of the stream is reached.
     * <p/>
     * <p> Subclasses that intend to support efficient single-character input
     * should override this method.
     *
     * @return The character read, as an integer in the range 0 to 65535
     *         (<tt>0x00-0xffff</tt>), or -1 if the end of the stream has
     *         been reached
     * @throws java.io.IOException If an I/O error occurs
     */
    public int read() throws IOException {
        return reader.Read();
    }
}
