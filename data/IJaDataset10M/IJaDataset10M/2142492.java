package fi.pyramus.I18N;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class JavaScriptMessages {

    public static JavaScriptMessages getInstance() {
        return instance;
    }

    private static JavaScriptMessages instance;

    public String getText(Locale locale, String key) {
        return getResourceBundle(locale).getString(key);
    }

    public String getText(Locale locale, String key, Object[] params) {
        return MessageFormat.format(getText(locale, key), params);
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        if (!bundles.containsKey(locale)) {
            ResourceBundleDelegate resourceBundle = new ResourceBundleDelegate(locale);
            for (String bundleName : bundleNames) {
                ResourceBundle localeBundle = ResourceBundle.getBundle(bundleName, locale);
                if (localeBundle != null) {
                    resourceBundle.addResourceBundle(localeBundle);
                }
            }
            bundles.put(locale, resourceBundle);
        }
        return bundles.get(locale);
    }

    private void loadBundleNames() {
        bundleNames.add("fi.pyramus.I18N.javascriptlocale");
    }

    private List<String> bundleNames = new ArrayList<String>();

    private Map<Locale, ResourceBundle> bundles = new HashMap<Locale, ResourceBundle>();

    static {
        instance = new JavaScriptMessages();
        instance.loadBundleNames();
    }
}
