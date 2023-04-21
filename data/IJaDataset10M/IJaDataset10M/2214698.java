package com.bluewhalesystems.client.lwuit.richtext;

import com.sun.lwuit.Font;
import com.sun.lwuit.plaf.Style;

/**
 * Control sequences within the 'formatted data' character stream consist of at least two characters, followed by 
 * sequence-specific data. The first character is the sequence identifier and the second character is the sequence 
 * length (in characters).
 * 
 * In order to be recognized as such, the the identifier values must belong to what is considered the 'ASCII control 
 * code set', i.e., characters 0-31 and 127.
 * 
 * The length value includes the first two characters, i.e., its value can never be less than 2.
 * 
 * This class is used by {@link FormattedDataGenerator} and {@link LineDataGenerator} and they both need to be in
 * aggreement as to the structure of 'formatted data'.
 * 
 * TODO: Only system fonts are supported.
 */
public class FormattedDataControlSequence {

    /**
     * Flag indicating that no font/color information is to be encoded. Use when only the 'cookie' is to be encoded.
     */
    public static final int FLAG_DO_NOT_ENCODE_FONT_OR_COLORS = 0x00;

    /**
     * Flag indicating that font information is to be encoded.
     */
    public static final int FLAG_ENCODE_FONT = 0x01;

    /**
     * Flag indicating that the 'unselected' colors are to be encoded.
     */
    public static final int FLAG_ENCODE_COLORS = 0x02;

    /**
     * Flag indicating that the 'selected' colors are to be encoded.
     */
    public static final int FLAG_ENCODE_SELECTED_COLORS = 0x04;

    /**
     * Encodes the provided font, color and cookie data.
     * 
     * @param aStyle 'unselected' style which may provide font and color data
     * @param aSelectedStyle 'selected' style which may provided color data
     * @param aFont font to be encoded (used in preference to the font that may be provided by the 'unselected' style)
     * @param aFontAndColorsFlags flags describing what is to be encoded
     * @param aCookie optional cookie to be encoded in the sequence
     */
    public void initialize(Style aStyle, Style aSelectedStyle, Font aFont, int aFontAndColorsFlags, char[] aCookie) {
        int flags = FLAG_INCLUDES_NOTHING;
        int face = 0, style = 0, size = 0, fgColor = 0, bgColor = 0, selectedFgColor = 0, selectedBgColor = 0;
        if (FLAG_DO_NOT_ENCODE_FONT_OR_COLORS != aFontAndColorsFlags) {
            if (null != aStyle) {
                if (FLAG_ENCODE_FONT == (aFontAndColorsFlags & FLAG_ENCODE_FONT)) {
                    Font font = null != aFont ? aFont : aStyle.getFont();
                    face = font.getFace();
                    style = font.getStyle();
                    size = font.getSize();
                    flags |= FLAG_INCLUDES_FONT;
                }
                if (FLAG_ENCODE_COLORS == (aFontAndColorsFlags & FLAG_ENCODE_COLORS)) {
                    fgColor = aStyle.getFgColor();
                    bgColor = aStyle.getBgColor();
                    flags |= FLAG_INCLUDES_COLORS;
                }
            } else if (null != aFont && FLAG_ENCODE_FONT == (aFontAndColorsFlags & FLAG_ENCODE_FONT)) {
                face = aFont.getFace();
                style = aFont.getStyle();
                size = aFont.getSize();
                flags |= FLAG_INCLUDES_FONT;
            }
            if (null != aSelectedStyle && FLAG_ENCODE_SELECTED_COLORS == (aFontAndColorsFlags & FLAG_ENCODE_SELECTED_COLORS)) {
                selectedFgColor = aSelectedStyle.getFgColor();
                selectedBgColor = aSelectedStyle.getBgColor();
                flags |= FLAG_INCLUDES_SELECTED_COLORS;
            }
        }
        if (null != aCookie) {
            flags |= FLAG_INCLUDES_COOKIE;
        }
        writeSequence(face, style, size, fgColor, bgColor, selectedFgColor, selectedBgColor, aCookie, flags, iData);
    }

    /**
     * Initializes the sequence with formatted data so that it may be decoded.
     * 
     * @param aFormattedData formatted data
     * @param aOffset offset at which to start reading the data (the length is obtained during reading)
     */
    public void initialize(char[] aFormattedData, int aOffset) {
        iData.clear();
        iData.append(aFormattedData, aOffset, aFormattedData[aOffset + 1]);
    }

