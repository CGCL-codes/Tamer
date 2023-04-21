package com.cs.util.db.ioc;

import com.cs.util.binary.ByteSize;
import com.cs.util.db.ResultSetMetaInfo;
import com.cs.util.db.ioc.IOCExceptionState;
import com.cs.util.db.ioc.IOCException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * MySqlTinyBlob is an IO controller for handling input and output of MySql TINYBLOB columns.
 * @author dimitris@jmike.gr
 */
public class MySqlTinyBlob extends GenericIOController implements IOController {

    public MySqlTinyBlob(ResultSetMetaInfo rsmi, int columnIndex) throws SQLException {
        super(rsmi, columnIndex);
    }

    public MySqlTinyBlob(ResultSetMetaInfo rsmi, String table, String column) throws SQLException {
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
            final Blob blob = rs.getBlob(columnIndex);
            StringBuilder s = new StringBuilder();
            int buf = -1;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
                while ((buf = in.read()) > -1) {
                    s.append((char) buf);
                }
                in.close();
            } catch (IOException ex) {
            }
            return s.toString();
        } catch (SQLException ex) {
            return null;
        }
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
            ps.setNull(parameterIndex, Types.BLOB);
        } else {
            ps.setBlob(parameterIndex, new ByteArrayInputStream(x.getBytes()));
        }
    }

    /**
     * Returns the value of the controller's column as a URL encoded String.
     * Please note that this function will always return null because of URL length limitations.
     * @param rs the resultset containing the current row position and the field's value.
     * @return
     */
    @Override
    public String getURLEncodedValue(ResultSet rs) {
        return null;
    }

    /**
     * Returns the byte size of the controller's column at the specified row as a String.
     * @param rs the resultset containing the current row position and the field's value.
     * @return
     */
    @Override
    public String getXhtmlTableData(ResultSet rs) {
        try {
            final long x = rs.getBlob(columnIndex).length();
            return new ByteSize(x).getString();
        } catch (SQLException ex) {
            return new ByteSize(0).getString();
        }
    }

    /**
     * Returns a Xhtml form element for the controller's column.
     * @param rs the resultset containing the current row position and the field's value.
     * @param isEmpty a boolean flag that indicates whether this element contains data.
     * @return
     */
    @Override
    public String getXhtmlFormElement(ResultSet rs, boolean isEmpty) {
        StringBuilder s = new StringBuilder();
        s.append("<input type=\"file\"");
        final String id = this.getId();
        s.append(" id=\"" + id + "\"");
        s.append(" name=\"" + id + "\"");
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
            if (value != null && !value.isEmpty()) {
                return value;
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
            return "'" + x + "'";
        }
    }
}
