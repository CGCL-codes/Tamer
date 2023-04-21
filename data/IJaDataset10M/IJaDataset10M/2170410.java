package jphotoshop.util;

import java.util.*;

public class LocaleUtil {

    private static Locale defaultLocale;

    public static Locale getDefault() {
        return (defaultLocale == null) ? Locale.getDefault() : defaultLocale;
    }

    public static void setDefault(Locale newValue) {
        defaultLocale = newValue;
    }

    /** Creates a new instance. */
    public LocaleUtil() {
    }
}
