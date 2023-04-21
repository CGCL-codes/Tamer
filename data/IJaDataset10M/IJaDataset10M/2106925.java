package org.jmlspecs.eclipse.jmldom;

import java.util.List;
import org.jmlspecs.eclipse.jmlast.IJavaASTVisitor;
import org.jmlspecs.eclipse.jmlast.IJmlASTVisitor;

/**
 * This represents both JML set and debug statements
 */
public class JmlSetStatement extends JmlStatement {

    /**
   * The token for this node (SET or DEBUG)
   */
    private int token;

    /**
   * The string representation of the token
   */
    private String tokenString;

    /** Constructs a new node of this type
   * @param ast the AST factory
   * @param token the token for this node (SET or DEBUG)
   * @param tokenString the string representation of the token
   * @param e the expression argument for the node
   */
    public JmlSetStatement(AST ast, int token, String tokenString, Expression e) {
        super(ast, e);
        this.token = token;
        this.tokenString = tokenString;
    }

    /** Returns the token for this type of node.
   * @return the token for this type of node
   */
    public int getToken() {
        return token;
    }

    /** Returns the string representation of the token for this type of node.
   * @return the string representation of the token for this type of node
   */
    public String getTokenString() {
        return tokenString;
    }

    public JmlSetStatement clone0(AST target) {
        JmlSetStatement result = new JmlSetStatement(target, token, tokenString, (Expression) ASTNode.copySubtree(target, getExpression()));
        result.setSourceRange(this.getStartPosition(), this.getLength());
        return result;
    }

    final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
        return false;
    }

    public void accept0(IJavaASTVisitor visitor) {
        if (((IJmlASTVisitor) visitor).visit(this)) getExpression().accept(visitor);
        ((IJmlASTVisitor) visitor).endVisit(this);
    }

    final int getNodeType0() {
        return JML_SET_STATEMENT;
    }

    int memSize() {
        return BASE_NODE_SIZE + 4;
    }

    int treeSize() {
        return memSize() + (getExpression().treeSize());
    }

    final List internalStructuralPropertiesForType(int apiLevel) {
        return null;
    }

    final Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
        return null;
    }

    final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
        return null;
    }
}
