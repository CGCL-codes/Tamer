package javax.media;

public class Time implements java.io.Serializable {

    public static final long ONE_SECOND = 1000000000L;

    public static final Time TIME_UNKNOWN = new Time(Long.MAX_VALUE - 1);

    private static final double NANO_TO_SEC = 1.0E-9;

    public Time(long nanoseconds) {
    }

    public Time(double seconds) {
    }

    protected long secondsToNanoseconds(double seconds) {
        return (long) (seconds * ONE_SECOND);
    }

    public long getNanoseconds() {
        return 0L;
    }

    public double getSeconds() {
        return 0D;
    }
}
