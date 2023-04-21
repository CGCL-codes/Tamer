package remote;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author Kasza Miklós
 */
public final class CalendarConversions {

    public static XMLGregorianCalendar createXMLGregorianCalendar(final GregorianCalendar cal) {
        try {
            final DatatypeFactory dtFactory = DatatypeFactory.newInstance();
            final XMLGregorianCalendar xmlGregCal = dtFactory.newXMLGregorianCalendar(cal);
            return xmlGregCal;
        } catch (DatatypeConfigurationException x) {
            x.printStackTrace();
        }
        return null;
    }

    public static XMLGregorianCalendar createXMLGregorianCalendar(final Date date) {
        final GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return createXMLGregorianCalendar(cal);
    }

    public static Date getDate(final XMLGregorianCalendar xmlGregCal) {
        return xmlGregCal.toGregorianCalendar().getTime();
    }
}
