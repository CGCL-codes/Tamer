package com.objectwave.persist.util;

import com.objectwave.utility.ScalarType;
import com.objectwave.utility.ScalarTypeFactory;
import com.objectwave.utility.ScalarTypeGeneratorIF;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * When building a SQLInsert statement, we had a problem where we had no
 * way of knowing if a given field of class java.util.Date should be
 * formatted to be accepted as a DATE or a DATETIME.  Thus this class has
 * been created to be able to check these types.  The persistent object's
 * class should define a given field to be of type DateWithoutTime if it's
 * corresponding database column is of type DATE.  Simply put, use the
 * following mapping:
 *      table column of type DATE      -->  use the DateWithoutType class
 *      table column of type DATETIME  -->  use the Date class
 *
 * @author  jay
 * @version  $Id: DateWithoutTime.java,v 2.1 2002/01/21 22:34:50 dave_hoag Exp $
 */
public class DateWithoutTime implements ScalarType, Serializable {

    private static java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");

    private Date date;

    /**
	 *Constructor for the DateWithoutTime object
	 */
    public DateWithoutTime() {
        date = floor(new Date());
    }

    /**
	 *Constructor for the DateWithoutTime object
	 *
	 * @param  d
	 */
    public DateWithoutTime(long d) {
        date = floor(new Date(d));
    }

    /**
	 *Constructor for the DateWithoutTime object
	 *
	 * @param  d
	 */
    public DateWithoutTime(Date d) {
        date = floor(d);
    }

    /**
	 *Sets the DateFormat attribute of the DateWithoutTime class
	 *
	 * @param  newFormat The new DateFormat value
	 */
    public static void setDateFormat(java.text.SimpleDateFormat newFormat) {
        dateFormat = newFormat;
    }

    /**
	 * @param  date
	 * @return
	 */
    private static Date floor(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(y, m, d);
        return cal.getTime();
    }

    /**
	 * @param  args
	 */
    public static void main(String args[]) {
        System.out.println("dwt: " + new DateWithoutTime());
    }

    /**
	 *Sets the Time attribute of the DateWithoutTime object
	 *
	 * @param  time The new Time value
	 */
    public void setTime(long time) {
        date = floor(new Date(time));
    }

    /**
	 *Gets the Time attribute of the DateWithoutTime object
	 *
	 * @return  The Time value
	 */
    public long getTime() {
        return date.getTime();
    }

    /**
	 * @param  dwt
	 * @return
	 */
    public boolean after(DateWithoutTime dwt) {
        return date.after(dwt.date);
    }

    /**
	 * @param  dwt
	 * @return
	 */
    public boolean before(DateWithoutTime dwt) {
        return date.before(dwt.date);
    }

    /**
	 * @param  o
	 * @return
	 */
    public boolean equals(Object o) {
        return DateWithoutTime.class.isInstance(o) ? date.equals(((DateWithoutTime) o).date) : false;
    }

    /**
	 * This method was created in VisualAge.
	 *
	 * @return  java.lang.String
	 */
    public String toDatabaseString() {
        return toString();
    }

    /**
	 * @return
	 */
    public String toString() {
        return dateFormat.format(date);
    }

    static {
        ScalarTypeFactory.registerGenerator(new ScalarTypeGeneratorIF() {

            /**
				 * @return
				 */
            public Class typeGenerated() {
                return DateWithoutTime.class;
            }

            /**
				 * @param  dbString
				 * @return
				 */
            public ScalarType createInstance(String dbString) {
                Date d = com.objectwave.utility.DateHelper.getDateFrom(dbString);
                return (d == null) ? null : new DateWithoutTime(d);
            }
        });
    }
}
