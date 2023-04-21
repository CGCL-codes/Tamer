package cx.ath.contribs.internal.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;
import cx.ath.contribs.internal.xerces.impl.dv.InvalidDatatypeValueException;
import cx.ath.contribs.internal.xerces.impl.dv.ValidationContext;

/**
 * Validator for &lt;dateTime&gt; datatype (W3C Schema Datatypes)
 *
 * @xerces.internal 
 *
 * @author Elena Litani
 * @author Gopal Sharma, SUN Microsystem Inc.
 *
 * @version $Id: DateTimeDV.java,v 1.2 2007/07/13 07:23:28 paul Exp $
 */
public class DateTimeDV extends AbstractDateTimeDV {

    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content);
        } catch (Exception ex) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "dateTime" });
        }
    }

    /**
     * Parses, validates and computes normalized version of dateTime object
     *
     * @param str    The lexical representation of dateTime object CCYY-MM-DDThh:mm:ss.sss
     *               with possible time zone Z or (-),(+)hh:mm
     * @return normalized dateTime representation
     * @exception SchemaDateTimeException Invalid lexical representation
     */
    protected DateTimeData parse(String str) throws SchemaDateTimeException {
        DateTimeData date = new DateTimeData(str, this);
        int len = str.length();
        int end = indexOf(str, 0, len, 'T');
        int dateEnd = getDate(str, 0, end, date);
        getTime(str, end + 1, len, date);
        if (dateEnd != end) {
            throw new RuntimeException(str + " is an invalid dateTime dataype value. " + "Invalid character(s) seprating date and time values.");
        }
        validateDateTime(date);
        saveUnnormalized(date);
        if (date.utc != 0 && date.utc != 'Z') {
            normalize(date);
        }
        return date;
    }

    protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData date) {
        return factory.newXMLGregorianCalendar(BigInteger.valueOf(date.unNormYear), date.unNormMonth, date.unNormDay, date.unNormHour, date.unNormMinute, (int) date.unNormSecond, date.unNormSecond != 0 ? new BigDecimal(date.unNormSecond - ((int) date.unNormSecond)) : null, date.timezoneHr * 60 + date.timezoneMin);
    }
}
