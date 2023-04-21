package logahawk;

/** A generic filtering {@link Logger}. All filtering decisions are handled by {@link Filter}. */
public class FilterLogger extends ProxyLogger {

    private final Filter filter;

    public FilterLogger(Logger logger, Filter filter) {
        super(logger);
        this.filter = filter;
    }

    @Override
    public void log(Severity severity, Object... data) {
        if (filter.accept(severity, data)) logger.log(severity, data);
    }

    /** Accepts or rejects the provided log statement. */
    public interface Filter {

        boolean accept(Severity severity, Object... data);
    }
}
