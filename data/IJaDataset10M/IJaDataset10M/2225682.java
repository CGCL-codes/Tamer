package com.avaje.ebean.server.deploy;

import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

public class DeployNamedQuery {

    final String name;

    final String query;

    final QueryHint[] hints;

    final RawSqlSelect sqlSelect;

    public DeployNamedQuery(NamedQuery namedQuery) {
        this.name = namedQuery.name();
        this.query = namedQuery.query();
        this.hints = namedQuery.hints();
        this.sqlSelect = null;
    }

    public DeployNamedQuery(String name, String query, QueryHint[] hints) {
        this.name = name;
        this.query = query;
        this.hints = hints;
        this.sqlSelect = null;
    }

    public DeployNamedQuery(RawSqlSelect sqlSelect) {
        this.name = sqlSelect.getName();
        this.query = null;
        this.hints = null;
        this.sqlSelect = sqlSelect;
    }

    public boolean isSqlSelect() {
        return sqlSelect != null;
    }

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }

    public QueryHint[] getHints() {
        return hints;
    }

    public RawSqlSelect getSqlSelect() {
        return sqlSelect;
    }
}
