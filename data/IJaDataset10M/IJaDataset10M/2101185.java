package org.xisemele.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.xisemele.api.Formatter;
import org.xisemele.exception.FormatterException;

/**
 * Implementação de {@link Formatter} para <code>java.util.Date</code>.
 * 
 * @author Carlos Eduardo Coral.
 */
class DateFormatter implements Formatter {

    /**
    * Serial version.
    */
    private static final long serialVersionUID = 1L;

    /**
    * instância de <code>java.text.SimpleDateFormat</code> que será usada
    * para formatar as instâncias de <code>java.util.Date</code>.
    */
    private final SimpleDateFormat sdf;

    /**
    * Cria uma instância de {@link DateFormatter}.
    * 
    * @param pattern
    *       <code>java.lang.String</code> contendo o padrão que será aplicado 
    *       na formatação.
    */
    DateFormatter(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }

    /**
    * {@inheritDoc}
    */
    public String format(Object value) {
        return sdf.format((Date) value);
    }

    /**
    * {@inheritDoc}
    */
    public Object parse(String text) {
        try {
            return sdf.parse(text);
        } catch (ParseException e) {
            throw new FormatterException(Date.class, text, e);
        }
    }

    /**
    * {@inheritDoc}
    */
    public Class<?> type() {
        return Date.class;
    }
}
