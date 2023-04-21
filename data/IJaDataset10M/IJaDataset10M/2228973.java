package pa_DynamicWeather;

public class DateTime {

    private String minute;

    private String hour;

    private Integer day;

    private Integer month;

    private Integer year;

    public DateTime(int day, int month, int year) {
        this.minute = "?Minute";
        this.hour = "?Hour";
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateTime() {
        this.minute = "?Minute";
        this.hour = "?Hour";
        this.day = null;
        this.month = null;
        this.year = null;
    }

    public String getMinute() {
        return minute;
    }

    public String getHour() {
        return hour;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
