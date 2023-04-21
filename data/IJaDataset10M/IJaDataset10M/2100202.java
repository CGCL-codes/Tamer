package test;

import java.text.DateFormat;
import java.util.Locale;

/**
 *
 * @author Jean-Pierre Muller
 */
public class LocaleTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Locale list[] = DateFormat.getAvailableLocales();
        for (Locale locale : list) System.out.println(locale.getLanguage() + ":" + locale.getDisplayName());
        System.out.println(Locale.FRENCH);
        System.out.println(Locale.ENGLISH);
        System.out.println(Locale.getDefault());
    }
}
