package com.sun.iis.tools.cmd;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Generic jdbc script runner.
 *
 * Usage:
 *
 *      import com.sun.iis.tools.cmd;
 *      ...
 *      Jsql myJsqlscriptrunner = new Jsql(URL, DRIVER, USER, PASSWORD);
 *      if (!scriptrunner.check_driver())
 *          ;    //FAILED - driver is not in class path
 *
 *      if (!scriptrunner.sqlsession(someScript))
 *          ;    //ERROR running script file
 *
 * @author Russ Tremain
 */
public class Jsql {

    /**
 * main entry point - you can call this as a tool or use the class directly
 * @param anArgList contains the command line words.
 */
    public static void main(String anArgList[]) {
        sMain = new Jsql();
        if (!parse_args(anArgList)) {
            System.exit(usage(1));
        }
        sMain.log(DEBUG, "main():  sMain.PKG is " + sMain.PKG + " PKG is " + sMain.PKG);
        if (sSHOWHELP) {
            System.exit(usage(0));
        }
        if (!sMain.check_driver()) {
            sMain.log(ERROR, sMain.PKG + ": JDBC driver '" + sMain.mDRIVER + "' is not available.");
            System.exit(1);
        }
        if (sUSESTDIN) {
            sMain.log(INFO, "using stdin");
            sMain.sqlsession(System.in);
        } else {
            for (int ii = 0; ii < sFILES.length; ii++) {
                sMain.sqlsession(sFILES[ii]);
            }
        }
        System.exit(0);
    }

    /**
 * Check and initialize our jdbc driver class.
 * @return true if driver is in the CLASSPATH
 */
    public boolean check_driver() {
        try {
            Class.forName(this.mDRIVER);
        } catch (ClassNotFoundException e) {
            log(ERROR, "JDBC driver '" + mDRIVER + "' is not available (" + e.toString() + ").");
            return false;
        }
        return true;
    }

    /**
 * Open a file and call the real sqlsesion driver.
 * @param aFn is a file containing sql statements.
 * @return false if error opening file.
 */
    public boolean sqlsession(String aFn) {
        FileInputStream myfile = null;
        boolean result = false;
        log(INFO, "input file name is '" + aFn + "'");
        try {
            myfile = new FileInputStream(aFn);
        } catch (Exception e) {
            log(ERROR, PKG + "[readsql(String)]: " + e.getMessage());
            return false;
        }
        result = sqlsession(myfile);
        try {
            if (myfile != null) {
                myfile.close();
            }
        } catch (IOException e) {
            ;
        }
        return result;
    }

    /**
 * Parse and execute sql statements.  Grammar:
 *
 * sqlsession     -> sql_statement* '<EOF>'
 *
 * sql_statement  -> stuff ';' '<EOL>'
 *             -> stuff '<EOL>' 'go' ( '<EOL>' | '<EOF>' )
 *             -> stuff '<EOL>' ';' ( '<EOL>' | '<EOF>' )
 *             -> stuff '<EOF>'
 *
 * Display prompts if session is interactive.
 *
 * @param aFh is the input stream containing the sql statements.
 * @return false if error getting connection
 */
    public boolean sqlsession(InputStream aFh) {
        String sqlbuf = "", lbuf = "";
        log(INFO, "InputStream is '" + aFh + "'");
        show_prompt();
        if (!sql_init_connection()) {
            log(ERROR, "sqlsession:  cannot get a database connection:  ABORT");
            return false;
        }
        do {
            lbuf = readline(aFh);
            if (sql_eoi(lbuf)) {
                if (lbuf != null) {
                    lbuf = delete_sql_eoi(lbuf);
                    sqlbuf += lbuf + "\n";
                }
                if (!whitespace(sqlbuf)) {
                    sql_exec(sqlbuf.toString());
                }
                if (lbuf != null) {
                    show_prompt();
                }
                sqlbuf = "";
            } else if (lbuf != null) {
                sqlbuf += lbuf + "\n";
            }
        } while (lbuf != null);
        sql_close_connection();
        return true;
    }

