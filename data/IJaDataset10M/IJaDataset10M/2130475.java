package com.apelon.dts.db.admin.data.handler;

import java.io.*;
import java.sql.*;
import java.text.*;
import com.apelon.dts.db.admin.data.*;
import com.apelon.dts.db.admin.*;
import com.apelon.common.sql.*;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;

/**
 * <p>Title: TableExporter</p>
 * <p>Description: Exports a given table to a destination specified by a java.io.Writer</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Apelon, Inc.</p>
 * @author unascribed
 * @version 1.0
 */
public class TableExporter {

    /**
	 * No arg Constructor
	 */
    public TableExporter() {
        init();
    }

    /**
	 * Constructor with a Writer
	 * @param writer    the java.io.writer used
	 */
    public TableExporter(Writer writer) {
        this.writer = writer;
        init();
    }

    /**
	 * Sets the ExtractorFormat class to be used
	 * @param ef    the ExtractorFormat class
	 */
    public void setExtractorFormat(ExtractorFormat ef) {
        this.ef = ef;
    }

    /**
	 * Gets the ExtractorFormat class used
	 * @return  the ExtractorFormat class
	 */
    public ExtractorFormat getExtractorFormat() {
        return ef;
    }

    /**
	 * Sets the delimiter for the ExtractorFormat class
	 * @param delimiter
	 */
    public void setDelimiter(String delimiter) {
        ef.setDelimiter(delimiter);
    }

    /**
	 * Gets the delimiter for the ExtractorFormat class
	 * @return  the delimiter string
	 */
    public String getDelimiter() {
        return ef.getDelimiter();
    }

    /**
	 * Sets the Writer for exporting
	 * @param writer    the java.io.Writer
	 */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
	 * Gets the Writer used in exporting
	 * @return  the java.io.Writer
	 */
    public Writer getWriter() {
        return writer;
    }

