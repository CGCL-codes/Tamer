package org.gochacha.test;

import junit.framework.TestCase;
import org.gochacha.impl.DateConverter;
import org.gochacha.ObjectBoxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: dmb
 * Date: Jun 7, 2007
 * Time: 6:46:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateConverterTest extends TestCase {

    public void testBadDate() {
        String badDate = "badDate";
        DateConverter dateConverter = new DateConverter();
        try {
            dateConverter.convertFromString(badDate);
        } catch (ObjectBoxException e) {
            return;
        }
        fail("DateConverter failed to throw an exception when presented with a malformed date string");
    }

    public void testGetSetDateFormat() {
        DateFormat df1 = new SimpleDateFormat();
        DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
        DateConverter dateConverter = new DateConverter(df1);
        assertEquals(df1, dateConverter.getDateFormat());
        dateConverter.setDateFormat(df2);
        assertEquals(df2, dateConverter.getDateFormat());
    }
}
