package org.iqual.chaplin.intro.lesson12;

import org.iqual.chaplin.FromContext;
import java.util.*;

/**
 * @author Zbynek Slajchrt
 * @since Jul 30, 2009 6:46:08 PM
 */
public class Dictionary {

    @FromContext
    private Locale locale;

    public boolean isInDictionary(String word) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("org.iqual.chaplin.intro.lesson12.dictionary", locale);
            return bundle.getString(word) != null;
        } catch (MissingResourceException e) {
            return false;
        }
    }
}
