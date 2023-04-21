package calclipse.caldron.gui.locale;

import java.util.ResourceBundle;

/**
 * Support for a locale.
 * To add support for a locale, one may create an
 * {@link calclipse.caldron.Initializer}
 * and have that add a locale support instance to the localizer.
 * @see calclipse.caldron.gui.locale.Localizer#add(LocaleSupport)
 * @author T. Sommerland
 */
public interface LocaleSupport {

    /**
     * A key identifying the locale supported by this class.
     * The key is the discriminator used to avoid duplication.
     */
    String getKey();

    /**
     * A human readable name for the locale.
     */
    String getName();

    /**
     * This method may throw <code>MissingResourceException</code>,
     * but should not return null.
     */
    ResourceBundle getBundle();
}
