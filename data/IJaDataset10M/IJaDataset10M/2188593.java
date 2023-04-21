package net.sf.jfuzzydate;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Provider for strings (.
 *
 * @author amaasch
  */
public interface FuzzyStringProvider {

    /**
     * Returns a string for a given key using the default locale.<br>
     * Calls to this method are equal to <br>
     * <code>getString(key, Locale.getDefault());</code>
     *
     * @param key the key to look up in the bundle.
     *
     * @return the string for the given key.
     *
     * @see #getString(String, Locale)
     */
    String getString(final String key);

    /**
     * Returns a string for a given key and locale.
     *
     * @param key the key to look up in the bundle.
     * @param locale the locale for which the string should be returned.
     *
     * @return the string for the given key.
     */
    String getString(final String key, final Locale locale);

    /**
     * Returns a string for a given key and locale. This method is also able to pass parameters to
     * the string. String replacement is done by using {@link MessageFormat} expressions like <code>"abc {0}
     * d {1} efg"</code>.
     *
     * @param key the key to look up in the bundle.
     * @param locale the locale for which the string should be returned.
     *
     * @return the string for the key and locale formatted with the given params.
     */
    String getString(final String key, final Locale locale, final Object... params);
}
