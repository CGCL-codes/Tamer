package org.minions.stigma.network;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.minions.stigma.globals.Constants;
import org.minions.stigma.globals.Position;
import org.minions.stigma.globals.SizeOf;
import org.minions.utils.logger.Log;

/**
 * Represents message frame as a vector of bytes. Every
 * frame starts with 2 bytes of type, and then contains
 * coded contents on bytes. Coding and decoding is realized
 * using this class (wrapper of {@link ByteBuffer}).
 */
public class Buffer {

    static final int HEADER_SIZE = SizeOf.SHORT;

    private ByteBuffer buffer;

    /**
     * Creates buffer, reserves given amount of bytes
     * (buffer can be easily lengthened, and cropped to real
     * byte usage) and assigns it a message type. Used for
     * coding.
     * @param size
     *            planned size of buffer (if it's to small,
     *            buffer will enlarge itself, if it's to
     *            big, buffer will crop itself when
     *            extracting bytes. Giving good amount of
     *            bytes optimizes coding process.)
     */
    public Buffer(int size) {
        buffer = ByteBuffer.allocateDirect(size + HEADER_SIZE);
        buffer.putShort((short) 0);
    }

    /**
     * Creates frame from read bytes buffer. Used for
     * decoding.
     * @param buffer
     *            buffer to read (decode) from
     */
    public Buffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Encodes 4 byte integer number. Enlarges buffer if
     * needed.
     * @param i
     *            coded number
     */
    public void encode(int i) {
        buffer.putInt(i);
    }

    /**
     * Encodes 2 byte integer number. Enlarges buffer if
     * needed.
     * @param s
     *            coded number
     */
    public void encode(short s) {
        buffer.putShort(s);
    }

    /**
     * Encodes single byte.Enlarges buffer if needed.
     * @param b
     *            coded byte
     */
    public void encode(byte b) {
        buffer.put(b);
    }

    /**
     * Encodes string. Uses coding from {@link Constants}.
     * String should be shorter than 2*
     * {@value java.lang.Short#MAX_VALUE}. Enlarges buffer
     * if needed.
     * @param s
     *            coded string
     */
    public void encode(String s) {
        try {
            byte[] txt = s.getBytes(Constants.Coding);
            encode(txt);
        } catch (UnsupportedEncodingException e) {
            Log.logger.error("Buffer.encode", e);
        }
    }

    /**
     * Encrypts and encodes String.
     * @see Cipher
     * @see #encode(String)
     * @param s
     *            string for encryption and coding
     */
    public void encryptAndEncodeString(String s) {
        try {
            byte[] txt = s.getBytes(Constants.Coding);
            txt = Cipher.encrypt(txt);
            encode(txt);
        } catch (UnsupportedEncodingException e) {
            Log.logger.error("Buffer.encryptAndEncodeString", e);
        }
    }

    /**
     * Encodes bytes vector. Vectors length should be
     * shorter than 2 {@value java.lang.Short#MAX_VALUE}.
     * Enlarges buffer if needed.
     * @param b
     *            vector to encode
     */
    public void encode(byte[] b) {
        encode((short) b.length);
        buffer.put(b);
    }

    /**
     * Encode (@link Position Position} class. This class is
     * often used in messages so it has its own
     * encode/decode function.
     * @param p
     *            Position to encode
     */
    public void encode(Position p) {
        encode(p.getX());
        encode(p.getY());
    }

    /**
     * Reads 4 byte integer number from buffer.
     * @return decoded number
     */
    public int decodeInt() {
        return buffer.getInt();
    }

    /**
     * Decodes 2 byte integer number from buffer.
     * @return decoded number
     */
    public short decodeShort() {
        return buffer.getShort();
    }

    /**
     * Reads one byte from buffer.
     * @return decoded byte
     */
    public byte decodeByte() {
        return buffer.get();
    }

    /**
     * Reads string from buffer.
     * @see #encode(String)
     * @return decoded string
     */
    public String decodeString() {
        try {
            byte[] txt = decodeBytes();
            String s = new String(txt, Constants.Coding);
            return s;
        } catch (UnsupportedEncodingException e) {
            Log.logger.error("Buffer.decodeString", e);
            return "";
        }
    }

    /**
     * Reads and decrypt string from buffer.
     * @return decrypted string
     * @see #encryptAndEncodeString(String)
     */
    public String decryptAndDecodeString() {
        try {
            byte[] txt = decodeBytes();
            txt = Cipher.decrypt(txt);
            String s = new String(txt, Constants.Coding);
            return s;
        } catch (UnsupportedEncodingException e) {
            Log.logger.error("Buffer.decryptAndDecodeString", e);
            return "";
        }
    }

    /**
     * Reads random length bytes vector. Length is assumed
     * to be stored as short number at the beginning of
     * vector.
     * @see #encode(byte[])
     * @return decoded bytes vector
     */
    public byte[] decodeBytes() {
        short len = decodeShort();
        byte[] b = new byte[len];
        buffer.get(b);
        return b;
    }

    /**
     * Decodes {@link Position Position} class. his class is
     * often used in messages so it has its own
     * encode/decode function.
     * @return decoded position
     */
    public Position decodePosition() {
        return new Position(decodeShort(), decodeShort());
    }

    /**
     * Gets frame (as {@link ByteBuffer}) ready to be send
     * via network. Crops unused bytes, encodes frame length
     * etc.
     * @return buffer containing frame
     * @see ByteBuffer
     */
    public ByteBuffer getByteBuffer() {
        short size = (short) (buffer.position() - HEADER_SIZE);
        buffer.putShort(0, size);
        buffer.flip();
        if (Log.isTraceEnabled()) {
            Log.logger.trace("Sending msg length: " + size);
            byte[] tmp = new byte[size + HEADER_SIZE];
            buffer.get(tmp);
            Log.logger.trace("Message out: " + Arrays.toString(tmp));
            buffer.flip();
        }
        return buffer;
    }

    /**
     * Some heuristic. Return maximum (pessimistic) amount
     * of bytes that can be used to store given string in
     * current coding. It's used just to optimize buffer
     * length on creation time.
     * @param s
     *            string to calculate bytes count
     * @return maximum bytes count needed to store given
     *         string
     */
    public static int stringBytesCount(String s) {
        return s.length() * 2 + 2;
    }
}
