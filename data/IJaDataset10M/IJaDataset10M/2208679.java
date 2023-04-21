package org.project.trunks.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p> Low level API for processing file uploads.
 *
 * <p> This class can be used to process data streams conforming to MIME
 * 'multipart' format as defined in
 * <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Arbitrarily
 * large amounts of data in the stream can be processed under constant
 * memory usage.
 *
 * <p> The format of the stream is defined in the following way:<br>
 *
 * <code>
 *   multipart-body := preamble 1*encapsulation close-delimiter epilogue<br>
 *   encapsulation := delimiter body CRLF<br>
 *   delimiter := "--" boundary CRLF<br>
 *   close-delimiter := "--" boudary "--"<br>
 *   preamble := &lt;ignore&gt;<br>
 *   epilogue := &lt;ignore&gt;<br>
 *   body := header-part CRLF body-part<br>
 *   header-part := 1*header CRLF<br>
 *   header := header-name ":" header-value<br>
 *   header-name := &lt;printable ascii characters except ":"&gt;<br>
 *   header-value := &lt;any ascii characters except CR & LF&gt;<br>
 *   body-data := &lt;arbitrary data&gt;<br>
 * </code>
 *
 * <p>Note that body-data can contain another mulipart entity.  There
 * is limited support for single pass processing of such nested
 * streams.  The nested stream is <strong>required</strong> to have a
 * boundary token of the same length as the parent stream (see {@link
 * #setBoundary(byte[])}).
 *
 * <p>Here is an exaple of usage of this class.<br>
 *
 * <pre>
 *    try {
 *        MultipartStream multipartStream = new MultipartStream(input,
 *                                                              boundary);
 *        boolean nextPart = malitPartStream.skipPreamble();
 *        OutputStream output;
 *        while(nextPart) {
 *            header = chunks.readHeader();
 *            // process headers
 *            // create some output stream
 *            multipartStream.readBodyPart(output);
 *            nextPart = multipartStream.readBoundary();
 *        }
 *    } catch(MultipartStream.MalformedStreamException e) {
 *          // the stream failed to follow required syntax
 *    } catch(IOException) {
 *          // a read or write error occurred
 *    }
 *
 * </pre>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @author Sean C. Sullivan
 *
 * @version $Id: MultipartStream.java,v 1.12 2003/06/01 00:18:13 martinc Exp $
 */
public class MultipartStream {

    /**
     * The maximum length of <code>header-part</code> that will be
     * processed (10 kilobytes = 10240 bytes.).
     */
    public static final int HEADER_PART_SIZE_MAX = 10240;

    /**
     * The default length of the buffer used for processing a request.
     */
    protected static final int DEFAULT_BUFSIZE = 4096;

    /**
     * A byte sequence that marks the end of <code>header-part</code>
     * (<code>CRLFCRLF</code>).
     */
    protected static final byte[] HEADER_SEPARATOR = { 0x0D, 0x0A, 0x0D, 0x0A };

    /**
     * A byte sequence that that follows a delimiter that will be
     * followed by an encapsulation (<code>CRLF</code>).
     */
    protected static final byte[] FIELD_SEPARATOR = { 0x0D, 0x0A };

    /**
     * A byte sequence that that follows a delimiter of the last
     * encapsulation in the stream (<code>--</code>).
     */
    protected static final byte[] STREAM_TERMINATOR = { 0x2D, 0x2D };

    /**
     * The input stream from which data is read.
     */
    private InputStream input;

    /**
     * The length of the boundary token plus the leading <code>CRLF--</code>.
     */
    private int boundaryLength;

    /**
     * The amount of data, in bytes, that must be kept in the buffer in order
     * to detect delimiters reliably.
     */
    private int keepRegion;

    /**
     * The byte sequence that partitions the stream.
     */
    private byte[] boundary;

    /**
     * The length of the buffer used for processing the request.
     */
    private int bufSize;

    /**
     * The buffer used for processing the request.
     */
    private byte[] buffer;

    /**
     * The index of first valid character in the buffer.
     * <br>
     * 0 <= head < bufSize
     */
    private int head;

    /**
     * The index of last valid characer in the buffer + 1.
     * <br>
     * 0 <= tail <= bufSize
     */
    private int tail;

    /**
     * The content encoding to use when reading headers.
     */
    private String headerEncoding;

    /**
     * Default constructor.
     *
     * @see #MultipartStream(InputStream, byte[], int)
     * @see #MultipartStream(InputStream, byte[])
     *
     */
    public MultipartStream() {
    }

    /**
     * <p> Constructs a <code>MultipartStream</code> with a custom size buffer.
     *
     * <p> Note that the buffer must be at least big enough to contain the
     * boundary string, plus 4 characters for CR/LF and double dash, plus at
     * least one byte of data.  Too small a buffer size setting will degrade
     * performance.
     *
     * @param input    The <code>InputStream</code> to serve as a data source.
     * @param boundary The token used for dividing the stream into
     *                 <code>encapsulations</code>.
     * @param bufSize  The size of the buffer to be used, in bytes.
     *
     *
     * @see #MultipartStream()
     * @see #MultipartStream(InputStream, byte[])
     *
     */
    public MultipartStream(InputStream input, byte[] boundary, int bufSize) {
        this.input = input;
        this.bufSize = bufSize;
        this.buffer = new byte[bufSize];
        this.boundary = new byte[boundary.length + 4];
        this.boundaryLength = boundary.length + 4;
        this.keepRegion = boundary.length + 3;
        this.boundary[0] = 0x0D;
        this.boundary[1] = 0x0A;
        this.boundary[2] = 0x2D;
        this.boundary[3] = 0x2D;
        System.arraycopy(boundary, 0, this.boundary, 4, boundary.length);
        head = 0;
        tail = 0;
    }

    /**
     * <p> Constructs a <code>MultipartStream</code> with a default size buffer.
     *
     * @param input    The <code>InputStream</code> to serve as a data source.
     * @param boundary The token used for dividing the stream into
     *                 <code>encapsulations</code>.
     *
     * @exception IOException when an error occurs.
     *
     * @see #MultipartStream()
     * @see #MultipartStream(InputStream, byte[], int)
     *
     */
    public MultipartStream(InputStream input, byte[] boundary) throws IOException {
        this(input, boundary, DEFAULT_BUFSIZE);
    }

    /**
     * Retrieves the character encoding used when reading the headers of an
     * individual part. When not specified, or <code>null</code>, the platform
     * default encoding is used.

     *
     * @return The encoding used to read part headers.
     */
    public String getHeaderEncoding() {
        return headerEncoding;
    }

    /**
     * Specifies the character encoding to be used when reading the headers of
     * individual parts. When not specified, or <code>null</code>, the platform
     * default encoding is used.
     *
     * @param encoding The encoding used to read part headers.
     */
    public void setHeaderEncoding(String encoding) {
        headerEncoding = encoding;
    }

    /**
     * Reads a byte from the <code>buffer</code>, and refills it as
     * necessary.
     *
     * @return The next byte from the input stream.
     *
     * @exception IOException if there is no more data available.
     */
    public byte readByte() throws IOException {
        if (head == tail) {
            head = 0;
            tail = input.read(buffer, head, bufSize);
            if (tail == -1) {
                throw new IOException("No more data is available");
            }
        }
        return buffer[head++];
    }

    /**
     * Skips a <code>boundary</code> token, and checks whether more
     * <code>encapsulations</code> are contained in the stream.
     *
     * @return <code>true</code> if there are more encapsulations in
     *         this stream; <code>false</code> otherwise.
     *
     * @exception MalformedStreamException if the stream ends unexpecetedly or
     *                                     fails to follow required syntax.
     */
    public boolean readBoundary() throws MalformedStreamException {
        byte[] marker = new byte[2];
        boolean nextChunk = false;
        head += boundaryLength;
        try {
            marker[0] = readByte();
            marker[1] = readByte();
            if (arrayequals(marker, STREAM_TERMINATOR, 2)) {
                nextChunk = false;
            } else if (arrayequals(marker, FIELD_SEPARATOR, 2)) {
                nextChunk = true;
            } else {
                throw new MalformedStreamException("Unexpected characters follow a boundary");
            }
        } catch (IOException e) {
            throw new MalformedStreamException("Stream ended unexpectedly");
        }
        return nextChunk;
    }

    /**
     * <p>Changes the boundary token used for partitioning the stream.
     *
     * <p>This method allows single pass processing of nested multipart
     * streams.
     *
     * <p>The boundary token of the nested stream is <code>required</code>
     * to be of the same length as the boundary token in parent stream.
     *
     * <p>Restoring the parent stream boundary token after processing of a
     * nested stream is left to the application.
     *
     * @param boundary The boundary to be used for parsing of the nested
     *                 stream.
     *
     * @exception IllegalBoundaryException if the <code>boundary</code>
     *                                     has a different length than the one
     *                                     being currently parsed.
     */
    public void setBoundary(byte[] boundary) throws IllegalBoundaryException {
        if (boundary.length != boundaryLength - 4) {
            throw new IllegalBoundaryException("The length of a boundary token can not be changed");
        }
        System.arraycopy(boundary, 0, this.boundary, 4, boundary.length);
    }

    /**
     * <p>Reads the <code>header-part</code> of the current
     * <code>encapsulation</code>.
     *
     * <p>Headers are returned verbatim to the input stream, including the
     * trailing <code>CRLF</code> marker. Parsing is left to the
     * application.
     *
     * <p><strong>TODO</strong> allow limiting maximum header size to
     * protect against abuse.
     *
     * @return The <code>header-part</code> of the current encapsulation.
     *
     * @exception MalformedStreamException if the stream ends unexpecetedly.
     */
    public String readHeaders() throws MalformedStreamException {
        int i = 0;
        byte b[] = new byte[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int sizeMax = HEADER_PART_SIZE_MAX;
        int size = 0;
        while (i < 4) {
            try {
                b[0] = readByte();
            } catch (IOException e) {
                throw new MalformedStreamException("Stream ended unexpectedly");
            }
            size++;
            if (b[0] == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            if (size <= sizeMax) {
                baos.write(b[0]);
            }
        }
        String headers = null;
        if (headerEncoding != null) {
            try {
                headers = baos.toString(headerEncoding);
            } catch (UnsupportedEncodingException e) {
                headers = baos.toString();
            }
        } else {
            headers = baos.toString();
        }
        return headers;
    }

    /**
     * <p>Reads <code>body-data</code> from the current
     * <code>encapsulation</code> and writes its contents into the
     * output <code>Stream</code>.
     *
     * <p>Arbitrary large amounts of data can be processed by this
     * method using a constant size buffer. (see {@link
     * #MultipartStream(InputStream,byte[],int) constructor}).
     *
     * @param output The <code>Stream</code> to write data into.
     *
     * @return the amount of data written.
     *
     * @exception MalformedStreamException if the stream ends unexpectedly.
     * @exception IOException              if an i/o error occurs.
     */
    public int readBodyData(OutputStream output) throws MalformedStreamException, IOException {
        boolean done = false;
        int pad;
        int pos;
        int bytesRead;
        int total = 0;
        while (!done) {
            pos = findSeparator();
            if (pos != -1) {
                output.write(buffer, head, pos - head);
                total += pos - head;
                head = pos;
                done = true;
            } else {
                if (tail - head > keepRegion) {
                    pad = keepRegion;
                } else {
                    pad = tail - head;
                }
                output.write(buffer, head, tail - head - pad);
                total += tail - head - pad;
                System.arraycopy(buffer, tail - pad, buffer, 0, pad);
                head = 0;
                bytesRead = input.read(buffer, pad, bufSize - pad);
                if (bytesRead != -1) {
                    tail = pad + bytesRead;
                } else {
                    output.write(buffer, 0, pad);
                    output.flush();
                    total += pad;
                    throw new MalformedStreamException("Stream ended unexpectedly");
                }
            }
        }
        output.flush();
        return total;
    }

    /**
     * <p> Reads <code>body-data</code> from the current
     * <code>encapsulation</code> and discards it.
     *
     * <p>Use this method to skip encapsulations you don't need or don't
     * understand.
     *
     * @return The amount of data discarded.
     *
     * @exception MalformedStreamException if the stream ends unexpectedly.
     * @exception IOException              if an i/o error occurs.
     */
    public int discardBodyData() throws MalformedStreamException, IOException {
        boolean done = false;
        int pad;
        int pos;
        int bytesRead;
        int total = 0;
        while (!done) {
            pos = findSeparator();
            if (pos != -1) {
                total += pos - head;
                head = pos;
                done = true;
            } else {
                if (tail - head > keepRegion) {
                    pad = keepRegion;
                } else {
                    pad = tail - head;
                }
                total += tail - head - pad;
                System.arraycopy(buffer, tail - pad, buffer, 0, pad);
                head = 0;
                bytesRead = input.read(buffer, pad, bufSize - pad);
                if (bytesRead != -1) {
                    tail = pad + bytesRead;
                } else {
                    total += pad;
                    throw new MalformedStreamException("Stream ended unexpectedly");
                }
            }
        }
        return total;
    }

    /**
     * Finds the beginning of the first <code>encapsulation</code>.
     *
     * @return <code>true</code> if an <code>encapsulation</code> was found in
     *         the stream.
     *
     * @exception IOException if an i/o error occurs.
     */
    public boolean skipPreamble() throws IOException {
        System.arraycopy(boundary, 2, boundary, 0, boundary.length - 2);
        boundaryLength = boundary.length - 2;
        try {
            discardBodyData();
            return readBoundary();
        } catch (MalformedStreamException e) {
            return false;
        } finally {
            System.arraycopy(boundary, 0, boundary, 2, boundary.length - 2);
            boundaryLength = boundary.length;
            boundary[0] = 0x0D;
            boundary[1] = 0x0A;
        }
    }

    /**
     * Compares <code>count</code> first bytes in the arrays
     * <code>a</code> and <code>b</code>.
     *
     * @param a     The first array to compare.
     * @param b     The second array to compare.
     * @param count How many bytes should be compared.
     *
     * @return <code>true</code> if <code>count</code> first bytes in arrays
     *         <code>a</code> and <code>b</code> are equal.
     */
    public static boolean arrayequals(byte[] a, byte[] b, int count) {
        for (int i = 0; i < count; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Searches for a byte of specified value in the <code>buffer</code>,
     * starting at the specified <code>position</code>.
     *
     * @param value The value to find.
     * @param pos   The starting position for searching.
     *
     * @return The position of byte found, counting from beginning of the
     *         <code>buffer</code>, or <code>-1</code> if not found.
     */
    protected int findByte(byte value, int pos) {
        for (int i = pos; i < tail; i++) {
            if (buffer[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for the <code>boundary</code> in the <code>buffer</code>
     * region delimited by <code>head</code> and <code>tail</code>.
     *
     * @return The position of the boundary found, counting from the
     *         beginning of the <code>buffer</code>, or <code>-1</code> if
     *         not found.
     */
    protected int findSeparator() {
        int first;
        int match = 0;
        int maxpos = tail - boundaryLength;
        for (first = head; (first <= maxpos) && (match != boundaryLength); first++) {
            first = findByte(boundary[0], first);
            if (first == -1 || (first > maxpos)) {
                return -1;
            }
            for (match = 1; match < boundaryLength; match++) {
                if (buffer[first + match] != boundary[match]) {
                    break;
                }
            }
        }
        if (match == boundaryLength) {
            return first - 1;
        }
        return -1;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return The string representation of this object.
     */
    public String toString() {
        StringBuffer sbTemp = new StringBuffer();
        sbTemp.append("boundary='");
        sbTemp.append(String.valueOf(boundary));
        sbTemp.append("'\nbufSize=");
        sbTemp.append(bufSize);
        return sbTemp.toString();
    }

    /**
     * Thrown to indicate that the input stream fails to follow the
     * required syntax.
     */
    public class MalformedStreamException extends IOException {

        /**
         * Constructs a <code>MalformedStreamException</code> with no
         * detail message.
         */
        public MalformedStreamException() {
            super();
        }

        /**
         * Constructs an <code>MalformedStreamException</code> with
         * the specified detail message.
         *
         * @param message The detail message.
         */
        public MalformedStreamException(String message) {
            super(message);
        }
    }

    /**
     * Thrown upon attempt of setting an invalid boundary token.
     */
    public class IllegalBoundaryException extends IOException {

        /**
         * Constructs an <code>IllegalBoundaryException</code> with no
         * detail message.
         */
        public IllegalBoundaryException() {
            super();
        }

        /**
         * Constructs an <code>IllegalBoundaryException</code> with
         * the specified detail message.
         *
         * @param message The detail message.
         */
        public IllegalBoundaryException(String message) {
            super(message);
        }
    }
}
