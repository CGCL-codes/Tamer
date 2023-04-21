package org.mandarax.sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

/**
 * A default implementation of a type mapping.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.5
 */
public class DefaultTypeMapping implements SQLTypeMapping {

    /**
     * Constructor.
     */
    public DefaultTypeMapping() {
        super();
    }

    /**
     * Get the java type for the respective sql type (as defined in @see java.sql.Types).
     * The column index is ignored.
     * @return a class
     * @param sqlType an SQL type
     * @param colIndex the column index
     */
    public Class getType(int sqlType, int colIndex) {
        if (sqlType == Types.CHAR) {
            return String.class;
        }
        if (sqlType == Types.VARCHAR) {
            return String.class;
        }
        if (sqlType == Types.LONGVARCHAR) {
            return String.class;
        }
        if (sqlType == Types.NUMERIC) {
            return BigDecimal.class;
        }
        if (sqlType == Types.DECIMAL) {
            return BigDecimal.class;
        }
        if (sqlType == Types.BIT) {
            return Boolean.class;
        }
        if (sqlType == Types.TINYINT) {
            return Integer.class;
        }
        if (sqlType == Types.SMALLINT) {
            return Integer.class;
        }
        if (sqlType == Types.INTEGER) {
            return Integer.class;
        }
        if (sqlType == Types.BIGINT) {
            return Long.class;
        }
        if (sqlType == Types.REAL) {
            return Float.class;
        }
        if (sqlType == Types.FLOAT) {
            return Float.class;
        }
        if (sqlType == Types.DOUBLE) {
            return Double.class;
        }
        if (sqlType == Types.DATE) {
            return Date.class;
        }
        if (sqlType == Types.TIME) {
            return Time.class;
        }
        if (sqlType == Types.TIMESTAMP) {
            return Timestamp.class;
        }
        throw new IllegalArgumentException("The mappoing of the following type is not (yet) supported");
    }

    /**
     * Return a type mapping. Just return null, there is no special mapping.
     * @return a map
     */
    public Map getTypeMapping() {
        return null;
    }

    /**
     * Post map an object. Nothing happend here.
     * @return an object
     * @param obj the object to be converted
     */
    public Object postMap(Object obj) {
        return obj;
    }

    /**
     * Compare objects.
     * @param obj another object
     * @return a boolean
     */
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof DefaultTypeMapping);
    }

    /**
     * Get the hash code of the object.
     * @return te hash code
     */
    public int hashCode() {
        return getClass().hashCode();
    }
}
