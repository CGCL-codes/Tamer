package com.ssg.db;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ssg
 */
public class DBExecutor {

    public static final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static boolean _force = false;

    public static boolean _test = false;

    public static boolean _space = false;

    public static void help() {
        System.out.println("USAGE: <app> options fileName");
        System.out.println("  Executes DB statements tadata and/or data into XML format using JDBC.");
        System.out.println("  Options are:");
        System.out.println("    -driver=<jdbc driver class name> [" + DEFAULT_JDBC_DRIVER + "]");
        System.out.println("    -url=<jdbc datasource url>");
        System.out.println("    -user=<db user name>");
        System.out.println("    -password=<password>");
        System.out.println("    -force=<true|false> try all SQL statements (true) or stop on 1st error (false). Default is " + _force);
        System.out.println("    -test=<true|false> Show (true) or show and execute (false) SQL. Default is " + _test);
        System.out.println("    -commit=<commit statement>. Commonly used: '/' for Oracle, 'GO' for MS SQL Server.");
        System.out.println("    -space=<true|false>. Convert line feeds into space (true). Default is " + _space);
        System.out.println("    -sql=<sql>");
        System.out.println("    -file=<file with sql statements>");
        System.out.println("  Last option value is used. Options '-sql' and '-file' are accumulated.");
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            help();
        } else {
            String driver = null;
            String url = null;
            String user = null;
            String password = null;
            String fileName = null;
            String forceS = Boolean.toString(_force);
            String testS = Boolean.toString(_test);
            String spaceS = Boolean.toString(_space);
            String commitS = null;
            List<String> sqls = new LinkedList<String>();
            for (int i = 0; i < args.length; i++) {
                String s = args[i];
                String name = null;
                String value = s;
                if (s.indexOf("=") != -1) {
                    int idx = s.indexOf("=");
                    name = s.substring(0, idx);
                    value = s.substring(idx + 1);
                }
                if (name != null) {
                    if (name.equalsIgnoreCase("-driver")) {
                        driver = value;
                    } else if (name.equalsIgnoreCase("-url")) {
                        url = value;
                    } else if (name.equalsIgnoreCase("-user")) {
                        user = value;
                    } else if (name.equalsIgnoreCase("-password")) {
                        password = value;
                    } else if (name.equalsIgnoreCase("-force")) {
                        forceS = value;
                    } else if (name.equalsIgnoreCase("-test")) {
                        testS = value;
                    } else if (name.equalsIgnoreCase("-space")) {
                        spaceS = value;
                    } else if (name.equalsIgnoreCase("-commit")) {
                        commitS = value;
                    } else if (name.equalsIgnoreCase("-sql")) {
                        sqls.add(value);
                    } else if (name.equalsIgnoreCase("-file")) {
                        sqls.add("@" + value);
                    } else {
                        System.err.println("UNKNOWN option(ignored): " + s);
                    }
                } else {
                    fileName = value;
                }
            }
            DBConnector db = new DBConnector(driver, url, user, password);
            DBReader dbr = new DBReader(db);
            DBWriter dbw = new DBWriter(db);
            boolean force = _force;
            try {
                force = Boolean.parseBoolean(forceS);
            } catch (Throwable th) {
            }
            boolean test = _test;
            try {
                test = Boolean.parseBoolean(testS);
            } catch (Throwable th) {
            }
            boolean space = _space;
            try {
                space = Boolean.parseBoolean(spaceS);
            } catch (Throwable th) {
            }
            List<String> ss = loadSQLStatements(sqls, commitS, space);
            System.out.println("Executing SQL statements batch (" + ss.size() + "):");
            for (String sql : ss) {
                try {
                    System.out.print(sql);
                    if (!test) {
                        if (commitS != null && sql.equals(commitS)) {
                            System.out.print(" commit... ");
                            db.getConnection().commit();
                        } else {
                            Object o = dbr.execSQL(sql, false);
                            String sss = (o instanceof ResultSet) ? "resultset" : "count=" + o;
                            System.out.print("; run... " + sss);
                        }
                    } else {
                    }
                    System.out.println(" OK");
                } catch (SQLException sqlex) {
                    if (!force) {
                        throw new RuntimeException("Error while executing SQL. Aborted. ERROR: " + sqlex.getMessage());
                    } else {
                        System.err.println("Failed to execute SQL: " + sql + ".\n ... Continue. ERROR: " + sqlex.getMessage());
                    }
                }
            }
            try {
                db.getConnection().commit();
            } catch (SQLException sqlex) {
            }
        }
    }

    /**
     * Converts loccection of SQL statement definitions into list of executable SQL statements.
     * If statement starts with '@' then it is interpreted as file name with statements.
     * If statement contains character/string specified in parameter "commitS" then new SQL statement is started.
     * If parameter "space" is set to "true" then all line feeds are replaced with space character.
     *
     * @param sqls - collection of SQL statements orreferences tofile with SQL statements (starts with '@')
     * @param commitS - string indicating commit action.Usually '/' for Oracle or 'GO' for SQL Server.
     */
    public static List<String> loadSQLStatements(Collection<String> sqls, String commitS, boolean space) {
        List<String> ss = new ArrayList<String>();
        for (String sql : sqls) {
            if (sql.startsWith("@")) {
                try {
                    LineNumberReader lnr = new LineNumberReader(new FileReader(sql.substring(1)));
                    String s = "";
                    boolean globalComment = false;
                    boolean continuedStatement = false;
                    boolean tmpCreateOrReplace = false;
                    boolean tmpAs = false;
                    int beginCount = 0;
                    StringBuffer sb = new StringBuffer();
                    while ((s = lnr.readLine()) != null) {
                        s = s.trim();
                        if (globalComment && s.indexOf("*/") != -1) {
                            globalComment = false;
                            s = s.substring(s.indexOf("*/") + 2);
                            s = s.trim();
                        }
                        if (s.startsWith("/*")) {
                            globalComment = true;
                        }
                        if (globalComment) {
                            continue;
                        }
                        if (s.startsWith("--")) {
                            continue;
                        }
                        if (beginCount == 0 && (s.toUpperCase().contains("CREATE ") || s.toUpperCase().contains("REPLACE "))) tmpCreateOrReplace = true;
                        if (beginCount == 0 && (s.toUpperCase().contains(" AS") || s.toUpperCase().contains(" AS"))) tmpAs = true;
                        if (s.toUpperCase().indexOf("BEGIN") != -1) {
                            beginCount++;
                            tmpCreateOrReplace = false;
                            tmpAs = false;
                        }
                        if (s.indexOf(";") != -1 && beginCount == 0 && !(tmpCreateOrReplace && tmpAs)) {
                            s = s.substring(0, s.indexOf(";")).trim();
                            continuedStatement = false;
                        } else {
                            continuedStatement = true;
                        }
                        if (commitS != null && s.equals(commitS)) {
                            continuedStatement = false;
                            beginCount = 0;
                            s = " ";
                        }
                        if (s.toUpperCase().indexOf("END;") != -1) {
                            beginCount--;
                            if (beginCount == 0) {
                                continuedStatement = false;
                            }
                        }
                        if (s.length() == 0) {
                            continue;
                        }
                        if (!s.trim().isEmpty()) {
                            if (sb.length() > 0) {
                                if (space) {
                                    sb.append(" ");
                                } else {
                                    sb.append("\n");
                                }
                            }
                            sb.append(s);
                        }
                        if (!continuedStatement) {
                            if (!sb.toString().trim().isEmpty()) {
                                ss.add(sb.toString());
                            } else {
                            }
                            sb = new StringBuffer();
                            tmpCreateOrReplace = false;
                            tmpAs = false;
                        }
                    }
                    lnr.close();
                } catch (IOException ioex) {
                    System.err.println("Failed to load SQL statements from file " + sql.substring(1) + ". ERROR: " + ioex.getMessage());
                }
            } else {
                if (sql.indexOf(";") != -1) {
                    sql = sql.substring(0, sql.indexOf(";")).trim();
                }
                if (sql.startsWith("--")) {
                    sql = "";
                }
                if (sql.length() > 0) {
                    ss.add(sql);
                }
            }
        }
        return ss;
    }
}
