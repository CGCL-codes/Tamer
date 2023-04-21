package vavi.tools.fixed;

/**
 * JavaParserVisitorAdapter.
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	020519	vavi	initial version <br>
 */
public abstract class JavaParserVisitorAdapter implements JavaParserVisitor {

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTCompilationUnit node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPackageDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTImportDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTTypeDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTClassDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTUnmodifiedClassDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTClassBody node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTNestedClassDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTClassBodyDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTMethodDeclarationLookahead node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTInterfaceDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTNestedInterfaceDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTUnmodifiedInterfaceDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTInterfaceMemberDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTFieldDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTVariableDeclarator node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTVariableDeclaratorId node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTVariableInitializer node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTArrayInitializer node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTMethodDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTMethodDeclarator node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTFormalParameters node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTFormalParameter node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTConstructorDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTExplicitConstructorInvocation node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTInitializer node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTType node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPrimitiveType node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTResultType node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTName node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTNameList node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTAssignmentOperator node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTConditionalExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTConditionalOrExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTConditionalAndExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTInclusiveOrExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTExclusiveOrExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTAndExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTEqualityExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTInstanceOfExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTRelationalExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTShiftExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTAdditiveExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTMultiplicativeExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTUnaryExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPreIncrementExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPreDecrementExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTCastLookahead node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPostfixExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTCastExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPrimaryExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPrimaryPrefix node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTPrimarySuffix node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTLiteral node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTBooleanLiteral node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTNullLiteral node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTArguments node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTArgumentList node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTAllocationExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTArrayDimsAndInits node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTLabeledStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTBlock node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTBlockStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTLocalVariableDeclaration node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTEmptyStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTStatementExpression node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTSwitchStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTSwitchLabel node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTIfStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTWhileStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTDoStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTForStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTForInit node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTStatementExpressionList node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTForUpdate node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTBreakStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTContinueStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTReturnStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTThrowStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTSynchronizedStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }

    public Object visit(ASTTryStatement node, Object data) {
        node.childrenAccept(this, data);
        return data;
    }
}