    /**
 * Execute a single sql statement.
 * @param anSqlbuf is the buffer containing the input.
 */
    public void sql_exec(String anSqlbuf) {
        log(INFO, anSqlbuf);
        try {
            mStatement = mConnection.createStatement();
            java.sql.ResultSet results = null;
            int updateCount = -1;
            if (mStatement.execute(anSqlbuf)) {
                results = mStatement.getResultSet();
                log(sql_results_to_string(results));
            } else {
                updateCount = mStatement.getUpdateCount();
                if (updateCount != -1) {
                    if (!mQUIET) {
                        log("update count " + updateCount);
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            log(ERROR, "Error while executing sql buffer: " + e.toString());
        }
    }

    /**
 * Translate ResultSet to String representation
 * @param aResultSet as ResultSet
 * @return String the resulting String
 */
    private String sql_results_to_string(java.sql.ResultSet aResultSet) {
        try {
            if (aResultSet == null) {
                return "(null)";
            }
            java.sql.ResultSetMetaData m = aResultSet.getMetaData();
            int col = m.getColumnCount();
            StringBuffer strbuf = new StringBuffer();
            int dispSize = -1;
            for (int i = 1; i <= col; i++) {
                strbuf = strbuf.append(m.getColumnLabel(i) + "\t");
            }
            strbuf = strbuf.append("\n");
            while (aResultSet.next()) {
                for (int i = 1; i <= col; i++) {
                    strbuf = strbuf.append(aResultSet.getString(i) + "\t");
                    if (aResultSet.wasNull()) {
                        strbuf = strbuf.append("(null)\t");
                    }
                }
                strbuf = strbuf.append("\n");
            }
            return strbuf.toString();
        } catch (java.sql.SQLException e) {
            return null;
        }
    }

    /**
 * open the jdbc connection.
 * @return true if successful.
 */
    public boolean sql_init_connection() {
        int nerrs = 0;
        try {
            mConnection = java.sql.DriverManager.getConnection(mURL, mUSER, mPASSWORD);
        } catch (java.sql.SQLException e) {
            log(ERROR, "Cannot get a connection:  " + e.toString());
            ++nerrs;
        }
        return (nerrs == 0);
    }

    /**
 * close the jdbc connection.
 */
    public void sql_close_connection() {
        if (mConnection == null) {
            return;
        }
        try {
            mConnection.close();
        } catch (java.sql.SQLException e) {
        }
    }

    /**
 * @return true if an end-of-input marker is found.
 * @param aLine is the current line buffer.
 */
    public boolean sql_eoi(String aLine) {
        boolean res = false;
        if (aLine == null) {
            res = true;
        } else {
            res = !(aLine.equals(delete_sql_eoi(aLine)));
        }
        log(DEBUG, "sql_eoi returned " + (res ? "true" : "false") + " aLine='" + (aLine == null ? "NULL" : aLine) + "'");
        return (res);
    }

    /**
 * @return new string without the sql end-of-input marker.
 * @param aLine is the current line buffer.
 */
    public String delete_sql_eoi(String aLine) {
        if (aLine == null) {
            return ("");
        }
        aLine = aLine.replaceFirst(";[ \t;]*$", "");
        aLine = aLine.replaceFirst("[ \t]*[gG][oO][ \t]*$", "");
        return (aLine);
    }

    /**
 * read a line from <aFh>.  strip EOL char sequences.
 * @return null if EOF, o'wise the line.
 * @param aFh is the input stream.
 */
    public String readline(InputStream aFh) {
        int bb = 0;
        char cc;
        long cnt = 0;
        StringBuffer abuf = new StringBuffer("");
        try {
            while ((bb = aFh.read()) != -1) {
                ++cnt;
                cc = (char) bb;
                if (cc == '\n' || cc == '\r') {
                    break;
                } else {
                    abuf.append(cc);
                }
            }
        } catch (Exception e) {
            log(ERROR, PKG + "[readline(InputStream)]: " + e.getMessage());
            return null;
        }
        log(DEBUG, "read " + cnt + " bytes" + " abuf='" + abuf + "'");
        if (bb != -1) {
            return (abuf.toString());
        }
        return (null);
    }

    /**
 * display the current input prompt string.
 */
    public void show_prompt() {
        if (mDOPROMPT) {
            System.out.print(mPROMPT);
        }
    }

    /**
 * @return true if <aStr> is only whitespace, empty, or null
 * @param aStr is the input string.
 */
    public static boolean whitespace(String aStr) {
        return (aStr == null || aStr.matches("[ \t\r\n]*"));
    }

    /**
 * output a message to stdout.
 * @param aMsg is the message.
 */
    private static void log(String aMsg) {
        System.out.println(aMsg);
    }

    /**
 * output a diagnostic message to stderr, if <aLevel> meets the
 * the current logging level.
 * @param aLevel is the level of the message (error, debug, or info).
 * @param aMsg is the message.
 */
    private void log(int aLevel, String aMsg) {
        boolean showit = (mDEBUG & (aLevel >= DEBUG)) | (mVERBOSE & (aLevel >= INFO)) | (mSHOWERRORS & (aLevel == ERROR));
        if (showit) {
            System.err.println(sLevelmsg[aLevel] + ": " + aMsg);
        }
    }

    /**
 * turn error logging off
 */
    public void errorsOff() {
        mSHOWERRORS = false;
    }

    /**
 * turn error logging on
 */
    public void errorsOn() {
        mSHOWERRORS = true;
    }

    /**
 * print a usage message, and return <aStatus>.
 * @param aStatus - pass status through as a notational convenience.
 * @return aStatus
 */
    public static int usage(int aStatus) {
        String usage[] = { "Usage: " + PKG + " [options] [file...]", "", "Creates a new database connection and runs each", "sql file provided on the command line.  If none", "present, then prompts for sql statements on stdin.", "", "Sql statements will be executed when the input contains a", "\"go\" or \";\" command delimiter. These delimiters must", "appear at the end of the line or alone on a line. If no", "delimiters are provided, then statements are executed at EOF.", "", "Options:", " -help          display this help message", " -verbose       display information messages", " -debug         display debug messages", " -q             quiet mode.  do not display prompt if reading from stdin", " -prompt string use <string> as prompt instead of default '" + sMain.mPROMPT + "'.", "", " -user name     username used for connection", " -password name password for this user", " -driver classname", "                name of the driver class", " -url name      jdbc url", "", "Example:", "", " " + PKG + " -url \"jdbc:oracle:thin:@pipit.sfbay.sun.com:1521:FORTE\"", "     -driver oracle.jdbc.driver.OracleDriver -user system -password manager foo.sql", "", "Environment:", "", " CLASSPATH      you must have the database jdbc driver in your classpath", "" };
        for (int ii = 0; ii < usage.length; ii++) {
            log(usage[ii]);
        }
        return (aStatus);
    }

    /**
 * parse command line arguments 
 * @param anArgList - the arguments
 * @return true if successful.
 */
    public static boolean parse_args(String anArgList[]) {
        String arg;
        int ii = 0;
        while (ii < anArgList.length) {
            arg = anArgList[ii].toLowerCase();
            if (arg.startsWith("-h")) {
                sSHOWHELP = true;
                return true;
            } else if (arg.startsWith("-v")) {
                sMain.mVERBOSE = true;
                sMain.mQUIET = false;
            } else if (arg.startsWith("-q")) {
                sMain.mVERBOSE = false;
                sMain.mQUIET = true;
            } else if (arg.startsWith("-url")) {
                if (ii + 1 < anArgList.length) {
                    sMain.mURL = anArgList[++ii];
                } else {
                    sMain.log(ERROR, PKG + ":  -url requires a value.");
                    return false;
                }
                if (sMain.mURL.startsWith("-")) {
                    sMain.log(ERROR, PKG + ":  -url parameter '" + sMain.mURL + "' is invalid.");
                    return false;
                }
            } else if (arg.startsWith("-dr")) {
                if (ii + 1 < anArgList.length) {
                    sMain.mDRIVER = anArgList[++ii];
                } else {
                    sMain.log(ERROR, PKG + ":  -driver requires a value.");
                    return false;
                }
                if (sMain.mDRIVER.startsWith("-")) {
                    sMain.log(ERROR, PKG + ":  -driver parameter '" + sMain.mDRIVER + "' is invalid.");
                    return false;
                }
            } else if (arg.startsWith("-u")) {
                if (ii + 1 < anArgList.length) {
                    sMain.mUSER = anArgList[++ii];
                } else {
                    sMain.log(ERROR, PKG + ":  -user requires a value.");
                    return false;
                }
                if (sMain.mUSER.startsWith("-")) {
                    sMain.log(ERROR, PKG + ":  -user parameter '" + sMain.mUSER + "' is invalid.");
                    return false;
                }
            } else if (arg.startsWith("-prompt")) {
                if (ii + 1 < anArgList.length) {
                    sMain.mPROMPT = anArgList[++ii];
                } else {
                    sMain.log(ERROR, PKG + ":  -prompt requires a value.");
                    return false;
                }
                if (sMain.mPROMPT.startsWith("-")) {
                    sMain.log(ERROR, PKG + ":  -prompt parameter '" + sMain.mPROMPT + "' is invalid.");
                    return false;
                }
            } else if (arg.startsWith("-p")) {
                if (ii + 1 < anArgList.length) {
                    sMain.mPASSWORD = anArgList[++ii];
                } else {
                    sMain.log(ERROR, PKG + ":  -password requires a value.");
                    return false;
                }
                if (sMain.mPASSWORD.startsWith("-")) {
                    sMain.log(ERROR, PKG + ":  -password parameter '" + sMain.mPASSWORD + "' is invalid.");
                    return false;
                }
            } else if (arg.startsWith("-d")) {
                sMain.mDEBUG = true;
            } else if (arg.startsWith("-")) {
                sMain.log(ERROR, PKG + ":  unrecognized arg, '" + arg + "'.");
                return false;
            } else {
                break;
            }
            ++ii;
        }
        if (sMain.mURL == null || sMain.mDRIVER == null || sMain.mUSER == null) {
            sMain.log(ERROR, PKG + ":  missing a required parameter (-user, -driver, or -url)");
            return false;
        }
        if (sMain.mPASSWORD == null) {
            sMain.mPASSWORD = "";
        }
        while (ii < anArgList.length && anArgList[ii].equals("")) {
            ++ii;
        }
        if (ii >= anArgList.length) {
            sUSESTDIN = true;
            if (!sMain.mQUIET) {
                sMain.mDOPROMPT = true;
            }
            return true;
        }
        int nempty = 0;
        for (int jj = ii; jj < anArgList.length; jj++) {
            if (anArgList[jj].equals("")) {
                ++nempty;
            }
        }
        sFILES = new String[anArgList.length - (ii + nempty)];
        int jj = 0;
        while (ii < anArgList.length) {
            if (!anArgList[ii].equals("")) {
                sFILES[jj++] = anArgList[ii];
            }
            ++ii;
        }
        if (sMain.mVERBOSE) {
            sMain.mDOPROMPT = true;
        }
        return true;
    }

    /**
 *
 */
    private static final String PKG = "Jsql";

    /**
 *
 */
    private String mPROMPT = PKG + "> ";

    /**
 *
 */
    private boolean mVERBOSE = false;

    /**
 *
 */
    private boolean mQUIET = false;

    /**
 * 
 */
    private boolean mDEBUG = false;

    /**
 * 
 */
    private boolean mSHOWERRORS = true;

    /**
 *
 */
    private boolean mDOPROMPT = false;

    /**
 * tmp variable used to init level message array
 */
    private static int sXX = 0;

    /**
 *
 */
    private static final int DEBUG = sXX++, INFO = sXX++, ERROR = sXX++;

    /**
 * names of the log levels
 */
    private static String[] sLevelmsg = { "DEBUG", " INFO", "ERROR" };

    /**
 * class variable used by main entry
 */
    private static Jsql sMain = null;

    /**
 * array of file names to be run by main
 */
    private static String[] sFILES;

    /**
 *
 */
    private static boolean sSHOWHELP = false;

    /**
 *
 */
    private static boolean sUSESTDIN = false;

    /**
 *
 */
    private java.sql.Connection mConnection;

    /**
 *
 */
    private java.sql.Statement mStatement;

    /**
 *
 */
    private String mURL = null;

    /**
 *
 */
    private String mDRIVER = null;

    /**
 *
 */
    private String mUSER = null;

    /**
 *
 */
    private String mPASSWORD = null;

    /**
 * Jsql class constructor.
 *
 * @param aUrl is the jdbc url.
 * @param aDriver is the jdbc driver class.
 * @param aUser is the database user name.
 * @param aPassword is the user's password.
 */
    public Jsql(String aUrl, String aDriver, String aUser, String aPassword) {
        mURL = aUrl;
        mDRIVER = aDriver;
        mUSER = aUser;
        mPASSWORD = aPassword;
    }

    /**
 * private Jsql class constructor - used when called thru main() entry point.
 *
 */
    private Jsql() {
    }
}
