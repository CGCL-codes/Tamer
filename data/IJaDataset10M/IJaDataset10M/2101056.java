package net.sf.evemsp.util;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMItem;
import net.sf.evemsp.SkillPlanner;
import net.sf.evemsp.data.ChrRecord;
import net.sf.evemsp.data.Skill;

/**
 * Provides basic support for the PIM API (JSR-75)
 * 
 * @author Jaabaa
 */
public final class PimSupport {

    /**
	 * The PIM instance, only loaded once to stop as many user confirmations as possible.
	 */
    private static PIM pimInstance = null;

    public static PIM getPimInstance() {
        if (pimInstance == null) {
            pimInstance = PIM.getInstance();
        }
        return pimInstance;
    }

    private static class AddCalendarEvent implements Runnable {

        ChrRecord record;

        public AddCalendarEvent(ChrRecord record) {
            this.record = record;
        }

        public void run() {
            try {
                PIM pim = getPimInstance();
                EventList events = (EventList) pim.openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE);
                try {
                    Skill skill = record.getTraining();
                    Date aDate = record.getTrainingCompletes();
                    long completes = aDate.getTime();
                    if (SkillPlanner.isFixCal()) {
                        TimeZone tz = TimeZone.getDefault();
                        int rawOffset = tz.getRawOffset();
                        completes += rawOffset;
                    }
                    Event event = events.createEvent();
                    if (events.isSupportedField(Event.NOTE) && events.isSupportedField(Event.SUMMARY)) {
                        String eventText = "EVE: " + record.getName() + " completes " + skill.getName() + " " + record.getTrainingToLevel();
                        event.addString(Event.SUMMARY, PIMItem.ATTR_NONE, "EVE: " + record.getName());
                        event.addString(Event.NOTE, PIMItem.ATTR_NONE, eventText);
                    } else {
                        String eventText = "EVE: " + record.getName() + " completes " + skill.getName() + " " + record.getTrainingToLevel();
                        if (events.isSupportedField(Event.SUMMARY)) {
                            event.addString(Event.SUMMARY, PIMItem.ATTR_NONE, eventText);
                        } else {
                            event.addString(Event.NOTE, PIMItem.ATTR_NONE, eventText);
                        }
                    }
                    if (events.isSupportedField(Event.START)) event.addDate(Event.START, PIMItem.ATTR_NONE, completes);
                    if (events.isSupportedField(Event.END)) event.addDate(Event.END, PIMItem.ATTR_NONE, completes + 10000L);
                    if (events.isSupportedField(Event.ALARM)) event.addInt(Event.ALARM, PIMItem.ATTR_NONE, 600);
                    event.commit();
                } finally {
                    events.close();
                }
            } catch (SecurityException e) {
            } catch (Exception e) {
                Alert alert = new Alert("Error adding event to calendar", e.toString(), null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                Display display = Device.getDisplay();
                Displayable disp = display.getCurrent();
                display.setCurrent(alert, disp);
            }
        }
    }

    public static void addTraingEvent(ChrRecord record) {
        if (record.getTraining() == null || !Device.isPimApiSupported()) {
            return;
        }
        AddCalendarEvent add = new AddCalendarEvent(record);
        Thread t = new Thread(add);
        t.start();
    }
}