    /**
     * Decodes the sequence font and color data. Returns the optional 'cookie'.
     * 
     * @param aStyle will hold the 'unselected' style information; may not be null
     * @param aSelectedStyle will hold the 'selected' style information; may not be null
     * @return optional cookie if set; null otherwise
     */
    public char[] decode(Style aStyle, Style aSelectedStyle) {
        return readSequence(null, aStyle, aSelectedStyle, iData.getArray(), 0);
    }

    /**
     * Appends this sequences data to the provided char array.
     * 
     * @param aCharArray char array to append to
     * @return number of characters appended
     */
    public int appendTo(GrowableCharArray aCharArray) {
        aCharArray.append(iData);
        return iData.length();
    }

    /**
     * Returns the encoded length of the sequence.
     * 
     * @return length of the sequence in characters
     */
    public int getLength() {
        return iData.length();
    }

    public FormattedDataControlSequence() {
        iData = new GrowableCharArray(INITIAL_CAPACITY, CAPACITY_INCREMENT);
    }

    /**
     * Writes the sequence attributes to the data buffer depending on provided flags.
     * 
     * Note: This function is abstracted out to make testing using JUnit easier.
     * 
     * @param aFace font face as defined by javax.microedition.lcdui.Font
     * @param aStyle font style as defined by javax.microedition.lcdui.Font
     * @param aSize font size as defined by javax.microedition.lcdui.Font
     * @param aFgColor 'unselected' style's fg color
     * @param aBgColor 'unselected' style's bg color
     * @param aSelectedFgColor 'selected' style's fg color
     * @param aSelectedBgColor 'selected' style's bg color
     * @param aCookie optional cookie
     * @param aFlags describes which attributes are to be included
     * @param aData data buffer to write to
     * @return number of characters written to the buffer
     */
    protected static int writeSequence(int aFace, int aStyle, int aSize, int aFgColor, int aBgColor, int aSelectedFgColor, int aSelectedBgColor, char[] aCookie, int aFlags, GrowableCharArray aData) {
        aData.clear();
        aData.append((char) BASE_SEQUENCE_ID);
        aData.append('L');
        if (FLAG_INCLUDES_NOTHING != aFlags) {
            aData.append((char) aFlags);
        }
        if (FLAG_INCLUDES_FONT == (aFlags & FLAG_INCLUDES_FONT)) {
            writeFont(aFace, aStyle, aSize, aData);
        }
        if (FLAG_INCLUDES_COLORS == (aFlags & FLAG_INCLUDES_COLORS)) {
            writeInt(aFgColor, aData);
            writeInt(aBgColor, aData);
        }
        if (FLAG_INCLUDES_SELECTED_COLORS == (aFlags & FLAG_INCLUDES_SELECTED_COLORS)) {
            writeInt(aSelectedFgColor, aData);
            writeInt(aSelectedBgColor, aData);
        }
        if (FLAG_INCLUDES_COOKIE == (aFlags & FLAG_INCLUDES_COOKIE)) {
            aData.append((char) aCookie.length);
            aData.append(aCookie);
        }
        aData.getArray()[1] = (char) aData.length();
        return aData.length();
    }

    /**
     * Writes (system) {@link Font} attributes out to the data buffer.
     * 
     * @param aFace font face as defined by javax.microedition.lcdui.Font
     * @param aStyle font style as defined by javax.microedition.lcdui.Font
     * @param aSize font size as defined by javax.microedition.lcdui.Font
     * @param aData data buffer to write to
     * @return number of characters written to the buffer
     */
    protected static int writeFont(int aFace, int aStyle, int aSize, GrowableCharArray aData) {
        char value = 0x0000;
        int bit = 0x00;
        switch(aFace) {
            case javax.microedition.lcdui.Font.FACE_MONOSPACE:
                value |= FONT_FACE_MONOSPACE << bit;
                break;
            case javax.microedition.lcdui.Font.FACE_PROPORTIONAL:
                value |= FONT_FACE_PROPORTIONAL << bit;
                break;
            case javax.microedition.lcdui.Font.FACE_SYSTEM:
            default:
                value |= FONT_FACE_SYSTEM << bit;
                break;
        }
        bit = 0x02;
        switch(aSize) {
            case javax.microedition.lcdui.Font.SIZE_SMALL:
                value |= (FONT_SIZE_SMALL << bit);
                break;
            case javax.microedition.lcdui.Font.SIZE_LARGE:
                value |= (FONT_SIZE_LARGE << bit);
                break;
            case javax.microedition.lcdui.Font.SIZE_MEDIUM:
            default:
                value |= (FONT_SIZE_MEDIUM << bit);
                break;
        }
        bit = 0x04;
        if (javax.microedition.lcdui.Font.STYLE_BOLD == ((aStyle) & javax.microedition.lcdui.Font.STYLE_BOLD)) {
            value |= (FONT_STYLE_BOLD << bit);
        }
        if (javax.microedition.lcdui.Font.STYLE_ITALIC == ((aStyle) & javax.microedition.lcdui.Font.STYLE_ITALIC)) {
            value |= (FONT_STYLE_ITALIC << bit);
        }
        if (javax.microedition.lcdui.Font.STYLE_UNDERLINED == ((aStyle) & javax.microedition.lcdui.Font.STYLE_UNDERLINED)) {
            value |= (FONT_STYLE_UNDERLINED << bit);
        }
        aData.append((char) value);
        return 1;
    }

