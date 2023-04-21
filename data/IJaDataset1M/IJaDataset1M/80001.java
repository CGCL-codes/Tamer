package com.continuent.tungsten.replicator.event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.conf.ReplicatorConf;
import com.continuent.tungsten.replicator.database.Database;
import com.continuent.tungsten.replicator.database.DatabaseFactory;
import com.continuent.tungsten.replicator.database.MySQLCommentEditor;
import com.continuent.tungsten.replicator.database.SqlCommentEditor;
import com.continuent.tungsten.replicator.database.SqlOperation;
import com.continuent.tungsten.replicator.database.SqlOperationMatcher;
import com.continuent.tungsten.replicator.dbms.DBMSData;
import com.continuent.tungsten.replicator.dbms.LoadDataFileFragment;
import com.continuent.tungsten.replicator.dbms.OneRowChange;
import com.continuent.tungsten.replicator.dbms.RowChangeData;
import com.continuent.tungsten.replicator.dbms.RowIdData;
import com.continuent.tungsten.replicator.dbms.StatementData;
import com.continuent.tungsten.replicator.filter.Filter;
import com.continuent.tungsten.replicator.plugin.PluginContext;

/**
 * This filter events newly extracted from the database log to answer the
 * following key questions. It must run as an auto-filter whenever we want
 * sharding and multi-master replication to work.
 * <ul>
 * <li>Is this an ordinary transaction or a Tungsten catalog update?</li>
 * <li>What is the original service of this event?</li>
 * <li>What is the shard ID of this event?</li>
 * </ul>
 * These questions are answered as follows. We begin with the assumption that
 * any event is from the local service and does not contain Tungsten catalog
 * metadata updates. We then modify assumptions and assign the shard ID as
 * follows.
 * <ul>
 * <li>Case 0: No database identified. Sadly, this is possible. Warn and assign
 * to the default shard ID.</li>
 * <li>Case 1: Single ordinary database. Mark shard with database name.</li>
 * <li>Case 2: 1 or more dbs including a tungsten_<svc> database. Mark the
 * service, mark tungsten metadata, and assign the shard name using the
 * tungsten_<svc> database name.</li>
 * <li>Case 3: Multiple ordinary databases. Assign the shard to the default ID.</li>
 * </ul>
 * Finally, we should note that this filter needs to be fast and to minimize
 * memory usage. The indexing structure used in the implementation is a local
 * hash table of database schema names and reference counts, which takes up
 * virtually no space.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class EventMetadataFilter implements Filter {

    class SchemaIndex {

        Map<String, Integer> dbMap = new HashMap<String, Integer>();

        SchemaIndex() {
        }

        void incrementSchema(String schemaName) {
            if (schemaName != null) {
                Integer count = dbMap.get(schemaName);
                if (count == null) dbMap.put(schemaName, 1); else dbMap.put(schemaName, count.intValue() + 1);
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (String schema : dbMap.keySet()) {
                if (sb.length() > 1) sb.append(";");
                sb.append(schema).append("=>").append(dbMap.get(schema));
            }
            sb.append("}");
            return sb.toString();
        }
    }

    private boolean unknownSqlUsesDefaultDb = false;

    private static String STRINGENT = "stringent";

    private static String RELAXED = "relaxed";

    private static Logger logger = Logger.getLogger(EventMetadataFilter.class);

    private PluginContext context;

    private SqlOperationMatcher opMatcher;

    private SqlCommentEditor commentEditor;

    private Pattern serviceNamePattern = Pattern.compile("tungsten_([a-zA-Z0-9-_]+)", Pattern.CASE_INSENSITIVE);

    private String serviceCommentRegex = "___SERVICE___ = \\[([a-zA-Z0-9-_]+)\\]";

    private Pattern serviceCommentPattern = Pattern.compile(serviceCommentRegex, Pattern.CASE_INSENSITIVE);

    private long curSeqno = -1;

    private String curService = null;

    private String curShardId = null;

    /** If set to true, use default database for unknown SQL operations. */
    public void setUnknownSqlUsesDefaultDb(boolean unknownSqlUsesDefaultDb) {
        this.unknownSqlUsesDefaultDb = unknownSqlUsesDefaultDb;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.filter.Filter#filter(com.continuent.tungsten.replicator.event.ReplDBMSEvent)
     */
    public ReplDBMSEvent filter(ReplDBMSEvent event) throws ReplicatorException, InterruptedException {
        if (logger.isDebugEnabled()) logger.debug("Scanning for basic event metadata: seqno=" + event.getSeqno());
        Map<String, String> metadataTags = new TreeMap<String, String>();
        metadataTags.put(ReplOptionParams.SERVICE, context.getServiceName());
        metadataTags.put(ReplOptionParams.SHARD_ID, ReplOptionParams.SHARD_ID_UNKNOWN);
        if (event.getDBMSEvent() instanceof DBMSEmptyEvent) {
            return adornEvent(event, metadataTags, false);
        }
        ArrayList<DBMSData> dbmsDataValues = event.getData();
        if (dbmsDataValues.size() == 0) {
            logger.warn("Empty event generated: seqno=" + event.getSeqno() + " eventId=" + event.getEventId());
            return adornEvent(event, metadataTags, false);
        }
        boolean needsServiceSessionVar = false;
        String serviceSessionVar = null;
        if (dbmsDataValues.size() >= 1 && dbmsDataValues.get(0) instanceof StatementData) {
            StatementData sd = (StatementData) dbmsDataValues.get(0);
            String query = sd.getQuery();
            SqlOperation op = (SqlOperation) sd.getParsingMetadata();
            if (op == null) {
                op = opMatcher.match(query);
                sd.setParsingMetadata(op);
            }
            String serviceComment = commentEditor.fetchComment(query, op);
            if (serviceComment != null) {
                Matcher m = serviceCommentPattern.matcher(serviceComment);
                if (m.find()) {
                    serviceSessionVar = m.group(1);
                }
            }
            if (serviceSessionVar == null) {
                needsServiceSessionVar = true;
            }
        }
        SchemaIndex index = new SchemaIndex();
        for (DBMSData dbmsData : dbmsDataValues) {
            if (dbmsData instanceof StatementData) {
                StatementData sd = (StatementData) dbmsData;
                String query = sd.getQuery();
                SqlOperation op = (SqlOperation) sd.getParsingMetadata();
                if (op == null) {
                    op = opMatcher.match(query);
                    sd.setParsingMetadata(op);
                }
                String opSchema = op.getSchema();
                String affectedSchema = null;
                if (opSchema != null) {
                    affectedSchema = opSchema;
                } else if (sd.getDefaultSchema() != null) {
                    if (op.getObjectType() == SqlOperation.UNRECOGNIZED && !this.unknownSqlUsesDefaultDb) affectedSchema = ReplOptionParams.SHARD_ID_UNKNOWN; else affectedSchema = sd.getDefaultSchema();
                }
                if (affectedSchema != null) index.incrementSchema(affectedSchema);
                if (op.isBidiUnsafe()) metadataTags.put(ReplOptionParams.BIDI_UNSAFE, "true");
            } else if (dbmsData instanceof RowChangeData) {
                RowChangeData rd = (RowChangeData) dbmsData;
                for (OneRowChange orc : rd.getRowChanges()) {
                    index.incrementSchema(orc.getSchemaName());
                }
            } else if (dbmsData instanceof LoadDataFileFragment) {
                String affectedSchema = ((LoadDataFileFragment) dbmsData).getDefaultSchema();
                index.incrementSchema(affectedSchema);
            } else if (dbmsData instanceof RowIdData) {
            } else {
                logger.warn("Unsupported DbmsData class: " + dbmsData.getClass().getName());
            }
        }
        int normalDbCount = 0;
        int tungstenDbCount = 0;
        String dbName = null;
        String tungstenDbName = null;
        String service = null;
        for (String schemaName : index.dbMap.keySet()) {
            String nextServiceName = schemaToServiceName(schemaName);
            if (nextServiceName == null) {
                dbName = schemaName;
                normalDbCount++;
                if (logger.isDebugEnabled()) logger.debug("Found local database: " + schemaName);
            } else {
                tungstenDbCount++;
                tungstenDbName = schemaName;
                service = nextServiceName;
                if (logger.isDebugEnabled()) logger.debug("Found tungsten database: " + schemaName);
            }
        }
        if (index.dbMap.size() == 0) {
            event.getDBMSEvent().addMetadataOption(ReplOptionParams.SHARD_ID, ReplOptionParams.SHARD_ID_UNKNOWN);
            if (logger.isDebugEnabled()) logger.debug("Unable to infer database: seqno=" + event.getSeqno());
        } else if (index.dbMap.size() == normalDbCount) {
            if (serviceSessionVar == null) {
                metadataTags.put(ReplOptionParams.SHARD_ID, dbName);
            } else {
                metadataTags.put(ReplOptionParams.SERVICE, serviceSessionVar);
                tungstenDbName = "tungsten_" + serviceSessionVar;
                metadataTags.put(ReplOptionParams.SHARD_ID, tungstenDbName);
            }
        } else if (tungstenDbCount == 1) {
            metadataTags.put(ReplOptionParams.SHARD_ID, tungstenDbName);
            metadataTags.put(ReplOptionParams.SERVICE, service);
            metadataTags.put(ReplOptionParams.TUNGSTEN_METADATA, "true");
        } else if (tungstenDbCount > 1) {
            logger.warn("Multiple Tungsten catalog databases in one transaction: seqno=" + event.getSeqno() + " index=" + index.toString());
            metadataTags.put(ReplOptionParams.SHARD_ID, ReplOptionParams.SHARD_ID_UNKNOWN);
            metadataTags.put(ReplOptionParams.TUNGSTEN_METADATA, "true");
        } else {
            logger.debug("Multiple user databases in one transaction: seqno=" + event.getSeqno() + " index=" + index.toString());
            metadataTags.put(ReplOptionParams.SHARD_ID, ReplOptionParams.SHARD_ID_UNKNOWN);
        }
        return adornEvent(event, metadataTags, needsServiceSessionVar);
    }

    private ReplDBMSEvent adornEvent(ReplDBMSEvent event, Map<String, String> tags, boolean needsServiceSessionVar) {
        if (event.getFragno() == 0) {
            curSeqno = event.getSeqno();
            if (event.getLastFrag()) {
                curService = null;
                curShardId = null;
            } else {
                curService = tags.get(ReplOptionParams.SERVICE);
                curShardId = tags.get(ReplOptionParams.SHARD_ID);
            }
        } else {
            if (curSeqno == event.getSeqno()) {
                String service = tags.get(ReplOptionParams.SERVICE);
                if (!curService.equals(service)) {
                    tags.put(ReplOptionParams.SERVICE, curService);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Overriding service name: seqno=" + event.getSeqno() + " fragno=" + event.getFragno() + " old service=" + service + " new service=" + curService);
                    }
                }
                String shardId = tags.get(ReplOptionParams.SHARD_ID);
                if (!curShardId.equals(shardId)) {
                    tags.put(ReplOptionParams.SHARD_ID, curShardId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Overriding shard Id: seqno=" + event.getSeqno() + " fragno=" + event.getFragno() + " old shard=" + shardId + " new shard=" + curShardId);
                    }
                }
            } else {
                logger.warn("Potential out-of-sequence event detected; " + "this may indicate an extractor restart problem: " + "event seqno=" + event.getSeqno() + " event fragno=" + event.getFragno() + " expected seqno=" + curSeqno);
            }
        }
        for (String name : tags.keySet()) {
            event.getDBMSEvent().addMetadataOption(name, tags.get(name));
        }
        if (needsServiceSessionVar) {
            try {
                StatementData sd = (StatementData) event.getData().get(0);
                String query = sd.getQuery();
                SqlOperation op = (SqlOperation) sd.getParsingMetadata();
                if (op == null) {
                    op = opMatcher.match(query);
                    sd.setParsingMetadata(op);
                }
                String comment = "___SERVICE___ = [" + tags.get(ReplOptionParams.SERVICE) + "]";
                String appendableComment = this.commentEditor.formatAppendableComment(op, comment);
                if (appendableComment == null) {
                    String queryCommented = this.commentEditor.addComment(query, op, "___SERVICE___ = [" + tags.get(ReplOptionParams.SERVICE) + "]");
                    sd.setQuery(queryCommented);
                } else sd.appendToQuery(appendableComment);
            } catch (Exception e) {
                logger.warn("Assumption for service session variable violated", e);
            }
        }
        return event;
    }

    private String schemaToServiceName(String schema) {
        Matcher m = this.serviceNamePattern.matcher(schema);
        if (m.find()) return m.group(1); else return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#configure(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void configure(PluginContext context) throws ReplicatorException {
        this.context = context;
        TungstenProperties replProps = context.getReplicatorProperties();
        String defaultSchema = replProps.getString(ReplicatorConf.SHARD_DEFAULT_DB_USAGE, STRINGENT, true);
        if (STRINGENT.equals(defaultSchema)) unknownSqlUsesDefaultDb = false; else if (RELAXED.equals(defaultSchema)) unknownSqlUsesDefaultDb = true; else throw new ReplicatorException("Unknown property value for " + ReplicatorConf.SHARD_DEFAULT_DB_USAGE + "; values must be stringent or relaxed");
        logger.info("Use default schema for unknown SQL statements: " + unknownSqlUsesDefaultDb);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#prepare(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void prepare(PluginContext context) throws ReplicatorException {
        String url = context.getJdbcUrl(context.getReplicatorSchemaName());
        String user = context.getJdbcUser();
        String password = context.getJdbcPassword();
        try {
            Database db = DatabaseFactory.createDatabase(url, user, password);
            opMatcher = db.getSqlNameMatcher();
            commentEditor = new MySQLCommentEditor();
            commentEditor.setCommentRegex(serviceCommentRegex);
        } catch (SQLException e) {
            throw new ReplicatorException("Unable to create database connection: " + url, e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.plugin.ReplicatorPlugin#release(com.continuent.tungsten.replicator.plugin.PluginContext)
     */
    public void release(PluginContext context) throws ReplicatorException {
    }
}
