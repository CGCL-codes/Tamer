package com.groovytagger.mp3.tags.io;

/** Text encoding representation used in v2 frames.
 *
 */
public class TextEncoding {

    private byte m_byEncoding;

    private TextEncoding(byte byEncoding) {
        m_byEncoding = byEncoding;
    }

    public static TextEncoding getTextEncoding(int iEncoding) throws Exception {
        return getTextEncoding((byte) iEncoding);
    }

    public static TextEncoding getTextEncoding(byte byEncoding) throws Exception {
        switch(byEncoding) {
            case (byte) 0x00:
                return ISO_8859_1;
            case (byte) 0x01:
                return UNICODE;
            default:
                throw new Exception("Unknown text encoding value " + byEncoding + ".");
        }
    }

    /** Get the byte value corresponding to this text encoding.
   *
   * @return the corresponding byte value
   */
    public byte getEncodingValue() {
        return m_byEncoding;
    }

    /** Get the Java encoding string matching this text encoding.
   *
   * @return the matching encoding string
   */
    public String getEncodingString() {
        switch(m_byEncoding) {
            case (byte) 0x00:
                return "ISO-8859-1";
            case (byte) 0x01:
                return "Unicode";
            default:
                return null;
        }
    }

    public boolean equals(Object oOther) {
        if ((oOther == null) || (!(oOther instanceof TextEncoding))) {
            return false;
        }
        TextEncoding oOtherTextEncoding = (TextEncoding) oOther;
        return (m_byEncoding == oOtherTextEncoding.m_byEncoding);
    }

    public static final TextEncoding ISO_8859_1 = new TextEncoding((byte) 0x00);

    public static final TextEncoding UNICODE = new TextEncoding((byte) 0x01);

    private static TextEncoding s_oDefaultTextEncoding = ISO_8859_1;

    /** Get the default text encoding which will be used in v2 frames, when not specified.
   *
   * @return the default text encoding used when not specified
   */
    public static TextEncoding getDefaultTextEncoding() {
        return s_oDefaultTextEncoding;
    }

    /** Set the default text encoding to be used in v2 frames, when not specified.
   *
   * @param oTextEncoding the default text encoding to be used when not specified
   */
    public static void setDefaultTextEncoding(TextEncoding oTextEncoding) {
        if (oTextEncoding == null) {
            throw new NullPointerException("Default text encoding cannot be null.");
        }
        s_oDefaultTextEncoding = oTextEncoding;
    }
}
