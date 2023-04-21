package cc.w3d.jawos.jinn.xdata.xdata.engines.xeMysqlData.structure;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import cc.w3d.jawos.jinn.xdata.xdata.engine.structure.RowData;

public class ResultSetRowData implements RowData {

    ResultSet resultSet;

    public ResultSetRowData(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public InputStream getAsciiStream(int columnIndex) throws Exception {
        return resultSet.getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnLabel) throws Exception {
        return resultSet.getAsciiStream(columnLabel);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws Exception {
        return resultSet.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws Exception {
        return resultSet.getBigDecimal(columnLabel);
    }

    public InputStream getBinaryStream(int columnIndex) throws Exception {
        return resultSet.getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnLabel) throws Exception {
        return resultSet.getBinaryStream(columnLabel);
    }

    public boolean getBoolean(int columnIndex) throws Exception {
        return resultSet.getBoolean(columnIndex);
    }

    public boolean getBoolean(String columnLabel) throws Exception {
        return resultSet.getBoolean(columnLabel);
    }

    public byte getByte(int columnIndex) throws Exception {
        return resultSet.getByte(columnIndex);
    }

    public byte getByte(String columnLabel) throws Exception {
        return resultSet.getByte(columnLabel);
    }

    public byte[] getBytes(int columnIndex) throws Exception {
        return resultSet.getBytes(columnIndex);
    }

    public byte[] getBytes(String columnLabel) throws Exception {
        return resultSet.getBytes(columnLabel);
    }

    public Reader getCharacterStream(int columnIndex) throws Exception {
        return resultSet.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnLabel) throws Exception {
        return resultSet.getCharacterStream(columnLabel);
    }

    public String[] getColumns() throws Exception {
        String r[] = new String[resultSet.getMetaData().getColumnCount()];
        for (int i = 0; i < r.length; i++) {
            r[i] = resultSet.getMetaData().getColumnName(i + 1);
        }
        return r;
    }

    public Date getDate(int columnIndex) throws Exception {
        return resultSet.getDate(columnIndex);
    }

    public Date getDate(String columnLabel) throws Exception {
        return resultSet.getDate(columnLabel);
    }

    public double getDouble(int columnIndex) throws Exception {
        return resultSet.getDouble(columnIndex);
    }

    public double getDouble(String columnLabel) throws Exception {
        return resultSet.getDouble(columnLabel);
    }

    public float getFloat(int columnIndex) throws Exception {
        return resultSet.getFloat(columnIndex);
    }

    public float getFloat(String columnLabel) throws Exception {
        return resultSet.getFloat(columnLabel);
    }

    public int getInt(int columnIndex) throws Exception {
        return resultSet.getInt(columnIndex);
    }

    public int getInt(String columnLabel) throws Exception {
        return resultSet.getInt(columnLabel);
    }

    public long getLong(int columnIndex) throws Exception {
        return resultSet.getLong(columnIndex);
    }

    public long getLong(String columnLabel) throws Exception {
        return resultSet.getLong(columnLabel);
    }

    public Object getObject(int columnIndex) throws Exception {
        return resultSet.getObject(columnIndex);
    }

    public Object getObject(String columnLabel) throws Exception {
        return resultSet.getObject(columnLabel);
    }

    public short getShort(int columnIndex) throws Exception {
        return resultSet.getShort(columnIndex);
    }

    public short getShort(String columnLabel) throws Exception {
        return resultSet.getShort(columnLabel);
    }

    public String getString(int columnIndex) throws Exception {
        return resultSet.getString(columnIndex);
    }

    public String getString(String columnLabel) throws Exception {
        return resultSet.getString(columnLabel);
    }

    public Time getTime(int columnIndex) throws Exception {
        return resultSet.getTime(columnIndex);
    }

    public Time getTime(String columnLabel) throws Exception {
        return resultSet.getTime(columnLabel);
    }

    public Timestamp getTimestamp(int columnIndex) throws Exception {
        return resultSet.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnLabel) throws Exception {
        return resultSet.getTimestamp(columnLabel);
    }
}
