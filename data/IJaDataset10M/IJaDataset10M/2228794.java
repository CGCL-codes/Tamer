package net.narusas.daumaccess;

public class SchedulerSetup {

    public void setup(String cfg, Scheduler scheduler, ScheduledJob run) {
        if (cfg == null) {
            return;
        }
        String[] times = cfg.split("\n");
        for (String time : times) {
            if ("".equals(time.trim()) || time == null) {
                continue;
            }
            String[] values = time.split(" ");
            String dir = getValue("Dir", values);
            String date = getValue("Date", values);
            String[] timeStr = getValue("Time", values).split(":");
            int hour = toInt(timeStr[0]);
            int min = toInt(timeStr[1]);
            int count = getIntValue("Count", values);
            if (scheduler != null) {
                scheduler.addJob(dir, date, hour, min, count, run);
            }
        }
    }

    private int getIntValue(String key, String[] values) {
        return Integer.parseInt(getValue(key, values).trim());
    }

    private String getValue(String key, String[] values) {
        for (String value : values) {
            if (value.startsWith(key)) {
                return value.split("=")[1];
            }
        }
        return null;
    }

    private int toInt(String s) {
        return Integer.parseInt(s);
    }
}
