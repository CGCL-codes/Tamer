package org.ujug.android.calendar;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class CalendarHelper {

    private static final String EVENTS_URI = "content://calendar/events";

    private static final String CALENDARS_URI = "content://calendar/calendars";

    public static List<Calendar> listCalenders(ContentResolver contentResolver) {
        List<Calendar> calendars = new ArrayList<Calendar>();
        Cursor cursor = contentResolver.query(getCalendarUri(), new String[] { "_id", "displayname" }, null, null, null);
        try {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                calendars.add(new Calendar(cursor.getInt(0), cursor.getString(1)));
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return calendars;
    }

    public static void add(ContentResolver contentResolver, Calendar calendar, CalendarItem calendarItem) {
        ContentValues cv = new ContentValues();
        cv.put("calendar_id", calendar.getId());
        cv.put("title", calendarItem.getTitle());
        cv.put("description", calendarItem.getDescription());
        cv.put("dtstart", calendarItem.getStartDate().getTime());
        cv.put("dtend", calendarItem.getEndDate().getTime());
        cv.put("eventLocation", calendarItem.getLocation());
        cv.put("eventStatus", calendarItem.getStatus().ordinal());
        cv.put("duration", calendarItem.getDuration());
        if (calendarItem.isAllDay()) cv.put("allDay", 1);
        contentResolver.insert(getEventsUri(), cv);
    }

    private static Uri getCalendarUri() {
        return Uri.parse(CALENDARS_URI);
    }

    private static Uri getEventsUri() {
        return Uri.parse(EVENTS_URI);
    }
}
