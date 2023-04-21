package org.achup.generador.datasource.jdbc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.achup.generador.print.PrintManager;

/**
 *
 * @author Marco Bassaletti
 */
public class JDBCHelper {

    public static void loadDriver(JDBCProperties properties, boolean registerDriver) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        List<File> jarFiles = properties.getJarFiles();
        final List<File> nativeDirs = properties.getNativeDirs();
        String jdbcClassName = properties.getDriverClass();
        if (jarFiles.size() == 0) {
            return;
        }
        List<URL> jarUrls = new ArrayList<URL>(jarFiles.size());
        for (File jarFile : jarFiles) {
            URL u = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/");
            jarUrls.add(u);
        }
        URL[] urlArray = jarUrls.toArray(new URL[0]);
        URLClassLoader ucl = new URLClassLoader(urlArray) {

            @Override
            protected String findLibrary(String libname) {
                File file = null;
                String filePath = null;
                for (File nativeDir : nativeDirs) {
                    file = new File(nativeDir.getAbsolutePath() + System.getProperty("file.separator") + System.mapLibraryName(libname));
                    if (file.exists()) {
                        filePath = file.getAbsolutePath();
                        break;
                    }
                }
                if (filePath != null) {
                    return filePath;
                } else {
                    return super.findLibrary(libname);
                }
            }
        };
        Driver d = (Driver) Class.forName(jdbcClassName, true, ucl).newInstance();
        if (registerDriver) {
            DriverManager.registerDriver(new DelegateDriver(d));
        }
    }

    public static Connection createConnection(JDBCProperties properties) throws SQLException {
        Connection conn = DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
        return conn;
    }

    public static List<String> getDatabaseTables(JDBCProperties properties) {
        LinkedList<String> databaseTables = null;
        try {
            PrintManager.out.println("DATABASE TABLES:");
            DatabaseMetaData metadata = properties.getConnection().getMetaData();
            ResultSet rs = metadata.getTables(properties.getCatalog(), properties.getSchema(), null, null);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                PrintManager.out.printf("DBTABLE: %s\n", tableName);
                if (databaseTables == null) {
                    databaseTables = new LinkedList<String>();
                }
                databaseTables.add(tableName);
            }
        } catch (SQLException ex) {
            PrintManager.out.printf("SQL FAILED: %s\n", ex);
        }
        return databaseTables;
    }

    public static List<String> getDatabaseCatalogs(JDBCProperties properties) throws SQLException {
        LinkedList<String> catalogs = null;
        if (properties.getConnection() == null) {
            properties.setConnection(createConnection(properties));
        }
        PrintManager.out.println("DATABASE CATALOGS:");
        DatabaseMetaData metadata = properties.getConnection().getMetaData();
        ResultSet rs = metadata.getCatalogs();
        while (rs.next()) {
            String catalogName = rs.getString("TABLE_CAT");
            PrintManager.out.printf("DBCAT: %s\n", catalogName);
            if (catalogs == null) {
                catalogs = new LinkedList<String>();
            }
            catalogs.add(catalogName);
        }
        return catalogs;
    }

    public static List<String> getDatabaseSchemas(JDBCProperties properties) throws SQLException {
        LinkedList<String> schemas = null;
        if (properties.getConnection() == null) {
            properties.setConnection(createConnection(properties));
        }
        PrintManager.out.println("DATABASE SCHEMAS:");
        DatabaseMetaData metadata = properties.getConnection().getMetaData();
        ResultSet rs = metadata.getSchemas(properties.getCatalog(), null);
        while (rs.next()) {
            String schemaName = rs.getString("TABLE_SCHEM");
            PrintManager.out.printf("DBSCHEMA: %s\n", schemaName);
            if (schemas == null) {
                schemas = new LinkedList<String>();
            }
            schemas.add(schemaName);
        }
        return schemas;
    }

    private JDBCHelper() {
    }
}
