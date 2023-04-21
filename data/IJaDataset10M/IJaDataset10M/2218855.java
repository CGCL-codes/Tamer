package ch.ethz.mxquery.util.browser.util;

class LocalDateTime {

    int year;

    int month;

    int date;

    int hourOfDay;

    int minutes;

    int seconds;

    int milliseconds;

    public LocalDateTime clone() {
        LocalDateTime ret = new LocalDateTime();
        ret.year = this.year;
        ret.month = this.month;
        ret.date = this.date;
        ret.hourOfDay = this.hourOfDay;
        ret.minutes = this.minutes;
        ret.seconds = this.seconds;
        ret.milliseconds = this.milliseconds;
        return ret;
    }
}
