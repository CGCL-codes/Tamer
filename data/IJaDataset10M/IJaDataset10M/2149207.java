package net.jofm;

import net.jofm.format.BooleanFormat;
import net.jofm.format.DateFormat;
import net.jofm.format.Format;
import net.jofm.format.Format.Pad;

/**
 * This class encapsulates global format configurations for a <code>FixedMapper</code> 
 * instance. The configurations defined in <code>FormatConfig</code>
 * will be used as default format configurations when corresponding configurations are 
 * not specified explicitly by @Field annotations of @Fixed classes. In other words,
 * explicit configurations specified @Field annotations override configurations defined
 * in <code>FormatConfig</code>. 
 * 
 * @author Chunyun Zhao
 * @since 1.0
 */
public class FormatConfig {

    private Pad pad = Format.DEFAULT_PAD;

    private char padWith = Format.DEFAULT_PADWITH;

    private String booleanFormat = BooleanFormat.DEFAULT_FORMAT;

    private String dateFormat = DateFormat.DEFAULT_FORMAT;

    private String shortFormat = "";

    private String intFormat = "";

    private String longFormat = "";

    private String floatFormat = "";

    private String doubleFormat = "";

    private String bigDecimalFormat = "";

    public Pad getPad() {
        return pad;
    }

    public void setPad(Pad pad) {
        this.pad = pad;
    }

    public char getPadWith() {
        return padWith;
    }

    public void setPadWith(char padWith) {
        this.padWith = padWith;
    }

    public String getBooleanFormat() {
        return booleanFormat;
    }

    public void setBooleanFormat(String booleanFormat) {
        this.booleanFormat = booleanFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getShortFormat() {
        return shortFormat;
    }

    public void setShortFormat(String shortFormat) {
        this.shortFormat = shortFormat;
    }

    public String getIntFormat() {
        return intFormat;
    }

    public void setIntFormat(String intFormat) {
        this.intFormat = intFormat;
    }

    public String getLongFormat() {
        return longFormat;
    }

    public void setLongFormat(String longFormat) {
        this.longFormat = longFormat;
    }

    public String getFloatFormat() {
        return floatFormat;
    }

    public void setFloatFormat(String floatFormat) {
        this.floatFormat = floatFormat;
    }

    public String getDoubleFormat() {
        return doubleFormat;
    }

    public void setDoubleFormat(String doubleFormat) {
        this.doubleFormat = doubleFormat;
    }

    public String getBigDecimalFormat() {
        return bigDecimalFormat;
    }

    public void setBigDecimalFormat(String bigDecimalFormat) {
        this.bigDecimalFormat = bigDecimalFormat;
    }
}
