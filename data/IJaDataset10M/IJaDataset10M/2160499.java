package ctagsinterface.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.gjt.sp.jedit.jEdit;
import ctagsinterface.main.CtagsInterfacePlugin;
import ctagsinterface.main.Tag;

public class TagDB {

    public static final String OPTION = CtagsInterfacePlugin.OPTION;

    public static final String CUSTOM_DB = "Custom";

    public static final String DB_SELECTED_PRESET = OPTION + "dbSelectedPreset";

    public static final String DB_PRESETS = OPTION + "dbPresets";

    public static final String DB_CLASS = OPTION + "dbClass";

    public static final String DB_CONNECTION = OPTION + "dbConnection";

    public static final String DB_USER = OPTION + "dbUser";

    public static final String DB_PASSWORD = OPTION + "dbPassword";

    public static final String DB_MAPPINGS_FILE = OPTION + "dbMappingsFile";

    public static final String DB_ON_EXIT = OPTION + "dbOnExit";

    private static final String DEFAULT_DB_FILE_SPEC = "<default>";

    private Connection conn;

    private Set<String> columns;

    Statement st = null;

    String onExit = null;

    public static final String IDENTITY_TYPE = "IDENTITY";

    public static final String VARCHAR_TYPE = "VARCHAR";

    public static final String INTEGER_TYPE = "INTEGER";

    private String identityType;

    private String varcharType;

    private String integerType;

    public static final String TAGS_TABLE = "TAGS";

    public static final String TAGS_NAME = "NAME";

    public static final String TAGS_FILE_ID = "FILE_ID";

    public static final String TAGS_PATTERN = "PATTERN";

    public static final String TAGS_EXTENSION_PREFIX = "A_";

    public static final String TAGS_LINE = "A_LINE";

    public static final String FILES_TABLE = "FILES";

    public static final String FILES_ID = "ID";

    public static final String FILES_NAME = "FILE";

    public static final String ORIGINS_TABLE = "ORIGINS";

    public static final String ORIGINS_ID = "ID";

    public static final String ORIGINS_NAME = "NAME";

    public static final String ORIGINS_TYPE = "TYPE";

    public static final String MAP_TABLE = "MAP";

    public static final String MAP_FILE_ID = "FILE_ID";

    public static final String MAP_ORIGIN_ID = "ORIGIN_ID";

    public static final int TEMP_ORIGIN_INDEX = 0;

    public static final String TEMP_ORIGIN_NAME = "Temp";

    public static final String TEMP_ORIGIN = "Temp";

    public static final String PROJECT_ORIGIN = "Project";

    public static final String DIR_ORIGIN = "Dir";

    public static final String ARCHIVE_ORIGIN = "Archive";

    private static String charsToEscape = null;

