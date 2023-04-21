package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;

/**
 * Outputs the given custom object surrounded by parentheses
 * <code>"(&lt;customExpr&gt;)"</code>.  Acts like {@link CustomSql} for
 * expressions.  If the given expression is <code>null</code>, nothing will be
 * output for the expression.
 *
 * @author James Ahlborn
 */
public class CustomExpression extends Expression {

    private SqlObject _expr;

    public CustomExpression(Object exprObj) {
        this((exprObj != null) ? (new CustomSql(exprObj)) : (SqlObject) null);
    }

    public CustomExpression(SqlObject exprStr) {
        _expr = exprStr;
    }

    @Override
    public boolean isEmpty() {
        return (_expr == null);
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
        if (_expr != null) {
            _expr.collectSchemaObjects(vContext);
        }
    }

    @Override
    public void appendTo(AppendableExt app) throws IOException {
        appendCustomIfNotNull(app, _expr);
    }
}
