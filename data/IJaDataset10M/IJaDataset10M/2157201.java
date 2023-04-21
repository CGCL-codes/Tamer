package org.jmlspecs.eclipse.jmldom;

import org.jmlspecs.eclipse.jmlast.IJavaASTVisitor;
import org.jmlspecs.eclipse.jmlast.IJmlASTVisitor;

/**
 * TODO
 */
public class JmlLblExpression extends JmlExpression {

    private String label;

    private Expression expression;

    public Expression getExpression() {
        return this.expression;
    }

    public String getLabel() {
        return label;
    }

    /**
   * Creates a new AST node for a method invocation expression owned by the 
   * given AST. By default, no expression, no type arguments, 
   * an unspecified, but legal, method name, and an empty list of arguments.
   * 
   * @param ast the AST that is to own this node
   */
    JmlLblExpression(AST ast) {
        super(ast);
    }

    public JmlLblExpression(AST ast, int token, String tokenString, String label, Expression expression) {
        super(ast, token, tokenString);
        this.label = label;
        this.expression = expression;
    }

    final int getNodeType0() {
        return JML_LBL_EXPRESSION;
    }

    ASTNode clone0(AST target) {
        return new JmlLblExpression(target, getToken(), getTokenString(), label, expression);
    }

    void accept0(IJavaASTVisitor visitor) {
        boolean visitChildren = ((IJmlASTVisitor) visitor).visit(this);
        if (visitChildren) {
            acceptChild(visitor, expression);
        }
        ((IJmlASTVisitor) visitor).endVisit(this);
    }
}
