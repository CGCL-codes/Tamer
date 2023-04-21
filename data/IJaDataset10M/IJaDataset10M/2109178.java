package net.sf.lunareclipse.ast.statements;

import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.utils.CorePrinter;

public class BreakStatement extends SimpleStatement {

    public BreakStatement(DLTKToken breakToken, DLTKToken label, int end) {
        super(breakToken.getColumn(), end, null);
        if (label != null) {
            this.fExpression = new SimpleReference(label);
            this.setEnd(fExpression.sourceEnd());
        }
    }

    public int getKind() {
        return S_BREAK;
    }

    public void printNode(CorePrinter output) {
        output.formatPrintLn("break ");
        Expression expression = this.getExpression();
        if (expression != null) {
            expression.printNode(output);
        }
    }
}
