package org.jcvi.glk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * @author jsitz
 * @author dkatzel
 */
public class ISODate {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String forDate(Date d) {
        synchronized (ISODate.formatter) {
            return formatter.format(d);
        }
    }
}
