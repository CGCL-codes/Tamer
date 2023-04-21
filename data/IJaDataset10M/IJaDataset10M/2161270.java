package com.ibm.icu.impl.data;

import java.util.ListResourceBundle;

/**
 * French date format symbols for the Hebrew Calendar.
 * This data actually applies to French Canadian.  If we receive
 * official French data from our France office, we should move the 
 * French Canadian data (if it's different) down into _fr_CA
 */
public class HebrewCalendarSymbols_fr extends ListResourceBundle {

    private static String copyright = "Copyright © 1998 IBM Corp. All Rights Reserved.";

    static final Object[][] fContents = { { "MonthNames", new String[] { "Tisseri", "Hesvan", "Kislev", "Tébeth", "Schébat", "Adar", "Adar II", "Nissan", "Iyar", "Sivan", "Tamouz", "Ab", "Elloul" } } };

    public synchronized Object[][] getContents() {
        return fContents;
    }
}

;
