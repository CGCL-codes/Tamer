package de.peathal.resource;

import de.peathal.util.ILanguageHelper;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class L=language reduces code for i18n.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class DefaultLanguageHelper implements ILanguageHelper {

    private String pkg;

    private ResourceBundle bundle;

    private NumberFormat nf;

    public DefaultLanguageHelper(String pkg) {
        this.pkg = pkg;
        bundle = ResourceBundle.getBundle(pkg);
    }

    public DefaultLanguageHelper() {
        pkg = "de/peathal/resource/peathal";
        bundle = ResourceBundle.getBundle(pkg);
    }

    @Override
    public String translate(String key) {
        try {
            return getBundle().getString(key);
        } catch (Exception exc) {
            return key + "[*]";
        }
    }

    @Override
    public synchronized ResourceBundle getBundle() {
        return bundle;
    }

    public synchronized void reloadBundle() {
        bundle = ResourceBundle.getBundle(pkg);
    }

    @Override
    public synchronized void setLocale(Locale locale) {
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle(pkg);
        nf = null;
    }

    @Override
    public synchronized NumberFormat getNumberFormat() {
        if (nf == null) {
            nf = NumberFormat.getInstance(Locale.getDefault());
            nf.setMaximumFractionDigits(5);
        }
        return nf;
    }
}
