package ssgen.sql.common.expression.simple;

import ssgen.core.element.ElementList;
import ssgen.core.element.LiteralElement;
import ssgen.sql.common.element.clause.GroupByClause;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class SimpleGroupByClauseExpression {

    private GroupByClause groupByClause = null;

    public void groupBy(String... elements) {
        initializeGroupByClauseIfNecessary();
        ElementList list = (ElementList) groupByClause.getElement();
        for (String element : elements) {
            list.add(new LiteralElement(element));
        }
    }

    public void initializeGroupByClauseIfNecessary() {
        if (groupByClause == null) initializeGroupByClause();
    }

    private void initializeGroupByClause() {
        groupByClause = new GroupByClause();
        groupByClause.setElement(new ElementList());
    }

    public GroupByClause getClause() {
        return groupByClause;
    }
}