    public TagDB() {
        removeStaleLock();
        try {
            Class.forName(TagDB.getDbClass());
            String connectionString = TagDB.getDbConnection();
            conn = DriverManager.getConnection(connectionString.replace(DEFAULT_DB_FILE_SPEC, getDBFilePath()), TagDB.getDbUser(), TagDB.getDbPassword());
            st = conn.createStatement();
            onExit = TagDB.getDbOnExit();
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        initDbSettings();
        createTables();
        columns = getColumns();
    }

    private void initDbSettings() {
        identityType = IDENTITY_TYPE;
        varcharType = VARCHAR_TYPE;
        integerType = INTEGER_TYPE;
        String mapFile = getDbMappingsFile();
        if (mapFile == null || mapFile.length() == 0) return;
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(mapFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        identityType = props.getProperty("identityType", identityType);
        varcharType = props.getProperty("varcharType", varcharType);
        integerType = props.getProperty("integerType", integerType);
        charsToEscape = props.getProperty("stringValueCharsToEscape");
        if (charsToEscape != null) {
            StringBuffer escaped = new StringBuffer();
            for (int i = 0; i < charsToEscape.length(); i++) {
                char c = charsToEscape.charAt(i);
                if (c == ']' || c == '^' || c == '\\' || c == '-') escaped.append('\\');
                escaped.append(c);
            }
            if (escaped.length() > 0) charsToEscape = "([" + escaped + "])"; else charsToEscape = null;
        }
    }

    public boolean isFailed() {
        return (st == null);
    }

    public boolean hasSourceFile(String file) {
        return tableColumnContainsValue(FILES_TABLE, FILES_NAME, file);
    }

    public int getSourceFileID(String file) {
        return queryInteger(FILES_ID, "SELECT " + FILES_ID + " FROM " + FILES_TABLE + " WHERE " + FILES_NAME + "=" + quote(file), -1);
    }

    public void insertSourceFile(String file) {
        try {
            update("INSERT INTO " + FILES_TABLE + " (" + FILES_NAME + ") VALUES (" + quote(file) + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSourceFileOrigin(int fileId, int originId) {
        try {
            Query q = new Query("*", MAP_TABLE, MAP_FILE_ID + "=" + quote(fileId));
            q.addCondition(MAP_ORIGIN_ID + "=" + quote(originId));
            ResultSet rs = query(q);
            if (rs.next()) return;
            update("INSERT INTO " + MAP_TABLE + " (" + MAP_FILE_ID + "," + MAP_ORIGIN_ID + ") VALUES (" + quote(fileId) + "," + quote(originId) + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTagsFromSourceFile(int fileId) {
        if (fileId < 0) return;
        deleteRowsWithValue(TAGS_TABLE, TAGS_FILE_ID, Integer.valueOf(fileId));
    }

    public void insertTag(Tag t, int fileId) {
        if (fileId < 0) {
            System.err.println("insertTag called with fileId=-1");
            return;
        }
        StringBuffer valueStr = new StringBuffer();
        StringBuffer columnStr = new StringBuffer();
        Iterator<String> it = t.getExtensions().iterator();
        while (it.hasNext()) {
            String extension = it.next();
            String col = extension2column(extension.toUpperCase());
            String val = t.getExtension(extension);
            if (!columns.contains(col)) {
                try {
                    update("ALTER TABLE " + TAGS_TABLE + " ADD " + col + " " + varcharType + ";");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                columns.add(col);
            }
            if (columnStr.length() > 0) columnStr.append(",");
            columnStr.append(col);
            if (valueStr.length() > 0) valueStr.append(",");
            valueStr.append(quote(val));
        }
        try {
            update("INSERT INTO " + TAGS_TABLE + " (" + TAGS_NAME + "," + TAGS_PATTERN + "," + TAGS_FILE_ID + "," + columnStr.toString() + ") VALUES (" + quote(t.getName()) + "," + quote(t.getPattern()) + "," + fileId + "," + valueStr.toString() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Query getBasicTagQuery() {
        Query q = new Query();
        q.addColumn(field(TAGS_TABLE, "*"));
        q.addColumn(field(FILES_TABLE, FILES_NAME));
        q.addTable(TAGS_TABLE);
        q.addTable(FILES_TABLE);
        q.addCondition(field(TAGS_TABLE, TAGS_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        return q;
    }

    public Query getTagNameQuery(String tag) {
        Query q = getBasicTagQuery();
        q.addCondition(field(TAGS_TABLE, TAGS_NAME) + "=" + quote(tag));
        return q;
    }

    public Query getTagPrefixQuery(String prefix) {
        Query q = getBasicTagQuery();
        q.addCondition(field(TAGS_TABLE, TAGS_NAME) + " LIKE " + quote(prefix + '%'));
        return q;
    }

    public void makeProjectScopedQuery(Query tagQuery, String project) {
        Query projectQuery = new Query(ORIGINS_ID, ORIGINS_TABLE, ORIGINS_NAME + "=" + quote(project));
        projectQuery.addCondition(ORIGINS_TYPE + "=" + quote(PROJECT_ORIGIN));
        Query projectFilesQuery = new Query();
        projectFilesQuery.setColumn(MAP_FILE_ID);
        projectFilesQuery.setTable(MAP_TABLE);
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_ORIGIN_ID) + "=(" + projectQuery.toString() + ")");
        tagQuery.addCondition("EXISTS (" + projectFilesQuery.toString() + ")");
    }

    private Query getTagInOriginsQuery(String tag, HashMap<String, Vector<String>> origins) {
        StringBuffer sb = new StringBuffer();
        Set<String> types = origins.keySet();
        for (String type : types) {
            Vector<String> names = origins.get(type);
            if (names == null || names.size() == 0) continue;
            if (sb.length() > 0) sb.append(" OR ");
            sb.append("(" + ORIGINS_TYPE + "=" + quote(type) + " AND " + ORIGINS_NAME + " IN (");
            StringBuffer nameList = new StringBuffer();
            for (String name : names) {
                if (nameList.length() > 0) nameList.append(", ");
                nameList.append(quote(name));
            }
            sb.append(nameList + "))");
        }
        Query projectQuery = new Query(ORIGINS_ID, ORIGINS_TABLE, sb);
        Query projectFilesQuery = new Query();
        projectFilesQuery.setColumn(MAP_FILE_ID);
        projectFilesQuery.setTable(MAP_TABLE);
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_ORIGIN_ID) + " IN (" + projectQuery.toString() + ")");
        Query q = new Query();
        q.addColumn("*");
        q.addTable(TAGS_TABLE);
        q.addTable(FILES_TABLE);
        q.addCondition(field(TAGS_TABLE, TAGS_NAME) + "=" + quote(tag));
        q.addCondition(field(TAGS_TABLE, TAGS_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        q.addCondition("EXISTS (" + projectFilesQuery.toString() + ")");
        return q;
    }

    private Query getTagInProjectQuery(String tag, String project) {
        Query projectQuery = new Query(ORIGINS_ID, ORIGINS_TABLE, ORIGINS_NAME + "=" + quote(project));
        projectQuery.addCondition(ORIGINS_TYPE + "=" + quote(PROJECT_ORIGIN));
        Query projectFilesQuery = new Query();
        projectFilesQuery.setColumn(MAP_FILE_ID);
        projectFilesQuery.setTable(MAP_TABLE);
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        projectFilesQuery.addCondition(field(MAP_TABLE, MAP_ORIGIN_ID) + "=(" + projectQuery.toString() + ")");
        Query q = new Query();
        q.addColumn("*");
        q.addTable(TAGS_TABLE);
        q.addTable(FILES_TABLE);
        q.addCondition(field(TAGS_TABLE, TAGS_NAME) + "=" + quote(tag));
        q.addCondition(field(TAGS_TABLE, TAGS_FILE_ID) + "=" + field(FILES_TABLE, FILES_ID));
        q.addCondition("EXISTS (" + projectFilesQuery.toString() + ")");
        return q;
    }

    public ResultSet queryTag(String tag) throws SQLException {
        return query(getTagNameQuery(tag));
    }

    public ResultSet queryTagInOrigins(String tag, HashMap<String, Vector<String>> origins) throws SQLException {
        Query q = getTagInOriginsQuery(tag, origins);
        return query(q);
    }

    public ResultSet queryTagInProject(String tag, String project) throws SQLException {
        Query q = getTagInProjectQuery(tag, project);
        return query(q);
    }

    public int getOriginID(String type, String name) {
        return queryInteger(ORIGINS_ID, "SELECT " + ORIGINS_ID + " FROM " + ORIGINS_TABLE + " WHERE " + ORIGINS_TYPE + "=" + quote(type) + " AND " + ORIGINS_NAME + "=" + quote(name), -1);
    }

    public void insertOrigin(String type, String name) throws SQLException {
        update("INSERT INTO " + ORIGINS_TABLE + " (" + ORIGINS_TYPE + "," + ORIGINS_NAME + ") VALUES (" + quote(type) + "," + quote(name) + ")");
    }

    public void deleteOriginAssociatedData(String type, String name) throws SQLException {
        int originId = getOriginID(type, name);
        if (originId < 0) return;
        update("DELETE FROM " + MAP_TABLE + " WHERE " + MAP_ORIGIN_ID + "=" + quote(originId));
        update("DELETE FROM " + FILES_TABLE + " WHERE NOT EXISTS " + "(SELECT " + MAP_FILE_ID + " FROM " + MAP_TABLE + " WHERE " + MAP_FILE_ID + "=" + FILES_ID + ")");
        update("DELETE FROM " + TAGS_TABLE + " WHERE NOT EXISTS " + "(SELECT " + FILES_ID + " FROM " + FILES_TABLE + " WHERE " + FILES_ID + "=" + TAGS_FILE_ID + ")");
    }

    public void deleteOrigin(String type, String name) throws SQLException {
        deleteOriginAssociatedData(type, name);
        update("DELETE FROM " + ORIGINS_TABLE + " WHERE " + ORIGINS_TYPE + "=" + quote(type) + " AND " + ORIGINS_NAME + "=" + quote(name));
    }

    public String field(String table, String column) {
        return table + "." + column;
    }

    private boolean tableColumnContainsValue(String table, String column, Object value) {
        try {
            Query q = new Query(column, table, column + "=" + quote(value));
            q.setLimit(1);
            ResultSet rs = query(q);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int queryInteger(String column, String query, int defaultValue) {
        try {
            ResultSet rs = query(query);
            if (!rs.next()) return defaultValue;
            return rs.getInt(column);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public Vector<Tag> getResultSetTags(ResultSet rs) {
        Vector<Tag> tags = new Vector<Tag>();
        try {
            ResultSetMetaData meta;
            meta = rs.getMetaData();
            String[] cols = new String[meta.getColumnCount()];
            int[] types = new int[meta.getColumnCount()];
            for (int i = 0; i < cols.length; i++) {
                cols[i] = meta.getColumnName(i + 1);
                types[i] = meta.getColumnType(i + 1);
            }
            while (rs.next()) {
                Tag t = new Tag(rs.getString(TAGS_NAME), rs.getString(FILES_NAME), rs.getString(TAGS_PATTERN));
                Hashtable<String, String> extensions = new Hashtable<String, String>();
                Hashtable<String, String> attachments = new Hashtable<String, String>();
                for (int i = 0; i < cols.length; i++) {
                    if (types[i] != Types.VARCHAR) continue;
                    String value = rs.getString(i + 1);
                    if (value != null) {
                        if (isExtensionColumn(cols[i])) extensions.put(column2extension(cols[i]), value); else attachments.put(cols[i], value);
                    }
                }
                t.setExtensions(extensions);
                t.setAttachments(attachments);
                tags.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public Vector<String> getOrigins(String type) {
        return queryStringList(ORIGINS_NAME, "SELECT * FROM " + ORIGINS_TABLE + " WHERE " + ORIGINS_TYPE + "=" + quote(type));
    }

    public synchronized void update(String expression) throws SQLException {
        try {
            if (st.executeUpdate(expression) == -1) System.err.println("db error : " + expression);
        } catch (SQLException e) {
            System.err.println("SQL update: " + expression);
            throw e;
        }
    }

    public synchronized ResultSet query(Query query) throws SQLException {
        return query(query.toString());
    }

    public synchronized ResultSet query(String expression) throws SQLException {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(expression);
            return rs;
        } catch (SQLException e) {
            System.err.println("Failed query: " + expression);
            throw e;
        }
    }

    public Vector<String> queryStringList(String column, String query) {
        Vector<String> values = new Vector<String>();
        try {
            ResultSet rs = query(query);
            while (rs.next()) values.add(rs.getString(column));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    public void shutdown() {
        try {
            if (st != null) {
                if ((onExit != null) && (onExit.length() > 0)) st.execute(onExit);
                st.close();
            }
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String quote(Object value) {
        if (value instanceof String) {
            if (charsToEscape != null) value = ((String) value).replaceAll(charsToEscape, "\\\\$1");
            return "'" + ((String) value).replaceAll("'", "''") + "'";
        }
        return value.toString();
    }

    public void deleteRowsWithValue(String table, String column, Object value) {
        try {
            update("DELETE FROM " + table + " WHERE " + column + "=" + quote(value));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String extension2column(String extensionName) {
        return TAGS_EXTENSION_PREFIX + extensionName.toUpperCase();
    }

    public static String column2extension(String columnName) {
        return columnName.substring(TAGS_EXTENSION_PREFIX.length()).toLowerCase();
    }

    public static boolean isExtensionColumn(String columnName) {
        return columnName.startsWith(TAGS_EXTENSION_PREFIX);
    }

    public HashSet<String> getColumns() {
        HashSet<String> columnNames = new HashSet<String>();
        try {
            Query q = new Query("*", TAGS_TABLE, TAGS_NAME + "=''");
            ResultSet rs = query(q);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            for (int i = 0; i < cols; i++) columnNames.add(meta.getColumnName(i + 1));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return columnNames;
    }

    private String getDBFilePath() {
        return jEdit.getSettingsDirectory() + "/CtagsInterface/tagdb";
    }

    private void removeStaleLock() {
        File lock = new File(getDBFilePath() + ".lck");
        if (lock.exists()) lock.delete();
    }

    public void createIndex(String index, String extension) throws SQLException {
        createIndex(index, TAGS_TABLE, extension2column(extension));
    }

    public void createIndex(String index, String table, String column) throws SQLException {
        boolean exists = false;
        ResultSet indices = conn.getMetaData().getIndexInfo(null, null, table, false, false);
        while (indices.next()) {
            if (indices.getString("INDEX_NAME").equals(index)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            indices.close();
            return;
        }
        update("CREATE INDEX " + index + " ON " + table + "(" + column + ")");
    }

    private void createTable(String table, String[] columns) throws SQLException {
        ResultSet tables = conn.getMetaData().getTables(null, null, table, null);
        if (tables.first()) {
            tables.close();
            return;
        }
        StringBuffer st = new StringBuffer("CREATE TABLE ");
        st.append(table);
        st.append("(");
        for (int i = 0; i < columns.length; i += 2) {
            if (i > 0) st.append(", ");
            st.append(columns[i]);
            st.append(" ");
            st.append(columns[i + 1]);
        }
        st.append(")");
        update(st.toString());
    }

    private void createTables() {
        try {
            createTable(TAGS_TABLE, new String[] { TAGS_NAME, varcharType, TAGS_FILE_ID, integerType, TAGS_PATTERN, varcharType });
            createIndex("TAGS_NAME", TAGS_TABLE, TAGS_NAME);
            createIndex("TAGS_FILE", TAGS_TABLE, TAGS_FILE_ID);
            createTable(FILES_TABLE, new String[] { FILES_ID, identityType, FILES_NAME, varcharType });
            createIndex("FILES_NAME", FILES_TABLE, FILES_NAME);
            createTable(ORIGINS_TABLE, new String[] { ORIGINS_ID, identityType, ORIGINS_NAME, varcharType, ORIGINS_TYPE, varcharType });
            if (getOrigins(TEMP_ORIGIN).isEmpty()) update("INSERT INTO " + ORIGINS_TABLE + " (" + ORIGINS_ID + "," + ORIGINS_NAME + "," + ORIGINS_TYPE + ") VALUES (" + TEMP_ORIGIN_INDEX + "," + quote(TEMP_ORIGIN_NAME) + ", " + quote(TEMP_ORIGIN) + ")");
            createTable(MAP_TABLE, new String[] { MAP_FILE_ID, integerType, MAP_ORIGIN_ID, integerType });
            createIndex("MAP_FILE_ID", MAP_TABLE, MAP_FILE_ID);
            createIndex("MAP_ORIGIN_ID", MAP_TABLE, MAP_ORIGIN_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDbSelectedPreset() {
        return jEdit.getProperty(DB_SELECTED_PRESET);
    }

    private static String getDbPropertyPresetSuffix(String preset) {
        if (preset.equals(CUSTOM_DB)) preset = ""; else preset = "." + preset;
        return preset;
    }

    public static String getDbPropertyByPreset(String propBase, String preset) {
        return jEdit.getProperty(propBase + getDbPropertyPresetSuffix(preset));
    }

    public static void setDbPropertyByPreset(String propBase, String preset, String value) {
        preset = TagDB.getDbPropertyPresetSuffix(preset);
        jEdit.setProperty(propBase + preset, value);
    }

    public static String getDbPropertyOfSelectedPreset(String propBase) {
        String preset = getDbSelectedPreset();
        return getDbPropertyByPreset(propBase, preset);
    }

    public static String getDbClass() {
        return getDbPropertyOfSelectedPreset(DB_CLASS);
    }

    public static String getDbConnection() {
        return getDbPropertyOfSelectedPreset(DB_CONNECTION);
    }

    public static String getDbUser() {
        return getDbPropertyOfSelectedPreset(DB_USER);
    }

    public static String getDbPassword() {
        return getDbPropertyOfSelectedPreset(DB_PASSWORD);
    }

    public static String getDbMappingsFile() {
        return getDbPropertyOfSelectedPreset(DB_MAPPINGS_FILE);
    }

    public static String getDbOnExit() {
        return getDbPropertyOfSelectedPreset(DB_ON_EXIT);
    }
}
