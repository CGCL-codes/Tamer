package java.text.spi;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.spi.LocaleServiceProvider;

/**
 * This abstract class should be extended by service provider which provides
 * instances of <code>DecimalFormatSymbols</code> class.
 */
public abstract class DecimalFormatSymbolsProvider extends LocaleServiceProvider {

    /**
     * The constructor
     *
     */
    protected DecimalFormatSymbolsProvider() {
    }

    /**
     * Get an instance of <code>DecimalFormatSymbols</code> with specified
     * locale.
     * 
     * @param locale
     *            the specified locale
     * @return a <code>DecimalFormatSymbols</code> instance
     * @throws NullPointerException, if locale is null 
     * @throws IllegalArgumentException, if locale isn't one of the locales 
     *            returned from getAvailableLocales().
     */
    public abstract DecimalFormatSymbols getInstance(Locale locale);
}
