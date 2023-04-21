package ossobook.client.synchronization;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ossobook.Messages;
import ossobook.client.io.file.Logging;
import ossobook.exceptions.MetaStatementNotExecutedException;
import ossobook.exceptions.StatementNotExecutedException;
import ossobook.model.Column;
import ossobook.model.Table;
import ossobook.queries.QueryManager;

/**
 * synchronizes the database scheme and logs differences
 * 
 * @author j.lamprecht
 */
class DatabaseScheme {

    private static final Log _log = LogFactory.getLog(DatabaseScheme.class);

    private final Logging changes;

    private final QueryManager localManager;

    private final QueryManager syncManager;

    private Vector<String[][]> projectScheme;

    private HashMap<String, String[]> definitionTables;

    /**
	 * instantiates amongst others the log file
	 * 
	 * @param localManager
	 * @param syncManager
	 */
    public DatabaseScheme(QueryManager localManager, QueryManager syncManager) {
        this.localManager = localManager;
        this.syncManager = syncManager;
        changes = new Logging("Schema", null);
    }

    /**
	 * 
	 * @return absolute path to log file
	 */
    public String synchronizeScheme() {
        String path = null;
        try {
            Vector<Table> globalTables = getGlobalDatabaseScheme();
            setScheme(globalTables);
            Vector<Table> localTables = getLocalDatabaseScheme();
            checkDatabaseScheme(globalTables, localTables);
            if (changes.getRowsOfContent() >= 1) {
                path = changes.getPath();
            }
        } catch (StatementNotExecutedException e) {
            LogFactory.getLog(DatabaseScheme.class).error(e, e);
        } catch (MetaStatementNotExecutedException e) {
            LogFactory.getLog(DatabaseScheme.class).error(e, e);
        }
        return path;
    }

    /**
	 * gets the global database scheme from the server
	 *
	 * @return
	 */
    private Vector<Table> getGlobalDatabaseScheme() {
        Vector<Table> tables = new Vector<Table>();
        try {
            Vector<String> tableNames = syncManager.getTables();
            for (int i = 0; i < tableNames.size(); i++) {
                String tableName = tableNames.elementAt(i);
                Vector<String[]> columnDescription;
                Vector<Column> columns = new Vector<Column>();
                columnDescription = syncManager.getTableDescription(tableName);
                for (int j = 0; j < columnDescription.size(); j++) {
                    String[] column = columnDescription.elementAt(j);
                    columns.add(new Column(column[0], column[1], column[2], column[3], column[4], column[5]));
                }
                tables.add(new Table(tableName, columns, syncManager.getPrimaryKeys(tableName)));
            }
        } catch (MetaStatementNotExecutedException e) {
            syncManager.closeConnection();
        }
        return tables;
    }

    /**
	 * gets the local database scheme
	 * 
	 * @return tables with their columns and primary keys
	 */
    private Vector<Table> getLocalDatabaseScheme() throws MetaStatementNotExecutedException {
        Vector<Table> tables = new Vector<Table>();
        Vector<String> tableNames = localManager.getTables();
        Vector<String[]> columnDescription;
        for (int i = 0; i < tableNames.size(); i++) {
            String tableName = tableNames.elementAt(i);
            Vector<Column> columns = new Vector<Column>();
            columnDescription = localManager.getTableDescription(tableName);
            for (int j = 0; j < columnDescription.size(); j++) {
                String[] column = columnDescription.elementAt(j);
                columns.add(new Column(column[0], column[1], column[2], column[3], column[4], column[5]));
            }
            tables.add(new Table(tableName, columns, localManager.getPrimaryKeys(tableName)));
        }
        return tables;
    }

    /**
	 * compares the global and local database scheme adapts the local scheme
	 * logs the differences
	 * 
	 * @param global
	 * @param local
	 */
    @SuppressWarnings("unchecked")
    private void checkDatabaseScheme(Vector<Table> global, Vector<Table> local) throws StatementNotExecutedException {
        Vector<Table> tableGlobal = (Vector<Table>) global.clone();
        Vector<Table> tableLocal = (Vector<Table>) local.clone();
        int i = 0;
        while (tableLocal.size() > i) {
            String logEntryColumn;
            String logEntryPrim;
            String tableName = tableLocal.elementAt(i).getName();
            if (_log.isInfoEnabled()) {
                _log.info("synchronisiere Tabelle " + tableName);
            }
            for (int j = 0; j < tableGlobal.size(); j++) {
                if (tableName.equals(tableGlobal.elementAt(j).getName())) {
                    logEntryColumn = adaptColumns(tableLocal.elementAt(i), tableGlobal.elementAt(j));
                    logEntryPrim = adaptPrimaryKey(tableLocal.elementAt(i), tableGlobal.elementAt(j));
                    tableLocal.remove(tableLocal.elementAt(i));
                    tableGlobal.remove(tableGlobal.elementAt(j));
                    if (!(logEntryColumn.equals("") && (logEntryPrim.equals("")))) {
                        changes.log(Messages.getString("DatabaseScheme.6", tableName));
                        if (!(logEntryColumn.equals(""))) {
                            changes.log(logEntryColumn);
                        }
                        if (!(logEntryPrim.equals(""))) {
                            changes.log(logEntryPrim + "\n\n");
                        }
                        changes.log("-------------------------------------------------------------\n\n");
                    }
                    break;
                }
                if (j == tableGlobal.size() - 1) {
                    i++;
                }
            }
        }
        for (int k = 0; k < tableLocal.size(); k++) {
            changes.log(Messages.getString("DatabaseScheme.13", tableLocal.elementAt(k).getName()));
            changes.log("-------------------------------------------------------------\n\n");
        }
        createTables(tableGlobal);
    }

