package org.jdbcdslog;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RDBMS specifics for the MySql DB.
 *
 * @author ShunLi
 */
public class MySqlRdbmsSpecifics implements RdbmsSpecifics {

    public MySqlRdbmsSpecifics() {
        super();
    }

    public String formatParameter(Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof String) {
            String text = LogUtils.replaceEach((String) object, new String[] { "\\", "$", "'", "\"", "\r", "\n", "\t" }, new String[] { "\\\\\\\\", "\\$", "\\\\'", "\\\\\"", "\\\\r", "\\\\n", "\\\\t" });
            return "'" + text + "'";
        } else if (object instanceof Timestamp) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
        } else if (object instanceof Time) {
            return "'" + new SimpleDateFormat("HH:mm:ss").format(object) + "'";
        } else if (object instanceof Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd").format(object) + "'";
        } else if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue() ? "'1'" : "'0'";
        } else {
            return object.toString();
        }
    }
}
