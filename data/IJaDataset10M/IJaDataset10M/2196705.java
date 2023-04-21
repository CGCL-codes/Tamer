package org.javalid.external.db.config;

import java.io.Serializable;

/**
 * Represents an SQL query, loaded from the db-ext configuration file.
 * @author  M.Reuvers
 * @version 1.0
 * @since   1.1
 */
public class SqlQuery implements Serializable {

    private String id;

    private String refDs;

    private String staticField;

    private String query;

    public SqlQuery(String id, String refDs, String staticField, String query) {
        this.id = id;
        this.refDs = refDs;
        this.staticField = staticField;
        this.query = query;
    }

    public String getId() {
        return id;
    }

    public String getRefDs() {
        return refDs;
    }

    public String getStaticField() {
        return staticField;
    }

    public String getQuery() {
        return query;
    }

    public String toString() {
        return "\n[SqlQuery=\n" + "  id=" + id + "\n" + "  refDs=" + refDs + "\n" + "  staticField=" + staticField + "\n" + "  query=" + query + "\n" + "]";
    }
}
