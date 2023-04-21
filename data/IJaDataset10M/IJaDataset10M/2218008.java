package org.rjam.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.rjam.gui.base.BaseUiLogging;

public class SqlProperties extends BaseUiLogging {

    public static final String PROP_PATH = "SqlPath";

    public static final String PROP_FILE_NAME = "SqlFileName";

    public static final String KEYWORD_FIELD = "Field";

    public static final String KEYWORD_POS = "Pos";

    public static final String DEFAULT_FILE_NAME = "Sql.properties";

    public static final String DEFAULT_PATH = "org/rjam/sql";

    public static final String ROLLED = ".Rolled";

    public static final String DATABASE_PROPERTIES = "database.properties";

    public static final String PROP_DDL_PATH = "DdlPath";

    public static final String DEFAULT_DDL_PATH = "org/rjam/ddl";

    public static final String DATABASE_INIT_FILE = "000_initialize.ddl";

    public static final String PARTITION_BY_TABLE = "byTable";

    public static final String PARTITION_BY_FIELD = "byField";

    public static final String PARTITION = "Partition";

    public static final String SQL_PARTITION_FIELD_NAME = "PartitionFieldName";

    public static final String DEFAULT_PARTITION_FIELD_NAME = "day";

    public static final String DEFAULT_PARTITION_FIELD_NAME_ROLLED = "month";

    public static final String SQL_GROUP_BY_MINUTE_TABLE = "GroupByMinuteTable";

    public static final String SQL_GROUP_BY_HOUR_TABLE = "GroupByHourTable";

    public static final String SQL_GROUP_BY_DAY_TABLE = "GroupByDayTable";

    public static final String DEFAULT_SUPPORTS_STATISTICS = "false";

    public static final String PROP_SUPPOSRTS_STATISTICS = "SupportsStatistics";

    private static Properties database;

    private Properties sqlProps;

    private String name = "";

    private static String path;

    private String fileName;

    private static String ddlPath;

    private static Boolean supportsStatistics;

    public SqlProperties() {
    }

    public SqlProperties(String name) {
        setName(name);
    }

    public String getDatabaseVendor() {
        return getDatabaseProperty("vendor", null);
    }

    public String getGroupByMinuteTableName() {
        return getDatabaseProperty(SQL_GROUP_BY_MINUTE_TABLE, null);
    }

    public String getGroupByHourTableName() {
        return getDatabaseProperty(SQL_GROUP_BY_HOUR_TABLE, null);
    }

    public String getGroupByDayTableName() {
        return getDatabaseProperty(SQL_GROUP_BY_DAY_TABLE, null);
    }

    public String getDatabasePartitionType() {
        return getDatabaseProperty(PARTITION, null);
    }

    public String getDatabaseProperty(String key, String def) {
        return getDatabaseProperties().getProperty(key, def);
    }

    public boolean isPartitionByField() {
        return PARTITION_BY_FIELD.equals(getDatabasePartitionType());
    }

    public boolean isPartitionByTable() {
        return PARTITION_BY_TABLE.equals(getDatabasePartitionType());
    }

    public String getPartitionFieldName(boolean isRolled) {
        String ret = null;
        if (isRolled) {
            ret = getDatabaseProperty(SQL_PARTITION_FIELD_NAME + ROLLED, DEFAULT_PARTITION_FIELD_NAME_ROLLED);
        } else {
            ret = getDatabaseProperty(SQL_PARTITION_FIELD_NAME, DEFAULT_PARTITION_FIELD_NAME);
        }
        return ret;
    }

    /**
	 * Convert a URL into a File.
	 * 
	 * @param url pointing to a JAR file on the local file system. 
	 * @return file representing the location of the URL
	 */
    public File getFile(URL url) {
        File ret = null;
        boolean debug = getLogger().isDebugEnabled();
        if (debug) logDebug("ddl URL=" + url);
        String tmp = url.getFile();
        if (tmp.startsWith("jar:")) {
            tmp = tmp.substring(4);
        }
        if (debug) logDebug("ddl file 01=" + tmp);
        if (tmp.startsWith("file:/")) {
            tmp = tmp.substring(6);
        }
        if (debug) logDebug("ddl file 02=" + tmp);
        int idx = tmp.indexOf('!');
        if (idx > 0) {
            tmp = tmp.substring(0, idx);
        }
        if (debug) logDebug("ddl file 03=" + tmp);
        if (tmp.charAt(1) != ':') {
            tmp = "/" + tmp;
        }
        ret = new File(tmp);
        if (debug) logDebug("ddl file ret=" + ret.getAbsolutePath());
        return ret;
    }

    public JarFile getJarFile(URL url) throws IOException {
        JarFile ret = null;
        File file = getFile(url);
        ret = new JarFile(file);
        return ret;
    }

