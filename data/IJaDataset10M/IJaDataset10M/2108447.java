package uk.org.ogsadai.converters.webrowset.resultset.types;

import java.sql.SQLException;

/**
 * Converts a string value into a boolean.
 *
 * @author The OGSA-DAI Team.
 */
public class BooleanStrategy extends TypeStrategy {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2005.";

    public boolean getBoolean(String data) {
        if (data == null) {
            return false;
        }
        return new Boolean(data).booleanValue();
    }

    public byte getByte(String data) throws SQLException {
        if (getBoolean(data)) {
            return 1;
        } else {
            return 0;
        }
    }

    public short getShort(String data) throws SQLException {
        return getByte(data);
    }

    public int getInt(String data) throws SQLException {
        return getByte(data);
    }

    public long getLong(String data) throws SQLException {
        return getByte(data);
    }

    public double getDouble(String data) throws SQLException {
        return getByte(data);
    }

    public float getFloat(String data) throws SQLException {
        return getByte(data);
    }

    public Object getObject(String value) throws SQLException {
        if (value == null) {
            return null;
        }
        return new Boolean(value);
    }

    public String getString(String data) {
        return data;
    }
}
