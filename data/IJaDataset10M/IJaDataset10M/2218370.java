package datadog.services.values;

import datadog.sql.SqlValues;
import datadog.sql.statements.BoundSql;
import datadog.sql.builder.InsertSqlBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Creates SQL to insert an object.
 *
 * @author tomichj
 */
public class ObjectInsertWriter implements ObjectWriter {

    Row[] saves;

    public ObjectInsertWriter(Row[] saves) {
        this.saves = saves;
    }

    public SqlValues[] write() {
        List inserts = new ArrayList();
        for (int i = 0; i < saves.length; i++) {
            Row row = saves[i];
            InsertSqlBuilder builder = new InsertSqlBuilder(row);
            BoundSql boundSql = builder.getBoundSql();
            SqlValues sqlValues = new SqlValues(boundSql, row);
            inserts.add(sqlValues);
        }
        return (SqlValues[]) inserts.toArray(new SqlValues[0]);
    }

    public String toString() {
        return "ObjectWrite{" + "saves=" + (saves == null ? null : Arrays.asList(saves)) + "}";
    }
}
