package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read out existing language files 
 * http://forum.java.sun.com/thread.jspa?threadID=556312&tstart=0
 */
public class ResourceBundleLocales {

    private static final String BUNDLE_BASENAME = "messages";

    static Pattern localePattern = Pattern.compile("(^[a-z]{2})(_([A-Z]{2})){0,1}$");

    public static void main(String[] args) {
        File langs = new File(ResourceBundleLocales.class.getClass().getResource("/").getPath() + File.separatorChar + "resources");
        System.out.println(langs);
        ArrayList<Locale> locales = getAvailableLocales(BUNDLE_BASENAME, langs);
        for (Locale locale : locales) {
            System.out.print(locale.getDisplayLanguage() + " ");
            System.out.println(locale.getLanguage());
        }
    }

    public static Locale getLocale(String ident) {
        Locale locale = null;
        Matcher m = localePattern.matcher(ident);
        if (m.find()) {
            locale = (m.group(3) == null) ? new Locale(m.group(1)) : new Locale(m.group(1), m.group(3));
        }
        return locale;
    }

    public static ArrayList<Locale> getAvailableLocales(String bundleName, File dir) {
        Pattern bundleFilePattern = Pattern.compile("^" + bundleName + "_([a-z]{2}(_([A-Z]{2})){0,1}).properties$");
        ArrayList<Locale> locales = new ArrayList<Locale>();
        String[] bundles = dir.list();
        for (int i = 0; i < bundles.length; i++) {
            Matcher m = bundleFilePattern.matcher(bundles[i]);
            if (m.find()) {
                locales.add(getLocale(m.group(1)));
            }
        }
        return locales;
    }
}