    /**
     * Creates a {@link Font} instance from encoded data.
     * 
     * Note: This function is called from JUnit tests which is the reason for the weird interface -- when running
     * in test mode we do not want {@link Font#createSystemFont(int, int, int)} to execute...
     * 
     * @param aValues optional holder for font style information; may be null
     * @param aData holds the font style information
     * @param aOffset offset at which to read from the data
     * @return a {@link Font} instance if aValue is null; null otherwise
     */
    protected static Font readFont(int[] aValues, char[] aData, int aOffset) {
        int face = javax.microedition.lcdui.Font.FACE_SYSTEM;
        int style = javax.microedition.lcdui.Font.STYLE_PLAIN;
        int size = javax.microedition.lcdui.Font.SIZE_MEDIUM;
        int value = aData[aOffset];
        int bit = 0x00;
        if (FONT_FACE_MONOSPACE == (value >>> bit & FONT_FACE_MONOSPACE)) {
            face = javax.microedition.lcdui.Font.FACE_MONOSPACE;
        } else if (FONT_FACE_PROPORTIONAL == (value >>> bit & FONT_FACE_PROPORTIONAL)) {
            face = javax.microedition.lcdui.Font.FACE_PROPORTIONAL;
        }
        bit = 0x02;
        if (FONT_SIZE_SMALL == (value >>> bit & FONT_SIZE_SMALL)) {
            size = javax.microedition.lcdui.Font.SIZE_SMALL;
        } else if (FONT_SIZE_LARGE == (value >>> bit & FONT_SIZE_LARGE)) {
            size = javax.microedition.lcdui.Font.SIZE_LARGE;
        }
        bit = 0x04;
        if (FONT_STYLE_BOLD == (value >>> bit & FONT_STYLE_BOLD)) {
            style |= javax.microedition.lcdui.Font.STYLE_BOLD;
        }
        if (FONT_STYLE_ITALIC == (value >>> bit & FONT_STYLE_ITALIC)) {
            style |= javax.microedition.lcdui.Font.STYLE_ITALIC;
        }
        if (FONT_STYLE_UNDERLINED == (value >>> bit & FONT_STYLE_UNDERLINED)) {
            style |= javax.microedition.lcdui.Font.STYLE_UNDERLINED;
        }
        if (null != aValues) {
            aValues[0] = face;
            aValues[1] = style;
            aValues[2] = size;
            return null;
        }
        return Font.createSystemFont(face, style, size);
    }

    /**
     * Writes an integer value out to the data buffer.
     * 
     * Note: The value is not compressed (down to a single character or using some scheme to a specific number of 
     * bytes -- 1, 2 or 3) because the values supported will likely not fall into a specific range.
     * 
     * @param aValue integer value to encode
     * @param aData data buffer to write to
     * @return number of characters written out to the buffer
     */
    protected static int writeInt(int aValue, GrowableCharArray aData) {
        aData.append((char) ((aValue & 0xFFFF0000) >>> 0x10));
        aData.append((char) (aValue & 0x0000FFFF));
        return 2;
    }

    /**
     * Reads an integer value from the data buffer.
     * 
     * @param aData data buffer to read from
     * @param aOffset offset to read from
     * @return integer value read
     */
    protected static int readInt(char[] aData, int aOffset) {
        return aData[aOffset + 1] | (aData[aOffset] << 0x10);
    }

