package de.internnetz.eaf.core.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import de.internnetz.eaf.i18n.LanguageEnum;

public class LanguagePreferences {

    private static final String FILENAME = "lang.cfg";

    private static final String LANG_KEY = "lang";

    private static final Properties p = new Properties();

    private static LanguageEnum language;

    static {
        File tmp = new File(FILENAME);
        try {
            if (tmp.exists() && tmp.canRead()) {
                FileInputStream propFile = new FileInputStream(tmp);
                p.load(propFile);
            }
        } catch (Exception e) {
            ;
        }
        language = LanguageEnum.parseFromString(p.getProperty(LANG_KEY, LanguageEnum.ENGLISH_GB.locale().toString()));
        if (language == null) {
            language = LanguageEnum.ENGLISH_GB;
        }
    }

    public static LanguageEnum getLanguage() {
        return language;
    }

    public static void setLanguage(LanguageEnum lang) {
        p.setProperty(LANG_KEY, lang.locale().toString());
    }

    protected static void save() {
        try {
            File outfile = new File(FILENAME);
            if (!outfile.exists()) {
                outfile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(outfile);
            p.store(out, "EAF Language Preferences");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
