package com.avaje.ebean.server.lib.sql;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import com.avaje.ebean.server.lib.GlobalProperties;
import com.avaje.ebean.server.lib.util.StringHelper;
import com.avaje.lib.log.LogFactory;

/**
 * Reads and caches meta data from <code>java.sql.DatabaseMetaData</code>.
 * <p>
 * It will try to make meta data queries case insensitive. For example different
 * databases have the meta data in different case and this attempts to determine
 * the case automatically.
 * </p>
 */
public class DictionaryInfo implements Serializable {

    public static final String[] DEFAULT_SINGLE_SEARCH_TYPES = { "TABLE", "VIEW", "SYNONYM", "ALIAS" };

    public static final String[] DEFAULT_REGISTER_SEARCH_TYPES = { "TABLE", "VIEW" };

    private static final long serialVersionUID = -5961057842487875002L;

    private static final int CaseUpper = 100;

    private static final int CaseLower = 101;

    private static final int CaseUnknown = 102;

    private final String monitor = new String();

    private int dictionaryCase = CaseUnknown;

    private transient Logger logger = LogFactory.get(DictionaryInfo.class);

    /**
	 * The DataSource.
	 */
    private transient DataSource dataSource;

    /**
	 * A map to remember what patterns have been previously been registered.
	 * registerTables() is a very slow method and we don't want to re-query the
	 * same metaData.
	 */
    private HashSet<String> registeredTableSearchPatternMap = new HashSet<String>();

    /**
	 * Map of the registered tables by table name. Each TableListByName holds a
	 * list of tables that share that name (but differ by schema name).
	 */
    private HashMap<String, TableListByName> tableListMap = new HashMap<String, TableListByName>();

    /**
	 * Used to output debugging information.
	 */
    private int traceLevel;

    private boolean isLoadForeignKeysAutomatically = true;

    /**
	 * Name of File that dictionary should be serialised to when it changes.
	 */
    private String serialiseToFile;

    /**
	 * Create a DictionaryInfo with a given DataSource.
	 */
    public DictionaryInfo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * Create a DictionaryInfo without a DataSource.
	 * <p>
	 * The DataSource needs to be set before you can use the Dictionary.
	 * Deserialized DictionaryInfo like this one needs to have a DataSource set
	 * before use.
	 * </p>
	 */
    public DictionaryInfo() {
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        logger = LogFactory.get(DictionaryInfo.class);
    }

    /**
	 * This should be set after deserialisation or creation.
	 */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * Set a file name that this should serialise to whenever a new table is
	 * registered with the dictionary.
	 */
    public void setSerializeToFile(String fileName) {
        this.serialiseToFile = fileName;
    }

