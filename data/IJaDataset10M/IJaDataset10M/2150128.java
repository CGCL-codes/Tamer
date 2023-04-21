package Main;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class StarDate extends Date {

    static boolean initialized = false;

    static boolean hasTimeZoneBug = false;

    StarDate() {
        super();
    }

    StarDate(Date d) {
        super();
        setTime(d.getTime());
    }

    StarDate(long milli) {
        super();
        setTime(milli);
    }

    public void addDays(int days) {
        setTime(getTime() + days * (24 * 60 * 60 * 1000));
    }

    public double daysSince(Date t) {
        return ((double) (getTime() - t.getTime())) / (24. * 60. * 60. * 1000.);
    }

    public double decimalYears() {
        return getTime() / 365.242191 / (24 * 60 * 60 * 1000);
    }

    public double getTimeAsDecimalDay() {
        Double doub = new Double((double) getTime() / (24. * 60. * 60. * 1000.));
        System.out.println("doub = " + doub.toString());
        return doub.doubleValue() - doub.intValue();
    }

    public double getJulianDate() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(c.YEAR, 1970);
        c.set(c.MONTH, c.JANUARY);
        c.set(c.DAY_OF_MONTH, 1);
        c.set(c.HOUR_OF_DAY, 0);
        c.set(c.MINUTE, 0);
        c.set(c.SECOND, 0);
        return (daysSince(c.getTime()) + 2440587.83333333333);
    }

    public String toDateString() {
        String s = super.toString();
        int firstindex = s.indexOf(' ') + 1;
        int index = s.indexOf(' ', firstindex);
        index = s.indexOf(' ', index + 1);
        int yearindex = s.lastIndexOf(' ');
        return s.substring(firstindex, index) + s.substring(yearindex);
    }

    public String toString() {
        String s = super.toString();
        int firstindex = s.indexOf(' ') + 1;
        return s.substring(firstindex);
    }

    public String toJulianString() {
        double JD = getJulianDate();
        Long l = new Long((long) JD);
        String decimal = new Double(JD - l.doubleValue()).toString();
        decimal = decimal.substring(decimal.indexOf("."));
        return l.toString() + decimal;
    }
}
