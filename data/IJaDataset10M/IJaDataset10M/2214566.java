package it.babel.funambol.CalDAV.conversion;

import java.util.TimeZone;
import net.fortuna.ical4j.model.component.CalendarComponent;
import com.funambol.common.pim.calendar.Calendar;
import com.funambol.common.pim.common.Property;
import com.funambol.common.pim.converter.ConverterException;
import com.funambol.common.pim.model.VAlarm;
import com.funambol.common.pim.model.VCalendar;
import com.funambol.common.pim.model.VCalendarContent;

public class VCalendarConverter extends com.funambol.common.pim.converter.VCalendarConverter {

    public VCalendarConverter(TimeZone timezone, String charset) {
        super(timezone, charset);
    }

    @Override
    public VCalendar calendar2vcalendar(Calendar cal, boolean xv) throws ConverterException {
        VCalendar vcal = super.calendar2vcalendar(cal, xv);
        VAlarm valarm = (VAlarm) (vcal.getComponent(CalendarComponent.VEVENT) != null ? vcal.getComponent(CalendarComponent.VEVENT).getComponent(CalendarComponent.VALARM) : null);
        if (valarm != null) {
            com.funambol.common.pim.model.Property p = valarm.getProperty("ACTION");
            valarm.addProperty(new com.funambol.common.pim.model.Property("ACTION", "AUDIO"));
        }
        if (!xv) {
            decodeProperty(vcal, "DESCRIPTION");
            decodeProperty(vcal, "SUMMARY");
            decodeProperty(vcal, "LOCATION");
        } else if (xv) {
            VCalendarContent vcc = vcal.getVCalendarContent();
            vcc.delProperty(vcc.getProperty("UID"));
            vcc.delProperty(vcc.getProperty("ORGANIZER"));
        }
        return vcal;
    }

    /**
	 * decode quoted-printable the prop specified
	 * 
	 * @param vcal the input calendar
	 * @param propName the properties to decode
	 */
    private void decodeProperty(VCalendar vcal, String propName) {
        VCalendarContent vcc = vcal.getVCalendarContent();
        com.funambol.common.pim.model.Property prop = vcc.getProperty(propName);
        if (prop != null) {
            Property decoded = decodeField(prop);
            vcc.delProperty(prop);
            String decodedValue = decoded.getPropertyValueAsString();
            decodedValue = decodedValue.replaceAll("\r\n", "\\\\n").replaceAll("\\\\n ", "\r\n ");
            vcc.addProperty(propName, decodedValue);
        }
    }
}
