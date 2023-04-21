package org.hsqldb.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.hsqldb.lib.java.JavaSystem;

/**
 * Script tool - command line tool to read in sql script and execute it.
 *
 *
 * @version 1.7.0
 */
public class ScriptTool {

    private static Properties pProperties = new Properties();

    private Connection cConn;

    private Statement sStatement;

    private boolean BATCH = true;

    private String EKW = new String("go");

    private boolean EOF = false;

    private int ln = 0;

    /**
     * Main method
     *
     *
     * @param arg
     */
    public static void main(String[] arg) {
        for (int i = 0; i < arg.length; i++) {
            String p = arg[i];
            if (p.equals("-?")) {
                printHelp();
                System.exit(0);
            }
        }
        ScriptTool tool = new ScriptTool();
        tool.execute(arg);
        System.exit(0);
    }

    public void execute(String[] arg) {
        for (int i = 0; i < arg.length; i++) {
            String p = arg[i];
            if (p.charAt(0) == '-') {
                pProperties.put(p.substring(1), arg[i + 1]);
                i++;
            }
        }
        ln = 0;
        EOF = false;
        BufferedReader in = null;
        Properties p = pProperties;
        String driver = p.getProperty("driver", "org.hsqldb.jdbcDriver");
        String url = p.getProperty("url", "jdbc:hsqldb:");
        String database = p.getProperty("database", "test");
        String user = p.getProperty("user", "sa");
        String password = p.getProperty("password", "");
        String script = p.getProperty("script", "st.sql");
        boolean log = p.getProperty("log", "false").equalsIgnoreCase("true");
        BATCH = p.getProperty("batch", "true").equalsIgnoreCase("true");
        try {
            if (log) {
                trace("driver   = " + driver);
                trace("url      = " + url);
                trace("database = " + database);
                trace("user     = " + user);
                trace("password = " + password);
                trace("script   = " + script);
                trace("log      = " + log);
                trace("batch    = " + BATCH);
                JavaSystem.setLogToSystem(true);
            }
            Class.forName(driver).newInstance();
            cConn = DriverManager.getConnection(url + database, user, password);
            in = new BufferedReader(new FileReader(script));
        } catch (Exception e) {
            System.out.println("ScriptTool.init error: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            sStatement = cConn.createStatement();
            String sql;
            while ((sql = fileToString(in)) != null) {
                if (sql.length() == 1) {
                    continue;
                }
                if (log) {
                    trace("SQL (" + ln + ") : " + sql.substring(0, sql.length() - 2));
                }
                sStatement.execute(sql);
                ResultSet results = sStatement.getResultSet();
                int updateCount = sStatement.getUpdateCount();
                if (updateCount == -1) {
                    trace(toString(results));
                } else {
                    trace("update count " + updateCount);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error at line " + ln + ": " + e);
        }
        try {
            cConn.close();
            in.close();
        } catch (Exception ce) {
        }
    }

    /**
     * Translate ResultSet to String representation
     * @param r
     */
    private String toString(ResultSet r) {
        try {
            if (r == null) {
                return "No Result";
            }
            ResultSetMetaData m = r.getMetaData();
            int col = m.getColumnCount();
            StringBuffer strbuf = new StringBuffer();
            for (int i = 1; i <= col; i++) {
                strbuf = strbuf.append(m.getColumnLabel(i) + "\t");
            }
            strbuf = strbuf.append("\n");
            while (r.next()) {
                for (int i = 1; i <= col; i++) {
                    strbuf = strbuf.append(r.getString(i) + "\t");
                    if (r.wasNull()) {
                        strbuf = strbuf.append("(null)\t");
                    }
                }
                strbuf = strbuf.append("\n");
            }
            return strbuf.toString();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Read file and convert it to string.
     */
    private String fileToString(BufferedReader in) {
        if (EOF) {
            return null;
        }
        EOF = true;
        StringBuffer a = new StringBuffer();
        try {
            String line;
            while ((line = in.readLine()) != null) {
                ln = ln + 1;
                if (BATCH) {
                    if (line.startsWith("print ")) {
                        trace("\n" + line.substring(5));
                        continue;
                    }
                    if (line.equalsIgnoreCase(EKW)) {
                        EOF = false;
                        break;
                    }
                }
                a.append(line);
                a.append('\n');
            }
            a.append('\n');
            return a.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Method declaration
     *
     *
     * @param s
     */
    private void trace(String s) {
        System.out.println(s);
    }

    /**
     * Method declaration
     *
     */
    private static void printHelp() {
        System.out.println("Usage: java ScriptTool [-options]\n" + "where options include:\n" + "    -driver <classname>     name of the driver class\n" + "    -url <name>             first part of the jdbc url\n" + "    -database <name>        second part of the jdbc url\n" + "    -user <name>            username used for connection\n" + "    -password <name>        password for this user\n" + "    -log <true/false>       write log to system out\n" + "    -batch <true/false>     allow go/print pseudo statements\n" + "    -script <script file>   reads from script file\n");
    }
}
