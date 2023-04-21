package net.sf.lavabeans.testbean.pubmed;

/**
 * <pre>
 * (Year,Month,Day,(Hour,(Minute,Second?)?)?)
 * </pre>
 */
public class NormalDate {

    private String year;

    private String month;

    private String day;

    private String hour;

    private String minute;

    private String second;

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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String toString() {
        return year + "/" + month + "/" + day + " " + (hour != null ? hour + ":" + minute + ":" + second : "");
    }
}
