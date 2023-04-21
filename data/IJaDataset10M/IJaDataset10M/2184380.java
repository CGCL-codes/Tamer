package org.dbe.composer.wfengine.bpeladmin.war.web.processview;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.dbe.composer.wfengine.bpeladmin.war.SdlMessages;
import org.dbe.composer.wfengine.util.SdlUtil;

/**
 * A simple wrapper aound name:value (key:value) sets.
 */
public class SdlPropertyNameValue {

    /** name of attrbute or property */
    private String mName;

    /** value of property. */
    private String mValue;

    /** Date version of the property value */
    private Date mDate = null;

    /**
     * Constructs the name/value wrapper.
     */
    public SdlPropertyNameValue(String aName, String aValue) {
        this(aName, aValue, false);
    }

    /**
     * Constructs the name/value wrapper.
     */
    public SdlPropertyNameValue(String aName, String aValue, boolean aIsDate) {
        mName = aName;
        mValue = aValue;
        if (aIsDate) {
            String dateFormatPattern = "yyyy.MM.dd HH:mm:ss.SSS Z";
            SimpleDateFormat df = new SimpleDateFormat(dateFormatPattern);
            try {
                mDate = df.parse(mValue);
            } catch (Exception e) {
            }
        }
    }

    /**
     * @return The propery display name based on the resoure bundle.
     */
    public String getDisplayName() {
        return SdlMessages.getString("SdlProcessViewBean.property." + getName(), SdlProcessViewUtil.formatLabel(getName()));
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Sets the value for the property.
     * @param aValue property value.
     */
    public void setValue(String aValue) {
        mValue = SdlUtil.getSafeString(aValue);
    }

    /**
     * @return True if this the value is a Date
     */
    public boolean isDateValue() {
        return (mDate != null);
    }

    /**
     * @return The value as Date object or null if the value is not a date.
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @return Returns the estimated number of rows the String value occupies based on
     *         the occurances of carriage return characters.
     */
    public int getRowCount() {
        int row = 1;
        String text = SdlUtil.getSafeString(getValue());
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                row++;
            }
        }
        return row;
    }
}
