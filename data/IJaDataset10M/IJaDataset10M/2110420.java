package uk.org.ogsadai.database.jdbc.extbook;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uk.org.ogsadai.database.jdbc.extbook.JDBCExtendedBookDataCreator;

/**
 * Class to create the extended OGSA-DAI
 * "littleblackbook"-style tables in PostgreSQL.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PostgreSQLExtendedBookDataCreator extends JDBCExtendedBookDataCreator {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) International Business Machines Corporation, 2002-2005, Copyright (c) The University of Edinburgh, 2002-2010.";

    /**
     * Constructor. If this is used then {@link #setDataDirectory} must
     * be called before any other method.
     */
    public PostgreSQLExtendedBookDataCreator() {
    }

    /**
     * Constructor.
     *
     * @param dataDirectory
     *     Directory with source data. 
     * @throws IOException
     *     If any problems arise.
     */
    public PostgreSQLExtendedBookDataCreator(String dataDirectory) throws IOException {
        super(dataDirectory);
    }

    /**
     * {@inheritDoc}
     */
    protected String getCreateTableSQL(String table) {
        return "CREATE TABLE " + table + " (id INTEGER, name VARCHAR(64), address VARCHAR(128), " + "phone VARCHAR(20), dob DATE, creationTimestamp TIMESTAMP, " + "timeofbirth TIME, bio TEXT," + "picture BYTEA, religious BOOLEAN, height REAL, " + "verylongnumber BIGINT, doubleformatnumber DOUBLE PRECISION)";
    }

    /**
     * {@inheritDoc}
     */
    public void drop(Connection connection, String table) throws SQLException {
        Statement checkTable = connection.createStatement();
        ResultSet resultSet = checkTable.executeQuery("SELECT COUNT(*) " + "FROM pg_class WHERE relname='" + table + "'");
        boolean tableExists = false;
        if (resultSet.next()) {
            if (Integer.parseInt(resultSet.getString(1)) == 1) {
                tableExists = true;
            }
        }
        resultSet.close();
        checkTable.close();
        if (tableExists) {
            Statement drop = connection.createStatement();
            drop.execute("DROP TABLE " + table);
            drop.close();
        }
    }
}
