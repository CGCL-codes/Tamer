package nz.org.venice.util;

/**
 * An exception which is raised when there is a problem parsing a
 * time.
 *
 * @author Andrew Leppard
 */
public class TradingTimeFormatException extends Throwable {

    private String time;

    /** 
     * Create a new trading time format exception.
     *
     * @param time the time being parsed.
     */
    public TradingTimeFormatException(String time) {
        super(Locale.getString("ERROR_PARSING_TIME", time));
        this.time = time;
    }

    /**
     * Return the time string being parsed.
     *
     * @return the time being parsed.
     */
    public String getTime() {
        return time;
    }
}
