package ssgen.sql.common.expression;

import ssgen.core.element.ElementList;
import ssgen.core.element.SsGenElement;
import ssgen.core.element.ElementUtils;
import ssgen.core.element.ExpressionMarker;
import ssgen.sql.common.element.clause.GroupByClause;
import java.util.Arrays;

/**
 * @author Tadaya Tsuyukubo
 * @version $version$
 *          <p/>
 *          $Id$
 */
public class GroupByClauseExpression implements ExpressionMarker {

    private ElementList elements = new ElementList();

    public GroupByClauseExpression add(String... exprs) {
        this.add(ElementUtils.toLiteralElementArray(exprs));
        return this;
    }

    public GroupByClauseExpression add(SsGenElement... groupBys) {
        elements.addAll(Arrays.asList(groupBys));
        return this;
    }

    public GroupByClauseExpression clear() {
        elements = new ElementList();
        return this;
    }

    public SsGenElement getElement() {
        GroupByClause groupByClause = new GroupByClause();
        groupByClause.setElement(elements);
        return groupByClause;
    }
}
