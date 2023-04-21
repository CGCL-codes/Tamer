package pl.kernelpanic.dbmonster.util;

/**
 * Converter.
 *
 * @author Piotr Maj &lt;pm@jcake.com&gt;
 *
 * @version $Id: Converter.java,v 1.2 2006/01/05 16:29:37 majek Exp $
 */
public final class Converter {

    /**
     * Creates a new converter.
     */
    private Converter() {
    }

    /**
     * Formats the period.
     *
     * @param ms millis
     *
     * @return formatted time
     */
    public static String formatTime(long ms) {
        if (ms < 0) {
            return "Faster than light!";
        }
        long millis = ms;
        long hours = millis / 3600000;
        StringBuffer time = new StringBuffer();
        if (hours != 0) {
            time.append(hours);
            time.append(" h.");
            millis -= (hours * 3600000);
        }
        long minutes = millis / (60000);
        if (minutes != 0) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(minutes);
            time.append(" min.");
            millis -= (minutes * 60000);
        }
        long seconds = millis / 1000;
        if (seconds != 0) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(seconds);
            time.append(" sec.");
            millis -= (seconds * 1000);
        }
        if (millis != 0) {
            if (time.length() > 0) {
                time.append(" ");
            }
            time.append(millis);
            time.append(" ms.");
        }
        if (time.length() == 0) {
            time.append("0 ms.");
        }
        return time.toString();
    }

    /**
     * Check whether nulls is in range of 0 <= nulls <= 100
     *
     * @param nulls nulls.
     *
     * @return proper nulls
     */
    public static int checkNulls(int nulls) {
        if (nulls >= 0 && nulls <= 100) {
            return nulls;
        }
        if (nulls > 100) {
            return 100;
        } else {
            return 0;
        }
    }
}
