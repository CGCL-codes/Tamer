package net.sf.lunareclipse.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;

public class ForStatement extends Statement {

    private Expression fInitialization;

    private Expression fCondition;

    private Expression fIncrement;

    private Statement fAction;

    public ForStatement(DLTKToken forToken, Expression initialization, Expression condition, Expression increment, Statement action, DLTKToken end) {
        this.fInitialization = initialization;
        this.fCondition = condition;
        this.fIncrement = increment;
        this.fAction = action;
        this.setStart(forToken.getColumn());
        this.setEnd(end.getColumn());
    }

    public int getKind() {
        return S_FOR;
    }

    public void traverse(ASTVisitor visitor) throws Exception {
        if (visitor.visit(this)) {
            if (fInitialization != null) {
                fInitialization.traverse(visitor);
            }
            if (fCondition != null) {
                fCondition.traverse(visitor);
            }
            if (fIncrement != null) {
                fIncrement.traverse(visitor);
            }
            if (fAction != null) {
                fAction.traverse(visitor);
            }
            visitor.endvisit(this);
        }
    }

    public void printNode(CorePrinter output) {
        output.formatPrintLn("for:");
        if (this.fCondition != null) {
            output.formatPrintLn("condition:");
            this.fCondition.printNode(output);
        }
        if (this.fIncrement != null) {
            output.formatPrintLn("increment:");
            this.fIncrement.printNode(output);
        }
        if (this.fInitialization != null) {
            output.formatPrintLn("initialization:");
            this.fInitialization.printNode(output);
        }
        if (this.fAction != null) {
            output.indent();
            this.fAction.printNode(output);
            output.dedent();
        }
    }
}
