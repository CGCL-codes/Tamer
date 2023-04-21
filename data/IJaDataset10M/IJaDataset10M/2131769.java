package com.pointcarbon.maven.plugins.utplsql.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import java.io.File;
import java.sql.*;
import com.pointcarbon.maven.plugins.utplsql.UtlPlsqlTestResult;

/**
 * $Id: TestMojo.java 3 2009-06-25 15:00:00Z thedotedge $
 *
 * @author olegtopchiy
 * @goal test
 * @requiresProject true
 * @created 2009-06-18
 */
public class TestMojo extends AbstractMojo {

    /**
     * @parameter expression="${utplsql.connectionString}" default-value="oracle.jdbc.driver.OracleDriver"
     * @description driver class to use, defaults to oracle.jdbc.driver.OracleDriver
     * @phase
     * @required
     */
    private String driver;

    /**
     * @parameter expression="${utplsql.connectionString}"
     * @description Oracle connection string, i.e. jdbc:oracle:thin:sandbox/sandbox@poc-t-dwh01/varehus
     * @phase
     * @required
     */
    private String connectionString;

    /**
     * @parameter expression="${project.build.directory}"
     * @description location to which we will write the report file, defaults to the Maven /target directory of the project.
     */
    private File outputDirectory;

    private static final String SQL_RUN_PACKAGE = "begin utplsql.test(?, recompile_in => FALSE); ? := utplsql2.runnum; end;";

    private static final String SQL_REPORT_BY_RUNID = "select status, description from utr_outcome where run_id = ?";

    private static final String SQL_VERSION = "{ ? = call utplsql.version }";

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Connecting to: " + connectionString);
        Connection conn = null;
        try {
            getLog().info("Trying to use JDBC driver: " + driver);
            Class.forName(driver);
            conn = DriverManager.getConnection(connectionString);
            getLog().info("Connected to " + conn.getMetaData().getDatabaseProductVersion());
            try {
                CallableStatement cs = conn.prepareCall(SQL_VERSION);
                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.execute();
                getLog().info("Found utplsql version " + cs.getObject(1));
            } catch (SQLException e) {
                throw new MojoExecutionException("Unable to find utplsql in target database, please make sure it's installed correctly.\n" + "Please see http://utplsql.sourceforge.net/Doc/fourstep.html for more information.");
            }
            int runId = runTestPackage(conn, "betwnstr");
            generateReport(runId, conn);
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("JDBC Driver class not found: ", e);
        } catch (SQLException e) {
            throw new MojoExecutionException("Error occured while connecting to database or executing SQL: ", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private void generateReport(int runId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SQL_REPORT_BY_RUNID);
        stmt.setInt(1, runId);
        ResultSet rs = stmt.executeQuery();
        int failureCounter = 0;
        int successCounter = 0;
        while (rs.next()) {
            String status = rs.getString("status");
            String desc = rs.getString("description");
            getLog().debug("processing description: " + desc);
            if (status.equals(UtlPlsqlTestResult.SUCCESS.toString())) {
                successCounter++;
            } else {
                failureCounter++;
            }
        }
        getLog().info("SUCCEEDED: " + successCounter + ", FAILED: " + failureCounter);
    }

    private int runTestPackage(Connection conn, String packageName) throws SQLException {
        CallableStatement stmt;
        getLog().info("Running test suite");
        stmt = conn.prepareCall(SQL_RUN_PACKAGE);
        stmt.setString(1, packageName);
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.execute();
        int runId = stmt.getInt(2);
        getLog().info("Finished run id #" + runId);
        return runId;
    }
}
