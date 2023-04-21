package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import java.io.UnsupportedEncodingException;

/**
 * This Constant represents a UTF-8 constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class Utf8Constant extends Constant {

    private static final char TWO_BYTE_LIMIT = 0x80;

    private static final int TWO_BYTE_CONSTANT1 = 0xc0;

    private static final int TWO_BYTE_CONSTANT2 = 0x80;

    private static final int TWO_BYTE_SHIFT1 = 6;

    private static final int TWO_BYTE_MASK1 = 0x1f;

    private static final int TWO_BYTE_MASK2 = 0x3f;

    private static final char THREE_BYTE_LIMIT = 0x800;

    private static final int THREE_BYTE_CONSTANT1 = 0xe0;

    private static final int THREE_BYTE_CONSTANT2 = 0x80;

    private static final int THREE_BYTE_CONSTANT3 = 0x80;

    private static final int THREE_BYTE_SHIFT1 = 12;

    private static final int THREE_BYTE_SHIFT2 = 6;

    private static final int THREE_BYTE_MASK1 = 0x0f;

    private static final int THREE_BYTE_MASK2 = 0x3f;

    private static final int THREE_BYTE_MASK3 = 0x3f;

    private byte[] bytes;

    private String string;

    /**
     * Creates an uninitialized Utf8Constant.
     *
     */
    public Utf8Constant() {
    }

    /**
     * Creates a Utf8Constant containing the given string.
     */
    public Utf8Constant(String string) {
        this.bytes = null;
        this.string = string;
    }

    /**
     * Initializes the UTF-8 data with an array of bytes.
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.string = null;
    }

    /**
     * Returns the UTF-8 data as an array of bytes.
     */
    public byte[] getBytes() {
        try {
            switchToByteArrayRepresentation();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return bytes;
    }

    /**
     * Initializes the UTF-8 data with a String.
     */
    public void setString(String utf8String) {
        this.bytes = null;
        this.string = utf8String;
    }

    /**
     * Returns the UTF-8 data as a String.
     */
    public String getString() {
        try {
            switchToStringRepresentation();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return string;
    }

    public int getTag() {
        return ClassConstants.CONSTANT_Utf8;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor) {
        constantVisitor.visitUtf8Constant(clazz, this);
    }

    /**
     * Switches to a byte array representation of the UTF-8 data.
     */
    private void switchToByteArrayRepresentation() throws UnsupportedEncodingException {
        if (bytes == null) {
            bytes = getByteArrayRepresentation(string);
            string = null;
        }
    }

    /**
     * Switches to a String representation of the UTF-8 data.
     */
    private void switchToStringRepresentation() throws UnsupportedEncodingException {
        if (string == null) {
            string = getStringRepresentation(bytes);
            bytes = null;
        }
    }

    /**
     * Returns the modified UTF-8 byte array representation of the given string.
     */
    private byte[] getByteArrayRepresentation(String string) throws UnsupportedEncodingException {
        int byteLength = 0;
        int stringLength = string.length();
        for (int stringIndex = 0; stringIndex < stringLength; stringIndex++) {
            char c = string.charAt(stringIndex);
            byteLength += c == 0 ? 2 : c < TWO_BYTE_LIMIT ? 1 : c < THREE_BYTE_LIMIT ? 2 : 3;
        }
        byte[] bytes = new byte[byteLength];
        int byteIndex = 0;
        for (int stringIndex = 0; stringIndex < stringLength; stringIndex++) {
            char c = string.charAt(stringIndex);
            if (c == 0) {
                bytes[byteIndex++] = (byte) TWO_BYTE_CONSTANT1;
                bytes[byteIndex++] = (byte) TWO_BYTE_CONSTANT2;
            } else if (c < TWO_BYTE_LIMIT) {
                bytes[byteIndex++] = (byte) c;
            } else if (c < THREE_BYTE_LIMIT) {
                bytes[byteIndex++] = (byte) (TWO_BYTE_CONSTANT1 | ((c >>> TWO_BYTE_SHIFT1) & TWO_BYTE_MASK1));
                bytes[byteIndex++] = (byte) (TWO_BYTE_CONSTANT2 | (c & TWO_BYTE_MASK2));
            } else {
                bytes[byteIndex++] = (byte) (THREE_BYTE_CONSTANT1 | ((c >>> THREE_BYTE_SHIFT1) & THREE_BYTE_MASK1));
                bytes[byteIndex++] = (byte) (THREE_BYTE_CONSTANT2 | ((c >>> THREE_BYTE_SHIFT2) & THREE_BYTE_MASK2));
                bytes[byteIndex++] = (byte) (THREE_BYTE_CONSTANT3 | (c & THREE_BYTE_MASK3));
            }
        }
        return bytes;
    }

    /**
     * Returns the String representation of the given modified UTF-8 byte array.
     */
    private String getStringRepresentation(byte[] bytes) throws UnsupportedEncodingException {
        char[] chars = new char[bytes.length];
        int charIndex = 0;
        int byteIndex = 0;
        while (byteIndex < bytes.length) {
            int b = bytes[byteIndex++] & 0xff;
            try {
                chars[charIndex++] = (char) (b < TWO_BYTE_CONSTANT1 ? b : b < THREE_BYTE_CONSTANT1 ? ((b & TWO_BYTE_MASK1) << TWO_BYTE_SHIFT1) | ((bytes[byteIndex++] & TWO_BYTE_MASK2)) : ((b & THREE_BYTE_MASK1) << THREE_BYTE_SHIFT1) | ((bytes[byteIndex++] & THREE_BYTE_MASK2) << THREE_BYTE_SHIFT2) | ((bytes[byteIndex++] & THREE_BYTE_MASK3)));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new UnsupportedEncodingException("Missing UTF-8 bytes after initial byte [0x" + Integer.toHexString(b) + "] in string [" + new String(chars, 0, charIndex) + "]");
            }
        }
        return new String(chars, 0, charIndex);
    }
}
