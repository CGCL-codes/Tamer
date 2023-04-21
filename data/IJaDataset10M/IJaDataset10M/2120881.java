package nickyb.sqleonardo.common.util;

import java.util.*;
import java.util.jar.*;
import java.net.*;
import java.io.*;

public class I18n {

    private static String localPackageName = "nickyb/sqleonardo/common/locale/";

    private static String baseName = "sqleonardo";

    private static java.util.ResourceBundle oLanguage = null;

    public static java.util.Vector languageChangedListeners = null;

    static {
        languageChangedListeners = new Vector();
    }

    public static void addOnLanguageChangedListener(LanguageChangedListener listener) {
        languageChangedListeners.add(listener);
    }

    /**
     * Look in the classpath for locale resource bundles.
     * Return a list of Locale objects.
     *
     */
    public static java.util.List getListOfAvailLanguages() {
        java.util.List supportedLocales = new java.util.ArrayList();
        try {
            Set names = getResoucesInPackage(getLocalPackageName());
            Iterator it = names.iterator();
            while (it.hasNext()) {
                String n = (String) it.next();
                String lang = n.substring(n.lastIndexOf('/') + 1);
                if (lang.indexOf(".properties") < 0) {
                    continue;
                }
                lang = lang.substring(0, lang.indexOf(".properties"));
                StringTokenizer tokenizer = new StringTokenizer(lang, "_");
                if (tokenizer.countTokens() <= 1) {
                    continue;
                }
                String language = "";
                String country = "";
                String variant = "";
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    switch(i) {
                        case 0:
                            break;
                        case 1:
                            language = token;
                            break;
                        case 2:
                            country = token;
                            break;
                        case 3:
                            variant = token;
                            break;
                        default:
                    }
                    i++;
                }
                Locale model = new Locale(language, country, variant);
                supportedLocales.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(supportedLocales, new Comparator() {

            public int compare(Object lhs, Object rhs) {
                String ls = ((Locale) lhs).getDisplayLanguage();
                String rs = ((Locale) rhs).getDisplayLanguage();
                return ls.compareTo(rs);
            }
        });
        return supportedLocales;
    }

    private static Locale currentLocale = Locale.ENGLISH;

    public static void setCurrentLocale(String language) {
        setCurrentLocale(language, null);
    }

    public static void setCurrentLocale(String language, String country) {
        if (language != null && !language.equals("")) {
            if (country != null && !country.equals("")) {
                setCurrentLocale(new java.util.Locale(language, country));
            } else {
                setCurrentLocale(new java.util.Locale(language));
            }
        } else {
            setCurrentLocale(java.util.Locale.getDefault());
        }
    }

    public static void setCurrentLocale(Locale locale) {
        currentLocale = locale;
        oLanguage = null;
        Enumeration enum_listeners = languageChangedListeners.elements();
        while (enum_listeners.hasMoreElements()) {
            try {
                ((LanguageChangedListener) (enum_listeners.nextElement())).languageChanged(new LanguageChangedEvent(locale));
            } catch (Exception ex) {
            }
        }
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Retreive a resource string using the current locale.
     * @param cID The resouce sting identifier
     * @return The locale specific string
     */
    public static String getString(String cID) {
        return getString(cID, currentLocale);
    }

    public static String getString(String cID, String defaultValue) {
        return getString(cID, currentLocale, defaultValue);
    }

    /**
     * Retreive a resource string using the current locale.
     * @param cID The resouce sting identifier
     * @return The locale specific string
     */
    public static String getFormattedString(String cID, String defaultValue, Object[] args) {
        String pattern = getString(cID, getCurrentLocale(), defaultValue);
        java.text.MessageFormat mf = new java.text.MessageFormat(pattern, I18n.getCurrentLocale());
        return mf.format(args);
    }

    private static String getString(String cID, Locale currentLocale) {
        if (currentLocale == null) {
            currentLocale = Locale.getDefault();
        }
        if (oLanguage == null) {
            oLanguage = java.util.ResourceBundle.getBundle(getLocalPackageName() + getBaseName(), currentLocale);
        }
        try {
            return oLanguage.getString(cID);
        } catch (Exception ex) {
            return cID;
        }
    }

    public static String getString(String cID, Locale currentLocale, String defaultValue) {
        try {
            if (oLanguage == null) {
                oLanguage = java.util.ResourceBundle.getBundle(getLocalPackageName() + getBaseName(), currentLocale);
            }
            return oLanguage.getString(cID);
        } catch (MissingResourceException ex) {
            System.out.println("Can't find the translation for key = " + cID + ": using default (" + defaultValue + ")");
        } catch (Exception ex) {
            System.out.println("Exception loading cID = " + cID + ": " + ex.getMessage());
        }
        return defaultValue;
    }

    /**
         * Enumerates the resouces in a give package name.
         * This works even if the resources are loaded from a jar file!
         *
         * Adapted from code by mikewse
         * on the java.sun.com message boards.
         * http://forum.java.sun.com/thread.jsp?forum=22&thread=30984
         *
         * @param packageName The package to enumerate
         * @return A Set of Strings for each resouce in the package.
         */
    public static Set getResoucesInPackage(String packageName) throws IOException {
        String localPackageName;
        if (packageName.endsWith("/")) {
            localPackageName = packageName;
        } else {
            localPackageName = packageName + '/';
        }
        ClassLoader cl = I18n.class.getClassLoader();
        Enumeration dirEnum = cl.getResources(localPackageName);
        Set names = new HashSet();
        while (dirEnum.hasMoreElements()) {
            URL resUrl = (URL) dirEnum.nextElement();
            if (resUrl.getProtocol().equals("file")) {
                try {
                    File dir = new File(resUrl.getFile());
                    File[] files = dir.listFiles();
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            File file = files[i];
                            if (file.isDirectory()) continue;
                            names.add(localPackageName + file.getName());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (resUrl.getProtocol().equals("jar")) {
                JarURLConnection jconn = (JarURLConnection) resUrl.openConnection();
                JarFile jfile = jconn.getJarFile();
                Enumeration entryEnum = jfile.entries();
                while (entryEnum.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entryEnum.nextElement();
                    String entryName = entry.getName();
                    if (entryName.equals(localPackageName)) continue;
                    String parentDirName = entryName.substring(0, entryName.lastIndexOf('/') + 1);
                    if (!parentDirName.equals(localPackageName)) continue;
                    names.add(entryName);
                }
            } else {
            }
        }
        return names;
    }

    public static String getLocalPackageName() {
        return localPackageName;
    }

    public static void setLocalPackageName(String aLocalPackageName) {
        localPackageName = aLocalPackageName;
    }

    public static String getBaseName() {
        return baseName;
    }

    public static void setBaseName(String aBaseName) {
        baseName = aBaseName;
    }
}
