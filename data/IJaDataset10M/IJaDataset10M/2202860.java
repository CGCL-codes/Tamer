package org.datanucleus.store.types;

import org.datanucleus.store.types.ObjectStringConverter;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;

/**
 * Class to handle the conversion between org.joda.time.Period and a String form.
 */
public class JodaPeriodStringConverter implements ObjectStringConverter {

    public Object toObject(String str) {
        if (str == null) {
            return null;
        }
        return new Period(str);
    }

    public String toString(Object obj) {
        String str;
        if (obj instanceof Period) {
            str = ((Period) obj).toString(ISOPeriodFormat.standard());
        } else {
            str = (String) obj;
        }
        return str;
    }
}
