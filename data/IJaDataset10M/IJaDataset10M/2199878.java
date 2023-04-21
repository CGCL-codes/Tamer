package org.xi8ix.jdbc;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

/**
 * This class contains various methods for generating SimpleParameter objects of the appropriate type.
 *
 * @author Iain Shigeoka
 */
public class Params {

    /**
     * Convenience method to generate a new VARCHAR simple parameter.
     * If the enum is not null, the value will be the result of Enum.ordinal()
     *
     * @param value the value to wrap
     * @return a simple parameter of type VARCHAR
     */
    public static SimpleParameter booleanYesNo(Boolean value) {
        String s = null;
        if (value != null) {
            s = value ? "Y" : "N";
        }
        return new SimpleParameter(s, Types.VARCHAR);
    }

    /**
     * Convenience method to generate a new INTEGER simple parameter.
     * If the enum is not null, the value will be the result of Enum.ordinal()
     *
     * @param value the value to wrap
     * @return a simple parameter of type INTEGER
     */
    public static SimpleParameter booleanZeroOne(Boolean value) {
        Integer i = null;
        if (value != null) {
            i = value ? 1 : 0;
        }
        return new SimpleParameter(i, Types.INTEGER);
    }

    /**
     * Convenience method to generate a new INTEGER simple parameter.
     * If the enum is not null, the value will be the result of Enum.ordinal()
     *
     * @param e the value to wrap
     * @return a simple parameter of type INTEGER
     */
    public static SimpleParameter enumInt(Enum e) {
        Object value = null;
        if (e != null) {
            value = e.ordinal();
        }
        return new SimpleParameter(value, Types.INTEGER);
    }

    /**
     * Convenience method to generate a new VARCHAR simple parameter.
     * If the enum is not null, the value will be the result of Enum.name()
     *
     * @param e the value to wrap
     * @return a simple parameter of type VARCHAR
     */
    public static SimpleParameter enumString(Enum e) {
        Object value = null;
        if (e != null) {
            value = e.name();
        }
        return new SimpleParameter(value, Types.VARCHAR);
    }

    /**
     * Convenience method to generate a new VARCHAR simple parameter
     *
     * @param value the value to wrap
     * @return a simple parameter of type VARCHAR
     */
    public static SimpleParameter string(Object value) {
        if (value != null) {
            value = value.toString();
        }
        return new SimpleParameter(value, Types.VARCHAR);
    }

    /**
     * Convenience method to generate a new INTEGER simple parameter
     *
     * @param value the value to wrap
     * @return a simple parameter of type INTEGER
     */
    public static SimpleParameter integer(Integer value) {
        return new SimpleParameter(value, Types.INTEGER);
    }

    /**
     * Convenience method to generate a new BIGINT simple parameter
     *
     * @param value the value to wrap
     * @return a simple parameter of type BIGINT
     */
    public static SimpleParameter longType(Long value) {
        return new SimpleParameter(value, Types.BIGINT);
    }

    /**
     * Convenience method to generate a new FLOAT simple parameter
     *
     * @param value the value to wrap
     * @return a simple parameter of type FLOAT
     */
    public static SimpleParameter floatType(Float value) {
        return new SimpleParameter(value, Types.FLOAT);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter.
     * Note that a date can be passed in and if it is not an instance
     * of java.sql.Timestamp it will be wrapped in a new timestamp object.
     *
     * @param value the value to wrap
     * @return a simple parameter of type TIMESTAMP
     */
    public static SimpleParameter timestamp(Date value) {
        if (value != null && !(value instanceof Timestamp)) {
            value = new Timestamp(value.getTime());
        }
        return new SimpleParameter(value, Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new DATE simple parameter.
     *
     * @param value the value to wrap
     * @return a simple parameter of type DATE
     */
    public static SimpleParameter date(Date value) {
        return new SimpleParameter(value, Types.DATE);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter now() {
        return new SimpleParameter(new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter yesterday() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DAY_OF_YEAR, false);
        return new SimpleParameter(new Timestamp(cal.getTimeInMillis()), Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DAY_OF_YEAR, true);
        return new SimpleParameter(new Timestamp(cal.getTimeInMillis()), Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter nextWeek() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DAY_OF_YEAR, 7);
        return new SimpleParameter(new Timestamp(cal.getTimeInMillis()), Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter nextYear() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.YEAR, true);
        return new SimpleParameter(new Timestamp(cal.getTimeInMillis()), Types.TIMESTAMP);
    }

    /**
     * Convenience method to generate a new TIMESTAMP simple parameter
     * for the current date/time.
     *
     * @return a simple parameter of type TIMESTAMP set to current date/time.
     */
    public static SimpleParameter lastYear() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.YEAR, false);
        return new SimpleParameter(new Timestamp(cal.getTimeInMillis()), Types.TIMESTAMP);
    }
}
