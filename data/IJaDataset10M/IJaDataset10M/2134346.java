package org.paccman.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.paccman.controller.DocumentController;
import org.paccman.tools.FileUtils;

/**
 *
 * @author joao
 */
public class PaccmanDao {

    private String database;

    private Connection connection;

    /**
     * 
     * @param database
     */
    public PaccmanDao(String database) {
        this.database = database;
    }

    private String getConnectionString(String option) {
        return "jdbc:derby:" + database + ";" + option;
    }

    /**
     * Create a new database file by copying the template database to the <code>database<code> location.
     * @throws java.io.IOException 
     * @throws java.net.URISyntaxException 
     * @throws java.sql.SQLException
     */
    private void create() throws IOException, URISyntaxException, SQLException {
        extractZip("/data/template.paccmandb.zip", database);
        String connectionString = getConnectionString("");
        connection = DriverManager.getConnection(connectionString);
    }

    private void extractZip(String zipFile, String destDir) throws IOException, URISyntaxException {
        final File createTempFile = File.createTempFile("templatedb", "zip");
        OutputStream os = new FileOutputStream(createTempFile);
        InputStream is = getClass().getResourceAsStream(zipFile);
        FileUtils.copyFile(is, os);
        final File fileDestDir = new File(destDir);
        assert !fileDestDir.exists() : "Target must not exist";
        fileDestDir.mkdir();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(createTempFile);
        java.util.Enumeration _enum = zip.entries();
        while (_enum.hasMoreElements()) {
            java.util.zip.ZipEntry file = (java.util.zip.ZipEntry) _enum.nextElement();
            java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            } else {
                InputStream fis = zip.getInputStream(file);
                OutputStream fos = new java.io.FileOutputStream(f);
                FileUtils.copyFile(fis, fos);
            }
        }
    }

    /**
     * Open database connection.
     * @throws java.sql.SQLException
     */
    private void open() throws SQLException {
        String connectionString = getConnectionString("");
        connection = DriverManager.getConnection(connectionString);
    }

    /**
     * Close database connection.
     * @throws java.sql.SQLException
     */
    private void shutdown() throws SQLException {
        String connectionString = getConnectionString("shutdown=true");
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            if (!"08006".equals(ex.getSQLState())) {
                throw ex;
            }
        }
    }

    /**
     * Save the document associated to the specified controller to the database file.
     * @param ctrl The document controller providing the document to save.
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException 
     */
    public void save(DocumentController ctrl) throws SQLException, IOException, URISyntaxException {
        create();
        PaccmanSave pl = new PaccmanSave();
        pl.saveDocument(connection, ctrl);
        shutdown();
    }

    /**:TODO:
     * Save the document associated to the specified controller to the database file.
     * @param ctrl The document controller providing the document to save.
     * @throws java.sql.SQLException
     */
    public void load(DocumentController ctrl) throws SQLException {
        open();
        PaccmanLoad pl = new PaccmanLoad();
        pl.loadDocument(connection, ctrl);
        shutdown();
    }

    /**
     * Backup the database to the specified directory.
     * @param backupTo Absolute path to the directory where to backup the database.
     * @throws java.sql.SQLException 
     */
    public void backup(File backupTo) throws SQLException {
        CallableStatement cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
        cs.setString(1, backupTo.getAbsolutePath());
        cs.execute();
        cs.close();
        logger.info("Backed up database to " + backupTo);
    }

    /**
     * :TODO:
     * @param exportTo
     */
    public void export(File exportTo) {
    }

    java.util.logging.Logger logger = org.paccman.tools.Logger.getDefaultLogger(this.getClass());
}
