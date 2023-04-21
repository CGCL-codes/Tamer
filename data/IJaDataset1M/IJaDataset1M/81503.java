package com.tecacet.jflat.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.tecacet.jflat.CSVLineMerger;
import com.tecacet.jflat.FlatFileWriter;
import com.tecacet.jflat.LineMerger;
import com.tecacet.jflat.LineMergerException;

/**
 * Writes a ResultSet to a Flat File
 * 
 * @author Dimitri Papaioannou
 *
 */
public class ResultSetFileWriter<T> extends FlatFileWriter<T> {

    private static final SimpleDateFormat DEFAULT_TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    private static final SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");

    private boolean includeColumnNames = true;

    private DateFormat dateFormat = DEFAULT_DATE_FORMATTER;

    private DateFormat timeFormat = DEFAULT_TIMESTAMP_FORMATTER;

    /**
     * The default constructor writes the result set in CSV format
     * @param writer
     */
    public ResultSetFileWriter(Writer writer) {
        this(writer, new CSVLineMerger());
    }

    public ResultSetFileWriter(Writer writer, LineMerger merger) {
        super(writer, merger, null);
    }

    /**
     * Writes the entire ResultSet to a CSV file.
     * 
     * The caller is responsible for closing the ResultSet.
     * 
     * @param rs
     *            the Result Set to write
     * @param includeColumnNames
     *            true if you want column names in the output, false otherwise
     * 
     */
    public void writeAll(ResultSet rs) throws SQLException, IOException, LineMergerException {
        ResultSetMetaData metadata = rs.getMetaData();
        if (includeColumnNames) {
            writeColumnNames(metadata);
        }
        int columnCount = metadata.getColumnCount();
        while (rs.next()) {
            String[] nextLine = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                nextLine[i] = getColumnValue(rs, metadata.getColumnType(i + 1), i + 1);
            }
            writeNext(nextLine);
        }
    }

    protected void writeColumnNames(ResultSetMetaData metadata) throws SQLException, LineMergerException {
        int columnCount = metadata.getColumnCount();
        String[] nextLine = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            nextLine[i] = metadata.getColumnName(i + 1);
        }
        writeNext(nextLine);
    }

    protected String getColumnValue(ResultSet rs, int colType, int colIndex) throws SQLException, IOException {
        String value = "";
        switch(colType) {
            case Types.BIT:
                Object bit = rs.getObject(colIndex);
                if (bit != null) {
                    value = String.valueOf(bit);
                }
                break;
            case Types.BOOLEAN:
                boolean b = rs.getBoolean(colIndex);
                if (!rs.wasNull()) {
                    value = Boolean.valueOf(b).toString();
                }
                break;
            case Types.CLOB:
                Clob c = rs.getClob(colIndex);
                if (c != null) {
                    value = read(c);
                }
                break;
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.REAL:
            case Types.NUMERIC:
                BigDecimal bd = rs.getBigDecimal(colIndex);
                if (bd != null) {
                    value = "" + bd.doubleValue();
                }
                break;
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
                int intValue = rs.getInt(colIndex);
                if (!rs.wasNull()) {
                    value = "" + intValue;
                }
                break;
            case Types.JAVA_OBJECT:
                Object obj = rs.getObject(colIndex);
                if (obj != null) {
                    value = String.valueOf(obj);
                }
                break;
            case Types.DATE:
                java.sql.Date date = rs.getDate(colIndex);
                if (date != null) {
                    value = dateFormat.format(date);
                }
                break;
            case Types.TIME:
                Time t = rs.getTime(colIndex);
                if (t != null) {
                    value = t.toString();
                }
                break;
            case Types.TIMESTAMP:
                Timestamp tstamp = rs.getTimestamp(colIndex);
                if (tstamp != null) {
                    value = timeFormat.format(tstamp);
                }
                break;
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
            case Types.CHAR:
                value = rs.getString(colIndex);
                break;
            default:
                value = "";
        }
        if (value == null) {
            value = "";
        }
        return value;
    }

    private static String read(Clob c) throws SQLException, IOException {
        StringBuffer sb = new StringBuffer((int) c.length());
        Reader r = c.getCharacterStream();
        char[] cbuf = new char[2048];
        int n = 0;
        while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
            if (n > 0) {
                sb.append(cbuf, 0, n);
            }
        }
        return sb.toString();
    }

    public boolean isIncludeColumnNames() {
        return includeColumnNames;
    }

    public void setIncludeColumnNames(boolean includeColumnNames) {
        this.includeColumnNames = includeColumnNames;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String format) {
        dateFormat = new SimpleDateFormat(format);
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String format) {
        timeFormat = new SimpleDateFormat(format);
    }

    public void setTimeFormat(DateFormat timeFormat) {
        this.timeFormat = timeFormat;
    }
}
