package org.dbunit;

import org.dbunit.operation.DatabaseOperation;
import org.dbunit.testutil.TestUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Manuel Laflamme
 * @version $Revision: 1162 $
 * @since Feb 18, 2002
 */
public class H2Environment extends DatabaseEnvironment {

    public H2Environment(DatabaseProfile profile) throws Exception {
        super(profile);
        File ddlFile = TestUtils.getFile("sql/h2.sql");
        Connection connection = getConnection().getConnection();
        executeDdlFile(ddlFile, connection);
    }

    public static void executeDdlFile(File ddlFile, Connection connection) throws Exception {
        BufferedReader sqlReader = new BufferedReader(new FileReader(ddlFile));
        StringBuffer sqlBuffer = new StringBuffer();
        while (sqlReader.ready()) {
            String line = sqlReader.readLine();
            if (!line.startsWith("-")) {
                sqlBuffer.append(line);
            }
        }
        String sql = sqlBuffer.toString();
        executeSql(connection, sql);
    }

    public static void executeSql(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute(sql);
        } finally {
            statement.close();
        }
    }

    public static Connection createJdbcConnection(String databaseName) throws Exception {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:" + databaseName, "sa", "");
        return connection;
    }

    public void closeConnection() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getInitDataSet());
    }

    public static void shutdown(Connection connection) throws SQLException {
        executeSql(connection, "SHUTDOWN IMMEDIATELY");
    }

    public static void deleteFiles(final String filename) {
        deleteFiles(new File("."), filename);
    }

    public static void deleteFiles(File directory, final String filename) {
        File[] files = directory.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.indexOf(filename) != -1) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            file.delete();
        }
    }
}
