package jolie.lang.parse.ast;

import jolie.lang.parse.OLVisitor;
import jolie.lang.parse.context.ParsingContext;

public class WhileStatement extends OLSyntaxNode {

    private final OLSyntaxNode condition, body;

    public WhileStatement(ParsingContext context, OLSyntaxNode condition, OLSyntaxNode body) {
        super(context);
        this.condition = condition;
        this.body = body;
    }

    public OLSyntaxNode condition() {
        return condition;
    }

    public OLSyntaxNode body() {
        return body;
    }

    public void accept(OLVisitor visitor) {
        visitor.visit(this);
    }
}
