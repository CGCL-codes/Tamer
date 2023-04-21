package org.jbfilter.test.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marcus Adrian
 *
 */
public class Utils {

    /**
	 * Create a date.
	 * @param ddMMyyyy the string representation (dd/MM/yyyy) of the date to create.
	 * @return the corresponding date
	 */
    public static Date createDate(String ddMMyyyy) {
        if (ddMMyyyy == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(ddMMyyyy);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Create a date.
	 * @param ddMMyyyyHHmmss the string representation (dd/MM/yyyy HH:mm:ss) of the date to create.
	 * @return the corresponding date
	 */
    public static Date createDateTime(String ddMMyyyyHHmmss) {
        if (ddMMyyyyHHmmss == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return sdf.parse(ddMMyyyyHHmmss);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
