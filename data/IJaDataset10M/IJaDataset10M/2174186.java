package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods related to codecs.
 */
public class CodecUtil {

    static final int DEFAULT_ENCODING_BUFFER_SIZE = 1024;

    /**
     * Copies the contents of one stream to the other.
     * @param in not null
     * @param out not null
     * @throws IOException
     */
    public static void copy(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[DEFAULT_ENCODING_BUFFER_SIZE];
        int inputLength;
        while (-1 != (inputLength = in.read(buffer))) {
            out.write(buffer, 0, inputLength);
        }
    }

    /**
     * Encodes the given stream using Quoted-Printable.
     * This assumes that stream is binary and therefore escapes
     * all line endings.
     * @param in not null
     * @param out not null
     * @throws IOException
     */
    public static void encodeQuotedPrintableBinary(final InputStream in, final OutputStream out) throws IOException {
        QuotedPrintableEncoder encoder = new QuotedPrintableEncoder(DEFAULT_ENCODING_BUFFER_SIZE, true);
        encoder.encode(in, out);
    }

    /**
     * Encodes the given stream using Quoted-Printable.
     * This assumes that stream is text and therefore does not escape
     * all line endings.
     * @param in not null
     * @param out not null
     * @throws IOException
     */
    public static void encodeQuotedPrintable(final InputStream in, final OutputStream out) throws IOException {
        final QuotedPrintableEncoder encoder = new QuotedPrintableEncoder(DEFAULT_ENCODING_BUFFER_SIZE, false);
        encoder.encode(in, out);
    }

    /**
     * Encodes the given stream using base64.
     *
     * @param in not null
     * @param out not null
     * @throws IOException if an I/O error occurs
     */
    public static void encodeBase64(final InputStream in, final OutputStream out) throws IOException {
        Base64OutputStream b64Out = new Base64OutputStream(out);
        copy(in, b64Out);
        b64Out.close();
    }

    /**
     * Wraps the given stream in a Quoted-Printable encoder.
     * @param out not null
     * @return encoding outputstream 
     * @throws IOException
     */
    public static OutputStream wrapQuotedPrintable(final OutputStream out, boolean binary) throws IOException {
        return new QuotedPrintableOutputStream(out, binary);
    }

    /**
     * Wraps the given stream in a Base64 encoder.
     * @param out not null
     * @return encoding outputstream 
     * @throws IOException
     */
    public static OutputStream wrapBase64(final OutputStream out) throws IOException {
        return new Base64OutputStream(out);
    }
}
