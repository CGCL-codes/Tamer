package hambo.weather;

import java.util.Date;
import hambo.app.base.Country;
import hambo.util.HTMLUtil;

/**
 * The Forecast container class, it contains all information the 
 * Presenation Objects needs. 
 *
 */
public class Forecast {

    public static final int MS = Country.METERPERSEC;

    public static final int MH = Country.MILESPERHOUR;

    public static final int KMH = Country.KILOMETERPERHOUR;

    public static final int C = Country.CELSIUS;

    public static final int F = Country.FAHRENHEIT;

    public static String CELSIUS = HTMLUtil.decode(" &#176;C");

    public static String FARENHEIT = HTMLUtil.decode(" &#176;F");

    public static String KELVIN = " K";

    public static String MS_STR = " (@glb_mpersec@)";

    public static String KMH_STR = " (@glb_kmperhour@)";

    public static String MH_STR = " (@glb_milesperhour@)";

    public static final int DEFAULT_DEGREE = Country.CELSIUS;

    public static final int DEFAULT_WINFDSPEED = Country.METERPERSEC;

    protected String date;

    protected int daycode;

    protected int nightcode;

    protected int daytemp;

    protected int nighttemp;

    protected String daywinddir;

    protected int daywindspeed;

    protected String nightwinddir;

    protected int nightwindspeed;

    protected int degreeFormat = Country.CELSIUS;

    protected int windspeedFormat = Country.MILESPERHOUR;

    /**
     * Empty constructor.
     */
    public Forecast() {
    }

    /**
     * Constructor with parameter, all parameters are String objects and 
     * will be converted to right object. The Wind Speed argument should 
     * given i km/h.     
     */
    public Forecast(String date, String daycode, String nightcode, String daytemp, String nighttemp, String daywinddir, String daywindspeed, String nightwinddir, String nightwindspeed) {
        this.date = trim(date);
        this.daycode = toInt(daycode);
        this.nightcode = toInt(nightcode);
        this.daytemp = toInt(daytemp);
        this.nighttemp = toInt(nighttemp);
        this.daywinddir = trim(daywinddir);
        this.daywindspeed = toInt(daywindspeed);
        this.nightwinddir = trim(nightwinddir);
        this.nightwindspeed = toInt(nightwindspeed);
    }

    /**
     * Constructor with parameters.
     */
    public Forecast(String date, int daycode, int nightcode, int daytemp, int nighttemp, String daywinddir, int daywindspeed, String nightwinddir, int nightwindspeed) {
        this.date = trim(date);
        this.daycode = daycode;
        this.nightcode = nightcode;
        this.daytemp = daytemp;
        this.nighttemp = nighttemp;
        this.daywinddir = trim(daywinddir);
        this.daywindspeed = daywindspeed;
        this.nightwinddir = trim(nightwinddir);
        this.nightwindspeed = nightwindspeed;
    }

    /**
     * Copy constructor.
     */
    public Forecast(Forecast f) {
        this.date = f.date;
        this.daycode = f.daycode;
        this.nightcode = f.nightcode;
        this.daytemp = f.daytemp;
        this.nighttemp = f.nighttemp;
        this.daywinddir = f.daywinddir;
        this.daywindspeed = f.daywindspeed;
        this.nightwinddir = f.nightwinddir;
        this.nightwindspeed = f.nightwindspeed;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getDaycode() {
        return daycode;
    }

    public int getNightcode() {
        return nightcode;
    }

    public String getDaytempString(int format) {
        setDegreeFormat(format);
        return getDaytempString();
    }

    public String getDaytempString() {
        switch(degreeFormat) {
            case C:
                return String.valueOf(daytemp) + CELSIUS;
            case F:
                return String.valueOf(toFahrenheit(daytemp)) + FARENHEIT;
        }
        return String.valueOf(daytemp);
    }

    public String getNighttempString(int format) {
        setDegreeFormat(format);
        return getNighttempString();
    }

    public String getNighttempString() {
        switch(degreeFormat) {
            case C:
                return String.valueOf(nighttemp) + CELSIUS;
            case F:
                return String.valueOf(toFahrenheit(nighttemp)) + FARENHEIT;
        }
        return String.valueOf(nighttemp);
    }

    public int getDaytemp() {
        return daytemp;
    }

    public int getNighttemp() {
        return nighttemp;
    }

    public String getDaywinddir() {
        return daywinddir;
    }

    public String getDaywindspeed() {
        switch(windspeedFormat) {
            case MS:
                return String.valueOf(toMeterHour(daywindspeed)) + MS_STR;
            case MH:
                return String.valueOf(toMilesHour(daywindspeed)) + MH_STR;
            case KMH:
                return String.valueOf(toKilometerHour(daywindspeed)) + KMH_STR;
        }
        return String.valueOf(daywindspeed);
    }

    public String getNightwinddir() {
        return nightwinddir;
    }

    /**
     * Converts the speed from km/h to m/s. 
     */
    public String getNightwindspeed() {
        switch(windspeedFormat) {
            case MS:
                return String.valueOf(toMeterHour(nightwindspeed)) + MS_STR;
            case MH:
                return String.valueOf(toMilesHour(nightwindspeed)) + MH_STR;
            case KMH:
                return String.valueOf(toKilometerHour(nightwindspeed)) + KMH_STR;
        }
        return String.valueOf(nightwindspeed);
    }

    public void setDaycode(int daycode) {
        this.daycode = daycode;
    }

    public void setNightcode(int nightcode) {
        this.nightcode = nightcode;
    }

    public void setDaytemp(int daytemp) {
        this.daytemp = daytemp;
    }

    public void setNighttemp(int nighttemp) {
        this.nighttemp = nighttemp;
    }

    public void setDaywinddir(String daywinddir) {
        this.daywinddir = daywinddir;
    }

    public void setDaywindspeed(int daywindspeed) {
        this.daywindspeed = daywindspeed;
    }

    public void setNightwinddir(String nightwinddir) {
        this.nightwinddir = nightwinddir;
    }

    public void setNightwindspeed(int nightwindspeed) {
        this.nightwindspeed = nightwindspeed;
    }

    public void setDegreeFormat(int format) {
        degreeFormat = format;
    }

    public void setWindspeedFormat(int windspeed) {
        windspeedFormat = windspeed;
    }

    private int toInt(String st) {
        int resint = 0;
        try {
            resint = Integer.parseInt(st.trim());
        } catch (Exception e) {
            System.err.println("Forecast: toInt: string = " + st);
        }
        return resint;
    }

    private String trim(String str) {
        if (str == null) return null;
        return str.trim();
    }

    private int toFahrenheit(int c) {
        return c * 9 / 5 + 32;
    }

    private int toMilesHour(int v) {
        return (int) Math.round(((float) v) / 1.6);
    }

    private int toMeterHour(int v) {
        return (int) Math.round(((float) v) / 3.6);
    }

    private int toKilometerHour(int v) {
        return v;
    }
}
