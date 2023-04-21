package com.continuent.tungsten.replicator.dbms;

import java.util.LinkedList;
import java.util.List;
import com.continuent.tungsten.replicator.event.ReplOption;

/**
 * Defines a SQL statement that must be replicated.
 * 
 * @author <a href="mailto:teemu.ollakka@continuent.com">Teemu Ollakka</a>
 * @version 1.0
 */
public class StatementData extends DBMSData {

    private static final long serialVersionUID = 1L;

    public static final String CREATE_OR_DROP_DB = "createOrDropDB";

    private String defaultSchema;

    private Long timestamp;

    private String query;

    private byte[] queryAsBytes;

    private transient String queryAsBytesTranslated;

    private List<ReplOption> options = null;

    private int errorCode;

    public StatementData(String query) {
        super();
        this.defaultSchema = null;
        this.timestamp = null;
        this.query = query;
    }

    /**
     * Creates a new instance including timestamp and default schema.
     */
    public StatementData(String query, Long timestamp, String defaultSchema) {
        this.defaultSchema = defaultSchema;
        this.timestamp = timestamp;
        this.query = query;
    }

    /**
     * Returns the default schema or null if default schema should be inferred
     * from a previous SqlStatement instance in the same transaction.
     */
    public String getDefaultSchema() {
        return defaultSchema;
    }

    /**
     * Returns the current timestamp in order to be able to process values that
     * refer to current time or a null if timestamp is not relevant to this
     * query.
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the SQL statement that must be replicated.
     */
    public String getQuery() {
        if (this.queryAsBytes == null) return query; else {
            if (this.queryAsBytesTranslated == null) {
                queryAsBytesTranslated = new String(queryAsBytes);
            }
            return queryAsBytesTranslated;
        }
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setQuery(String query) {
        this.query = query;
        this.queryAsBytes = null;
    }

    public void setQuery(byte[] query) {
        this.queryAsBytes = query;
        this.query = null;
    }

    public void addOption(String name, String value) {
        if (options == null) options = new LinkedList<ReplOption>();
        options.add(new ReplOption(name, value));
    }

    public List<ReplOption> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        if (query != null) {
            return query.substring(0, Math.min(1000, query.length()));
        } else if (queryAsBytes != null) {
            return new String(queryAsBytes, 0, Math.min(1000, queryAsBytes.length));
        }
        return null;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public byte[] getQueryAsBytes() {
        return queryAsBytes;
    }
}
