package fi.helsinki.cs.kaisei;

import java.util.HashMap;

public class FullReport extends Report {

    public FullReport(Schedule schedule, HashMap<String, Object> options) {
        super(schedule, options);
    }

    public String toString() {
        String res = "";
        for (Weekday.Day day : schedule.getSchedule().keySet()) {
            res += day + ":\n";
            res += "----\n";
            for (Event event : schedule.getSchedule().get(day)) {
                res += event.getTitle();
                res += "\n at " + event.getStartTime() + "-" + event.getEndTime();
                res += "\n in " + event.getLocation() + "\n";
            }
            res += "\n\n";
        }
        return res;
    }
}
