package org.databene.commons.converter;

import java.util.Date;

/**
 * Converts {@link Integer} values to {@link Date} objects.<br/><br/>
 * Created: 10.01.2011 11:59:43
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class Int2DateConverter extends ConverterChain<Integer, Date> {

    public Int2DateConverter() {
        super(new NumberToNumberConverter<Integer, Long>(Integer.class, Long.class), new Long2DateConverter());
    }
}
