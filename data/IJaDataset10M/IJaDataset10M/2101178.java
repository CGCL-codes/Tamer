package com.ibm.icu.impl.data;

import java.util.ListResourceBundle;

/**
 * Hungarian date format symbols for the Hebrew Calendar
 */
public class HebrewCalendarSymbols_hu extends ListResourceBundle {

    private static String copyright = "Copyright © 1998 IBM Corp. All Rights Reserved.";

    static final Object[][] fContents = { { "MonthNames", new String[] { "Tisri", "Hesván", "Kiszlév", "Tévész", "Svát", "Ádár risón", "Ádár séni", "Niszán", "Ijár", "Sziván", "Tamuz", "Áv", "Elul" } }, { "Eras", new String[] { "TÉ" } } };

    public synchronized Object[][] getContents() {
        return fContents;
    }
}

;
