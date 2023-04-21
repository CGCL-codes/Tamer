package DE.FhG.IGD.logging;

import DE.FhG.IGD.util.URL;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * This is a factory for timestamps and interval anchor entries. Each
 * <code>Chronometer</code> needs to be assigned to a {@link URL}, so it can
 * be reused in case the underlying logging mechanism needs to be exchanged.
 *
 * @author Matthias Pressfreund
 * @version "$Id: Chronometer.java 1470 2004-09-15 10:44:13Z jpeters $"
 */
public final class Chronometer {

    /**
     * Contains the output pattern for a timestamp (actually uses
     * {@link SimpleDateFormat})
     */
    protected static final DateFormat TIMESTAMP_FORMAT_ = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss.SSS Z:z", Locale.ENGLISH);

    /**
     * The storage for all used <code>Chronometer</code> instances as
     * {@link WeakReference} objects
     */
    private static Set cache_ = new HashSet();

    /**
     * The length of an interval in milliseconds
     */
    protected static Long interval_ = LoggingConfiguration.atPresent().getInterval();

    /**
     * The <code>URL</code> assignment for this
     * <code>Chronometer</code>
     */
    protected URL url_;

    /**
     * Timestamp of last interval
     */
    protected long zerotime_;

    /**
     * This flag will be set to <code>false</code> as soon as the first
     * interval has been requested
     */
    protected boolean first_;

    /**
     * Hidden construction.
     *
     * @param url The assigned <code>URL</code>
     */
    protected Chronometer(URL url) {
        url_ = url;
        zerotime_ = System.currentTimeMillis();
        first_ = true;
        cache_.add(new WeakReference(this));
    }

    /**
     * Get the <code>Chronometer</code> instance for a given <code>URL</code>.
     * If there has no <code>Chronometer</code> instance assigned to the
     * specified <code>URL</code> yet, a new instance will be created.
     *
     * @param url The <code>URL</code> to create the
     *   <code>Chronometer</code> for
     * @return The <code>Chronometer</code> instance assigned to
     *   the given <code>URL</code>
     */
    protected static Chronometer instanceFor(URL url) {
        Chronometer cm;
        Iterator i;
        synchronized (cache_) {
            for (i = cache_.iterator(); i.hasNext(); ) {
                cm = (Chronometer) ((WeakReference) i.next()).get();
                if (cm != null && cm.url_.equals(url)) {
                    return cm;
                }
            }
        }
        return new Chronometer(url);
    }

    /**
     * Change the interval length
     * 
     * @param interval The new interval length
     */
    static void changeInterval(Long interval) {
        interval_ = interval;
    }

    /**
     * Get the <code>URL</code> this <code>Chronometer</code> instance
     * has been assigned to.
     *
     * @return The assigned <code>URL</code>
     */
    public URL getURL() {
        return url_;
    }

    /**
     * Get the current timestamp which is either a number of anchor
     * timestamps for each interval followed by the number of milliseconds
     * elapsed since the last interval or only the complete date and time
     * info in case the interval has been set to zero.
     *
     * @param logtime Timestamp of last log message
     * @return The next timestamp formatted to be put into a log entry,
     *   if necessary with preceding anchor timestamps
     */
    public synchronized StringBuffer nextInterval(long logtime) {
        StringBuffer ts;
        long ival;
        ts = new StringBuffer();
        ival = interval_.longValue();
        if (ival <= 0) {
            ts.append("[");
            ts.append(TIMESTAMP_FORMAT_.format(new Date(logtime)));
            ts.append("]");
        } else {
            if (first_) {
                if (logtime - zerotime_ < ival) {
                    ts.append("##### ");
                    ts.append(TIMESTAMP_FORMAT_.format(new Date(zerotime_)));
                    ts.append(LoggingConfiguration.CR);
                }
                first_ = false;
            }
            while (logtime - zerotime_ >= ival) {
                ts.append("##### ");
                ts.append(TIMESTAMP_FORMAT_.format(new Date(zerotime_ += ival)));
                ts.append(LoggingConfiguration.CR);
            }
            ts.append("[");
            ts.append(logtime - zerotime_);
            ts.append("ms]");
        }
        return ts;
    }

    /**
     * Remove this instance and <code>null</code> references from the storage
     * of deployed <code>Chronometer</code> objects.
     */
    protected void finalize() {
        Chronometer cm;
        Iterator i;
        synchronized (cache_) {
            for (i = cache_.iterator(); i.hasNext(); ) {
                cm = (Chronometer) ((WeakReference) i.next()).get();
                if (cm == null || cm.equals(this)) {
                    i.remove();
                }
            }
        }
    }

    public boolean equals(Object obj) {
        boolean equal = (obj == this);
        if (!equal && obj instanceof Chronometer) {
            Chronometer cm = (Chronometer) obj;
            equal = url_.equals(cm.url_) && zerotime_ == cm.zerotime_ && first_ == cm.first_;
        }
        return equal;
    }

    public int hashCode() {
        return (url_.hashCode() + (int) zerotime_ + (first_ ? 15 : 16));
    }

    public String toString() {
        return ("Chronometer[url=" + url_ + "]");
    }
}
