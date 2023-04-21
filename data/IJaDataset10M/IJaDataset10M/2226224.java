package de.derbsen.jkangoo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 * @author Niels
 * @version Created ${Date}
 */
public class Setup {

    private static Logger log = Logger.getLogger(Setup.class);

    public static void createTables() throws KangooException {
        Connection conn = Config.getDBConnection();
        Statement stmnt = null;
        try {
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE packages IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE packages (packageId INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255), projectId INTEGER)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE projects IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE projects (projectId INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255), description LONGVARCHAR)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE emailnotifications IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE emailnotifications (emailnotificationId INTEGER IDENTITY PRIMARY KEY, projectId INTEGER, email VARCHAR(255))");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE releases IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE releases (releaseId INTEGER IDENTITY PRIMARY KEY, created DATETIME, projectId INTEGER, name VARCHAR(255), releasenotes LONGVARCHAR, changelog LONGVARCHAR)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE files_release IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE files_release (fileId INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255), releaseId INTEGER, filetype INTEGER)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE files_project IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE files_project (fileId INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255), projectId INTEGER, filetype INTEGER)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE files_global IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE files_global (fileId INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255), description VARCHAR(255), filetype INTEGER)");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("DROP TABLE releases_ref_files_global IF EXISTS");
            stmnt.close();
            stmnt = conn.createStatement();
            stmnt.execute("CREATE TABLE releases_ref_files_global (refId INTEGER IDENTITY, releaseId INTEGER, fileId INTEGER)");
            stmnt.close();
            conn.commit();
        } catch (SQLException e) {
            throw new KangooException("Error creating database. Unable to rollback.", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new KangooException("Error closing connection", e);
            }
        }
    }
}
