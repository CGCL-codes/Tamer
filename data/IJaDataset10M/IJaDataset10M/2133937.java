package com.lemckes.cellarboss.util;

import com.lemckes.cellarboss.Bottle;
import com.lemckes.cellarboss.IdName;
import com.lemckes.cellarboss.WineList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods for database
 */
public class DbUtils {

    public static final int CURRENT_VERSION = 3;

    /**
     * Create all the tables needed for the winelist database
     * @param connection the connection to the database
     * @throws SQLException
     */
    public static void createDatabaseTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String wineries = "CREATE TABLE wineries (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT wineries_id_pk PRIMARY KEY," + "name character varying(40) NOT NULL" + " CONSTRAINT wineries_name_check CHECK (name <> '')" + ")";
        statement.execute(wineries);
        String regions = "CREATE TABLE regions (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT regions_id_pk PRIMARY KEY," + "name character varying(40) NOT NULL" + " CONSTRAINT regions_name_check CHECK (name <> '')" + ")";
        statement.execute(regions);
        String varieties = "CREATE TABLE varieties (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT varieties_id_pk PRIMARY KEY," + "name character varying(40) NOT NULL" + " CONSTRAINT varieties_name_check CHECK (name <> '')" + ")";
        statement.execute(varieties);
        String locations = "CREATE TABLE locations (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT locations_id_pk PRIMARY KEY," + "name character varying(20) NOT NULL" + " CONSTRAINT locations_name_check CHECK (name <> '')" + ")";
        statement.execute(locations);
        String styles = "CREATE TABLE styles (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT styles_id_pk PRIMARY KEY," + "name character varying(20) NOT NULL" + " CONSTRAINT styles_name_check CHECK (name <> '')" + ")";
        statement.execute(styles);
        String defaultStyles = "CREATE TABLE default_styles (" + "variety_id bigint NOT NULL " + " CONSTRAINT default_styles_id_pk PRIMARY KEY," + "style_id bigint NOT NULL" + ")";
        statement.execute(defaultStyles);
        String defaultRegions = "CREATE TABLE default_regions (" + "winery_id bigint NOT NULL " + " CONSTRAINT default_regions_id_pk PRIMARY KEY," + "region_id bigint NOT NULL" + ")";
        statement.execute(defaultRegions);
        String bottles = "CREATE TABLE bottles (" + "id bigint NOT NULL GENERATED ALWAYS AS IDENTITY" + " CONSTRAINT bottles_id_pk PRIMARY KEY," + "winery bigint NOT NULL CONSTRAINT winery_fk REFERENCES wineries," + "region bigint NOT NULL CONSTRAINT region_fk REFERENCES regions," + "name character varying(40)," + "variety bigint NOT NULL CONSTRAINT variety_fk REFERENCES varieties," + "vintage smallint," + "keep_til smallint," + "quantity smallint," + "location bigint NOT NULL CONSTRAINT location_fk REFERENCES locations," + "style bigint NOT NULL CONSTRAINT style_fk REFERENCES styles," + "price smallint," + "notes character varying(80)" + ")";
        statement.execute(bottles);
        String schemaVersion = "CREATE TABLE schema_version (" + "version bigint NOT NULL " + ")";
        statement.execute(schemaVersion);
        String setVersion = "INSERT INTO schema_version values(" + CURRENT_VERSION + ")";
        statement.execute(setVersion);
    }

    /**
     * Retrieve the schema version from the database
     * @param connection the connection to the database
     * @return the schema version number
     * @throws SQLException if a statement cannot be created
     */
    public static int getDatabaseVersion(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        int dbVersion = 1;
        try {
            String schemaVersion = "SELECT version from schema_version";
            ResultSet rs = statement.executeQuery(schemaVersion);
            if (rs.next()) {
                dbVersion = rs.getInt("version");
            }
        } catch (SQLException ex) {
        }
        return dbVersion;
    }

    /**
     * Update the database from schema version <code>from</code>
     * to schema version <code>to</code>
     * @param connection the database connection
     * @param from the current schema version
     * @param to the target schema version
     * @throws SQLException
     */
    public static void updateSchema(Connection connection, int from, int to) throws SQLException {
        for (int i = from; i < to; ++i) {
            if (i == 1) {
                updateFrom1to2(connection);
            }
            if (i == 2) {
                updateFrom2to3(connection);
            }
            if (i == 3) {
                updateFrom3to4(connection);
            }
        }
        Statement statement = connection.createStatement();
        String setVersion = "UPDATE schema_version set version=" + CURRENT_VERSION;
        statement.execute(setVersion);
    }

    private static void updateFrom1to2(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String regions = "ALTER TABLE regions ALTER name SET DATA TYPE VARCHAR(40)";
        statement.execute(regions);
        String schemaVersion = "CREATE TABLE schema_version (" + "version bigint NOT NULL " + ")";
        statement.execute(schemaVersion);
        String setVersion = "INSERT INTO schema_version values(2)";
        statement.execute(setVersion);
    }

    private static void updateFrom2to3(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            String defaultStyles = "CREATE TABLE default_styles (" + "variety_id bigint NOT NULL " + " CONSTRAINT default_styles_id_pk PRIMARY KEY," + "style_id bigint NOT NULL" + ")";
            statement.execute(defaultStyles);
        } catch (SQLException e) {
            if (((e.getErrorCode() == 30000) && ("X0Y32".equals(e.getSQLState())))) {
            } else {
                throw (e);
            }
        }
        try {
            String defaultRegions = "CREATE TABLE default_regions (" + "winery_id bigint NOT NULL " + " CONSTRAINT default_regions_id_pk PRIMARY KEY," + "region_id bigint NOT NULL" + ")";
            statement.execute(defaultRegions);
        } catch (SQLException e) {
            if (((e.getErrorCode() == 30000) && ("X0Y32".equals(e.getSQLState())))) {
            } else {
                throw (e);
            }
        }
    }

    private static void updateFrom3to4(Connection connection) throws SQLException {
    }

    /**
     * Close the given statement ignoring any exceptions
     * @param s
     */
    public static void closeStatement(Statement s) {
        if (s != null) {
            try {
                s.close();
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * Get a list of the messages of all the exceptions that are chained to <code>ex</code>
     * @param ex the base exception
     * @return the list a chained exception messages
     */
    public static ArrayList<String> getMessages(SQLException ex) {
        ArrayList<String> messages = new ArrayList<String>();
        messages.add(ex.getLocalizedMessage());
        SQLException nestedEx = ex.getNextException();
        while (nestedEx != null) {
            messages.add(nestedEx.getLocalizedMessage());
            nestedEx = nestedEx.getNextException();
        }
        return messages;
    }

    /**
     * Log the messages associated with the given SQLException
     * @param logClass the class that is reporting the exception
     * @param messagePrefix the message that is shown before the SQLException messages
     * @param ex the exception to be logged
     */
    public static void logSQLException(Class logClass, String messagePrefix, SQLException ex) {
        ArrayList<String> messages = DbUtils.getMessages(ex);
        StringBuilder sbLog = new StringBuilder();
        for (String m : messages) {
            sbLog.append(m);
            sbLog.append("\n");
        }
        Logger.getLogger(logClass.getName()).log(Level.SEVERE, messagePrefix + " - {0}", sbLog);
    }

    /**
     * Dump the contents of the database tables to stdout
     * @param wineList
     */
    public static void dumpData(WineList wineList) {
        System.out.println("Schema Version: " + CURRENT_VERSION);
        System.out.println("Wineries:");
        for (IdName n : wineList.getWineries()) {
            Integer defaultRegion = wineList.getDefaultRegion(n.getId());
            String region = "";
            if (defaultRegion != null) {
                region = wineList.getRegions().get(defaultRegion).getName();
            }
            System.out.println(n.getId() + " - " + n.getName() + " @ " + region);
        }
        System.out.println("Regions:");
        for (IdName n : wineList.getRegions()) {
            System.out.println(n.getId() + " - " + n.getName());
        }
        System.out.println("Varieties:");
        for (IdName n : wineList.getVarieties()) {
            Integer defaultStyle = wineList.getDefaultStyle(n.getId());
            String style = "";
            if (defaultStyle != null) {
                style = wineList.getStyles().get(defaultStyle).getName();
            }
            System.out.println(n.getId() + " - " + n.getName() + "(" + style + ")");
        }
        System.out.println("Styles:");
        for (IdName n : wineList.getStyles()) {
            System.out.println(n.getId() + " - " + n.getName());
        }
        System.out.println("Locations:");
        for (IdName n : wineList.getLocations()) {
            System.out.println(n.getId() + " - " + n.getName());
        }
        System.out.println("Bottles:");
        for (Bottle b : wineList.getBottles()) {
            System.out.println(b.getWinery() + ", " + b.getName() + ", " + b.getVintage() + ", " + b.getVariety());
        }
    }
}
