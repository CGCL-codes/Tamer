package net.entropysoft.transmorph.modifiers;

import java.util.Locale;
import net.entropysoft.transmorph.ConversionContext;

/**
 * Modifier that calls toLowerCase on String
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class LowerCaseString implements IModifier<String> {

    private Locale locale;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String modify(ConversionContext context, String object) throws ModifierException {
        if (object == null) {
            return null;
        }
        if (locale == null) {
            return object.toLowerCase();
        } else {
            return object.toLowerCase(locale);
        }
    }
}
