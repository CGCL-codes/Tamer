package org.mitre.caasd.aixm.xml;

import org.mitre.caasd.aixm.CoreTypes;
import org.mitre.caasd.aixm.types.*;
import org.mitre.caasd.aixm.typeinfo.ValueFormatter;

class XmlGYearFormatter extends XmlFormatter {

    public String format(DateTime dt) {
        String result = "";
        int year = dt.getYear();
        if (year < 0) {
            result += '-';
            year = -year;
        }
        result += CoreTypes.formatNumber(year, 4);
        if (dt.hasTimezone() != CalendarBase.TZ_MISSING) result += CoreTypes.formatTimezone(dt.getTimezoneOffset());
        return result;
    }
}
