package jolie.lang.parse.ast;

import jolie.lang.parse.OLVisitor;
import jolie.lang.parse.ParsingContext;

public class ExpressionConditionNode extends OLSyntaxNode {

    private final OLSyntaxNode expression;

    public ExpressionConditionNode(ParsingContext context, OLSyntaxNode expression) {
        super(context);
        this.expression = expression;
    }

    public OLSyntaxNode expression() {
        return expression;
    }

    public void accept(OLVisitor visitor) {
        visitor.visit(this);
    }
}
