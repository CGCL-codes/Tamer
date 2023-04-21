package ssgen.sql.common.expression.simple;

import ssgen.sql.common.element.clause.UpdateClause;
import ssgen.core.element.LiteralElement;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class SimpleUpdateClauseExpression {

    private UpdateClause updateClause = null;

    public void update(String updateTable) {
        initializeUpdateClauseIfNecessary();
        updateClause.setTableElement(new LiteralElement(updateTable));
    }

    private void initializeUpdateClauseIfNecessary() {
        if (updateClause == null) initializeUpdateClause();
    }

    public void initializeUpdateClause() {
        updateClause = new UpdateClause();
    }

    public UpdateClause getClause() {
        return updateClause;
    }
}
