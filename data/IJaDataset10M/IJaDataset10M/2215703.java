package de.uni_leipzig.lots.common.util;

import junit.framework.TestCase;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Alexander Kiel
 * @version $Id$
 */
public class DateTest extends TestCase {

    public void test() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.GERMAN);
        String str = dateFormat.format(new Date(0));
        System.out.println(str);
    }
}
