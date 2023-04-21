package pl.krakow.ae.knp.wikiboard.gui.bundles;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author slawek
 */
public class Lang {

    private static String BUNDLE_PATH = "pl/krakow/ae/knp/wikiboard/gui/bundles/LangBundle";

    private static ResourceBundle text;

    public static String get(String string) {
        if (text == null) {
            text = ResourceBundle.getBundle(BUNDLE_PATH, Locale.getDefault());
        }
        return text.getString(string);
    }
}
