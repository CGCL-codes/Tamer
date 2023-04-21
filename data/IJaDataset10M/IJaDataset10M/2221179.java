package com.werno.wmflib.records.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.werno.wmflib.WMFConstants;

/**
 * This class implements the font object type
 *
 * @author Peter Werno
 */
public class Font implements RecordObject {

    /** Definitions for charset */
    public static final int CHARSET_ANSI = 0x00;

    public static final int CHARSET_DEFAULT = 0x01;

    public static final int CHARSET_SYMBOL = 0x02;

    public static final int CHARSET_MAC = 0x4D;

    public static final int CHARSET_SHIFTJIS = 0x80;

    public static final int CHARSET_HANGUL = 0x81;

    public static final int CHARSET_JOHAB = 0x82;

    public static final int CHARSET_GB2312 = 0x86;

    public static final int CHARSET_CHINESEBIG5 = 0x88;

    public static final int CHARSET_GREEK = 0xA1;

    public static final int CHARSET_TURKISH = 0xA2;

    public static final int CHARSET_VIETNAMESE = 0xA3;

    public static final int CHARSET_HEBREW = 0xB1;

    public static final int CHARSET_ARABIC = 0xB2;

    public static final int CHARSET_BALTIC = 0xBA;

    public static final int CHARSET_RUSSIAN = 0xCC;

    public static final int CHARSET_THAI = 0xDE;

    public static final int CHARSET_EASTEUROPE = 0xEE;

    public static final int CHARSET_OEM = 0xFF;

    /** Definitions for outprecision */
    public static final int OUTPRECISION_DEFAULT = 0x00;

    public static final int OUTPRECISION_STRING = 0x01;

    public static final int OUTPRECISION_STROKE = 0x03;

    public static final int OUTPRECISION_TT = 0x04;

    public static final int OUTPRECISION_DEVICE = 0x05;

    public static final int OUTPRECISION_RASTER = 0x06;

    public static final int OUTPRECISION_TT_ONLY = 0x07;

    public static final int OUTPRECISION_OUTLINE = 0x08;

    public static final int OUTPRECISION_SCREEN_OUTLINE = 0x09;

    public static final int OUTPRECISION_PS_ONLY = 0x0A;

    /** Definitions for clipprecision */
    public static final int CLIPPRECISION_DEFAULT = 0x00;

    public static final int CLIPPRECISION_CHARACTER = 0x01;

    public static final int CLIPPRECISION_STROKE = 0x02;

    public static final int CLIPPRECISION_LH_ANGLES = 0x10;

    public static final int CLIPPRECISION_TT_ALWAYS = 0x20;

    public static final int CLIPPRECISION_DFA_DISABLE = 0x40;

    public static final int CLIPPRECISION_EMBEDDED = 0x80;

    /** Definitions for quality */
    public static final int QUALITY_DEFAULT = 0x00;

    public static final int QUALITY_DRAFT = 0x01;

    public static final int QUALITY_PROOF = 0x02;

    public static final int QUALITY_NONANTIALIASED = 0x03;

    public static final int QUALITY_ANTIALIASED = 0x04;

    public static final int QUALITY_CLEARTYPE = 0x05;

    /** Definitions for family and pitch */
    public static final int FONTFAMILY_DONTCARE = 0x00;

    public static final int FONTFAMILY_ROMAN = 0x01;

    public static final int FONTFAMILY_SWISS = 0x02;

    public static final int FONTFAMILY_MODERN = 0x03;

    public static final int FONTFAMILY_SCRIPT = 0x04;

    public static final int FONTFAMILY_DECORATIVE = 0x05;

    public static final int FONTPITCH_DEFAULT = 0;

    public static final int FONTPITCH_FIXED = 1;

    public static final int FONTPITCH_VARIABLE = 2;

    /** Definitions for the weight */
    public static final short WEIGHT_NORMAL = 400;

    public static final short WEIGHT_BOLD = 700;

    /** Properties */
    short height;

    short width;

    short escapement;

    short orientation;

    short weight;

    boolean italic;

    boolean underline;

    boolean strikeOut;

    int charSet;

    int outPrecision;

    int clipPrecision;

    int quality;

    int pitchAndFamily;

    String faceName;

