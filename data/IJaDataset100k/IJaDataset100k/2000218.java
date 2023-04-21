package cryptix.sasl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.security.sasl.SaslException;
import java.math.BigInteger;
import cryptix.sasl.SaslUtil;
import org.apache.log4j.Category;

/**
 * The cryptix implementation of an outgoing SASL buffer.
 *
 * @version $Revision: 1.4 $
 * @since draft-burdis-cat-sasl-srp-04
 */
public class OutputBuffer implements SaslParams {

    private static Category cat = Category.getInstance(OutputBuffer.class);

    /** The internal output stream. */
    private ByteArrayOutputStream out;

    public OutputBuffer() {
        out = new ByteArrayOutputStream();
    }

    /**
    * Encodes a SASL scalar quantity, <tt>count</tt>-octet long, to the current
    * buffer.
    *
    * @param count number of octets to encode <tt>b</tt> with.
    * @param b the scalar quantity.
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    * @exception IOException if any other I/O exception occurs during the
    * operation.
    */
    public void setScalar(int count, int b) throws IOException {
        cat.debug("==> setScalar(" + String.valueOf(count) + ", " + String.valueOf(b) + ")");
        if (count < 0 || count > 4) throw new SaslEncodingException("Invalid SASL scalar octet count: " + String.valueOf(count));
        byte[] element = new byte[count];
        for (int i = count; --i >= 0; b >>>= 8) element[i] = (byte) b;
        out.write(element);
        cat.debug("<== setScalar()");
    }

    /**
    * Encodes a SASL OS to the current buffer.
    *
    * @param b the OS element.
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    * @exception IOException if any other I/O exception occurs during the
    * operation.
    */
    public void setOS(byte[] b) throws IOException {
        cat.debug("==> setOS()");
        cat.debug("b = " + SaslUtil.dumpString(b));
        int length = b.length;
        if (length > ONE_BYTE_HEADER_LIMIT) {
            cat.error("SASL OS too long");
            throw new SaslEncodingException("SASL octet sequence too long");
        }
        out.write(length & 0xFF);
        out.write(b);
        cat.debug("<== setOS()");
    }

    /**
    * Encodes a SASL EOS to the current buffer.
    *
    * @param b the EOS element.
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    * @exception IOException if any other I/O exception occurs during the
    * operation.
    */
    public void setEOS(byte[] b) throws IOException {
        cat.debug("==> setEOS()");
        cat.debug("b = " + SaslUtil.dumpString(b));
        int length = b.length;
        if (length > FOUR_BYTE_HEADER_LIMIT) {
            cat.error("SASL EOS too long");
            throw new SaslEncodingException("SASL extended octet sequence too long");
        }
        byte[] lengthBytes = { (byte) (length >>> 24), (byte) (length >>> 16), (byte) (length >>> 8), (byte) length };
        out.write(lengthBytes);
        out.write(b);
        cat.debug("<== setEOS()");
    }

    /**
    * Encodes a SASL MPI to the current buffer.
    *
    * @param val the MPI element.
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    * @exception IOException if any other I/O exception occurs during the
    * operation.
    */
    public void setMPI(BigInteger val) throws IOException {
        cat.debug("==> setMPI()");
        byte[] b = SaslUtil.trim(val);
        cat.debug("b = " + SaslUtil.dumpString(b));
        int length = b.length;
        if (length > TWO_BYTE_HEADER_LIMIT) {
            cat.error("SASL MPI too long");
            throw new SaslEncodingException("SASL multi-precision integer too long");
        }
        byte[] lengthBytes = { (byte) (length >>> 8), (byte) length };
        out.write(lengthBytes);
        out.write(b);
        cat.debug("<== setMPI()");
    }

    /**
    * Encodes a SASL Text to the current buffer.
    *
    * @param str the Text element.
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    * @exception UnsupportedEncodingException if the UTF-8 encoding is not
    * supported on this platform.
    * @exception IOException if any other I/O exception occurs during the
    * operation.
    */
    public void setText(String str) throws IOException {
        cat.debug("==> setText()");
        byte[] b = str.getBytes("UTF8");
        cat.debug("b = " + SaslUtil.dumpString(b));
        int length = b.length;
        if (length > TWO_BYTE_HEADER_LIMIT) {
            cat.error("SASL Text too long");
            throw new SaslEncodingException("SASL text too long");
        }
        byte[] lengthBytes = { (byte) (length >>> 8), (byte) length };
        out.write(lengthBytes);
        out.write(b);
        cat.debug("<== setText()");
    }

    /**
    * Returns the encoded form of the current buffer including the 4-byte
    * length header.
    *
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    */
    public byte[] encode() throws SaslEncodingException {
        cat.debug("==> encode()");
        byte[] buffer = wrap();
        int length = buffer.length;
        byte[] result = new byte[length + 4];
        result[0] = (byte) (length >>> 24);
        result[1] = (byte) (length >>> 16);
        result[2] = (byte) (length >>> 8);
        result[3] = (byte) length;
        System.arraycopy(buffer, 0, result, 4, length);
        cat.debug("<== encode() --> " + SaslUtil.dumpString(result));
        return result;
    }

    /**
    * Returns the encoded form of the current buffer excluding the 4-byte
    * length header.
    *
    * @exception SaslEncodingException if an encoding size constraint is
    * violated.
    */
    public byte[] wrap() throws SaslEncodingException {
        cat.debug("==> wrap()");
        int length = out.size();
        if (length > BUFFER_LIMIT || length < 0) {
            cat.error("SASL Buffer too long");
            throw new SaslEncodingException("SASL buffer too long");
        }
        cat.debug("<== wrap()");
        return out.toByteArray();
    }
}
