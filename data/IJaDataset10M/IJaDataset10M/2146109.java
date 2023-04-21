package net.martinimix.webflow.accessor;

import java.util.Locale;
import org.springframework.webflow.execution.RequestContext;

/**
 * Provides an interface to retrieve the request locale from a
 * Web Flow request context.
 * 
 * @author Scott Rossillo
 *
 */
public interface LocaleAccessor {

    /**
	 * Returns the locale of the current request.
	 * 
	 * @param context the <code>RequestContext</code>
	 * from which to retrieve the current <code>Locale</code>
	 * 
	 * @return the <code>Locale</code> of the current request
	 */
    public Locale getLocale(RequestContext context);
}
