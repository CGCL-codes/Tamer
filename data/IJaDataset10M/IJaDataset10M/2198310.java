package com.continuent.tungsten.replicator.filter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.database.Column;
import com.continuent.tungsten.replicator.database.Database;
import com.continuent.tungsten.replicator.database.DatabaseFactory;
import com.continuent.tungsten.replicator.database.MySQLOperationMatcher;
import com.continuent.tungsten.replicator.database.SqlOperation;
import com.continuent.tungsten.replicator.database.SqlOperationMatcher;
import com.continuent.tungsten.replicator.database.Table;
import com.continuent.tungsten.replicator.dbms.DBMSData;
import com.continuent.tungsten.replicator.dbms.OneRowChange;
import com.continuent.tungsten.replicator.dbms.OneRowChange.ColumnSpec;
import com.continuent.tungsten.replicator.dbms.RowChangeData;
import com.continuent.tungsten.replicator.dbms.StatementData;
import com.continuent.tungsten.replicator.event.ReplDBMSEvent;
import com.continuent.tungsten.replicator.plugin.PluginContext;

/**
 * This class defines a ColumnNameFilter. It adds column name information to
 * events on the extractor side.
 * 
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class ColumnNameFilter implements Filter {

    private static Logger logger = Logger.getLogger(ColumnNameFilter.class);

    private Hashtable<String, Hashtable<String, Table>> metadataCache;

    Database conn = null;

    private String user;

    private String url;

    private String password;

    SqlOperationMatcher sqlMatcher = new MySQLOperationMatcher();

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#configure(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void configure(PluginContext context) throws ReplicatorException {
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#prepare(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void prepare(PluginContext context) throws ReplicatorException {
        metadataCache = new Hashtable<String, Hashtable<String, Table>>();
        if (url == null) url = context.getJdbcUrl("tungsten_" + context.getServiceName());
        if (user == null) user = context.getJdbcUser();
        if (password == null) password = context.getJdbcPassword();
        try {
            conn = DatabaseFactory.createDatabase(url, user, password);
            conn.connect();
        } catch (SQLException e) {
            throw new ReplicatorException(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#release(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void release(PluginContext context) throws ReplicatorException {
        if (metadataCache != null) {
            metadataCache.clear();
            metadataCache = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.filter.Filter#filter(com.continuent.tungsten.replicator.event.ReplDBMSEvent)
     */
    public ReplDBMSEvent filter(ReplDBMSEvent event) throws ReplicatorException, InterruptedException {
        ArrayList<DBMSData> data = event.getData();
        if (data == null) return event;
        for (DBMSData dataElem : data) {
            if (dataElem instanceof RowChangeData) {
                RowChangeData rdata = (RowChangeData) dataElem;
                for (OneRowChange orc : rdata.getRowChanges()) {
                    getColumnInformation(orc);
                }
            } else if (dataElem instanceof StatementData) {
                StatementData sdata = (StatementData) dataElem;
                String query = sdata.getQuery();
                if (query == null) query = new String(sdata.getQueryAsBytes());
                SqlOperation sqlOperation = sqlMatcher.match(query);
                if (sqlOperation.getOperation() == SqlOperation.DROP && sqlOperation.getObjectType() == SqlOperation.SCHEMA) {
                    String dbName = sqlOperation.getSchema();
                    if (metadataCache.remove(dbName) != null) {
                        if (logger.isDebugEnabled()) logger.debug("DROP DATABASE detected - Removing database metadata for '" + dbName + "'");
                    } else if (logger.isDebugEnabled()) logger.debug("DROP DATABASE detected - no cached database metadata to delete for '" + dbName + "'");
                    continue;
                } else if (sqlOperation.getOperation() == SqlOperation.ALTER) {
                    String name = sqlOperation.getName();
                    String defaultDB = sdata.getDefaultSchema();
                    removeTableMetadata(name, sqlOperation.getSchema(), defaultDB);
                    continue;
                }
            }
        }
        return event;
    }

    private void removeTableMetadata(String tableName, String schemaName, String defaultDB) {
        if (schemaName != null) {
            Hashtable<String, Table> tableCache = metadataCache.get(schemaName);
            if (tableCache != null && tableCache.remove(tableName) != null) {
                if (logger.isDebugEnabled()) logger.debug("ALTER TABLE detected - Removing table metadata for '" + schemaName + "." + tableName + "'");
            } else if (logger.isDebugEnabled()) logger.debug("ALTER TABLE detected - no cached table metadata to remove for '" + schemaName + "." + tableName + "'");
        } else {
            Hashtable<String, Table> tableCache = metadataCache.get(defaultDB);
            if (tableCache != null && tableCache.remove(tableName) != null) logger.info("ALTER TABLE detected - Removing table metadata for '" + defaultDB + "." + tableName + "'"); else logger.info("ALTER TABLE detected - no cached table metadata to remove for '" + defaultDB + "." + tableName + "'");
        }
    }

    private void getColumnInformation(OneRowChange orc) throws ReplicatorException {
        String tableName = orc.getTableName();
        if (!metadataCache.containsKey(orc.getSchemaName())) {
            metadataCache.put(orc.getSchemaName(), new Hashtable<String, Table>());
        }
        Hashtable<String, Table> dbCache = metadataCache.get(orc.getSchemaName());
        if (!dbCache.containsKey(tableName) || orc.getTableId() == -1 || dbCache.get(tableName).getTableId() != orc.getTableId()) {
            if (dbCache.remove(tableName) != null && logger.isDebugEnabled()) logger.debug("Detected a schema change for table " + orc.getSchemaName() + "." + tableName + " - Removing table metadata from cache");
            Table newTable = null;
            try {
                newTable = conn.findTable(orc.getSchemaName(), orc.getTableName());
            } catch (SQLException e) {
                throw new ReplicatorException("Unable to retrieve column metadata: schema=" + orc.getSchemaName() + " table=" + orc.getTableName());
            }
            if (newTable == null) {
                throw new ReplicatorException("Unable to find column metadata; table may be missing: schema=" + orc.getSchemaName() + " table=" + orc.getTableName());
            }
            newTable.setTableId(orc.getTableId());
            dbCache.put(tableName, newTable);
        }
        Table table = dbCache.get(tableName);
        ArrayList<Column> columns = table.getAllColumns();
        int index = 0;
        for (Iterator<ColumnSpec> iterator = orc.getColumnSpec().iterator(); iterator.hasNext(); ) {
            ColumnSpec type = iterator.next();
            type.setName(columns.get(index).getName());
            index++;
        }
        index = 0;
        for (Iterator<ColumnSpec> iterator = orc.getKeySpec().iterator(); iterator.hasNext(); ) {
            ColumnSpec type = iterator.next();
            type.setName(columns.get(index).getName());
            index++;
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
