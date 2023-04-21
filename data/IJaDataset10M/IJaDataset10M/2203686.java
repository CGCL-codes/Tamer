package org.dasein.persist.jdbc;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import org.dasein.persist.PersistenceException;
import org.dasein.persist.Transaction;

public class Deleter extends AutomatedSql {

    private String sql = null;

    public Deleter() {
        super();
    }

    public synchronized String getStatement() throws SQLException {
        if (sql == null) {
            StringBuilder str = new StringBuilder();
            str.append("DELETE FROM ");
            str.append(getIdentifier(getTableName()));
            if (!getCriteria().isEmpty()) {
                Iterator<Criterion> criteria;
                str.append(" WHERE ");
                criteria = getCriteria().iterator();
                while (criteria.hasNext()) {
                    Criterion criterion = criteria.next();
                    String col = criterion.column;
                    if (col.equals("timestamp")) {
                        str.append(getIdentifier("last_modified"));
                    } else {
                        str.append(getIdentifier(getSqlName(col)));
                    }
                    str.append(" ");
                    str.append(criterion.operator.toString());
                    str.append(" ?");
                    if (criteria.hasNext()) {
                        str.append(" ");
                        str.append(getJoin().toString());
                        str.append(" ");
                    }
                }
            }
            sql = str.toString();
        }
        return sql;
    }

    public void prepare(Map<String, Object> params) throws SQLException {
        int i = 1;
        for (Criterion criterion : getCriteria()) {
            String col = criterion.column;
            prepare(col, i++, params.get(col));
        }
    }

    public Map<String, Object> run(Transaction xaction, Map<String, Object> params) throws SQLException, PersistenceException {
        prepare(params);
        statement.executeUpdate();
        if (isTranslating()) {
            removeStringTranslations(xaction, getTarget(), params.get(params.keySet().iterator().next()).toString());
        }
        return params;
    }
}