    public List<String> getDDLNames() throws IOException {
        List<String> ret = new ArrayList<String>();
        String path = getDdlPath();
        URL url = getClass().getResource("/" + path + "/" + DATABASE_INIT_FILE);
        if (url != null) {
            String fileName = url.getFile();
            int idx = fileName.indexOf("!");
            if (idx > 0) {
                JarFile jar = getJarFile(url);
                Enumeration<JarEntry> list = jar.entries();
                while (list.hasMoreElements()) {
                    JarEntry el = list.nextElement();
                    String name = el.getName();
                    if (name.startsWith(path)) {
                        if (name.endsWith(".ddl")) {
                            ret.add(name);
                        }
                    }
                }
                jar.close();
            } else {
                File file = new File(fileName);
                File dir = file.getParentFile();
                File[] list = dir.listFiles();
                for (File file2 : list) {
                    String name = file2.getAbsolutePath();
                    if (name.endsWith(".ddl")) {
                        ret.add(name);
                    }
                }
            }
        }
        return ret;
    }

    public static Properties getDatabaseProperties() {
        if (database == null) {
            synchronized (SqlProperties.class) {
                if (database == null) {
                    String fileName = getDdlPath() + "/" + DATABASE_PROPERTIES;
                    SqlProperties tmp = new SqlProperties();
                    database = tmp.readProperties(fileName);
                }
            }
        }
        return database;
    }

    public static boolean supportsStatistics() {
        if (supportsStatistics == null) {
            synchronized (SqlProperties.class) {
                if (supportsStatistics == null) {
                    supportsStatistics = new Boolean(getDatabaseProperties().getProperty(PROP_SUPPOSRTS_STATISTICS, DEFAULT_SUPPORTS_STATISTICS).toLowerCase().startsWith("t"));
                }
            }
        }
        return supportsStatistics.booleanValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getSqlProps() {
        return sqlProps;
    }

    public void setSqlProps(Properties sqlProps) {
        this.sqlProps = sqlProps;
    }

    public String getFileName() {
        if (fileName == null) {
            synchronized (this) {
                if (fileName == null) {
                    logDebug("getting file name for name=" + name);
                    fileName = System.getProperty(name + PROP_FILE_NAME, name + DEFAULT_FILE_NAME);
                }
            }
        }
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static String getDdlPath() {
        if (ddlPath == null) {
            synchronized (SqlProperties.class) {
                if (ddlPath == null) {
                    ddlPath = System.getProperty(PROP_DDL_PATH, DEFAULT_DDL_PATH);
                }
            }
        }
        return ddlPath;
    }

    public static String getPath() {
        if (path == null) {
            synchronized (SqlProperties.class) {
                if (path == null) {
                    path = System.getProperty(PROP_PATH, DEFAULT_PATH);
                }
            }
        }
        return path;
    }

    public static void setPath(String path) {
        SqlProperties.path = path;
    }

    private Properties readProperties(String fileName) {
        Properties ret = new Properties();
        logDebug("SqlProperties looking for file name =" + fileName);
        try {
            logDebug("Attempt to load SqlProperties from " + fileName);
            InputStream input = getClass().getResourceAsStream(fileName);
            if (input == null) {
                logDebug("Attempt to load SqlProperties from /" + fileName);
                input = getClass().getResourceAsStream("/" + fileName);
                if (input == null) {
                    logDebug("Attempt to load from file system.");
                    input = new FileInputStream(fileName);
                }
            }
            ret.load(input);
            logDebug("Properties Loaded from " + fileName + " OK.");
            try {
                input.close();
            } catch (Exception ex) {
            }
            String[] names = new String[ret.size()];
            int idx = 0;
            for (Iterator<Object> it = ret.keySet().iterator(); it.hasNext(); ) {
                names[idx++] = (String) it.next();
            }
            for (idx = 0; idx < names.length; idx++) {
                ret.put(names[idx], ret.getProperty(names[idx]).trim());
            }
        } catch (Exception e) {
            logError(fileName + " SqlProperties are not availible", e);
        }
        return ret;
    }

    public Properties getSqlProperties() {
        if (sqlProps == null) {
            synchronized (this) {
                String defaultPath = getPath();
                String fileName = defaultPath + "/" + getFileName();
                sqlProps = readProperties(fileName);
            }
        }
        return sqlProps;
    }

    public String getSql(String queryType) {
        String ret = null;
        Properties p = getSqlProperties();
        ret = p.getProperty(queryType);
        return ret;
    }

    public int getFieldPosition(String queryType, String fieldName, int def) {
        int ret = def;
        String key = queryType + "." + KEYWORD_POS + "." + fieldName;
        String val = getSql(key);
        if (val != null && val.equals("null")) {
            val = null;
        }
        if (val != null) {
            try {
                ret = Integer.parseInt(val);
            } catch (Throwable e) {
            }
        }
        return ret;
    }

    public String getFieldName(String tableName, String fieldName) {
        String key = tableName + "." + KEYWORD_FIELD + "." + fieldName;
        String ret = getSql(key);
        return ret;
    }

    public String getFieldName(String tableName, String fieldName, String def) {
        String key = tableName + "." + KEYWORD_FIELD + "." + fieldName;
        String ret = getSql(key, def);
        return ret;
    }

    public String getSql(String queryType, String def) {
        String ret = getSql(queryType);
        if (ret == null) {
            ret = def;
        }
        return ret;
    }

    public static void main(String[] args) {
        SqlProperties p = new SqlProperties();
        String sql = p.getSql("ValidateDatabase");
        System.out.println("sql=" + sql);
        sql = p.getSql("JoinByMin");
        System.out.println("sql=" + sql);
        sql = p.getSql("Won't find this");
        System.out.println("sql=" + sql);
    }
}
