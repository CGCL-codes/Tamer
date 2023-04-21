package org.one.stone.soup.wiki.database.connection;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.one.stone.soup.stringhelper.StringArrayHelper;
import org.one.stone.soup.wiki.jar.manager.SystemAPI;
import org.one.stone.soup.xml.XmlElement;

public class DatabaseAPI extends SystemAPI {

    private Hashtable connections = new Hashtable();

    public void registerDriver(String driverName, String pageName, String jarFileName) throws Exception {
        URL jarURL = getBuilder().getFileManager().getResourceStore(getBuilder().getSystemLogin()).getResourceURL(getBuilder().getFileManager().getResourceStore(getBuilder().getSystemLogin()).getResource(pageName + "/" + jarFileName));
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[] { jarURL });
            Driver driver = (Driver) classLoader.loadClass(driverName).newInstance();
            System.out.println(driver);
            System.out.println("v" + driver.getMajorVersion() + "." + driver.getMinorVersion());
            System.out.println(driver.acceptsURL("jdbc:hsqldb:http://10.0.0.9/logger"));
            driver = new JDBCDriverWrapper(driver);
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAdminConnection(String url, String password) throws Exception {
        Properties info = new java.util.Properties();
        info.put("user", "sys");
        info.put("password", password);
        info.put("internal_logon", "sysdba");
        Connection connection = DriverManager.getConnection(url, info);
        connections.put("admin", connection);
    }

    public void createConnection(String alias, String url, String user, String password) throws Exception {
        Connection connection = DriverManager.getConnection(url, user, password);
        connections.put(alias, connection);
    }

    public XmlElement query(String alias, String query) throws Exception {
        Connection connection = getConnection(alias);
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            boolean hasMoreRows = result.next();
            ResultSetMetaData metaData = result.getMetaData();
            int columns = metaData.getColumnCount();
            XmlElement table = new XmlElement("table");
            XmlElement columnsXml = table.addChild("columns");
            for (int loop = 0; loop < columns; loop++) {
                XmlElement column = columnsXml.addChild("column");
                column.addValue(metaData.getColumnName(loop + 1));
                column.addAttribute("type", metaData.getColumnTypeName(loop + 1));
            }
            int row = 0;
            while (hasMoreRows) {
                XmlElement rowXml = table.addChild("row");
                for (int loopC = 0; loopC < columns; loopC++) {
                    rowXml.addChild("cell").addValue(result.getString(loopC + 1));
                }
                hasMoreRows = result.next();
                row++;
            }
            table.addAttribute("columns", "" + columns);
            table.addAttribute("rows", "" + row);
            System.out.println("Database Processed Query " + query + " for schema alias " + alias);
            return table;
        } finally {
            statement.close();
        }
    }

    public boolean execute(String alias, String executeStatement) throws Exception {
        Connection connection = getConnection(alias);
        Statement statement = connection.createStatement();
        try {
            boolean state = statement.execute(executeStatement);
            if (state == true) {
                connection.commit();
            }
            System.out.println("Database Executed " + executeStatement + " for schema alias " + alias);
            return true;
        } finally {
            statement.close();
        }
    }

    protected Connection getConnection(String alias) {
        return (Connection) connections.get(alias);
    }

    public DatabaseMetaData getDatabaseMetaData(String alias) throws SQLException {
        return ((Connection) connections.get(alias)).getMetaData();
    }

    public String[] getConnections() {
        Vector<String> keyList = new Vector<String>();
        Enumeration<String> keys = connections.keys();
        while (keys.hasMoreElements()) {
            keyList.addElement((String) keys.nextElement());
        }
        return StringArrayHelper.vectorToStringArray(keyList);
    }

    protected XmlElement getNextRow(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        XmlElement rowXml = new XmlElement("row");
        for (int loopC = 0; loopC < columns; loopC++) {
            rowXml.addChild("cell").addValue(resultSet.getString(loopC + 1));
        }
        return rowXml;
    }

    public XmlElement doPagedQuery(String alias, String selection, String from, int pageSize, int pageNumber) throws Throwable {
        String rowsQuery = "SELECT COUNT(*) FROM " + from;
        XmlElement rowsResult = query(alias, rowsQuery);
        int rows = Integer.parseInt(rowsResult.getElementByName("row").getElementByName("cell").getValue());
        int pagesAvailable = rows / pageSize;
        int startRow = pageNumber * pageSize;
        int endRow = (pageNumber + 1) * pageSize;
        String query = "SELECT * FROM ( SELECT p.*, ROWNUM row# FROM ( SELECT " + selection + " FROM " + from + ") p WHERE ROWNUM < " + endRow + " ) WHERE row# >= " + startRow;
        XmlElement result = query(alias, query);
        result.addAttribute("pagesAvailable", "" + pagesAvailable);
        result.addAttribute("pageSize", "" + pageSize);
        result.addAttribute("pageNumber", "" + pageNumber);
        System.out.println("Database Processed Paged Query " + selection + " for schema alias " + alias + " page " + pageNumber + " of " + pageSize);
        return result;
    }
}
