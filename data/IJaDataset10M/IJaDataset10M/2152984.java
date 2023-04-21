package ops.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import ops.Application;

/**
 * This class handles the overall i18n of the application.
 * @author flavio
 */
public class Resources {

    private ResourceBundle bundle;

    public static Resources getInstance() {
        return Application.getInstance().getResources();
    }

    public Resources(Locale locale) {
        bundle = ResourceBundle.getBundle("ops/i18n/Resources", locale);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getStringUnchecked(String key) throws MissingResourceException {
        return bundle.getString(key);
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public String getFormattedString(String key, Object... arguments) {
        String str = getString(key);
        return MessageFormat.format(str, arguments);
    }
}
