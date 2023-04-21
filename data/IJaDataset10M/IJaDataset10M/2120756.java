package org.caleigo.core.service;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import org.caleigo.core.*;
import org.caleigo.toolkit.log.Log;

/**
 * @author Klas Ehnrot
 *
 * DataService used only for Demo databases that are read from a URL, and put into
 * Memory. Uses HSQLDB that can be found on "Sourceforge.net".
 */
public class DemoDataService extends JDBCDataService {

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            Log.printError(null, "Failed to load hsqldb driver", e);
        }
    }

    /**
     * Creates empty Demo Database
     * 
     * @param descriptor descriptor
     */
    public DemoDataService(IDataSourceDescriptor descriptor) {
        super(descriptor);
    }

    /**
     * Creates the Demo Database and copies data from the supplied URL.
     * The File pointed out by the URL should contain CREATE, INSERT and
     * ALTER TABLE commands, and they will be run on the in-memory database.
     * 
     * @param descriptor 
     * @param serviceIdentity
     * @param url
     */
    public DemoDataService(IDataSourceDescriptor descriptor, URL databaseURL) {
        super(descriptor, descriptor.getSourceName(), "jdbc:hsqldb:.", "sa", "", 1);
        copyDataFromURL(databaseURL);
    }

    /**
     * Creates the Demo Database and copies data from the supplied HSQLDB database.
     * 
     * @param descriptor
     * @param hsqldbDir  the directory containing the hsqldb database from which
     *                   data should be copied.
     * @param dbName     the name of the hsqldb database.
     */
    public DemoDataService(IDataSourceDescriptor descriptor, File hsqldbDir, String dbName) {
        super(descriptor, descriptor.getSourceName(), "jdbc:hsqldb:.", "sa", "", 0);
        copyDataFromHSQLDB(descriptor, hsqldbDir, dbName);
        this.setMaxConnectionPoolSize(1);
    }

    private void copyDataFromURL(URL scriptFileURL) {
        InputStream stream = null;
        Connection conn = openConnection();
        Statement stmnt = null;
        try {
            stmnt = conn.createStatement();
            stream = scriptFileURL.openStream();
            ArrayList creates = new ArrayList();
            ArrayList inserts = new ArrayList();
            ArrayList constraints = new ArrayList();
            Reader fileReader = null;
            if (stream != null) {
                fileReader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(fileReader, 10000);
                String currentLine = translateEscapeCodes(bufferedReader.readLine());
                while (currentLine != null) {
                    if (currentLine != null) {
                        if (currentLine.indexOf("CREATE TABLE") != -1) {
                            creates.add(currentLine);
                        } else if (currentLine.indexOf("ALTER TABLE") != -1) {
                            constraints.add(currentLine);
                        } else if (currentLine.indexOf("INSERT INTO") != -1) {
                            inserts.add(currentLine);
                        }
                    }
                    currentLine = translateEscapeCodes(bufferedReader.readLine());
                }
                bufferedReader.close();
            }
            stmnt.executeQuery("SET REFERENTIAL_INTEGRITY FALSE;");
            Iterator iter = creates.iterator();
            while (iter.hasNext()) {
                String sql = (String) iter.next();
                stmnt.executeUpdate(sql);
            }
            iter = inserts.iterator();
            while (iter.hasNext()) {
                String sql = (String) iter.next();
                stmnt.executeUpdate(sql);
            }
            iter = constraints.iterator();
            while (iter.hasNext()) {
                String sql = (String) iter.next();
                stmnt.executeUpdate(sql);
            }
            Log.print(this, "Memory database successfully created... ");
            Log.print(this, "    " + creates.size() + " tables created.");
            Log.print(this, "    " + inserts.size() + " rows copied.");
            stmnt.executeQuery("SET REFERENTIAL_INTEGRITY TRUE;");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (stmnt != null) {
                    stmnt.close();
                }
                if (conn != null) {
                    closeConnection(conn);
                }
            } catch (SQLException e3) {
                e3.printStackTrace();
            }
        }
    }

    private void copyDataFromHSQLDB(IDataSourceDescriptor descriptor, File hsqldbDir, String dbName) {
        JDBCDataService sourceService = new JDBCDataService(descriptor, descriptor.getSourceName(), "jdbc:hsqldb:" + hsqldbDir.getAbsolutePath() + "/" + dbName, "sa", "", 0);
        this.setValidating(false);
        IDataSource.DataSourceCreator.createDataSource(new SingleServiceDataSource(sourceService), this, true);
        this.setValidating(true);
    }

    public static void main(String args[]) {
        translateEscapeCodes("Test starts-<>-Test finished!\\u00e");
    }

    /**
     * Help method that translates escape codes like "c" to their real 
     * characters.
     */
    protected static String translateEscapeCodes(String text) {
        try {
            StringBuffer buf = new StringBuffer(text.length());
            int prevIndex = 0;
            int index = text.indexOf("\\u");
            while (index >= 0) {
                if (text.length() >= index + 6) {
                    int charNum = Integer.parseInt(text.substring(index + 2, index + 6), 16);
                    buf.append(text.substring(prevIndex, index));
                    buf.append((char) charNum);
                    prevIndex = index + 6;
                    index = text.indexOf("\\u", prevIndex);
                } else index = -1;
            }
            buf.append(text.substring(prevIndex));
            return buf.toString();
        } catch (Exception e) {
            Log.printWarning(null, "Failed to translate escape codes for: \"" + text + "\"");
            return text;
        }
    }
}
