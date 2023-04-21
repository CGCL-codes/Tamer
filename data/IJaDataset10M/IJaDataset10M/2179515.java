package com.ibm.icu.impl.data;

import java.util.ListResourceBundle;

/**
 * Finnish date format symbols for the Islamic Calendar
 */
public class IslamicCalendarSymbols_fi extends ListResourceBundle {

    private static String copyright = "Copyright © 1998 IBM Corp. All Rights Reserved.";

    static final Object[][] fContents = { { "MonthNames", new String[] { "Muhárram", "Sáfar", "Rabí' al-áwwal", "Rabí' al-ákhir", "Džumada-l-úla", "Džumada-l-ákhira", "Radžab", "Ša'bán", "Ramadán", "Šawwal", "Dhu-l-qada", "Dhu-l-hiddža" } }, { "Eras", new String[] { "AH" } } };

    public synchronized Object[][] getContents() {
        return fContents;
    }
}

;
