package uk.org.ogsadai.database.jdbc.book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import uk.org.ogsadai.database.jdbc.book.JDBCBookDataCreator;

/**
 * Class to create the standard OGSA-DAI
 * "littleblackbook"-style tables in Oracle.
 *
 * @author The OGSA-DAI Project Team.
 */
public class OracleBookDataCreator extends JDBCBookDataCreator {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) International Business Machines Corporation, 2002-2005, Copyright (c) The University of Edinburgh, 2002-2010.";

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(Connection connection, String table) throws SQLException {
        Statement checkTable = connection.createStatement();
        ResultSet resultSet = checkTable.executeQuery("select count(*) from all_tables where table_name = '" + table.toUpperCase() + "'");
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
