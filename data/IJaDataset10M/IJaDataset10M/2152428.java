package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;

/**
 * Outputs the given object as is (can be used to insert any custom SQL into a
 * statement).
 * <p>
 * Example:
 * <pre>
 * String selectQuery =
 *   (new SelectQuery())
 *   .addCustomColumns(new CustomSql("foo"),
 *                     new CustomSql("baz"),
 *                     new CustomSql("buzz"))
 *   .addCustomJoin(SelectQuery.JoinType.INNER_JOIN,
 *                  new CustomSql("table1"), new CustomSql("table2"),
 *                  BinaryCondition.equalTo(
 *                    new CustomSql("table1.id"), new CustomSql("table2.id")))
 *   .addCustomOrderings(new CustomSql("foo"))
 *   .validate().toString();
 *
 * // Output:
 * SELECT foo,baz,buzz FROM table1 INNER JOIN table2 ON (table1.id = table2.id) ORDER BY foo
 * </pre>
 *
 * @see com.healthmarketscience.sqlbuilder.dbspec dbspec database model
 * 
 * @author James Ahlborn
 */
public class CustomSql extends SqlObject {

    private Object _obj;

    public CustomSql(Object obj) {
        _obj = obj;
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
    }

    @Override
    public void appendTo(AppendableExt app) throws IOException {
        app.append(_obj);
    }
}
