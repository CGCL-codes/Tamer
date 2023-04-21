package com.cs.util.db.ioc;

import com.cs.util.db.ResultSetMetaInfo;
import com.cs.util.db.ioc.IOCExceptionState;
import com.cs.util.db.ioc.IOCException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MySqlDecimal is an IO controller for handling input and output of MySql DECIMAL columns.
 * @author dimitris@jmike.gr
 */
public class MySqlDecimal extends GenericIOController implements IOController {

    public MySqlDecimal(ResultSetMetaInfo rsmi, int columnIndex) throws SQLException {
        super(rsmi, columnIndex);
    }

    public MySqlDecimal(ResultSetMetaInfo rsmi, String table, String column) throws SQLException {
        super(rsmi, table, column);
    }

    /**
     * Returns the value of the controller's column at the specified row as a String.
     * @param rs the resultset containing the current row position and the field's value.
     * @return
     */
    @Override
    public String getValue(ResultSet rs) {
        try {
            final BigDecimal x = rs.getBigDecimal(columnIndex);
            if (x != null) {
                return x.toPlainString();
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    /**
     * Set the value of the designated parameter in the specified PreparedStatement.
     * @param ps an object that represents a precompiled SQL statement.
     * @param parameterIndex the first parameter in the PreparedStatement is 1, the second is 2, ...
     * @param value the value of the parameter.
     * @throws java.sql.SQLException
     * @throws IOCException
     */
    @Override
    public void setParameterValue(PreparedStatement ps, int parameterIndex, String value) throws SQLException, IOCException {
        final String x = validate(value);
        if (x == null) {
            ps.setNull(parameterIndex, Types.DECIMAL);
        } else {
            ps.setBigDecimal(parameterIndex, new BigDecimal(x));
        }
    }

    /**
     * Returns the value of the controller's column as a URL encoded String.
     * @param rs the resultset containing the current row position and the field's value.
     * @return
     */
    @Override
    public String getURLEncodedValue(ResultSet rs) {
        try {
            return URLEncoder.encode(this.getValue(rs), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * Returns the value of the controller's column at the specified row as a String.
     * @param rs the resultset containing the current row position and the field's value.
     * @return
     */
    @Override
    public String getXhtmlTableData(ResultSet rs) {
        return this.getValue(rs);
    }

    /**
     * Returns a Xhtml form element for the controller's column.
     * @param rs the resultset containing the current row position and the field's value.
     * @param isEmpty a boolean flag that indicates whether this form element contains data.
     * @return
     */
    @Override
    public String getXhtmlFormElement(ResultSet rs, boolean isEmpty) {
        StringBuilder s = new StringBuilder();
        s.append("<input type=\"text\"");
        final String id = this.getId();
        s.append(" id=\"" + id + "\"");
        s.append(" name=\"" + id + "\"");
        s.append(" maxlength=\"" + String.valueOf(maxLength) + "\"");
        final String value;
        if (locked) {
            s.append(" readonly=\"readonly\"");
            value = this.lockValue;
        } else {
            if (rs != null && !isEmpty) {
                value = this.getValue(rs);
            } else {
                value = this.getDefaultValue();
            }
        }
        if (value != null) {
            s.append(" value=\"" + value + "\"");
        }
        s.append(" />");
        return s.toString();
    }

    /**
     * Tries to validate the given value.
     * @param value the value as a String.
     * @return the correct value.
     * @throws IOCException
     */
    @Override
    public String validate(String value) throws IOCException {
        if (locked) {
            return lockValue;
        } else {
            String x = value.replaceAll("\\s+", "");
            if (x != null && !x.isEmpty()) {
                final Matcher matcher = Pattern.compile("^(\\+|-)?\\d+(\\.|,)?\\d*$").matcher(x);
                if (matcher.matches()) {
                    x = x.replace(",", ".");
                    return x;
                } else {
                    throw new IOCException(table, column, IOCExceptionState.MISFORMATTED);
                }
            } else {
                if (isNullable) {
                    return null;
                } else {
                    throw new IOCException(table, column, IOCExceptionState.CANNOT_BE_EMPTY);
                }
            }
        }
    }

    /**
     * Formats the given value, so it can be used in a SQL command.
     * @param value the value as a String.
     * @return
     * @throws IOCException
     */
    @Override
    public String formatSQLValue(String value) throws IOCException {
        final String x = validate(value);
        if (x == null) {
            return "NULL";
        } else {
            return x;
        }
    }
}
