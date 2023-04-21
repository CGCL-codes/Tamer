package tw.edu.shu.im.iccio.datatype;

import java.io.*;
import tw.edu.shu.im.iccio.*;

/**
 * This class contains the color space tags defined in the specification.
 * Each tag is a 4-byte integer by nature and can be represented as a four-letter
 * word for output if necessary. To reference the tag value, just use the
 * public static constant value, such as ColorSpace.XYZ_DATA.  A tag instance
 * can be constructed by the constructor or assigned a value by one of the
 * setter methods.  To return the 4-letter word, use toString() method.
 */
public class ColorSpace extends UInt32Number {

    public static final int XYZ_DATA = 0x58595A20;

    public static final int LAB_DATA = 0x4C616220;

    public static final int LUV_DATA = 0x4C757620;

    public static final int YCBCR_DATA = 0x59436272;

    public static final int YXY_DATA = 0x59787920;

    public static final int RGB_DATA = 0x52474220;

    public static final int GRAY_DATA = 0x47524159;

    public static final int HSV_DATA = 0x48535620;

    public static final int HLS_DATA = 0x484C5320;

    public static final int CMYK_DATA = 0x434D594B;

    public static final int CMY_DATA = 0x434D5920;

    public static final int COLOR2_DATA = 0x32434C52;

    public static final int COLOR3_DATA = 0x33434C52;

    public static final int COLOR4_DATA = 0x34434C52;

    public static final int COLOR5_DATA = 0x35434C52;

    public static final int COLOR6_DATA = 0x36434C52;

    public static final int COLOR7_DATA = 0x37434C52;

    public static final int COLOR8_DATA = 0x38434C52;

    public static final int COLOR9_DATA = 0x39434C52;

    public static final int COLOR10_DATA = 0x41434C52;

    public static final int COLOR11_DATA = 0x42434C52;

    public static final int COLOR12_DATA = 0x43434C52;

    public static final int COLOR13_DATA = 0x44434C52;

    public static final int COLOR14_DATA = 0x45434C52;

    public static final int COLOR15_DATA = 0x46434C52;

    public ColorSpace() {
    }

    /**
     * Construct a ColorSpace instance from a byte array that contains the value.
     * @param byteArray - byte array with the 1st 4 bytes as the tag bits.
     */
    public ColorSpace(byte[] byteArray) throws ICCProfileException {
        super(byteArray);
    }

    /**
     * Construct a ColorSpace instance from a byte array starting at a given position.
     * @param byteArray - byte array that contains the tag value.
     * @param offset - the index into the byte array where the tag value bytes start
     */
    public ColorSpace(byte[] byteArray, int offset) throws ICCProfileException {
        super(byteArray, offset);
    }

    /**
     * Construct a ColorSpace instance from a copy.
     * @param copy - another ColorSpace object
     */
    public ColorSpace(ColorSpace copy) {
        super(copy);
    }

    /**
     * Construct a ColorSpace instance from the int value.
     * The value can be one of the above public constant values.
     * @param value - int value for the tag.
     */
    public ColorSpace(int value) {
        super(value);
    }

    public void setColorSpace(int value) {
        setValue(value);
    }

    /**
     * Sets the current ColorSpace instance to be XYZ_DATA value.
     */
    public void setXYZData() {
        setValue(XYZ_DATA);
    }

    public void setLabData() {
        setValue(LAB_DATA);
    }

    public void setLuvData() {
        setValue(LUV_DATA);
    }

    public void setYCbCrData() {
        setValue(YCBCR_DATA);
    }

    public void setYxyData() {
        setValue(YXY_DATA);
    }

    public void setRgbData() {
        setValue(RGB_DATA);
    }

    public void setGrayData() {
        setValue(GRAY_DATA);
    }

    public void setHsvData() {
        setValue(HSV_DATA);
    }

    public void setHlsData() {
        setValue(HLS_DATA);
    }

    public void setCmykData() {
        setValue(CMYK_DATA);
    }

    public void setCmyData() {
        setValue(CMY_DATA);
    }

    public void set2ColorData() {
        setValue(COLOR2_DATA);
    }

    public void set3ColorData() {
        setValue(COLOR3_DATA);
    }

