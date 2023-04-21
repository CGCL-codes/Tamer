package SASLib.SQL;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provide simple sql support for java.
 * @author Wil Cecil
 */
public class SQLManager {

    SQLLoad loader;

    /**
     * Holds trings like <br>
     * <code>
     * "CREATE TABLE default ( id INT IDENTITY, hash INT )"<br></code>
     * and <code>
     * "CREATE TABLE example ( id IDENTITY, pk_id INT , CONSTRAINT FK_locs 
     * FOREIGN KEY ( simu_id ) REFERENCES simu (id) )"<br></code>
     */
    LinkedList<String> tables;

    /** 
     * Creates a new instance of SQLManager with a default SQLLoad. <br>
     * usually a bad idea...
     */
    public SQLManager() {
        try {
            loader = new SQLLoad();
            loader.activate();
        } catch (Exception e) {
        }
        tables = new LinkedList<String>();
    }

    /** 
     * Creates a new instance of SQLManager with a pre existing SQLLoad
     * @param load this is the sqlloader to use
     */
    public SQLManager(SQLLoad load) {
        loader = load;
        tables = new LinkedList<String>();
    }

    /**
     * Generates Tables that are predefined in tables.
     */
    public void generateTables() {
        try {
            Iterator<String> iter = tables.iterator();
            while (iter.hasNext()) {
                loader.getStatement().execute(iter.next());
            }
        } catch (SQLException ex) {
            new SASLib.Util.GUI.MessageBox("Error From DOIT...\n" + ex.toString());
            sqlError(ex);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() throws Exception {
        return loader.getStatement();
    }

    public Connection getConnection() throws Exception {
        return loader.getConnection();
    }

    /**
     * attempts to parse a SQL File and send commands to server
     * @param f file to read, is a file with ; delimited sql commands
     * @param print if true displays commands as executed
     * @return false if an exception occured else true.
     */
    public boolean doFile(File f, boolean print) {
        try {
            Statement stmt = getStatement();
            Scanner s = new Scanner(f);
            int index1 = -1, index2 = -1;
            s.useDelimiter(";");
            while (s.hasNext()) {
                String str = s.next();
                index1 = str.indexOf("\n-- ");
                if (index1 != -1) {
                    index2 = str.indexOf("\n");
                    str = str.substring(0, index1) + str.substring(index2 - 1);
                }
                str = str.trim();
                if (str.length() > 0) {
                    if (print) {
                        System.out.println(str);
                    }
                    stmt.execute(str);
                }
            }
        } catch (SQLException ex) {
            new SASLib.Util.GUI.MessageBox("Error From DOIT...\n" + ex.toString());
            sqlError(ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(SQLManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * COMMIT's And SHUTDOWN Command issued if they fail a 
     * SHUTDOWN IMMEDIATELY command is sent.
     * @return false only if SHUTDOWN IMMEDIATELY fails else true
     */
    public boolean Shutdown() {
        try {
            getStatement().execute("COMMIT");
            getStatement().execute("SHUTDOWN");
        } catch (Exception e) {
            try {
                System.err.println("COMMIT / SHUTDOWN FAILED");
                getStatement().execute("SHUTDOWN IMMEDIATELY");
            } catch (Exception e2) {
                return false;
            }
        }
        return true;
    }

    /**
     * This is the default way to handle a SQL Error within this Project.
     * @param ex 
     */
    public static void sqlError(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState:     " + ex.getSQLState());
        System.out.println("VendorError:  " + ex.getErrorCode());
        ex.printStackTrace();
    }

    /**
     * This method is used to run anysql command and if it has a ResultSet to display it
     * @param sqlcommand
     * @return boolean  true if no exceptions encountered
     */
    public boolean doit(String sqlcommand) {
        try {
            ResultSet rs = getStatement().executeQuery(sqlcommand);
            String message = "";
            while (!rs.isLast()) {
                rs.next();
                int i = rs.getMetaData().getColumnCount();
                for (int j = 1; j <= i; j++) {
                    message += rs.getString(j) + " | ";
                }
                message += "\n";
            }
            new SASLib.Util.GUI.MessageBox(message);
        } catch (SQLException ex) {
            new SASLib.Util.GUI.MessageBox("Error From DOIT...\n" + ex.toString());
            sqlError(ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(SQLManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /** 
     * Preforms BOOLEAN Bit eval, returns 1 if true 0 else
     * @param b
     * @return 1 if true else 0
     */
    public static int eval(boolean b) {
        if (b) return 1;
        return 0;
    }
}
