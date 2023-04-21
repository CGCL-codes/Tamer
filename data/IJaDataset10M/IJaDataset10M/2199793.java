package fi.foyt.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import fi.foyt.cs.api.SystemProperty;
import fi.foyt.cs.controller.SystemController;
import fi.foyt.cs.persistence.domainmodel.system.Property;

public class Settings {

    public static String getValue(SystemProperty systemProperty) {
        return getValues().get(systemProperty.getInternalName());
    }

    public static Double getDoubleValue(SystemProperty systemProperty) {
        return NumberUtils.createDouble(getValue(systemProperty));
    }

    public static Integer getIntegerValue(SystemProperty systemProperty) {
        return NumberUtils.createInteger(getValue(systemProperty));
    }

    public static Long getLongValue(SystemProperty systemProperty) {
        return NumberUtils.createLong(getValue(systemProperty));
    }

    public static String getServerUrl() {
        return getServerUrl("");
    }

    public static String getServerUrl(String path) {
        StringBuilder urlBuilder = new StringBuilder().append("http://").append(Settings.getValue(SystemProperty.HOST)).append(':').append(Settings.getValue(SystemProperty.PORT)).append(path);
        return urlBuilder.toString();
    }

    public static List<Locale> getSupportedLocales() {
        List<Locale> result = new ArrayList<Locale>();
        String supportedLocales = getValue(SystemProperty.SUPPORTED_LOCALES);
        for (String supportedLocale : supportedLocales.split(",")) {
            result.add(new Locale(supportedLocale));
        }
        return result;
    }

    public static Locale getDefaultLocale() {
        return new Locale(Settings.getValue(SystemProperty.DEFAULT_LOCALE));
    }

    public static synchronized void flushSettings() {
        values = null;
    }

    private static synchronized Map<String, String> getValues() {
        if (values == null) {
            values = new HashMap<String, String>();
            SystemController systemController = new SystemController();
            List<Property> properties = systemController.listProperties();
            for (Property property : properties) {
                values.put(property.getName(), property.getValue());
            }
        }
        return values;
    }

    private static Map<String, String> values;
}
