package cn.jsprun.vo.otherset;

public class CronInfo {

    private String cronid = null;

    private String available = null;

    private String type = null;

    private String name = null;

    private String filename = null;

    private String lastrun = null;

    private String nextrun = null;

    private String weekday = null;

    private String day = null;

    private String hour = null;

    private String minute = null;

    public boolean isDisabled() {
        return minute == null || minute.equals("");
    }

    public String getCronid() {
        return cronid;
    }

    public void setCronid(String cronid) {
        this.cronid = cronid;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLastrun() {
        return lastrun;
    }

    public void setLastrun(String lastrun) {
        this.lastrun = lastrun;
    }

    public String getNextrun() {
        return nextrun;
    }

    public void setNextrun(String nextrun) {
        this.nextrun = nextrun;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}
