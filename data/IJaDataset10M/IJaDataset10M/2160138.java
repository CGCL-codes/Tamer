package uk.ac.gla.cmt.animatics.apps.aniplay;

/**
 * A collection of useful time utilities
 *
 */
public class TimeUtilities {

    /** sample rate used within this TimeUtilities */
    private long theSampleRate;

    /** defined constant - avoid typos !! */
    private static long NANOSECONDS_IN_ONE_SECOND = 1000000000;

    /**
	 * Constructs a new TimeUtilities for the specified sample rate
	 *
	 * @param sampleRate the sameple rate used for conversions
	 */
    public TimeUtilities(long sampleRate) {
        this.theSampleRate = sampleRate;
    }

    /**
	 * Sets the sample rate sed within this TimeUtilities
	 *
	 * @param samepleRate the new sameple rate for use within this TimeUtilities
	 */
    public void setSampleRate(long sampleRate) {
        this.theSampleRate = sampleRate;
    }

    /**
	 * Returns the sample rate used within this TimeUtilities
	 *
	 * @return the samepl rate used within this TimeUtilities
	 */
    public long getSampleRate() {
        return (this.theSampleRate);
    }

    /**
	 * Converts nanoseconds to jack audio framse using the sample rate of this TimeUtilities
	 * Animatic time data is stored in jack frames, this method is used to convert
	 * from nanos used within the JMF to jack frames
	 *
	 */
    public long nanosecondsToJackFrames(long nanos) {
        return (TimeUtilities.nanosecondsToJackFrames(this.theSampleRate, nanos));
    }

    /**
	 * Converts nanoseconds to jack audio framse using specified sample rate
	 * Animatic time data is stored in jack frames, this method is used to convert
	 * from nanos used within the JMF to jack frames
	 *
	 */
    public static long nanosecondsToJackFrames(long sampleRate, long nanos) {
        long jackFrames = 0;
        double jackFramesPerNano = (double) sampleRate / (double) TimeUtilities.NANOSECONDS_IN_ONE_SECOND;
        jackFrames = (long) (jackFramesPerNano * nanos);
        return (jackFrames);
    }

    /**
	 * Converts jack frames to nanoseconds using the sample rate of this TimeUtilities
	 * Animatic time data is stored in jack frames, this method is used to convert
	 * from jack frames to nanos used within the JMF
	 *
	 */
    public static long jackFramesToNanoseconds(long sampleRate, long jackFrames) {
        long nanos = 0;
        double nanosPerJackFrame = (double) TimeUtilities.NANOSECONDS_IN_ONE_SECOND / (double) sampleRate;
        nanos = (long) (nanosPerJackFrame * jackFrames);
        return (nanos);
    }

    public static String animaticTimeToString(long sampleRate, long jackFrames) {
        long minutes = jackFrames / (60 * sampleRate);
        long seconds = (jackFrames / sampleRate) % 60;
        return (formatTimeString(minutes, seconds));
    }

    public static String nanosecondsToString(long nanos) {
        long minutes = nanos / (60 * TimeUtilities.NANOSECONDS_IN_ONE_SECOND);
        long seconds = (nanos / (TimeUtilities.NANOSECONDS_IN_ONE_SECOND)) % 60;
        return (formatTimeString(minutes, seconds));
    }

    public static String formatTimeString(long minutes, long seconds) {
        StringBuffer buf = new StringBuffer();
        buf.append(String.valueOf(minutes));
        buf.append(":");
        if (seconds < 10) {
            buf.append("0");
        }
        buf.append(String.valueOf(seconds));
        return (buf.toString());
    }
}
