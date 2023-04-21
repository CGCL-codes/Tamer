package jolie.lang.parse.ast.expression;

import jolie.lang.parse.OLVisitor;
import jolie.lang.parse.ast.OLSyntaxNode;
import jolie.lang.parse.context.ParsingContext;

public class FreshValueExpressionNode extends OLSyntaxNode {

    public FreshValueExpressionNode(ParsingContext context) {
        super(context);
    }

    public void accept(OLVisitor visitor) {
        visitor.visit(this);
    }
}
