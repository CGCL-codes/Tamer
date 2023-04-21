package sf.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Configuration properties.
 * 
 * @author Sualeh Fatehi
 */
public final class TemplatingUtility {

    private static final String DELIMITER_START = "${";

    private static final String DELIMITER_END = "}";

    /**
   * Expands a template using system properties. Variables in the
   * template are in the form of ${variable}.
   * 
   * @param template
   *        Template to expand.
   * @return Expanded template
   */
    public static String expandTemplate(final String template) {
        return expandTemplate(template, propertiesMap(System.getProperties()));
    }

    /**
   * Expands a template using variable values in the provided map.
   * Variables in the template are in the form of ${variable}.
   * 
   * @param template
   *        Template to expand.
   * @param variablesMap
   *        Variables and values.
   * @return Expanded template
   */
    public static String expandTemplate(final String template, final Map<String, String> variablesMap) {
        if (Utility.isBlank(template) || variablesMap == null) {
            return template;
        }
        final StringBuilder buffer = new StringBuilder();
        int currentPosition = 0;
        int delimiterStartPosition;
        int delimiterEndPosition;
        while (true) {
            delimiterStartPosition = template.indexOf(DELIMITER_START, currentPosition);
            if (delimiterStartPosition == -1) {
                if (currentPosition == 0) {
                    return template;
                } else {
                    buffer.append(template.substring(currentPosition, template.length()));
                    return buffer.toString();
                }
            } else {
                buffer.append(template.substring(currentPosition, delimiterStartPosition));
                delimiterEndPosition = template.indexOf(DELIMITER_END, delimiterStartPosition);
                if (delimiterEndPosition > -1) {
                    delimiterStartPosition = delimiterStartPosition + DELIMITER_START.length();
                    final String key = template.substring(delimiterStartPosition, delimiterEndPosition);
                    final String value = variablesMap.get(key);
                    if (value != null) {
                        buffer.append(value);
                    } else {
                        buffer.append(DELIMITER_START).append(key).append(DELIMITER_END);
                    }
                    currentPosition = delimiterEndPosition + DELIMITER_END.length();
                } else {
                    buffer.append(DELIMITER_START);
                    currentPosition = delimiterStartPosition + DELIMITER_START.length();
                }
            }
        }
    }

    /**
   * Extracts variables from the template. Variables are in the form of
   * ${variable}.
   * 
   * @param template
   *        Template to extract variables from.
   * @return Extracted variables
   */
    public static Set<String> extractTemplateVariables(final String template) {
        if (Utility.isBlank(template)) {
            return new HashSet<String>();
        }
        String shrunkTemplate = template;
        final Set<String> keys = new HashSet<String>();
        for (int left; (left = shrunkTemplate.indexOf(DELIMITER_START)) >= 0; ) {
            final int right = shrunkTemplate.indexOf(DELIMITER_END, left + 2);
            if (right >= 0) {
                final String propertyKey = shrunkTemplate.substring(left + 2, right);
                keys.add(propertyKey);
                shrunkTemplate = shrunkTemplate.substring(0, left) + shrunkTemplate.substring(right + 1);
            } else {
                break;
            }
        }
        return keys;
    }

    /**
   * Does one pass over the values in the map, and expands each as a
   * template, using the rest of the values in the same map. Variables
   * in the template are in the form of ${variable}.
   * 
   * @param variablesMap
   *        Map to expand.
   */
    public static void substituteVariables(final Map<String, String> variablesMap) {
        if (variablesMap != null && !variablesMap.isEmpty()) {
            for (final Map.Entry<String, String> entry : variablesMap.entrySet()) {
                variablesMap.put(entry.getKey(), expandTemplate(entry.getValue(), variablesMap));
            }
        }
    }

    /**
   * Copies properties into a map.
   * 
   * @param properties
   *        Properties to copy
   * @return Map of properties and values
   */
    private static Map<String, String> propertiesMap(final Properties properties) {
        final Map<String, String> propertiesMap = new HashMap<String, String>();
        if (properties != null) {
            final Set<Entry<Object, Object>> entries = properties.entrySet();
            for (final Entry<Object, Object> entry : entries) {
                propertiesMap.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return propertiesMap;
    }

    private TemplatingUtility() {
    }
}