    /**
     * Creates new (blank) instance of Font. The properties can subsequently
     * be set via the read method or the property setters
     */
    public Font() {
        this.height = 0;
        this.width = 0;
        this.escapement = 0;
        this.orientation = 0;
        this.weight = 0;
        this.italic = false;
        this.underline = false;
        this.strikeOut = false;
        this.charSet = 0;
        this.outPrecision = 0;
        this.clipPrecision = 0;
        this.quality = 0;
        this.pitchAndFamily = 0;
        this.faceName = null;
    }

    /**
     * Creates a new instance of Font with all properties preset
     *
     * @param height (short) the height
     * @param width (short) the width
     * @param escapement (short) the escapement
     * @param orientation (short) the orientation
     * @param weight (short) the weight (bold, normal)
     * @param italic (boolean) the italic flag
     * @param underline (boolean) the underline flag
     * @param strikeOut (boolean) the strikeout flag
     * @param charSet (int) the char set
     * @param outPrecision (int) the out precision
     * @param clipPrecision (int) the clip precision
     * @param quality (int) the quality flag
     * @param pitchAndFamily (int) the pitch and family flag
     * @param faceName (String) the font name
     */
    public Font(short height, short width, short escapement, short orientation, short weight, boolean italic, boolean underline, boolean strikeOut, int charSet, int outPrecision, int clipPrecision, int quality, int pitchAndFamily, String faceName) {
        this.height = height;
        this.width = width;
        this.escapement = escapement;
        this.orientation = orientation;
        this.weight = weight;
        this.italic = italic;
        this.underline = underline;
        this.strikeOut = strikeOut;
        this.charSet = charSet;
        this.outPrecision = outPrecision;
        this.clipPrecision = clipPrecision;
        this.quality = quality;
        this.pitchAndFamily = pitchAndFamily;
        this.faceName = faceName;
    }

    /**
     * Creates a new instance of Font and reads all the properties from a
     * stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public Font(InputStream in) throws IOException {
        this.read(in);
    }

    /**
     * Setter for property height
     *
     * @param height (short) new value of property height
     */
    public void setHeight(short height) {
        this.height = height;
    }

    /**
     * Getter for property height
     *
     * @return the value of the property height (short)
     */
    public short getHeight() {
        return this.height;
    }

    /**
     * Setter for property width
     *
     * @param width (short) new value of property width
     */
    public void setWidth(short width) {
        this.width = width;
    }

    /**
     * Getter for property width
     *
     * @return the value of the property width (short)
     */
    public short getWidth() {
        return this.width;
    }

    /**
     * Setter for property escapement
     *
     * @param escapement (short) new value of property escapement
     */
    public void setEscapement(short escapement) {
        this.escapement = escapement;
    }

    /**
     * Getter for property escapement
     *
     * @return the value of the property escapement (short)
     */
    public short getEscapement() {
        return this.escapement;
    }

    /**
     * Setter for property orientation
     *
     * @param orientation (short) new value of property orientation
     */
    public void setOrientation(short orientation) {
        this.orientation = orientation;
    }

    /**
     * Getter for property orientation
     *
     * @return the value of the property orientation (short)
     */
    public short getOrientation() {
        return this.orientation;
    }

    /**
     * Setter for property weight
     *
     * @param weight (short) new value of property weight
     */
    public void setWeight(short weight) {
        this.weight = weight;
    }

    /**
     * Getter for property weight
     *
     * @return the value of the property weight (short)
     */
    public short getWeight() {
        return this.weight;
    }

    /**
     * Setter for property italic
     *
     * @param italic (boolean) new value of property italic
     */
    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    /**
     * Getter for property italic
     *
     * @return the value of the property italic (boolean)
     */
    public boolean isItalic() {
        return this.italic;
    }

    /**
     * Setter for property underline
     *
     * @param underline (boolean) new value of property underline
     */
    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    /**
     * Getter for property underline
     *
     * @return the value of the property underline (boolean)
     */
    public boolean isUnderline() {
        return this.underline;
    }

    /**
     * Setter for property strikeOut
     *
     * @param strikeOut (boolean) new value of property strikeOut
     */
    public void setStrikeOut(boolean strikeOut) {
        this.strikeOut = strikeOut;
    }

    /**
     * Getter for property strikeOut
     *
     * @return the value of the property strikeOut (boolean)
     */
    public boolean isStrikeOut() {
        return this.strikeOut;
    }

    /**
     * Setter for property charSet
     *
     * @param charSet (int) new value of property charSet
     */
    public void setCharSet(int charSet) {
        this.charSet = charSet;
    }

