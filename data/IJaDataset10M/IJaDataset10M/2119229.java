package pikes.ecma;

import java.io.IOException;
import pikes.ecma.EcmaSyntaxVisitingException;
import pikes.ecma.IAssignmentExpression;
import pikes.ecma.SyntaxTreeVisitor;

public class MockIExpression implements IAssignmentExpression {

    public SyntaxTreeVisitor syntaxTreeVisitor = null;

    public Appendable appendable = null;

    public void visit(SyntaxTreeVisitor syntaxTreeVisitor) throws EcmaSyntaxVisitingException {
        this.syntaxTreeVisitor = syntaxTreeVisitor;
    }

    public void print(Appendable appendable) throws IOException {
        this.appendable = appendable;
    }
}
