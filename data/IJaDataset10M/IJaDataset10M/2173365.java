package com.healthmarketscience.sqlbuilder;

import java.io.IOException;
import com.healthmarketscience.common.util.AppendableExt;

/**
 * Outputs the negation of the given expression "(- &lt;expression&gt;)"
 *
 * @author James Ahlborn
 */
public class NegateExpression extends Expression {

    private Expression _expression;

    /**
   * {@code Object} -&gt; {@code Expression} conversions handled by
   * {@link Converter#toExpressionObject(Object)}.
   */
    public NegateExpression(Object obj) {
        this(Converter.toExpressionObject(obj));
    }

    public NegateExpression(Expression expr) {
        _expression = expr;
    }

    @Override
    public boolean isEmpty() {
        return _expression.isEmpty();
    }

    @Override
    protected void collectSchemaObjects(ValidationContext vContext) {
        _expression.collectSchemaObjects(vContext);
    }

    @Override
    public void appendTo(AppendableExt app) throws IOException {
        if (!_expression.isEmpty()) {
            openParen(app);
            app.append("- ").append(_expression);
            closeParen(app);
        }
    }
}
