package org.ujac.util.types;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import org.ujac.util.TypeConverterException;
import org.ujac.util.UjacTypes;
import org.ujac.util.text.FormatHelper;

/**
 * Name: TimeType<br>
 * Description: A class handling Time values.
 * 
 * @author lauerc
 */
public class TimeType implements DataType {

    /**
   * @see org.ujac.util.types.DataType#getTypeId()
   */
    public int getTypeId() {
        return UjacTypes.TYPE_TIME;
    }

    /**
   * @see org.ujac.util.types.DataType#convertObject(java.lang.Object, org.ujac.util.text.FormatHelper)
   */
    public Object convertObject(Object value, FormatHelper formatHelper) throws TypeConverterException {
        if (value == null) {
            return null;
        }
        if (value instanceof Time) {
            return value;
        }
        String strVal = value.toString();
        try {
            return formatHelper.getIsoTimeFormat().parse(strVal);
        } catch (ParseException ex) {
            throw new TypeConverterException("Failed to parse Time value out of given value '" + strVal + "'.", ex);
        }
    }

    /**
   * @see org.ujac.util.types.DataType#formatValue(java.lang.Object, org.ujac.util.text.FormatHelper)
   */
    public String formatValue(Object value, FormatHelper formatHelper) throws TypeConverterException {
        return formatHelper.getIsoDateFormat().format((Date) convertObject(value, formatHelper));
    }
}
