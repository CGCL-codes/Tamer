package net.teqlo.utils.log;

import java.text.SimpleDateFormat;
import java.util.*;

public class LogSession {

    private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private Date firstDate;

    private Date lastDate;

    private int logons = 0;

    private int saves = 0;

    private int lookups = 0;

    public LogSession() {
    }

    public void setActivity(Date d, String line) {
        if (firstDate == null || d.before(firstDate)) firstDate = d;
        if (lastDate == null || d.after(lastDate)) lastDate = d;
        if (line.indexOf("Put document into database: source:") != -1) saves++; else if (line.indexOf("Logon:") != -1) logons++; else if (line.indexOf("Service Lookup:") != -1) lookups++;
    }

    public boolean isActive(Date d) {
        return (d.getTime() - lastDate.getTime()) < (5 * 60 * 1000);
    }

    public String report() {
        return String.format("%1$s  %2$s  %3$4ds  %4$5d  %5$4d  %6$6d", df.format(firstDate), df.format(lastDate), (lastDate.getTime() - firstDate.getTime()) / 1000, logons, saves, lookups);
    }
}
