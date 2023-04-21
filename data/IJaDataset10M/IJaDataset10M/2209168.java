package com.germinus.xpression.cms.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

public class DateConverter implements Converter {

    private Format formatter;

    public DateConverter() {
        super();
    }

    public DateConverter(DateFormat formatter) {
        super();
        this.formatter = formatter;
    }

    @SuppressWarnings("unchecked")
    public Object convert(Class type, Object value) {
        if (formatter == null) return null;
        if (Date.class.isAssignableFrom(type)) {
            if (value instanceof String) {
                String stringValue = (String) value;
                if (StringUtils.isEmpty(stringValue)) {
                    return null;
                }
                try {
                    return ((DateFormat) formatter).parse((String) value);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return value;
    }
}