    /**
	 * compares primary keys of local and global table, adapts primary key if
	 * necessary
	 * 
	 * @param tableLocal
	 * @param tableGlobal
	 * @return
	 */
    private String adaptPrimaryKey(Table tableLocal, Table tableGlobal) throws StatementNotExecutedException {
        String logEntry = "";
        String tableName = tableGlobal.getName();
        if (!tableLocal.getPrimaryKeyIdentic(tableGlobal)) {
            Vector<String> primaryKeys = tableGlobal.getPrimaryKey();
            String[] primaryKey = new String[primaryKeys.size()];
            primaryKeys.toArray(primaryKey);
            if (tableLocal.getPrimaryKey().size() > 0) {
                localManager.changePrimaryKey(tableName, primaryKey);
            } else {
                localManager.addPrimaryKey(tableName, primaryKey);
            }
            logEntry = Messages.getString("DatabaseScheme.18");
            for (String element : primaryKey) {
                logEntry = logEntry + element;
            }
        }
        return logEntry;
    }

    /**
	 * compares every single column of the local table to its global pendant and
	 * the other way round adapts local table columns to its namesake if
	 * necessary logs differences
	 * 
	 * @param localTable
	 * @param globalTable
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private String adaptColumns(Table localTable, Table globalTable) {
        Vector<Column> local = (Vector<Column>) localTable.getColumns().clone();
        Vector<Column> global = (Vector<Column>) globalTable.getColumns().clone();
        String logEntry = "";
        String logEntryAdd = Messages.getString("DatabaseScheme.20");
        boolean mustLogAdd = false;
        String logEntryChange = Messages.getString("DatabaseScheme.22");
        boolean mustLogChange = false;
        boolean mustLogExist = false;
        String logEntryExist = Messages.getString("DatabaseScheme.24");
        outer: for (Column g : global) {
            for (Column l : local) {
                if (g.getName().equals(l.getName())) {
                    continue outer;
                }
            }
            logEntryAdd += addColumn(g, globalTable.getName()) + "; ";
            mustLogAdd = true;
        }
        local = (Vector<Column>) localTable.getColumns().clone();
        outer: for (Column l : local) {
            for (Column g : global) {
                if (l.getName().equals(g.getName())) {
                    continue outer;
                }
            }
            logEntryExist += l.getName() + "; ";
            mustLogExist = true;
        }
        try {
            outer: for (Column globalColumn : global) {
                if (_log.isInfoEnabled()) {
                    _log.info("synchronisiere Spalte " + globalColumn.getName());
                }
                for (Column localColumn : local) {
                    if (localColumn.getName().equals(globalColumn.getName())) {
                        if (!localColumn.equals(globalColumn)) {
                            localManager.changeColumn(localTable.getName(), globalColumn.getName(), globalColumn.getType(), globalColumn.getSize(), globalColumn.getNullable(), globalColumn.getDefaultValue(), globalColumn.getAutoincrement());
                            logEntryChange += logColumn(globalColumn);
                            mustLogChange = true;
                        }
                        continue outer;
                    }
                }
            }
        } catch (StatementNotExecutedException e) {
            e.printStackTrace();
        }
        if (mustLogExist) {
            logEntry += logEntryExist + "\n";
        }
        if (mustLogAdd) {
            logEntry += logEntryAdd + "\n";
        }
        if (mustLogChange) {
            logEntry += logEntryChange + "\n";
        }
        return logEntry;
    }

    /**
	 * adds a column to a local table
	 * 
	 * @param column
	 * @param tableName
	 * @return
	 */
    private String addColumn(Column column, String tableName) {
        String logEntry = "";
        try {
            if (_log.isInfoEnabled()) {
                _log.info("füge Spalte hinzu: " + column.getName());
            }
            localManager.addColumn(tableName, column.getName(), column.getType(), column.getSize(), column.getNullable(), column.getDefaultValue(), column.getAutoincrement());
            logEntry = logEntry + logColumn(column);
        } catch (StatementNotExecutedException e) {
            e.printStackTrace();
        }
        return (logEntry);
    }

