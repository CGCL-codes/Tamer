package org.myjerry.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {

    public static final String END_OF_LINE = System.getProperty("line.separator");

    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isBlank(String string) {
        if (isEmpty(string) || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static String getString(InputStream stream) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(END_OF_LINE);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Long getLong(String value) {
        if (isNotEmpty(value)) {
            try {
                Long l = Long.parseLong(value);
                return l;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static Boolean getBoolean(String value) {
        return getBoolean(value, false);
    }

    public static Boolean getBoolean(String value, boolean defaultValue) {
        if (isNotEmpty(value)) {
            if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value)) {
                return false;
            }
        }
        return defaultValue;
    }
}
