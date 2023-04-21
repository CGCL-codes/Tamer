package com.ivis.xprocess.web.translator;

import java.text.Format;
import java.util.Locale;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.translator.DateTranslator;
import com.ivis.xprocess.util.Day;

/**
 * This day translator is bound by Tapestry for components
 * that require day translating to a formatted String.
 *
 */
public class XDayTranslator extends DateTranslator {

    public XDayTranslator() {
        super();
    }

    public XDayTranslator(String initializer) {
        super(initializer);
    }

    @Override
    protected String formatObject(IFormComponent field, Locale locale, Object obj) {
        if ((obj != null) && obj instanceof Day) {
            Day day = (Day) obj;
            Format format = getFormat(locale);
            return format.format(day.getJavaDate());
        }
        return "";
    }
}
