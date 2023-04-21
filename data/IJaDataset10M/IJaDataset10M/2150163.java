package edu.rabbit.kernel.schema;

import org.antlr.runtime.tree.CommonTree;
import edu.rabbit.DbException;
import edu.rabbit.schema.ICastExpression;
import edu.rabbit.schema.IExpression;
import edu.rabbit.schema.ITypeDef;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public class CastExpression extends Expression implements ICastExpression {

    private final IExpression expression;

    private final ITypeDef type;

    public CastExpression(CommonTree ast) throws DbException {
        assert "cast".equalsIgnoreCase(ast.getText());
        expression = create((CommonTree) ast.getChild(0));
        type = new TypeDef((CommonTree) ast.getChild(1));
    }

    public IExpression getExpression() {
        return expression;
    }

    public ITypeDef getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CAST (");
        buffer.append(getExpression());
        buffer.append(" AS ");
        buffer.append(getType());
        buffer.append(')');
        return buffer.toString();
    }
}
