package org.upm;

import java.util.Date;
import java.text.SimpleDateFormat;

public class SimpleDate extends Date {

    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public SimpleDate(String datestr) throws Exception {
        setTime(format.parse(datestr).getTime());
    }
}
