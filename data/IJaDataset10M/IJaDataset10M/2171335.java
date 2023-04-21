package org.omegat.tools.align.bundles;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Class that scans Bundle.properties / Bundle_ru.properties pairs
 * and passes found legacy translations to TMXSaver.
 *
 * @author  Maxym Mykhalchuk
 */
public class FileScanner {

    private String filename, locale;

    private TMXSaver saver;

    /** Creates a new instance of FileScanner */
    public FileScanner(String filename, String locale, TMXSaver saver) {
        this.filename = filename;
        this.locale = locale;
        this.saver = saver;
    }

    public void scan() throws IOException {
        String name = filename.substring(0, filename.lastIndexOf('.'));
        String locfilename = name + '_' + locale + ".properties";
        ResourceBundle parent = new PropertyResourceBundle(new FileInputStream(filename));
        ResourceBundle bundle;
        try {
            bundle = new PropertyResourceBundle(new FileInputStream(locfilename));
        } catch (IOException ioe) {
            return;
        }
        Enumeration keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            try {
                String locvalue = bundle.getString(key);
                String srcvalue = parent.getString(key);
                saver.add(srcvalue, locvalue);
            } catch (MissingResourceException mre) {
                mre.printStackTrace();
            }
        }
    }
}
