package org.datanucleus.store.rdbms.mapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.exceptions.NullValueException;
import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.SingleFieldMapping;
import org.datanucleus.store.rdbms.adapter.RDBMSAdapter;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.TypeConversionHelper;

/**
 * Mapping of a CHAR RDBMS type.
 */
public class CharRDBMSMapping extends RDBMSMapping {

    private static final ThreadLocal<FormatterInfo> formatterThreadInfo = new ThreadLocal<CharRDBMSMapping.FormatterInfo>() {

        protected FormatterInfo initialValue() {
            return new FormatterInfo();
        }
    };

    static class FormatterInfo {

        SimpleDateFormat formatter;
    }

    /**
     * Constructor.
     * @param storeMgr Store Manager
     * @param mapping Java type mapping
     */
    protected CharRDBMSMapping(MappedStoreManager storeMgr, JavaTypeMapping mapping) {
        super(storeMgr, mapping);
    }

    /**
     * Constructor.
     * @param mapping Java type mapping
     * @param storeMgr Store Manager
     * @param field Field to be mapped
     */
    public CharRDBMSMapping(JavaTypeMapping mapping, MappedStoreManager storeMgr, DatastoreField field) {
        super(storeMgr, mapping);
        column = (Column) field;
        initialize();
    }

    /**
     * Method to initialise the column mapping.
     * Provides default length specifications for the CHAR column to fit the data being stored.
     */
    protected void initialize() {
        if (column != null) {
            if (getJavaTypeMapping() instanceof SingleFieldMapping && column.getColumnMetaData().getLength() == null) {
                SingleFieldMapping m = (SingleFieldMapping) getJavaTypeMapping();
                if (m.getDefaultLength(0) > 0) {
                    column.getColumnMetaData().setLength(m.getDefaultLength(0));
                }
            }
            column.getColumnMetaData().setJdbcType("CHAR");
            column.checkString();
            if (getJavaTypeMapping() instanceof SingleFieldMapping) {
                Object[] validValues = ((SingleFieldMapping) getJavaTypeMapping()).getValidValues(0);
                if (validValues != null) {
                    String constraints = getDatabaseAdapter().getCheckConstraintForValues(column.getIdentifier(), validValues, column.isNullable());
                    column.setConstraints(constraints);
                }
            }
            if (getJavaTypeMapping().getJavaType() == Boolean.class) {
                column.getColumnMetaData().setLength(1);
                StringBuffer constraints = new StringBuffer("CHECK (" + column.getIdentifier() + " IN ('Y','N')");
                if (column.isNullable()) {
                    constraints.append(" OR " + column.getIdentifier() + " IS NULL");
                }
                constraints.append(')');
                column.setConstraints(constraints.toString());
            }
            int maxlength = getTypeInfo().getPrecision();
            if (column.getColumnMetaData().getLength().intValue() <= 0 || column.getColumnMetaData().getLength().intValue() > maxlength) {
                if (getTypeInfo().isAllowsPrecisionSpec()) {
                    throw new NucleusUserException("String max length of " + column.getColumnMetaData().getLength() + " is outside the acceptable range [0, " + maxlength + "] for column \"" + column.getIdentifier() + "\"");
                }
            }
        }
        initTypeInfo();
    }

    /**
     * Accessor for whether the mapping is string-based.
     * @return Whether the mapping is string based
     */
    public boolean isStringBased() {
        return true;
    }

    public SQLTypeInfo getTypeInfo() {
        if (column != null && column.getColumnMetaData().getSqlType() != null) {
            return storeMgr.getSQLTypeInfoForJDBCType(Types.CHAR, column.getColumnMetaData().getSqlType());
        }
        return storeMgr.getSQLTypeInfoForJDBCType(Types.CHAR);
    }

