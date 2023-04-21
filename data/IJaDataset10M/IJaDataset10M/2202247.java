package de.bea.domingo.groupware;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Interface to the calendar functionality of a Notes mail database.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public interface CalendarInterface {

    /**
     * Creates a new meeting.
     *
     * @return the new meeting instance
     */
    Meeting newMeeting();

    /**
     * Creates a new anniversary.
     *
     * @return the new anniversary instance
     */
    Anniversary newAnniversary();

    /**
     * Creates a new all day event.
     *
     * @return the new all day even instance
     */
    AllDayEvent newAllDayEvent();

    /**
     * Creates a new appointment.
     *
     * @return the new appointment instance
     */
    Appointment newAppointment();

    /**
     * Creates a new reminder.
     *
     * @return the new appointment instance
     */
    Reminder newReminder();

    /**
     * Saves a new calendar entry.
     *
     * @param entry the calendar entry to save
     */
    void save(CalendarEntry entry);

    /**
     * Saves a new calendar entry and sends invitations to all required,
     * optional and informed invitees.
     *
     * @param entry the calendar entry to save
     */
    void saveAndSendInvitations(CalendarEntry entry);

    /**
     * Returns calendar objects within the specified time frame.
     *
     * @param from start date
     * @param to end date
     * @return list of calendar entries
     */
    List getObjects(Calendar from, Calendar to);

    /**
     * Returns an iterator over all entries in the Calendar.
     *
     * @return iterator over all entries in the Calendar.
     */
    Iterator getCalendar();

    /**
     * Returns an iterator over all entries in the Calendar.
     *
     * <p>Depending on how the Calendar is sorted (ascending or descending by
     * date), choose where to start reading entries.</p>
     *
     * @param reverseOrder <code>true</code> if iterator should iterate in
     *            reverse order
     * @return iterator over all entries in the Calendar.
     */
    Iterator getCalendar(boolean reverseOrder);

    /**
     * Given a CalendarEntryDigest, retrieve the corresponding CalendarEntry.
     * @param ced the calendar entry digest
     * @return CalendarEntry
     */
    CalendarEntry getCalendarEntry(CalendarEntryDigest ced);

    /**
     * Deletes an existing calendar entry.
     *
     * @param entry an entry to delete
     */
    void remove(CalendarEntry entry);

    /**
     * Deletes an existing calendar entry.
     *
     * @param digest a calendar entry digest to delete
     */
    void remove(CalendarEntryDigest digest);

    /**
     * Returns the meeting for a given meeting notice.
     *
     * @param notice a notice for a meeting
     * @return the meeting for the given notice
     */
    Meeting getMeeting(Notice notice);

    /**
     * Accepts the invitation to a meeting.
     *
     * @param meeting the meeting to accept
     */
    void accept(Meeting meeting);

    /**
     * Declines a given meeting.
     *
     * @param meeting the meeting to decline
     */
    void decline(Meeting meeting);
}
