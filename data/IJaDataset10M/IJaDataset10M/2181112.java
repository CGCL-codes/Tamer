package net.sourceforge.processdash.log.time;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import net.sourceforge.processdash.util.EnumerIterator;

public class BaseTimeLog implements TimeLog {

    private File timeLogFile;

    public BaseTimeLog(File file) {
        timeLogFile = file;
    }

    public EnumerIterator filter(String path, Date from, Date to) throws IOException {
        EnumerIterator result = new TimeLogReader(timeLogFile);
        if (path != null || from != null || to != null) result = new TimeLogIteratorFilter(result, path, from, to);
        return result;
    }
}
