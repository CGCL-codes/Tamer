package goofs.fs.calendar;

import com.google.gdata.data.calendar.CalendarEntry;
import goofs.calendar.ICalendar;
import goofs.fs.Dir;
import goofs.fs.SimpleFile;

public class QuickEventTempFile extends SimpleFile {

    public QuickEventTempFile(Dir parent, String name) throws Exception {
        super(parent, name);
    }

    protected ICalendar getCalendarService() {
        return ((CalendarsDir) getParent().getParent()).getCalendarService();
    }

    protected CalendarEntry getCalendar() throws Exception {
        return ((CalendarDir) getParent()).getCalendar();
    }

    @Override
    public int rename(Dir newParent, String name) {
        int rt = super.rename(newParent, name);
        if ("quick".equals(getName())) {
            try {
                getCalendarService().createQuickEvent(getCalendar(), new String(getContent()));
                remove();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
        return rt;
    }
}
