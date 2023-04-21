package org.springframework.context.i18n;

import java.util.Locale;
import org.springframework.util.Assert;

/**
 * Simple implementation of the LocaleContext interface,
 * always returning a specified Locale.
 *
 * @author Juergen Hoeller
 * @since 1.2
 */
public class SimpleLocaleContext implements LocaleContext {

    private final Locale locale;

    /**
	 * Create a new SimpleLocaleContext that exposes the specified Locale.
	 * Every <code>getLocale()</code> will return this Locale.
	 * @param locale the Locale to expose
	 */
    public SimpleLocaleContext(Locale locale) {
        Assert.notNull(locale, "Locale must not be null");
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
