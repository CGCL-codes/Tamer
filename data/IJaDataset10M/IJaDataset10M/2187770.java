package org.noip.milhous;

import java.util.Calendar;
import java.util.Date;
import org.noip.milhous.annotations.foriegnKey;
import org.noip.milhous.annotations.transientVar;

/**
 * Class that maps the {@link foriegnKey} key constrained children
 * 
 * @author millermj
 * 
 * @param <ParentClass>
 */
public class DatabaseRow implements PrimaryKey {

    public static final String delimiter = "-";

    @transientVar
    private Object PrimaryKey;

    @transientVar
    public void setPrimaryKey(Object primaryKey) {
        PrimaryKey = primaryKey;
    }

    public Object getPrimaryKey() {
        return PrimaryKey;
    }

    public DatabaseRow() {
        PrimaryKey = null;
    }

    @SuppressWarnings("rawtypes")
    public DatabaseRow(Class parent) {
        PrimaryKey = null;
    }

    public static String dateToString(Date d) {
        String result = "";
        Calendar day = Calendar.getInstance();
        day.setTime(d);
        try {
            result += (day.get(Calendar.YEAR)) + delimiter;
            String monPad = ((day.get(Calendar.MONTH) + 1) < 10) ? "0" : "";
            result += monPad + (day.get(Calendar.MONTH) + 1) + delimiter;
            String dayPad = ((day.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" : "";
            result += dayPad + (day.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
        }
        return result;
    }

    public static String dateToString(Calendar day) {
        String result = "";
        try {
            result += (day.get(Calendar.YEAR)) + delimiter;
            String monPad = ((day.get(Calendar.MONTH) + 1) < 10) ? "0" : "";
            result += monPad + (day.get(Calendar.MONTH) + 1) + delimiter;
            String dayPad = ((day.get(Calendar.DAY_OF_MONTH) + 1) < 10) ? "0" : "";
            result += dayPad + (day.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
        }
        return result;
    }
}
