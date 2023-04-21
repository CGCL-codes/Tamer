package org.atlantal.impl.cms.field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.atlantal.api.app.db.Query;
import org.atlantal.api.app.db.QueryException;
import org.atlantal.api.app.db.QueryResult;
import org.atlantal.api.app.db.QueryWhere;
import org.atlantal.api.cms.data.MapContentData;
import org.atlantal.api.cms.field.Field;
import org.atlantal.api.cms.field.FieldType;
import org.atlantal.api.cms.util.ContentAccessMode;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public class FieldDate extends AbstractFieldTypeInstance {

    /** MINUTES_IN_DAY */
    public static final int MINUTES_IN_DAY = 1440;

    private static FieldType singleton = new FieldDate();

    /**
     * Constructor
     */
    protected FieldDate() {
    }

    /**
     * @return itemtype
     */
    public static FieldType getInstance() {
        return singleton;
    }

    /**
     * {@inheritDoc}
     */
    public String toString(Field field, MapContentData values) {
        Date date = (Date) values.get(field.getValueAlias());
        return (date == null) ? "" : "Le " + toStringDate(date);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasValue(Field field, MapContentData values) {
        return (values.get(field.getValueAlias()) != null);
    }

    /**
     * {@inheritDoc}
     */
    public boolean sameValue(Field field, MapContentData values1, MapContentData values2) {
        String fullaliasSource = field.getValueAlias();
        Object value1 = values1.get(fullaliasSource);
        Object value2 = values2.get(fullaliasSource);
        if (value1 == null) {
            return (value2 == null);
        } else {
            if (value2 == null) {
                return false;
            } else {
                return value1.equals(value2);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void querySelect(Field field, Query query, String source, String fieldname, String fieldalias, ContentAccessMode cam) {
        query.addSelect(source, fieldname, fieldalias, true);
    }

    /**
     * {@inheritDoc}
     */
    public void queryOrderBy(Field field, Query query, String srcAlias, String fieldalias, String order) {
        query.addOrderBy(srcAlias, fieldalias + " " + order.toUpperCase(Locale.getDefault()));
    }

    /**
     * {@inheritDoc}
     */
    public QueryWhere queryWhere(Field field, Query query, String srcAlias, String fieldname, MapContentData values, ContentAccessMode mode) {
        QueryWhere qw = null;
        String fullalias = field.getValueAlias();
        Object value = values.get(fullalias);
        if (value != null) {
            String operator = (String) values.get(fullalias + "_operator");
            if ((operator == null) || (operator.length() == 0)) {
                operator = "=";
            }
            StringBuilder where = new StringBuilder();
            where.append(fieldname).append(" ").append(operator).append(" ");
            if (value.getClass().getName().equals("java.lang.String")) {
                where.append(value);
            } else {
                where.append("'").append(toString((Date) value)).append("'");
            }
            qw = new QueryWhere(srcAlias, where.toString());
        }
        return qw;
    }

    /**
     * {@inheritDoc}
     */
    public void queryValues(Field field, QueryResult result, MapContentData values, String fieldalias) throws QueryException {
        String dateStr = result.getString(fieldalias);
        if (dateStr != null) {
            String[] dateTab = dateStr.split("-");
            int year = Integer.valueOf(dateTab[0]).intValue();
            int month = Integer.valueOf(dateTab[1]).intValue() - 1;
            int day = Integer.valueOf(dateTab[2]).intValue();
            GregorianCalendar gc = new GregorianCalendar(year, month, day);
            values.put(field.getValueAlias(), gc.getTime());
        } else {
            values.put(field.getValueAlias(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String sqlInsertFields(Field field, String fieldname) {
        return fieldname;
    }

    /**
     * {@inheritDoc}
     */
    public String sqlInsertValues(Field field, MapContentData values) {
        Date value = (Date) values.get(field.getValueAlias());
        String sql;
        if (value == null) {
            sql = "NULL";
        } else {
            sql = "'" + toString(value) + "'";
        }
        return sql;
    }

    /**
     * {@inheritDoc}
     */
    public String sqlUpdate(Field field, String fieldname, MapContentData values) {
        StringBuilder sql = new StringBuilder();
        sql.append(fieldname).append(" = ");
        Date value = (Date) values.get(field.getValueAlias());
        if (value == null) {
            sql.append("NULL");
        } else {
            sql.append("'").append(toString(value)).append("'");
        }
        return sql.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String sqlCreateFields(Field field, String fieldname) {
        return "`" + fieldname + "` date default NULL";
    }

    /**
     * {@inheritDoc}
     */
    public String sqlCreateIndexes(Field field, String fieldname) {
        String name = "`" + fieldname + "`";
        return "KEY " + name + " (" + name + ")";
    }

    /**
     * @param date date
     * @return string
     */
    private String toString(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int day = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int month = gc.get(GregorianCalendar.MONTH) + 1;
        int year = gc.get(GregorianCalendar.YEAR);
        return year + "-" + month + "-" + day;
    }

    /**
     * @param date date
     * @return string
     */
    protected static String toStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * @param gc gc
     * @return string
     */
    protected static String toStringTime(GregorianCalendar gc) {
        int hour = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = gc.get(GregorianCalendar.MINUTE);
        String hourstr = ((hour < TEN) ? "0" : "") + hour;
        String minstr = ((minute < TEN) ? "0" : "") + minute;
        return hourstr + "h" + minstr;
    }

    /**
     * @param gc gc
     * @return string
     */
    protected static String toStringDateTime(GregorianCalendar gc) {
        int year = gc.get(GregorianCalendar.YEAR);
        int day = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int month = gc.get(GregorianCalendar.MONTH) + 1;
        String daystr = ((day < TEN) ? "0" : "") + day;
        String monthstr = ((month < TEN) ? "0" : "") + month;
        int hour = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = gc.get(GregorianCalendar.MINUTE);
        String hourstr = ((hour < TEN) ? "0" : "") + hour;
        String minstr = ((minute < TEN) ? "0" : "") + minute;
        return daystr + "/" + monthstr + "/" + year + " � " + hourstr + ":" + minstr;
    }

    /**
     * @param gc1 gc1
     * @param gc2 gc2
     * @return boolean
     */
    protected static boolean sameDay(GregorianCalendar gc1, GregorianCalendar gc2) {
        int year1 = gc1.get(GregorianCalendar.YEAR);
        int day1 = gc1.get(GregorianCalendar.DAY_OF_MONTH);
        int month1 = gc1.get(GregorianCalendar.MONTH) + 1;
        int year2 = gc2.get(GregorianCalendar.YEAR);
        int day2 = gc2.get(GregorianCalendar.DAY_OF_MONTH);
        int month2 = gc2.get(GregorianCalendar.MONTH) + 1;
        return ((year1 == year2) && (month1 == month2) && (day1 == day2));
    }

    /**
     * @param gc1 gc1
     * @param gc2 gc2
     * @return boolean
     */
    protected static boolean sameTime(GregorianCalendar gc1, GregorianCalendar gc2) {
        int hour1 = gc1.get(GregorianCalendar.HOUR_OF_DAY);
        int min1 = gc1.get(GregorianCalendar.MINUTE);
        int hour2 = gc2.get(GregorianCalendar.HOUR_OF_DAY);
        int min2 = gc2.get(GregorianCalendar.MINUTE);
        return ((hour1 == hour2) && (min1 == min2));
    }

    /**
     * @param gc gc
     * @return boolean
     */
    protected static boolean isMidnight(GregorianCalendar gc) {
        int hour = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int min = gc.get(GregorianCalendar.MINUTE);
        return ((hour + min) == 0);
    }

    /**
     * @param date date
     * @return string
     */
    protected static String toSQLString(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        int year = gc.get(GregorianCalendar.YEAR);
        int day = gc.get(GregorianCalendar.DAY_OF_MONTH);
        int month = gc.get(GregorianCalendar.MONTH) + 1;
        String daystr = ((day < TEN) ? "0" : "") + day;
        String monthstr = ((month < TEN) ? "0" : "") + month;
        int hour = gc.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = gc.get(GregorianCalendar.MINUTE);
        String hourstr = ((hour < TEN) ? "0" : "") + hour;
        String minstr = ((minute < TEN) ? "0" : "") + minute;
        return year + "-" + monthstr + "-" + daystr + " " + hourstr + ":" + minstr;
    }
}
