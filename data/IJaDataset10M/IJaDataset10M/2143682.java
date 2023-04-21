package nz.org.venice.util;

/**
 * An exception which is raised when there is a problem parsing a
 * date.
 *
 * @author Andrew Leppard
 */
public class TradingDateFormatException extends Throwable {

    private String date;

    /** 
     * Create a new trading date format exception.
     *
     * @param date the date being parsed.
     */
    public TradingDateFormatException(String date) {
        super(Locale.getString("ERROR_PARSING_DATE", date));
        this.date = date;
    }

    /**
     * Return the date string being parsed.
     *
     * @return the date being parsed.
     */
    public String getDate() {
        return date;
    }
}
