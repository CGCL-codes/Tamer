package org.objectstyle.cayenne.dba.postgres;

import org.objectstyle.cayenne.access.trans.SelectTranslator;

/**
 * @since 1.2
 * @author Andrus Adamchik
 */
class PostgresSelectTranslator extends SelectTranslator {

    public String createSqlString() throws Exception {
        String sql = super.createSqlString();
        int limit = getQuery().getMetaData(getEntityResolver()).getFetchLimit();
        if (limit > 0) {
            return sql + " LIMIT " + limit;
        }
        return sql;
    }
}
