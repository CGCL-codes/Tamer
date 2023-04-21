package db.humanoid;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import db.DbManager;

public class HumanoidTables {

    private static Logger log = Logger.getLogger("HumanoidTables");

    /**
	 * Initializes the humanoid table.
	 */
    public static void initHumanoidTable(Connection conn) {
        log.log(Level.INFO, "initHumanoidTable()");
        conn = DbManager.getConnection();
        Statement stmt = null;
        String query = null;
        try {
            stmt = conn.createStatement();
            query = "";
            stmt.executeQuery(query);
            log.info(query);
        } catch (SQLException e) {
            if (e.getErrorCode() != -21) {
                JOptionPane.showMessageDialog(null, "Die Tabelle humanoid existiert bereits.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
	 * Initializes all tables in the database.
	 */
    public static void initTables(Connection conn) {
        log.log(Level.INFO, "initDB()");
        HumanoidTables.initHumanoidTable(conn);
    }

    /**
	 * Deletes the table "".
	 */
    public static void dropHumanoidTable(Connection conn) {
        log.log(Level.INFO, "dropHumanoidTable()");
        conn = DbManager.getConnection();
        Statement stmt = null;
        String query = null;
        try {
            stmt = conn.createStatement();
            query = "DROP TABLE humanoid CASCADE";
            stmt.executeQuery(query);
            log.info(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Es trat ein Fehler beim L�schen der Tabelle humanoid auf.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
	 * Deletes all db tables.
	 */
    public static void dropTables(Connection conn) {
        log.log(Level.INFO, "dropTables()");
        HumanoidTables.dropHumanoidTable(conn);
    }
}
