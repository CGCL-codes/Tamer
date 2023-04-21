package lu.tudor.santec.bizcal.export;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import lu.tudor.santec.bizcal.NamedCalendar;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import bizcal.common.Event;

public class ICALExporter {

    private Calendar calendar;

    private UidGenerator uidGenerator;

    private boolean showCalendarName = true;

    /**
	 * static logger for this class
	 */
    private static Logger logger = Logger.getLogger(ICALExporter.class.getName());

    public ICALExporter() {
        this.calendar = new Calendar();
        this.calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        this.calendar.getProperties().add(Version.VERSION_2_0);
        this.calendar.getProperties().add(CalScale.GREGORIAN);
        try {
            this.uidGenerator = new UidGenerator("1");
        } catch (SocketException e) {
            logger.log(Level.WARNING, "uidGenerator failed", e);
        }
    }

    public void saveEvents(Collection<Event> events) {
        for (Iterator iter = events.iterator(); iter.hasNext(); ) {
            Event event = (Event) iter.next();
            String summary = "";
            if (this.showCalendarName && event.get(NamedCalendar.CALENDAR_NAME) != null) {
                summary += event.get(NamedCalendar.CALENDAR_NAME) + ": " + "\n";
            }
            summary += event.getSummary();
            VEvent vev = new VEvent(new DateTime(event.getStart()), new DateTime(event.getEnd()), summary);
            Description d = new Description(event.getDescription());
            vev.getProperties().add(d);
            Location l = new Location((String) event.get(NamedCalendar.CALENDAR_NAME));
            vev.getProperties().add(l);
            vev.getProperties().add(this.uidGenerator.generateUid());
            this.calendar.getComponents().add(vev);
        }
    }

    public void writeICSFile(File f) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(f);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(this.calendar, fout);
        } catch (Exception e) {
            logger.log(Level.WARNING, "writing ICS file failed", e);
        }
    }

    /**
	 * @return the showCalendarName
	 */
    public boolean isShowCalendarName() {
        return this.showCalendarName;
    }

    /**
	 * @param showCalendarName
	 *            the showCalendarName to set
	 */
    public void setShowCalendarName(boolean showCalendarName) {
        this.showCalendarName = showCalendarName;
    }
}
