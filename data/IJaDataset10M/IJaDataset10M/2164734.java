package logahawk.listeners;

import logahawk.*;

/** A generic filtering {@link Listener}. All filtering decisions are handled by {@link Filter}. */
public class FilterListener extends ProxyListener {

    private final Filter filter;

    public FilterListener(Listener listener, Filter filter) {
        super(listener);
        this.filter = filter;
    }

    public void log(LogMeta meta, String text) {
        if (filter.accept(meta, text)) listener.log(meta, text);
    }

    /** Accepts or rejects the provided log statement. */
    public interface Filter {

        boolean accept(LogMeta meta, String text);
    }
}