    /**
     * Method to set a character at the specified position in the JDBC PreparedStatement.
     * @param ps The PreparedStatement
     * @param param Parameter position
     * @param value The value to set
     */
    public void setChar(Object ps, int param, char value) {
        try {
            if (value == Character.UNASSIGNED && !getDatabaseAdapter().supportsOption(RDBMSAdapter.PERSIST_OF_UNASSIGNED_CHAR)) {
                value = ' ';
                NucleusLogger.DATASTORE.warn(LOCALISER_RDBMS.msg("055008"));
            }
            ((PreparedStatement) ps).setString(param, Character.valueOf(value).toString());
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "char", "" + value, column, e.getMessage()), e);
        }
    }

    /**
     * Method to extract a character from the ResultSet at the specified position
     * @param rs The Result Set
     * @param param The parameter position
     * @return the character
     */
    public char getChar(Object rs, int param) {
        char value;
        try {
            value = ((ResultSet) rs).getString(param).charAt(0);
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "char", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    /**
     * Method to set a String at the specified position in the JDBC PreparedStatement.
     * @param ps The PreparedStatement
     * @param param Parameter position
     * @param value The value to set
     */
    public void setString(Object ps, int param, String value) {
        try {
            if (value == null) {
                if (column != null && column.isDefaultable() && column.getDefaultValue() != null) {
                    ((PreparedStatement) ps).setString(param, column.getDefaultValue().toString().trim());
                } else {
                    ((PreparedStatement) ps).setNull(param, getTypeInfo().getDataType());
                }
            } else if (value.length() == 0) {
                if (storeMgr.getBooleanProperty("datanucleus.rdbms.persistEmptyStringAsNull")) {
                    ((PreparedStatement) ps).setString(param, null);
                } else {
                    if (getDatabaseAdapter().supportsOption(RDBMSAdapter.NULL_EQUALS_EMPTY_STRING)) {
                        value = getDatabaseAdapter().getSurrogateForEmptyStrings();
                    }
                    ((PreparedStatement) ps).setString(param, value);
                }
            } else {
                if (column != null) {
                    Integer colLength = column.getColumnMetaData().getLength();
                    if (colLength != null && colLength.intValue() < value.length()) {
                        String action = storeMgr.getStringProperty("datanucleus.rdbms.stringLengthExceededAction");
                        if (action.equals("EXCEPTION")) {
                            throw new NucleusUserException(LOCALISER_RDBMS.msg("055007", value, column.getIdentifier().toString(), "" + colLength.intValue())).setFatal();
                        } else if (action.equals("TRUNCATE")) {
                            value = value.substring(0, colLength.intValue());
                        }
                    }
                }
                ((PreparedStatement) ps).setString(param, value);
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "String", "" + value, column, e.getMessage()), e);
        }
    }

    /**
     * Method to extract a String from the ResultSet at the specified position
     * @param rs The Result Set
     * @param param The parameter position
     * @return the String
     */
    public String getString(Object rs, int param) {
        try {
            String value = ((ResultSet) rs).getString(param);
            if (value == null) {
                return value;
            } else if (getDatabaseAdapter().supportsOption(RDBMSAdapter.NULL_EQUALS_EMPTY_STRING) && value.equals(getDatabaseAdapter().getSurrogateForEmptyStrings())) {
                return "";
            } else {
                if (column.getJdbcType() == Types.CHAR && getDatabaseAdapter().supportsOption(RDBMSAdapter.CHAR_COLUMNS_PADDED_WITH_SPACES)) {
                    int numPaddingChars = 0;
                    for (int i = value.length() - 1; i >= 0; i--) {
                        if (value.charAt(i) == ' ') {
                            numPaddingChars++;
                        } else {
                            break;
                        }
                    }
                    if (numPaddingChars > 0) {
                        value = value.substring(0, value.length() - numPaddingChars);
                    }
                }
                return value;
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "String", "" + param, column, e.getMessage()), e);
        }
    }

    /**
     * Method to set a boolean at the specified position in the JDBC PreparedStatement.
     * @param ps The PreparedStatement
     * @param param Parameter position
     * @param value The value to set
     */
    public void setBoolean(Object ps, int param, boolean value) {
        try {
            ((PreparedStatement) ps).setString(param, value ? "Y" : "N");
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "boolean", "" + value, column, e.getMessage()), e);
        }
    }

    /**
     * Method to extract a boolean from the ResultSet at the specified position
     * @param rs The Result Set
     * @param param The parameter position
     * @return the boolean
     */
    public boolean getBoolean(Object rs, int param) {
        boolean value;
        try {
            String s = ((ResultSet) rs).getString(param);
            if (s == null) {
                if (column == null || column.getColumnMetaData() == null || !column.getColumnMetaData().isAllowsNull()) {
                    if (((ResultSet) rs).wasNull()) {
                        throw new NullValueException(LOCALISER_RDBMS.msg("055003", column));
                    }
                }
                return false;
            }
            if (s.equals("Y")) {
                value = true;
            } else if (s.equals("N")) {
                value = false;
            } else {
                throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055003", column));
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "boolean", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    /**
     * Method to set an object at the specified position in the JDBC PreparedStatement.
     * @param ps The PreparedStatement
     * @param param Parameter position
     * @param value The value to set
     */
    public void setObject(Object ps, int param, Object value) {
        try {
            if (value == null) {
                ((PreparedStatement) ps).setNull(param, getTypeInfo().getDataType());
            } else {
                if (value instanceof Boolean) {
                    ((PreparedStatement) ps).setString(param, ((Boolean) value).booleanValue() ? "Y" : "N");
                } else if (value instanceof java.sql.Time) {
                    ((PreparedStatement) ps).setString(param, ((java.sql.Time) value).toString());
                } else if (value instanceof java.sql.Date) {
                    ((PreparedStatement) ps).setString(param, ((java.sql.Date) value).toString());
                } else if (value instanceof java.util.Date) {
                    ((PreparedStatement) ps).setString(param, getJavaUtilDateFormat().format((java.util.Date) value));
                } else if (value instanceof java.sql.Timestamp) {
                    Calendar cal = storeMgr.getCalendarForDateTimezone();
                    if (cal != null) {
                        ((PreparedStatement) ps).setTimestamp(param, (Timestamp) value, cal);
                    } else {
                        ((PreparedStatement) ps).setTimestamp(param, (Timestamp) value);
                    }
                } else if (value instanceof String) {
                    ((PreparedStatement) ps).setString(param, ((String) value));
                } else {
                    ((PreparedStatement) ps).setString(param, value.toString());
                }
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "Object", "" + value, column, e.getMessage()), e);
        }
    }

    /**
     * Method to extract an object from the ResultSet at the specified position
     * @param rs The Result Set
     * @param param The parameter position
     * @return the object
     */
    public Object getObject(Object rs, int param) {
        Object value;
        try {
            String s = ((ResultSet) rs).getString(param);
            if (s == null) {
                value = null;
            } else {
                if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_LANG_BOOLEAN)) {
                    if (s.equals("Y")) {
                        value = Boolean.TRUE;
                    } else if (s.equals("N")) {
                        value = Boolean.FALSE;
                    } else {
                        throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055003", column));
                    }
                } else if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_LANG_CHARACTER)) {
                    value = Character.valueOf(s.charAt(0));
                } else if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_SQL_TIME)) {
                    value = java.sql.Time.valueOf(s);
                } else if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_SQL_DATE)) {
                    value = java.sql.Date.valueOf(s);
                } else if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_UTIL_DATE)) {
                    value = getJavaUtilDateFormat().parse(s);
                } else if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_SQL_TIMESTAMP)) {
                    Calendar cal = storeMgr.getCalendarForDateTimezone();
                    value = TypeConversionHelper.stringToTimestamp(s, cal);
                } else {
                    value = s;
                }
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "Object", "" + param, column, e.getMessage()), e);
        } catch (ParseException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "Object", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    /**
     * Get a Format object to handle java.util.Date.
     * If a TimeZone is present, it will be used to format dates to that zone.
     * @return Date formatter to use
     */
    public SimpleDateFormat getJavaUtilDateFormat() {
        FormatterInfo formatInfo = formatterThreadInfo.get();
        if (formatInfo.formatter == null) {
            Calendar cal = storeMgr.getCalendarForDateTimezone();
            formatInfo.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            if (cal != null) {
                formatInfo.formatter.setTimeZone(cal.getTimeZone());
            }
        }
        return formatInfo.formatter;
    }
}
