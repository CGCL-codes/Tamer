package org.jmlspecs.eclipse.jmldom;

import java.util.List;
import org.jmlspecs.eclipse.jmlast.IJavaASTVisitor;
import org.jmlspecs.eclipse.jmlast.IJmlASTVisitor;
import org.jmlspecs.eclipse.jmlchecker.ModifierSet;

/**
 * TODO
 */
public class JmlLocalGhostDeclarationStatement extends JmlStatement {

    private VariableDeclarationStatement statement;

    public JmlLocalGhostDeclarationStatement(AST ast, VariableDeclarationStatement statement) {
        super(ast);
        this.statement = statement;
    }

    public int getToken() {
        return 0;
    }

    public String getTokenString() {
        return "???";
    }

    public void setStatement(VariableDeclarationStatement s) {
        statement = s;
    }

    public VariableDeclarationStatement getStatement() {
        return statement;
    }

    public JmlLocalGhostDeclarationStatement clone0(AST target) {
        JmlLocalGhostDeclarationStatement result = new JmlLocalGhostDeclarationStatement(target, (VariableDeclarationStatement) statement.clone(target));
        result.setSourceRange(this.getStartPosition(), this.getLength());
        return result;
    }

    final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
        return false;
    }

    public void accept0(IJavaASTVisitor visitor) {
        if (((IJmlASTVisitor) visitor).visit(this)) statement.accept(visitor);
        ((IJmlASTVisitor) visitor).endVisit(this);
    }

    final int getNodeType0() {
        return JML_LOCAL_GHOST_STATEMENT;
    }

    int memSize() {
        return BASE_NODE_SIZE + 4;
    }

    int treeSize() {
        return memSize() + (statement == null ? 0 : statement.treeSize());
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
