package net.sourceforge.nocalsync;

import java.util.Vector;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;
import net.sourceforge.nocalsync.CommonRecurrence.Frequency;

/**
 * {@link NotesEvent} represents an event on Notes Calendar
 * 
 * @author urchin
 */
public class NotesEvent extends CommonEvent {

    public interface FIELD_NAME {

        public static final String APPOINTMENT_TYPE = "AppointmentType";

        public static final String BODY = "Body";

        public static final String CALENDAR_DATE_TIME = "CalendarDateTime";

        public static final String END_DATE_TIME = "EndDateTime";

        public static final String EXCLUDE_FROM_VIEW = "ExcludeFromView";

        public static final String FORM = "Form";

        public static final String LOCATION = "Location";

        public static final String ORG_TABLE = "orgTable";

        public static final String REPEAT_INSTANCE_DATES = "RepeatInstanceDates";

        public static final String REPEATS = "Repeats";

        public static final String START_DATE_TIME = "StartDateTime";

        public static final String SUBJECT = "Subject";
    }

    @SuppressWarnings("unchecked")
    public NotesEvent(Document doc) throws NotesException {
        super(doc.getUniversalID());
        setTitle(NoCalSync.convNull(doc.getItemValueString(FIELD_NAME.SUBJECT)));
        setContent(NoCalSync.convNull(doc.getItemValueString(FIELD_NAME.BODY)));
        setLocation(NoCalSync.convNull(doc.getItemValueString(FIELD_NAME.LOCATION)));
        setStartDateTime(new CommonDateTime((lotus.domino.DateTime) doc.getItemValue(FIELD_NAME.START_DATE_TIME).get(0)));
        setEndDateTime(new CommonDateTime((lotus.domino.DateTime) doc.getItemValue(FIELD_NAME.END_DATE_TIME).get(0)));
        setUpdatedDateTime(new CommonDateTime(doc.getLastModified()));
        Type type = Type.get(Integer.parseInt(NoCalSync.convNull(doc.getItemValueString(FIELD_NAME.APPOINTMENT_TYPE))));
        setType(type);
        if (doc.hasItem(FIELD_NAME.REPEATS) && type != Type.ALL_DAY_EVENT && type != Type.ANNIVERSARY) {
            Vector<DateTime> vector = (Vector<DateTime>) doc.getItemValue(FIELD_NAME.REPEAT_INSTANCE_DATES);
            if (!vector.isEmpty()) {
                CommonRecurrence recurrence = new CommonRecurrence();
                Frequency frequency = findFrequency(vector);
                if (frequency == Frequency.OTHER) {
                    for (DateTime dt : vector) recurrence.addRdate(new CommonDateTime(dt));
                }
                recurrence.setUntil(new CommonDateTime(vector.lastElement()));
                recurrence.setFrequency(frequency);
                setRecurrence(recurrence);
            }
        }
    }

    private static Frequency findFrequency(Vector<DateTime> vector) {
        if (isDaily(vector)) return Frequency.DAILY;
        if (isWeekly(vector)) return Frequency.WEEKLY;
        if (isMonthly(vector)) return Frequency.MONTHLY;
        if (isYearly(vector)) return Frequency.YEARLY;
        return Frequency.OTHER;
    }

    private static boolean isDaily(Vector<DateTime> vector) {
        try {
            for (int i = 1; i < vector.size(); i++) {
                DateTime prev = vector.get(i - 1);
                DateTime curr = vector.get(i);
                if (curr.timeDifference(prev) != CommonDateTime.ONE_DAY_IN_SECONDS) return false;
            }
            return true;
        } catch (NotesException e) {
            throw new InternalException(e);
        }
    }

    private static boolean isWeekly(Vector<DateTime> vector) {
        try {
            for (int i = 1; i < vector.size(); i++) {
                DateTime prev = vector.get(i - 1);
                DateTime curr = vector.get(i);
                if (curr.timeDifference(prev) != CommonDateTime.ONE_DAY_IN_SECONDS * 7) return false;
            }
            return true;
        } catch (NotesException e) {
            throw new InternalException(e);
        }
    }

    private static boolean isMonthly(Vector<DateTime> vector) {
        try {
            DateTime prev = vector.get(0);
            for (int i = 1; i < vector.size(); i++) {
                DateTime curr = vector.get(i);
                prev.adjustMonth(1);
                int diff = prev.timeDifference(curr);
                prev.adjustMonth(-1);
                if (diff != 0) return false;
                prev = curr;
            }
            return true;
        } catch (NotesException e) {
            throw new InternalException(e);
        }
    }

    private static boolean isYearly(Vector<DateTime> vector) {
        try {
            DateTime prev = vector.get(0);
            for (int i = 1; i < vector.size(); i++) {
                DateTime curr = vector.get(i);
                prev.adjustYear(1);
                int diff = prev.timeDifference(curr);
                prev.adjustYear(-1);
                if (diff != 0) return false;
                prev = curr;
            }
            return true;
        } catch (NotesException e) {
            throw new InternalException(e);
        }
    }
}