    public void set4ColorData() {
        setValue(COLOR4_DATA);
    }

    public void set5ColorData() {
        setValue(COLOR5_DATA);
    }

    public void set6ColorData() {
        setValue(COLOR6_DATA);
    }

    public void set7ColorData() {
        setValue(COLOR7_DATA);
    }

    public void set8ColorData() {
        setValue(COLOR8_DATA);
    }

    public void set9ColorData() {
        setValue(COLOR9_DATA);
    }

    public void set10ColorData() {
        setValue(COLOR10_DATA);
    }

    public void set11ColorData() {
        setValue(COLOR11_DATA);
    }

    public void set12ColorData() {
        setValue(COLOR12_DATA);
    }

    public void set13ColorData() {
        setValue(COLOR13_DATA);
    }

    public void set14ColorData() {
        setValue(COLOR14_DATA);
    }

    public void set15ColorData() {
        setValue(COLOR15_DATA);
    }

    /**
     * Check whether this ColorSpace instance is a XYZ_DATA.
     * @return true if the current ColorSpace is XYZ_DATA.
     */
    public boolean isXYZData() {
        return intValue() == XYZ_DATA;
    }

    public boolean isLabData() {
        return intValue() == LAB_DATA;
    }

    public boolean isLuvData() {
        return intValue() == LUV_DATA;
    }

    public boolean isYCbCrData() {
        return intValue() == YCBCR_DATA;
    }

    public boolean isYxyData() {
        return intValue() == YXY_DATA;
    }

    public boolean isRgbData() {
        return intValue() == RGB_DATA;
    }

    public boolean isGrayData() {
        return intValue() == GRAY_DATA;
    }

    public boolean isHsvData() {
        return intValue() == HSV_DATA;
    }

    public boolean isHlsData() {
        return intValue() == HLS_DATA;
    }

    public boolean isCmykData() {
        return intValue() == CMYK_DATA;
    }

    public boolean isCmyData() {
        return intValue() == CMY_DATA;
    }

    public boolean is2ColorData() {
        return intValue() == COLOR2_DATA;
    }

    public boolean is3ColorData() {
        return intValue() == COLOR3_DATA;
    }

    public boolean is4ColorData() {
        return intValue() == COLOR4_DATA;
    }

    public boolean is5ColorData() {
        return intValue() == COLOR5_DATA;
    }

    public boolean is6ColorData() {
        return intValue() == COLOR6_DATA;
    }

    public boolean is7ColorData() {
        return intValue() == COLOR7_DATA;
    }

    public boolean is8ColorData() {
        return intValue() == COLOR8_DATA;
    }

    public boolean is9ColorData() {
        return intValue() == COLOR9_DATA;
    }

    public boolean is10ColorData() {
        return intValue() == COLOR10_DATA;
    }

    public boolean is11ColorData() {
        return intValue() == COLOR11_DATA;
    }

    public boolean is12ColorData() {
        return intValue() == COLOR12_DATA;
    }

    public boolean is13ColorData() {
        return intValue() == COLOR13_DATA;
    }

    public boolean is14ColorData() {
        return intValue() == COLOR14_DATA;
    }

    public boolean is15ColorData() {
        return intValue() == COLOR15_DATA;
    }

    /**
     * Return the ColorSpace value as a four-letter word such as 'text'.
     * @return string of the current ColorSpace value.
     */
    public String toString() {
        try {
            byte[] ba = toByteArray();
            String s = new String(ba, "ISO-8859-1");
            return s;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding");
        } catch (ICCProfileException x) {
            System.err.println("toByteArray() exception:" + x.getMessage());
        }
        return "NONE";
    }

    /**
     * Return XML element of this object.
     * @param name - attribute name on element
     * @return XML fragment as a string
     */
    public String toXmlString(String name) {
        StringBuffer sb = new StringBuffer();
        if (name == null || name.length() < 1) {
            sb.append("<colorSpace>");
        } else {
            sb.append("<colorSpace name=\"" + name + "\">");
        }
        sb.append(toString());
        sb.append("</colorSpace>");
        return sb.toString();
    }

    public String toXmlString() {
        return toXmlString(null);
    }
}
