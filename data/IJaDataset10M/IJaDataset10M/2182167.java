package com.riversoforion.orinoco.core.convert;

import java.util.Date;
import com.riversoforion.orinoco.core.ValueConverter;

public abstract class DateTimeConverter extends DateTimeConverterBase implements ValueConverter<Date> {

    @Override
    public Date convert(String value) {
        return stringToDate(value);
    }

    @Override
    public String unconvert(Date value) {
        return dateToString(value);
    }
}
