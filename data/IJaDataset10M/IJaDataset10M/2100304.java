package com.meidusa.amoeba.sqljep.function;

import java.util.*;
import java.text.*;

public final class OracleDateFormat extends OracleTimestampFormat {

    private static final long serialVersionUID = 1L;

    protected OracleDateFormat(ArrayList<Object> format, Calendar calendar, DateFormatSymbols dateSymb) {
        this.format = format;
        this.cal = calendar;
        this.symb = dateSymb;
    }

    public OracleDateFormat(String pattern) throws java.text.ParseException {
        cal = Calendar.getInstance();
        symb = new DateFormatSymbols();
        format = formatsCache.get(pattern);
        if (format == null) {
            format = new ArrayList<Object>();
            compilePattern(format, dateSymbols, pattern);
            formatsCache.put(pattern, format);
        }
    }

    public OracleDateFormat(String pattern, Calendar calendar, DateFormatSymbols dateSymb) throws java.text.ParseException {
        cal = calendar;
        symb = dateSymb;
        format = formatsCache.get(pattern);
        if (format == null) {
            format = new ArrayList<Object>();
            compilePattern(format, dateSymbols, pattern);
            formatsCache.put(pattern, format);
        }
    }

    public Object parseObject(String source, ParsePosition pos) {
        return new java.sql.Date(parseInMillis(source, pos));
    }
}
