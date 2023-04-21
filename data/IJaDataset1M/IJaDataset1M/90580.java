package ee.webAppToolkit.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import com.google.inject.Binder;
import com.google.inject.Key;
import ee.webAppToolkit.localization.expert.impl.LocalizedStringImp;
import ee.webAppToolkit.localization.expert.impl.LocalizedStringProvider;

public class LocalizedStrings {

    public static LocalizedString create(String value) {
        return new LocalizedStringImp(value);
    }

    private static Key<String> EMPTY_VALUE = Key.get(String.class, new LocalizedStringImp(""));

    private static Map<Key<String>, LocalizedStringProvider> _providers = new HashMap<Key<String>, LocalizedStringProvider>();

    public static void bindProperties(Binder binder, Properties properties, Locale locale) {
        binder = binder.skipSources(LocalizedStrings.class);
        LocalizedStringProvider provider;
        if (!_providers.containsKey(EMPTY_VALUE)) {
            provider = _createProvider(binder, EMPTY_VALUE, "");
            provider.addLocalizedString(locale, "");
        }
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
            String propertyName = (String) e.nextElement();
            String value = properties.getProperty(propertyName);
            Key<String> key = Key.get(String.class, new LocalizedStringImp(propertyName));
            if (_providers.containsKey(key)) {
                provider = _providers.get(key);
            } else {
                provider = _createProvider(binder, key, propertyName);
            }
            provider.addLocalizedString(locale, value);
        }
    }

    private static LocalizedStringProvider _createProvider(Binder binder, Key<String> key, String propertyName) {
        LocalizedStringProvider provider = new LocalizedStringProvider(propertyName);
        binder.bind(key).toProvider(provider);
        _providers.put(key, provider);
        return provider;
    }

    public static void bindPropertiesToLocale(Binder binder, ClassLoader classLoader, String propertiesName, Locale locale) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = classLoader.getResourceAsStream(propertiesName + "_" + locale.toString() + ".properties");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            properties.load(inputStreamReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        bindProperties(binder, properties, locale);
    }
}
