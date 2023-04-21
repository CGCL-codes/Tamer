package gtf.client.Data;

import java.util.TreeMap;

public class Trip_Data {

    int calendar_id;

    TreeMap stopTimes = new TreeMap();

    public int getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(int calendar_id) {
        this.calendar_id = calendar_id;
    }

    public TreeMap getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(TreeMap stopTimes) {
        this.stopTimes = stopTimes;
    }
}
