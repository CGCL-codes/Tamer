package org.melati.template;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import org.melati.MelatiUtil;
import org.melati.poem.Field;

/**
 * A SimpleDateAdaptor is used to format a date field into 
 * a dd/mm/yyyy format for display. 
 *
 * It also adapts the input (given in dd/mm/yyyy or yyyyMMdd)
 * back to a java.sql.Date.
 */
public class SimpleDateAdaptor implements TempletAdaptor {

    public static final SimpleDateAdaptor it = new SimpleDateAdaptor();

    private static SimpleDateFormat dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy");

    private static SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyyMMdd");

    public Object rawFrom(TemplateContext context, String fieldName) {
        String value = MelatiUtil.getFormNulled(context, fieldName);
        if (value == null) return null;
        java.util.Date date = dateFormatter1.parse(value, new ParsePosition(0));
        if (date == null) date = dateFormatter2.parse(value, new ParsePosition(0));
        return new Date(date.getTime());
    }

    public String rendered(Field dateField) {
        return dateField.getRaw() == null ? "" : dateFormatter1.format((java.util.Date) dateField.getRaw());
    }
}