    /**
     * Decodes a sequence from the buffer.
     * 
     * Note: Just as is the case with readFont(...), this method is defined the way it is because it is also used by 
     * JUnit tests. If you provide the values int[], do not provide Style instances, and vice-versa.
     * 
     * @param aValues holder for font/color information; may be null
     * @param aStyle 'unselected' style to update with font/color information; may be null
     * @param aSelectedStyle 'selected' style to update with color information; may be null
     * @param aData data buffer to read from
     * @param aOffset offset to read from
     * @return cookie data; may be null
     */
    static char[] readSequence(int[] aValues, Style aStyle, Style aSelectedStyle, char[] aData, int aOffset) {
        int dataOffset = aOffset;
        if (aData[dataOffset++] != BASE_SEQUENCE_ID) {
            return null;
        }
        if (aData[dataOffset++] <= 2) {
            return null;
        }
        char[] cookie = null;
        int flags = aData[dataOffset++];
        int valuesOffset = 0;
        if (FLAG_INCLUDES_FONT == (flags & FLAG_INCLUDES_FONT)) {
            if (null != aValues) {
                readFont(aValues, aData, dataOffset);
                valuesOffset += 3;
                dataOffset++;
            } else {
                aStyle.setFont(readFont(null, aData, dataOffset));
                dataOffset++;
            }
        }
        if (FLAG_INCLUDES_COLORS == (flags & FLAG_INCLUDES_COLORS)) {
            if (null != aValues) {
                aValues[valuesOffset++] = readInt(aData, dataOffset);
                dataOffset += 2;
                aValues[valuesOffset++] = readInt(aData, dataOffset);
                dataOffset += 2;
            } else {
                aStyle.setFgColor(readInt(aData, dataOffset));
                dataOffset += 2;
                aStyle.setBgColor(readInt(aData, dataOffset));
                dataOffset += 2;
            }
        }
        if (FLAG_INCLUDES_SELECTED_COLORS == (flags & FLAG_INCLUDES_SELECTED_COLORS)) {
            if (null != aValues) {
                aValues[valuesOffset++] = readInt(aData, dataOffset);
                dataOffset += 2;
                aValues[valuesOffset++] = readInt(aData, dataOffset);
                dataOffset += 2;
            } else {
                aSelectedStyle.setFgColor(readInt(aData, dataOffset));
                dataOffset += 2;
                aSelectedStyle.setBgColor(readInt(aData, dataOffset));
                dataOffset += 2;
            }
        }
        if (FLAG_INCLUDES_COOKIE == (flags & FLAG_INCLUDES_COOKIE)) {
            int length = aData[dataOffset];
            cookie = new char[length];
            dataOffset++;
            System.arraycopy(aData, dataOffset, cookie, 0, length);
            dataOffset += length;
        }
        return cookie;
    }

    private static final int FONT_FACE_SYSTEM = 0x00;

    private static final int FONT_FACE_MONOSPACE = 0x01;

    private static final int FONT_FACE_PROPORTIONAL = 0x02;

    private static final int FONT_SIZE_MEDIUM = 0x00;

    private static final int FONT_SIZE_SMALL = 0x01;

    private static final int FONT_SIZE_LARGE = 0x02;

    private static final int FONT_STYLE_PLAIN = 0x00;

    private static final int FONT_STYLE_BOLD = 0x01;

    private static final int FONT_STYLE_ITALIC = 0x02;

    private static final int FONT_STYLE_UNDERLINED = 0x04;

    private static final int INITIAL_CAPACITY = 128;

    private static final int CAPACITY_INCREMENT = 256;

    static final int FLAG_INCLUDES_NOTHING = 0x00;

    static final int FLAG_INCLUDES_FONT = 0x01;

    static final int FLAG_INCLUDES_COLORS = 0x02;

    static final int FLAG_INCLUDES_SELECTED_COLORS = 0x04;

    static final int FLAG_INCLUDES_COOKIE = 0x08;

    /**
     * Constant used by this class to identify sequences it can parse.
     */
    protected static final char BASE_SEQUENCE_ID = 0;

    /**
     * Holds the entire data for this sequence. This is dependent on sequence id although the assumption is -- at least
     * for this version of formatted data -- that the first character contains the sequence id and the second character
     * contains the encoded sequence data length.
     */
    protected GrowableCharArray iData;
}
