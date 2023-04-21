package net.sf.zcatalog;

import java.io.File;
import java.util.*;
import net.sf.zcatalog.xml.jaxb.Preferences;

/**
 * Global stuff.
 * You don't like global stuff?
 * Oh, come on, get a life!
 * @author Alessandro Zigliani
 * @version 0.9
 * @since ZCatalog 0.9
 */
public final class Global {

    public static final String HOME_SITE = "http://zcatalog.sf.net";

    private static final Vector<LocaleObserver> localeObservers = new Vector<LocaleObserver>();

    public static Preferences PREFERENCES;

    /**
     * Adds a locale observer to a static list that mantains all the
     * references to locale-sensible objects.
     * @param lo the locale observer.
     */
    public static void addLocaleObserver(LocaleObserver lo) {
        localeObservers.add(lo);
    }

    /** Sets the application's locale. Do not use static
     *  methods from Locale class. Use this.
     *  @param l the new locale
     */
    public static void setLocale(Locale l) {
        Iterator<LocaleObserver> i = localeObservers.iterator();
        Locale.setDefault(l);
        while (i.hasNext()) {
            i.next().localeUpdated(l);
        }
    }

    public static final File TEST_DIR;

    public static final File PRJ_DIR;

    static {
        File prj = new File(String.format("..%c..", File.separatorChar)), usrh = new File(System.getProperty("user.home"));
        try {
            prj = prj.getCanonicalFile();
            usrh = usrh.getCanonicalFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PRJ_DIR = prj;
        TEST_DIR = new File(prj.getPath() + File.separatorChar + "test_cases");
        USR_HOME = usrh;
        HOME = new File(usrh.getPath() + File.separatorChar + ".zcatalog");
        if (!Global.HOME.exists() && !Global.HOME.mkdir()) {
            throw new UnsupportedOperationException("NON POSSO CREARE LA DIR");
        }
    }

    public static File USR_HOME;

    public static File HOME;
}