    /**
     * Getter for property charSet
     *
     * @return the value of the property charSet (int)
     */
    public int getCharSet() {
        return this.charSet;
    }

    /**
     * Setter for property outPrecision
     *
     * @param outPrecision (int) new value of property outPrecision
     */
    public void setOutPrecision(int outPrecision) {
        this.outPrecision = outPrecision;
    }

    /**
     * Getter for property outPrecision
     *
     * @return the value of the property outPrecision (int)
     */
    public int getOutPrecision() {
        return this.outPrecision;
    }

    /**
     * Setter for property clipPrecision
     *
     * @param clipPrecision (int) new value of property clipPrecision
     */
    public void setClipPrecision(int clipPrecision) {
        this.clipPrecision = clipPrecision;
    }

    /**
     * Getter for property clipPrecision
     *
     * @return the value of the property clipPrecision (int)
     */
    public int getClipPrecision() {
        return this.clipPrecision;
    }

    /**
     * Setter for property quality
     *
     * @param quality (int) new value of property quality
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * Getter for property quality
     *
     * @return the value of the property quality (int)
     */
    public int getQuality() {
        return this.quality;
    }

    /**
     * Setter for property pitchAndFamily
     *
     * @param pitchAndFamily (int) new value of property pitchAndFamily
     */
    public void setPitchAndFamily(int pitchAndFamily) {
        this.pitchAndFamily = pitchAndFamily;
    }

    /**
     * Getter for property pitchAndFamily
     *
     * @return the value of the property pitchAndFamily (int)
     */
    public int getPitchAndFamily() {
        return this.pitchAndFamily;
    }

    /**
     * Setter for property faceName
     *
     * @param faceName (String) new value of property faceName
     */
    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    /**
     * Getter for property faceName
     *
     * @return the value of the property faceName (String)
     */
    public String getFaceName() {
        return this.faceName;
    }

    /**
     * Writes the font object to a stream
     *
     * @param out (OutputStream) the stream
     * @throws IOException
     */
    public void write(OutputStream out) throws IOException {
        WMFConstants.writeLittleEndian(out, this.height);
        WMFConstants.writeLittleEndian(out, this.width);
        WMFConstants.writeLittleEndian(out, this.escapement);
        WMFConstants.writeLittleEndian(out, this.orientation);
        WMFConstants.writeLittleEndian(out, this.weight);
        out.write((this.italic ? 0x01 : 0x00));
        out.write((this.underline ? 0x01 : 0x00));
        out.write((this.strikeOut ? 0x01 : 0x00));
        out.write(this.charSet);
        out.write(this.outPrecision);
        out.write(this.clipPrecision);
        out.write(this.quality);
        out.write(this.pitchAndFamily);
        byte[] name = this.faceName.getBytes();
        int size = name.length;
        if (size > 31) size = 31;
        for (int i = 0; i < size; i++) {
            out.write(name[i]);
        }
        out.write(0);
        if ((size % 2) == 0) out.write(0);
    }

    /**
     * Reads in the font object from a stream
     *
     * @param in (InputStream) the stream
     * @throws IOException
     */
    public void read(InputStream in) throws IOException {
        this.height = WMFConstants.readLittleEndianShort(in);
        this.width = WMFConstants.readLittleEndianShort(in);
        this.escapement = WMFConstants.readLittleEndianShort(in);
        this.orientation = WMFConstants.readLittleEndianShort(in);
        this.weight = WMFConstants.readLittleEndianShort(in);
        this.italic = in.read() == 1;
        this.underline = in.read() == 1;
        this.strikeOut = in.read() == 1;
        this.charSet = in.read();
        this.outPrecision = in.read();
        this.clipPrecision = in.read();
        this.quality = in.read();
        this.pitchAndFamily = in.read();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = in.read();
        while (b != 0) {
            baos.write(b);
            b = in.read();
        }
        byte[] name = baos.toByteArray();
        if ((name.length % 2) == 1) in.read();
        this.faceName = new String(name);
    }

    /**
     * Returns the size of this object
     *
     * @return the size (short)
     */
    public short getSize() {
        if (faceName == null) return 10;
        int faceLen = faceName.getBytes().length;
        if (faceLen > 31) faceLen = 31;
        return (short) (10 + (faceLen) / 2);
    }
}