    private void notifyRegister(TableInfo tableInfo) {
        synchronized (monitor) {
            if (serialiseToFile != null) {
                try {
                    FileOutputStream fo = new FileOutputStream(serialiseToFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fo);
                    oos.writeObject(this);
                    oos.flush();
                    oos.close();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String toString() {
        synchronized (monitor) {
            return "DictionaryInfo " + tableListMap;
        }
    }

    /**
	 * Return true if exported and imported keys are loaded when a TableInfo is
	 * initialised. This defaults to true.
	 */
    public boolean isLoadForeignKeysAutomatically() {
        return isLoadForeignKeysAutomatically;
    }

    /**
	 * Set this to false to stop automatic foreign key loading. Otherwise
	 * foreign key information is queried and loaded when a TableInfo is
	 * initialised.
	 */
    public void setLoadForeignKeysAutomatically(boolean isLoadForeignKeysAutomatically) {
        this.isLoadForeignKeysAutomatically = isLoadForeignKeysAutomatically;
    }

    /**
	 * Set the trace level.
	 */
    public void setTraceLevel(int traceLevel) {
        this.traceLevel = traceLevel;
    }

    /**
	 * Return the trace level.
	 */
    public int getTraceLevel() {
        return traceLevel;
    }

    private TableInfo registerTableInfo(TableSearch tableSearch) {
        synchronized (monitor) {
            boolean loadKeys = isLoadForeignKeysAutomatically();
            TableInfo tableInfo = new TableInfo(tableSearch, loadKeys);
            tableSearch.setTableInfo(tableInfo);
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("registerTableInfo [" + tableInfo.getFullName() + "]");
            }
            String unquotedLowerCaseTableName = stripQuotesLowerCase(tableInfo.getName());
            TableListByName listByName = tableListMap.get(unquotedLowerCaseTableName);
            if (listByName == null) {
                listByName = new TableListByName();
                tableListMap.put(unquotedLowerCaseTableName, listByName);
            }
            if (listByName.register(tableInfo)) {
                if (tableSearch.isWithReferences()) {
                    tableInfo.loadForeignKeys(tableSearch);
                }
                notifyRegister(tableInfo);
            }
            return tableInfo;
        }
    }

    private boolean isRegisteredSearchPattern(TableSearch tableSearch) {
        synchronized (monitor) {
            String key = tableSearch.getKey();
            return registeredTableSearchPatternMap.add(key);
        }
    }

    /**
	 * Return the type of tables to search for when doing a single table search.
	 * <p>
	 * This typically defaults to TABLE, VIEW, SYNONYM and ALIAS and can be
	 * configured to include other types such as SYSTEM TABLE etc.
	 * </p>
	 */
    private String[] getSingleTableSearchTypes() {
        String searchTypes = GlobalProperties.getProperty("ebean.dictionary.singlesearchtypes");
        if (searchTypes == null) {
            return DEFAULT_SINGLE_SEARCH_TYPES;
        }
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("TABLE");
        set.add("VIEW");
        String[] values = StringHelper.delimitedToArray(searchTypes, ",", false);
        for (int i = 0; i < values.length; i++) {
            set.add(values[i].trim().toUpperCase());
        }
        return set.toArray(new String[set.size()]);
    }

    /**
	 * Return the type of tables to search for when doing a register tables
	 * search.
	 * <p>
	 * This typically defaults to TABLE and VIEW and can be configured to
	 * include other types such as ALIAS and SYNONYM etc.
	 * </p>
	 */
    private String[] getRegisterSearchTableTypes() {
        String searchTypes = GlobalProperties.getProperty("ebean.dictionary.registersearchtypes");
        if (searchTypes == null) {
            return DEFAULT_REGISTER_SEARCH_TYPES;
        }
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("TABLE");
        set.add("VIEW");
        String[] values = StringHelper.delimitedToArray(searchTypes, ",", false);
        for (int i = 0; i < values.length; i++) {
            set.add(values[i].trim().toUpperCase());
        }
        return set.toArray(new String[set.size()]);
    }

    public int registerTables(String catalog, String schemaPattern, String tableNamePattern, boolean withReferences) {
        TableSearch tableSearch = new TableSearch(getRegisterSearchTableTypes());
        tableSearch.setCatalog(catalog);
        tableSearch.setSchemaPattern(schemaPattern);
        tableSearch.setTableNamePattern(tableNamePattern);
        tableSearch.setWithReferences(withReferences);
        synchronized (monitor) {
            if (isRegisteredSearchPattern(tableSearch)) {
                return 0;
            }
            try {
                tableSearch.setConnection(getConnection());
                return searchForTables(tableSearch);
            } catch (SQLException ex) {
                throw new DataSourceException(ex);
            } finally {
                tableSearch.closeAll();
            }
        }
    }

    /**
	 * Search using the patterns. Will try lower and upper case searches.
	 * <p>
	 * This is because it is tricky to exactly know the case of the meta data.
	 * This multiple search approach seems better than using buggy
	 * DatabaseMetaData API.
	 * </p>
	 */
    private int searchForTables(TableSearch tableSearch) {
        synchronized (monitor) {
            int matchedTables = 0;
            if (dictionaryCase == CaseUnknown) {
                matchedTables = searchForTablesDetail(tableSearch);
                if (matchedTables > 0) {
                    return matchedTables;
                }
            }
            if (dictionaryCase != CaseUpper) {
                matchedTables = searchForTablesDetail(tableSearch.setToLowerCase());
                if (matchedTables > 0) {
                    dictionaryCase = CaseLower;
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("JDBC Dictionary is in LowerCase[" + tableSearch + "]");
                    }
                    return matchedTables;
                }
            }
            if (dictionaryCase != CaseLower) {
                matchedTables = searchForTablesDetail(tableSearch.setToUpperCase());
                if (matchedTables > 0) {
                    dictionaryCase = CaseUpper;
                    if (logger.isLoggable(Level.FINER)) {
                        logger.finer("JDBC Dictionary is in UpperCase[" + tableSearch + "]");
                    }
                    return matchedTables;
                }
            }
            return 0;
        }
    }

    /**
	 * Register the tables that conform to the patterns.
	 */
    private int searchForTablesDetail(TableSearch tableSearch) {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("registerTables " + tableSearch);
        }
        synchronized (monitor) {
            try {
                ResultSet rset = tableSearch.query();
                int count = 0;
                while (rset.next()) {
                    registerTableInfo(tableSearch);
                    count++;
                }
                isRegisteredSearchPattern(tableSearch);
                return count;
            } catch (SQLException e) {
                throw new DataSourceException(e);
            } finally {
                tableSearch.closeQuery();
            }
        }
    }

    /**
	 * Remove quoted identifiers and lower case.
	 */
    public static String stripQuotesLowerCase(String tableName) {
        tableName = tableName.toLowerCase();
        char firstChar = tableName.charAt(0);
        if (!Character.isLetterOrDigit(firstChar)) {
            if (firstChar != '_') {
                tableName = tableName.substring(1, tableName.length() - 1);
            }
        }
        return tableName;
    }

    /**
	 * Get the TableInfo for a given fullTableName. The fullTableName can
	 * optionally include the catalog and schema.
	 * <p>
	 * fullTableName can be catalog.schema.tableName or schema.tableName or just
	 * tableName.
	 * </p>
	 * 
	 * @param fullTableName
	 *            The table name to search for optionally include the catalog
	 *            and schema.
	 * 
	 * @return A matching TableInfo or null.
	 */
    public TableInfo getTableInfo(String fullTableName) {
        TableSearchName searchName = new TableSearchName(fullTableName);
        String unquotedLowerCaseTableName = stripQuotesLowerCase(searchName.getTableName());
        TableInfo tableInfo = null;
        synchronized (monitor) {
            TableListByName listByName = tableListMap.get(unquotedLowerCaseTableName);
            if (listByName != null) {
                tableInfo = listByName.find(searchName);
            }
            if (tableInfo == null) {
                tableInfo = searchForOneTable(searchName, unquotedLowerCaseTableName);
            }
            return tableInfo;
        }
    }

    /**
	 * Find the inverse of a given foreign key.
	 */
    public Fkey findInverse(Fkey fkey) {
        String tableName = fkey.getTableName();
        TableInfo tableInfo = getTableInfo(tableName);
        return tableInfo.findInverse(fkey);
    }

    /**
	 * Find an intersection table and appropriate foreign keys between two
	 * tables.
	 * 
	 * @param sourceTable
	 *            the source table
	 * @param destTable
	 *            the destination table
	 * @return the intersection information or null if no intersection was
	 *         found.
	 * @throws SQLException
	 *             it multiple matches where found between the source and
	 *             destination tables.
	 */
    public IntersectionInfo findIntersection(String sourceTable, String destTable) throws SQLException {
        synchronized (monitor) {
            TableInfo source = getTableInfo(sourceTable);
            TableInfo dest = getTableInfo(destTable);
            if (source == null || dest == null) {
                String msg = "source [" + source + "] or dest[" + dest + "] was not found";
                throw new SQLException(msg);
            }
            IntersectionInfo intInfo = new IntersectionInfo(source, dest);
            int matchCount = 0;
            Fkey[] srcExpKeys = source.getExportedFkeys();
            for (int i = 0; i < srcExpKeys.length; i++) {
                Fkey exportKey = (Fkey) srcExpKeys[i];
                String intTable = exportKey.getTableName();
                TableInfo interTable = getTableInfo(intTable);
                List<Fkey> importKeyList = interTable.getImportedFkeys(destTable);
                if (importKeyList.size() > 1) {
                    String msg = "Multiple Fkeys from [" + interTable + "] to [";
                    msg += destTable + "]. I am expecting 0 or one?";
                    throw new SQLException(msg);
                }
                if (importKeyList.size() == 1) {
                    ++matchCount;
                    if (matchCount > 1) {
                        String msg = "ERROR COMING: Multiple Intersections [" + intInfo + "]";
                        logger.log(Level.SEVERE, msg);
                    }
                    Fkey importKey = (Fkey) importKeyList.get(0);
                    intInfo.setSourceExportedKey(exportKey);
                    intInfo.setIntersectionImportedKey(importKey);
                    intInfo.setIntersection(interTable);
                }
            }
            if (matchCount == 0) {
                return null;
            }
            if (matchCount == 1) {
                return intInfo;
            }
            String msg = "Found [" + matchCount + "] intersection tables between [";
            msg += sourceTable + "] and [" + destTable + "]?  Only expecting one or none.";
            throw new SQLException(msg);
        }
    }

    /**
	 * Dictionary search is case sensitive. This can make code database
	 * dependent. Try to determine the case used in this dictionary.
	 * 
	 */
    private TableInfo searchForOneTable(TableSearchName searchName, String unquotedLowerCaseTableName) {
        synchronized (monitor) {
            TableSearch tableSearch = new TableSearch(getSingleTableSearchTypes());
            tableSearch.setWithReferences(true);
            String tableNameLowerCase = searchName.getTableName().toLowerCase();
            boolean usingQuotes = !unquotedLowerCaseTableName.equals(tableNameLowerCase);
            try {
                tableSearch.setConnection(getConnection());
                TableInfo tableInfo = null;
                if (usingQuotes) {
                    tableSearch.setSchemaPattern(searchName.getSchema());
                    tableSearch.setTableNamePattern(searchName.getTableName());
                    tableInfo = searchForOneTableDetail(tableSearch);
                    if (tableInfo != null) {
                        return tableInfo;
                    }
                }
                tableSearch.setSchemaPattern(searchName.getSchema());
                tableSearch.setTableNamePattern(unquotedLowerCaseTableName);
                if (dictionaryCase == CaseUnknown) {
                    tableInfo = searchForOneTableDetail(tableSearch);
                    if (tableInfo != null) {
                        return tableInfo;
                    }
                }
                if (dictionaryCase != CaseLower) {
                    tableInfo = searchForOneTableDetail(tableSearch.setToUpperCase());
                    if (tableInfo != null) {
                        dictionaryCase = CaseUpper;
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("JDBC Dictionary is in UpperCase[" + unquotedLowerCaseTableName.toUpperCase() + "]");
                        }
                        return tableInfo;
                    }
                }
                if (dictionaryCase != CaseUpper) {
                    tableInfo = searchForOneTableDetail(tableSearch.setToLowerCase());
                    if (tableInfo != null) {
                        dictionaryCase = CaseLower;
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("JDBC Dictionary is in LowerCase[" + unquotedLowerCaseTableName + "]");
                        }
                        return tableInfo;
                    }
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            } finally {
                tableSearch.closeAll();
            }
            if (traceLevel > 0) {
                String msg = "table not found[" + searchName + "][" + dictionaryCase + "]";
                logger.fine(msg);
            }
            return null;
        }
    }

    private TableInfo searchForOneTableDetail(TableSearch tableSearch) throws SQLException {
        TableInfo tableInfo = null;
        try {
            ResultSet rset = tableSearch.query();
            if (rset.next()) {
                tableInfo = registerTableInfo(tableSearch);
                if (rset.next()) {
                    throw new SQLException("Search for 1 table via catalog.schema.tableName " + tableSearch + " but found multiple matching tables?");
                }
            }
        } finally {
            tableSearch.closeQuery();
        }
        return tableInfo;
    }

    /**
	 * An Iterator of all the registered TableInfo's.
	 */
    public List<TableInfo> getTableInfoList() {
        ArrayList<TableInfo> fullList = new ArrayList<TableInfo>();
        synchronized (monitor) {
            Iterator<TableListByName> it = tableListMap.values().iterator();
            while (it.hasNext()) {
                TableListByName listByName = it.next();
                listByName.append(fullList);
            }
        }
        return Collections.unmodifiableList(fullList);
    }

    /**
	 * Returns a Connection used to get a DatabaseMetaData object. The
	 * Connection should always be closed once you have finished with it.
	 */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }
}
