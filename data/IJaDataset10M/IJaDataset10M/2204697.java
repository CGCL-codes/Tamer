package edu.ucdavis.genomics.metabolomics.util.database;

import edu.ucdavis.genomics.metabolomics.util.SQLObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author wohlgemuth
 */
public class UserUtils extends SQLObject {

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException, SQLException {
        if (args.length == 1) {
            new UserUtils().executeCommandFile(args[0], ";");
        }
    }

    /**
     * l?scht den tabellen inhalt
     *
     */
    public void clearTable() throws IOException, SQLException {
        executeCommandFile("sql/clear.sql", "//");
    }

    /**
     * l?scht einen userspace
     *
     * @param name
     * @throws SQLException
     */
    public void dropUserSpace(String name) throws SQLException {
        Savepoint f = null;
        try {
            this.getConnection().setAutoCommit(false);
            f = this.getConnection().setSavepoint("before");
            this.getConnection().createStatement().execute("drop user " + name);
        } catch (SQLException e) {
            this.getConnection().rollback(f);
            throw e;
        }
    }

    /**
     * f?hrt einen sql file aus
     *
     * @param file
     * @param seperator
     * @throws IOException
     * @throws SQLException
     */
    public void executeCommandFile(String file, String seperator) throws IOException, SQLException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--") == false) {
                    buffer.append(" " + line);
                }
            }
            String[] token = buffer.toString().split(seperator);
            this.getConnection().setAutoCommit(false);
            this.getConnection().commit();
            for (int i = 0; i < token.length; i++) {
                String currenttoken = token[i];
                System.out.println("currentline: " + currenttoken);
                try {
                    this.getConnection().createStatement().execute(currenttoken);
                    System.out.println("success");
                } catch (Exception e) {
                    System.out.println("failure " + e.getMessage());
                }
            }
            this.getConnection().commit();
        } catch (SQLException e) {
            System.out.println("failed");
            this.getConnection().rollback();
            throw e;
        }
    }

    /**
     * initialisiert die tabellen
     *
     */
    public void initTable() throws IOException, SQLException {
        executeCommandFile("sql/init.sql", "//");
    }
}
