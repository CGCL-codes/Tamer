package java.util.spi;

import java.util.Locale;

/**
 * CurrencyNameProvider is an abstract class to get localized currency symbols
 * from service providers.
 * 
 * @since 1.6
 * 
 */
public abstract class CurrencyNameProvider extends LocaleServiceProvider {

    /**
	 * The constructor
	 * 
	 */
    protected CurrencyNameProvider() {
    }

    /**
	 * Returns the symbol for the specified currency
	 * 
	 * @param code
	 *            the code of the specified currency in "ISO 4217"
	 * @param locale
	 *            the locale
	 * @return the symbol or null if there is no available symbol in the locale
	 * @throws NullPointerException
	 *             if code or locale is null
	 * @throws IllegalArgumentException
	 *             if code or locale is not in a legal format or not available
	 */
    public abstract String getSymbol(String code, Locale locale);
}
