package edge.querybuilder.generic.impl.expression;

import edge.querybuilder.generic.SelectQuery;
import edge.querybuilder.generic.TableSubquery;

public class TableSubqueryImpl extends AbstractSubqueryExpression implements TableSubquery {

    TableSubqueryImpl(SelectQuery query, String alias) {
        super(query, alias);
    }
}
