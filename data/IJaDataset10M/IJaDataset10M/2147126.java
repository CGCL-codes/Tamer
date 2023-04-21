package net.sf.jdigg.arguments;

/**
 * Abstract base class for arguments with 'sort', 'count', 'offset', 'minDate' and 'maxDate'.
 * <p>Please read this before you use the API:
 * <a href="http://apidoc.digg.com/BasicConcepts#BePolitePlease">Be Polite, Please!</a></p>
 * @author Philip May
 * @since 1.0 M1
 */
public abstract class AbstractSortCountOffsetMinMaxDateArguments extends AbstractSortCountOffsetArguments {

    private static final String MIN_DATE_NAME = "min_date";

    private static final String MAX_DATE_NAME = "max_date";

    public AbstractSortCountOffsetMinMaxDateArguments(String appkey) {
        super(appkey);
    }

    /**
     * Get results within a time period.
     * @param minDate Unix epoch integer
     * @see net.sf.jdigg.util.DateUtil
     */
    public final void setMinDate(long minDate) {
        addArgument(MIN_DATE_NAME, String.valueOf(minDate));
    }

    /**
     * Get results within a time period.
     * @param maxDate Unix epoch integer
     * @see net.sf.jdigg.util.DateUtil
     */
    public final void setMaxDate(long maxDate) {
        addArgument(MAX_DATE_NAME, String.valueOf(maxDate));
    }
}
