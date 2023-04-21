package org.fgraph.sql;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Wraps a H2 Database JDBC connection to provide TripleStore specific
 *  utility and access that are tailored to the specific implementation
 *  details of the H2 database.
 *
 *  @version $Revision$
 *  @author singram
 */
public class H2TableAdapter<U, V, W> extends TableAdapter<U, V, W> {

    public H2TableAdapter(Class<U> u, Class<V> v, Class<W> w, SqlSession session, String table) {
        super(u, v, w, session, table);
    }

    @Override
    protected long addRow(Object[] values, int... idFields) throws SQLException {
        Object id = null;
        Insert insert = new Insert(getTable());
        insert.setSequence("id");
        String[] fields = getFields();
        for (int i = 0; i < values.length; i++) insert.set(fields[i], toDb(i, values[i]));
        if (idFields != null) {
            for (int i : idFields) insert.setSequence(fields[i]);
        }
        String sql = insert.toString();
        PreparedStatement statement = getSession().prepareStatement(sql, true);
        insert.applyParameters(statement);
        statement.execute();
        CallableStatement idStatement = getSession().prepareCall("{call IDENTITY();}", true);
        idStatement.execute();
        ResultSet results = idStatement.getResultSet();
        try {
            if (results.next()) id = results.getObject(1);
            if (id == null) {
                throw new UnsupportedOperationException("DB did not return ID and relookup not supported.");
            }
            return (Long) id;
        } finally {
            results.close();
        }
    }
}
