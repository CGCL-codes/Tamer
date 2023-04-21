package oxygen.tool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Copies information from one Database to another.
 *
 * @author  Ugorji Dick-Nwoke ugorji.dick-nwoke@bea.com
 * @version 1.0, August 3, 2001
 */
public class DBCleanup {

    private static final Date logdate = new Date();

    private static final SimpleDateFormat logdateFmt = new SimpleDateFormat("HH:mm:ss:SSS");

    private List tablematchstrings = new ArrayList();

    private List tables = new ArrayList();

    private String driver;

    private String url;

    private String user;

    private String password;

    private String action = "delete";

    private static String HELP_MESSAGE = null;

    static {
        String lsep = System.getProperty("line.separator");
        HELP_MESSAGE = "Usage: " + lsep + "java DBCleanup \\" + lsep + "  -driver <driverclassname> \\" + lsep + "  -url <url> \\" + lsep + "  -username <user> \\" + lsep + "  -password <pass> \\" + lsep + "  -action <delete|drop|list> \\" + lsep + "  -tables <table ...> \\" + lsep + "";
    }

    public DBCleanup() {
    }

    public void run() throws Exception {
        if (driver != null) Class.forName(driver);
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            for (Iterator itr = tables.iterator(); itr.hasNext(); ) {
                String tablename = (String) itr.next();
                actOnSingleTable(tablename, conn, stmt);
            }
            DatabaseMetaData dmd = conn.getMetaData();
            String[] tabletypes = new String[] { "TABLE" };
            List tables2 = new ArrayList();
            for (Iterator itr = tablematchstrings.iterator(); itr.hasNext(); ) {
                String pattern = (String) itr.next();
                ResultSet rs = dmd.getTables(null, null, pattern, tabletypes);
                while (rs.next()) {
                    String tablename = rs.getString("TABLE_NAME");
                    tables2.add(tablename);
                }
                rs.close();
            }
            for (Iterator itr = tables2.iterator(); itr.hasNext(); ) {
                String tablename = (String) itr.next();
                actOnSingleTable(tablename, conn, stmt);
            }
        } finally {
            close(stmt);
            close(conn);
        }
    }

    private void actOnSingleTable(String tablename, Connection conn, Statement stmt) throws Exception {
        String query = null;
        boolean doSQL = false;
        boolean doList = false;
        if ("delete".equals(action)) {
            query = "delete from " + tablename;
            doSQL = true;
        } else if ("drop".equals(action)) {
            query = "drop table " + tablename;
            doSQL = true;
        } else if ("list".equals(action)) {
            query = "table name: " + tablename;
            doList = true;
        }
        try {
            if (doSQL) {
                System.out.println("Executing: " + query + " ... ");
                int x = stmt.executeUpdate(query);
                System.out.println("Num actions (e.g. rows deleted): " + x);
            }
            if (doList) {
                System.out.println(query);
            }
        } catch (Throwable thr) {
            System.out.println("Exception caught: " + thr);
        } finally {
            System.out.println("");
        }
    }

    public void setDriver(String s) {
        driver = s;
    }

    public void setUrl(String s) {
        url = s;
    }

    public void setUser(String s) {
        user = s;
    }

    public void setPassword(String s) {
        password = s;
    }

    public void setAction(String s) {
        action = s;
    }

    public void addTable(String s) {
        tables.add(s);
    }

    public void addTableMatchStrings(String s) {
        tablematchstrings.add(s);
    }

    public static final void close(Connection arg) {
        try {
            if (arg != null) arg.close();
        } catch (Throwable exc) {
            exc.printStackTrace();
        }
    }

    public static final void close(Statement arg) {
        try {
            if (arg != null) arg.close();
        } catch (Throwable exc) {
            exc.printStackTrace();
        }
    }

    public static final void close(ResultSet arg) {
        try {
            if (arg != null) arg.close();
        } catch (Throwable exc) {
            exc.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println(HELP_MESSAGE);
            return;
        }
        int UNKNOWN = 1;
        int ADDING_TABLES = 2;
        int ADDING_TABLE_MATCH_STRINGS = 3;
        int state = UNKNOWN;
        DBCleanup dbc = new DBCleanup();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-driver")) {
                dbc.setDriver(args[++i]);
            } else if (args[i].equals("-url")) {
                dbc.setUrl(args[++i]);
            } else if (args[i].equals("-user")) {
                dbc.setUser(args[++i]);
            } else if (args[i].equals("-password")) {
                dbc.setPassword(args[++i]);
            } else if (args[i].equals("-action")) {
                dbc.setAction(args[++i]);
            } else if (args[i].equals("-tables")) {
                state = ADDING_TABLES;
            } else if (args[i].equals("-tablematchstrings")) {
                state = ADDING_TABLE_MATCH_STRINGS;
            } else if (state == ADDING_TABLES) {
                dbc.addTable(args[i]);
            } else if (state == ADDING_TABLE_MATCH_STRINGS) {
                dbc.addTableMatchStrings(args[i]);
            }
        }
        dbc.run();
    }
}
