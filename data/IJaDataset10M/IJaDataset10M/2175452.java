package securus.services;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import securus.entity.Account;
import securus.entity.User;

/**
 * @author e.dorofeev
 */
public final class DateFormatter {

    private static final Map<String, Locale> map = new HashMap<String, Locale>();

    static {
        for (Locale locale : DateFormat.getAvailableLocales()) {
            map.put(locale.getCountry(), locale);
        }
    }

    public static String formatDate(Date date, User user) {
        if (date == null) {
            return null;
        }
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, getLocale(user)).format(date);
    }

    private static Locale getLocale(User user) {
        if (user == null) {
            return Locale.getDefault();
        }
        Account acc = user.getAccount();
        if (acc == null) {
            return Locale.getDefault();
        }
        Locale locale = map.get(acc.getCountry());
        if (locale == null) {
            return Locale.getDefault();
        }
        return locale;
    }
}
