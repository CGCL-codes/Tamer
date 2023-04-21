package at.ac.arcs.itt.yau.libs.timeseries.api;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Immutable class holding one point in time.
 * @author Peter Kutschera
 */
public class TimeStamp implements Comparable {

    private static final String DATEFORMATSTRING = "yyyy-MM-dd'T'HH:mm:ssZ";

    protected long timeMillis;

    protected long timePicos;

    /**
   * Create one TimeStamp for the given time.
   * @param timeMillis
   * @see System#currentTimeMillis()
   */
    public TimeStamp(long timeMillis) {
        this(timeMillis, 0L);
    }

    /**
   * Create one TimeStamp with better resolution.
   * @param timeMillis
   * @param timePicos
   * @see System#currentTimeMillis()
   */
    public TimeStamp(long timeMillis, long timePicos) {
        this.timeMillis = timeMillis;
        this.timePicos = timePicos;
    }

    /**
   * @return the timeMillis
   */
    public long getTimeMillis() {
        return timeMillis;
    }

    /**
   * @return the timePicos
   */
    public long getTimePicos() {
        return timePicos;
    }

    @Override
    public int compareTo(Object o) throws ClassCastException {
        TimeStamp other = (TimeStamp) o;
        if (this.timeMillis < other.timeMillis) {
            return -1;
        }
        if (this.timeMillis > other.timeMillis) {
            return +1;
        }
        if (this.timePicos < other.timePicos) {
            return -1;
        }
        if (this.timePicos > other.timePicos) {
            return +1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TimeStamp) && (this.timeMillis == ((TimeStamp) obj).timeMillis) && (this.timePicos == ((TimeStamp) obj).timePicos);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.timeMillis ^ (this.timeMillis >>> 32));
        hash = 67 * hash + (int) (this.timePicos ^ (this.timePicos >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATSTRING);
        return sdf.format(new Date(this.timeMillis));
    }

    /**
   * Implement the <code>&lt;</code> operator for TimeStamp.
   * @param a
   * @param b
   * @return <code>a &lt; b</code>
   */
    public static boolean lt(TimeStamp a, TimeStamp b) {
        return a.compareTo(b) < 0;
    }

    public static TimeStamp parse(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATSTRING);
        Date temp = sdf.parse(s);
        TimeStamp ts = new TimeStamp(temp.getTime(), 0L);
        return ts;
    }
}
