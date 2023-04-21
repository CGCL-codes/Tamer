package com.ibm.icu.impl.data;

import java.util.ListResourceBundle;

/**
 * Default Date Format symbols for the Islamic Calendar
 */
public class IslamicCalendarSymbols_ar extends ListResourceBundle {

    private static String copyright = "Copyright © 1999 IBM Corp. All Rights Reserved.";

    static final Object[][] fContents = { { "MonthNames", new String[] { "محرم", "صفر", "ربيع الأول", "ربيع الآخر", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة" } }, { "Eras", new String[] { "ه‍" } } };

    public synchronized Object[][] getContents() {
        return fContents;
    }
}

;