    /**
	 * Cleanup method before discarding this object
	 * @throws IOException
	 */
    public void cleanup() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    /**
	 * Exports content of a given table with a specified export SQL statement.
	 * @param con           the connection to the database
	 * @param tableName     name of the table to export
	 * @param exportStmt    SQL statement defining export
   * @param baseTable     DTSBaseTable (null if no special place holders are needed to be substituted)
	 * @throws SQLException
	 * @throws IOException
	 */
    public void exportTable(Connection con, String tableName, String exportStmt, DTSBaseTable baseTable) throws SQLException, IOException {
        Statement stmt = con.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(exportStmt);
            exportTable(rs, tableName, baseTable);
            rs.close();
        } finally {
            stmt.close();
        }
    }

    /**
	 * Exports content of a given table with a specified export SQL statement.
	 * @param rs            the ResultSet object containing export data
	 * @param tableName     name of the table to export
   * @param baseTable     DTSBaseTable (null if no special place holders are needed to be substituted)
	 * @throws SQLException
	 * @throws IOException
	 */
    protected void exportTable(ResultSet rs, String tableName, DTSBaseTable baseTable) throws SQLException, IOException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        String[] headerName = new String[colCount];
        int[] headerType = new int[colCount];
        for (int i = 1; i <= colCount; i++) {
            String colName = rsmd.getColumnName(i);
            int type = rsmd.getColumnType(i);
            headerName[i - 1] = colName;
            headerType[i - 1] = type;
        }
        printHeader(headerName, headerType);
        int c = 0;
        long beg = System.currentTimeMillis();
        int buffer = 25000;
        StringBuffer rowData = new StringBuffer(buffer);
        while (rs.next()) {
            String[] rowdata = getColData(rs, colCount, headerType);
            replaceVar(baseTable, rowdata, tableName);
            rowData.append(DataConversion.LINE_SEPERATOR);
            rowData.append(ef.getFormattedOutput(rowdata, null));
            if (rowData.length() > buffer) {
                String rd = rowData.toString();
                writer.write(rd);
                rowData = new StringBuffer(buffer);
            }
            c++;
            if ((c % 2000) == 0) {
                Categories.dataDb().info("Exported [" + tableName + "] rows : # " + c + " in " + ((System.currentTimeMillis() - beg) / 1000) + " secs");
            }
        }
        String rd = rowData.toString();
        writer.write(rd);
        writer.flush();
        Categories.dataDb().info("Exported [" + tableName + "] rows : # " + c + " in " + ((System.currentTimeMillis() - beg) / 1000) + " secs");
    }

    private void replaceVar(DTSBaseTable baseTable, String[] cols, String tableName) {
        for (int i = 0; i < cols.length; i++) {
            cols[i] = baseTable.replaceVarPostQuery(cols[i], tableName);
        }
    }

    /**
	 * This returns a row data.
	 * @param rs    the resultset holding the row data
	 * @param numCols   number of columns expected
	 * @param type      column types for the above columns
	 * @return  an array of String holding row data
	 * @throws SQLException
	 */
    public String[] getColData(ResultSet rs, int numCols, int[] type) throws SQLException, IOException {
        String[] row = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            if ((type[i] == Types.DECIMAL) || (type[i] == Types.FLOAT) || (type[i] == Types.DOUBLE)) {
                double db = rs.getDouble(i + 1);
                if (!rs.wasNull()) row[i] = String.valueOf(db); else row[i] = DataConversion.NULL_STR;
            } else if (type[i] == Types.INTEGER || type[i] == Types.NUMERIC) {
                long num = rs.getLong(i + 1);
                if (!rs.wasNull()) row[i] = String.valueOf(rs.getLong(i + 1)); else row[i] = DataConversion.NULL_STR;
            } else if (type[i] == Types.CHAR || type[i] == Types.VARCHAR || (type[i] == Types.LONGVARCHAR)) {
                String data = rs.getString(i + 1);
                if (data == null) {
                    row[i] = DataConversion.NULL_STR;
                } else {
                    data = DataConversion.escape(data);
                    row[i] = data;
                }
            } else if (type[i] == Types.CLOB) {
                Clob clob = rs.getClob(i + 1);
                if (clob != null) {
                    long len = clob.length();
                    row[i] = clob.getSubString(1, (int) len);
                    row[i] = DataConversion.escape(row[i]);
                } else row[i] = DataConversion.NULL_STR;
            } else if (type[i] == Types.LONGVARBINARY) {
                byte[] bytes = rs.getBytes(i + 1);
                if (bytes != null) {
                    row[i] = new String(bytes, System.getProperty("file.encoding"));
                    row[i] = DataConversion.escape(row[i]);
                } else {
                    row[i] = DataConversion.NULL_STR;
                }
            } else if (type[i] == Types.BLOB) {
                Blob blob = rs.getBlob(i + 1);
                if (blob != null) {
                    InputStream inStream = blob.getBinaryStream();
                    long size = blob.length();
                    byte[] buffer = new byte[(int) size];
                    int length = -1;
                    inStream.read(buffer);
                    inStream.close();
                    row[i] = new String(buffer);
                    row[i] = DataConversion.escape(row[i]);
                } else row[i] = DataConversion.NULL_STR;
            } else if ((type[i] == Types.DATE) || (type[i] == Types.TIMESTAMP)) {
                Timestamp ts = rs.getTimestamp(i + 1);
                if (ts != null) {
                    String date = sdf.format(new java.sql.Date(ts.getTime()));
                    row[i] = date;
                } else row[i] = DataConversion.NULL_STR;
            } else if (type[i] == Types.LONGVARCHAR) {
                String s = rs.getString(i + 1);
                if (s != null) row[i] = s; else row[i] = DataConversion.NULL_STR;
            } else {
                byte[] ob = rs.getBytes(i + 1);
                if (ob != null) {
                    String s = new String(ob);
                    s = DataConversion.escape(s);
                    row[i] = s;
                } else row[i] = DataConversion.NULL_STR;
                Categories.dataDb().warn("Undefined data-type for column [" + i + "] has a value [" + row[i] + "]");
            }
        }
        return row;
    }

    private char[] escapeText(char[] text, int start, int length) {
        for (int i = start; i < length; i++) {
            text[i] = escapeChar(text[i]);
        }
        return text;
    }

    private char escapeChar(char c) {
        if (c >= 0x20) return c; else if (c == '\n') return c; else if (c == '\r') return c; else if (c == '\t') return c;
        return '�';
    }

    /**
	 * Prints the header for the data
	 * @param headerName    array of String headers of columns
	 * @param types         array of data types of the columns
	 * @throws IOException
	 */
    protected void printHeader(String[] headerName, int[] types) throws IOException {
        String header = ef.getHeader(headerName, types);
        writer.write(header);
    }

    /**
	 * Prints the row data
	 * @param rowData   an String array containing row data
	 * @throws IOException
	 */
    protected void printRowData(String[] rowData) throws IOException {
        String data = ef.getFormattedOutput(rowData, null);
        data += DataConversion.LINE_SEPERATOR;
        writer.write(data);
    }

    /**
	 * a hook method to perform custom work before exporting data
	 */
    public void init() {
    }

    public static void main(String[] args) {
        try {
            String user = "icd";
            String pass = "icd";
            String host = "localhost";
            int port = 1521;
            String inst = "ORCL";
            String table = "content_license";
            String exportStmt = "select * from " + table;
            Connection con = SQL.getConnection(user, pass, host, port, inst);
            if (con != null) {
                BufferedWriter w = new BufferedWriter(new FileWriter(table + ".txt"));
                TableExporter te = new TableExporter();
                te.initLogger();
                te.setWriter(w);
                te.setExtractorFormat(new DelimitedFormat());
                te.setDelimiter("\t");
                te.exportTable(con, table, exportStmt, null);
                w.flush();
                w.close();
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Finished exporting table.");
    }

    public void initLogger() {
        logCfgLoader().loadDefault();
    }

    protected LogConfigLoader logCfgLoader() {
        if (logConfig == null) logConfig = new LogConfigLoader("dbcontentlog.xml", TableImporter.class);
        return logConfig;
    }

    protected static LogConfigLoader logConfig = null;

    protected boolean headerPrinted = false;

    protected ExtractorFormat ef;

    protected Writer writer = null;

    protected SimpleDateFormat sdf = new SimpleDateFormat(DataConversion.DATE_FORMAT);
}