    /**
	 * creates non existing table in local database
	 * 
	 * @param tables
	 */
    private void createTables(Vector<Table> tables) {
        for (int i = 0; i < tables.size(); i++) {
            String tableName = tables.elementAt(i).getName();
            Vector<Column> columns = tables.elementAt(i).getColumns();
            String[] columnNames = new String[columns.size()];
            String[] types = new String[columns.size()];
            String[] sizes = new String[columns.size()];
            String[] nullable = new String[columns.size()];
            String[] defaultValues = new String[columns.size()];
            String[] autoincrement = new String[columns.size()];
            Vector<String> primKey = tables.elementAt(i).getPrimaryKey();
            String[] primaryKey = new String[tables.elementAt(i).getPrimaryKey().size()];
            primKey.toArray(primaryKey);
            for (int j = 0; j < columns.size(); j++) {
                columnNames[j] = columns.elementAt(j).getName();
                types[j] = columns.elementAt(j).getType();
                sizes[j] = columns.elementAt(j).getSize();
                nullable[j] = columns.elementAt(j).getNullable();
                defaultValues[j] = columns.elementAt(j).getDefaultValue();
                autoincrement[j] = columns.elementAt(j).getAutoincrement();
            }
            try {
                localManager.createTable(tableName, columnNames, types, sizes, nullable, defaultValues, autoincrement, primaryKey);
                logCreateTable(tables.elementAt(i));
            } catch (StatementNotExecutedException e) {
                LogFactory.getLog(DatabaseScheme.class).error(e, e);
            }
        }
    }

    /**
	 * logs the description for the created table
	 * 
	 * @param table
	 */
    private void logCreateTable(Table table) {
        String logEntry = table.getName() + ":\n\n" + Messages.getString("DatabaseScheme.35");
        Vector<Column> columns = table.getColumns();
        Vector<String> primKey = table.getPrimaryKey();
        logEntry += Messages.getString("DatabaseScheme.36");
        for (int j = 0; j < columns.size(); j++) {
            logEntry = logEntry + logColumn(columns.elementAt(j));
        }
        logEntry += Messages.getString("DatabaseScheme.37");
        for (int j = 0; j < primKey.size(); j++) {
            logEntry = logEntry + primKey.elementAt(j);
            if (j < primKey.size() - 1) {
                logEntry = logEntry + "; ";
            }
        }
        logEntry += "\n\n";
        changes.log(logEntry);
        changes.log("-------------------------------------------------------------\n\n");
    }

    /**
	 * describes the structure of a column
	 * 
	 * @param column
	 * @return
	 */
    private static String logColumn(Column column) {
        return column.getName() + "; " + column.getType() + "; " + column.getSize() + "; " + column.getNullable() + "; " + column.getDefaultValue() + "; " + column.getAutoincrement() + "\n";
    }

    /**
	 * remember global scheme
	 * 
	 * @param tables
	 */
    private void setScheme(Vector<Table> tables) {
        String[][] artefact = null;
        String[][] masse = null;
        String[][] input = null;
        String[][] project = null;
        definitionTables = new HashMap<String, String[]>();
        projectScheme = new Vector<String[][]>();
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.elementAt(i);
            if (table.getName().equals("projekt")) {
                project = new String[2][table.getColumns().size()];
                for (int k = 0; k < table.getColumns().size(); k++) {
                    Column column = table.getColumns().elementAt(k);
                    project[0][k] = table.getName() + "." + column.getName();
                    project[1][k] = column.getType();
                }
            } else if (table.getName().equals("eingabeeinheit")) {
                input = new String[2][table.getColumns().size()];
                for (int k = 0; k < table.getColumns().size(); k++) {
                    Column column = table.getColumns().elementAt(k);
                    input[0][k] = table.getName() + "." + column.getName();
                    input[1][k] = column.getType();
                }
            } else if (table.getName().equals("artefaktmaske")) {
                artefact = new String[2][table.getColumns().size()];
                for (int k = 0; k < table.getColumns().size(); k++) {
                    Column column = table.getColumns().elementAt(k);
                    artefact[0][k] = table.getName() + "." + column.getName();
                    artefact[1][k] = column.getType();
                }
            } else if (table.getName().equals("masse")) {
                masse = new String[2][table.getColumns().size()];
                for (int k = 0; k < table.getColumns().size(); k++) {
                    Column column = table.getColumns().elementAt(k);
                    masse[0][k] = table.getName() + "." + column.getName();
                    masse[1][k] = column.getType();
                }
            } else if (!table.getName().equals("version") && !table.getName().equals("nachrichten")) {
                String[] definition = new String[table.getColumns().size()];
                for (int k = 0; k < table.getColumns().size(); k++) {
                    Column column = table.getColumns().elementAt(k);
                    definition[k] = column.getName();
                    definitionTables.put(table.getName(), definition);
                }
            }
        }
        projectScheme.add(project);
        projectScheme.add(input);
        projectScheme.add(masse);
        projectScheme.add(artefact);
    }

    public Vector<String[][]> getProjectScheme() {
        return projectScheme;
    }

    public String[] getDefinitionTableScheme(String tableName) {
        return definitionTables.get(tableName);
    }

    /**
	 * get tables of database
	 * 
	 * @return
	 */
    public String[] getDefinitionTableNames() {
        Set<String> tableSet = definitionTables.keySet();
        String[] tables = new String[tableSet.size()];
        tableSet.toArray(tables);
        return tables;
    }
}
