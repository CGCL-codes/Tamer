package com.ufnasoft.dms.server;

import java.util.Date;
import java.util.Locale;
import java.text.*;
import org.apache.log4j.Logger;

public class UniqueFileName {

    Logger logger1 = Logger.getLogger(UniqueFileName.class);

    public String getUniqueName() {
        String name = "";
        Date dt = new Date();
        Locale locale = Locale.US;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssS", locale);
        sdf.format(dt);
        name = sdf.format(dt);
        return name;
    }
}
