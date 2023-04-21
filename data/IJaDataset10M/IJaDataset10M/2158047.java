package jeliot.tracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import jeliot.util.DebugUtil;

/**
 * 
 * @author Niko Myller
 */
public class TrackerClock {

    /**
     * @return
     */
    public native long getTrackerTime();

    /**
     * @return
     */
    public native String getTrackerTimeStamp();

    /**
     * Comment for <code>trackerClock</code>
     */
    protected static TrackerClock trackerClock;

    /**
     * Comment for <code>nativeTracking</code>
     */
    private static boolean nativeTracking;

    /**
     * Comment for <code>startTime</code>
     */
    private Date startTime;

    /**
     * Comment for <code>DATE_FORMAT</code>
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd't'HH.mm.ss.SSSS");

    /**
     * @return
     */
    public synchronized Date getCurrentTime() {
        if (TrackerClock.nativeTracking) {
            System.loadLibrary("JeliotJNI");
            String trackerTimeStamp = getTrackerTimeStamp();
            try {
                return DATE_FORMAT.parse(trackerTimeStamp);
            } catch (ParseException e) {
                DebugUtil.handleThrowable(e);
            }
        } else {
            return Calendar.getInstance().getTime();
        }
        return null;
    }

    /**
     * @return
     */
    protected static TrackerClock getInstance() {
        if (trackerClock == null) {
            trackerClock = new TrackerClock();
            trackerClock.startTime = trackerClock.getCurrentTime();
        }
        return trackerClock;
    }

    /**
     * 
     */
    protected TrackerClock() {
    }

    /**
     * @return
     */
    public static long currentTimeMillis() {
        TrackerClock clock = getInstance();
        return clock.getCurrentTime().getTime() - clock.startTime.getTime();
    }

    /**
     * @param b
     */
    public static void setNativeTracking(boolean b) {
        nativeTracking = b;
    }

    /**
     * @return
     */
    public Date getStartTime() {
        return this.startTime;
    }
}
