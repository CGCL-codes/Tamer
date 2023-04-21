package org.japano.el.parser;

public interface ELParserVisitor {

    public Object visit(SimpleNode node, Object data);

    public Object visit(ELRoot node, Object data);

    public Object visit(ELNonExpressionText node, Object data);

    public Object visit(ELConditionalExpression node, Object data);

    public Object visit(ELOrExpression node, Object data);

    public Object visit(ELAndExpression node, Object data);

    public Object visit(ELEqualityExpression node, Object data);

    public Object visit(ELRelationExpression node, Object data);

    public Object visit(ELAdditiveExpression node, Object data);

    public Object visit(ELMultiplicativeExpression node, Object data);

    public Object visit(ELUnaryExpression node, Object data);

    public Object visit(ELFunctionInvocation node, Object data);

    public Object visit(ELChainedExpression node, Object data);

    public Object visit(ELLiteral node, Object data);

    public Object visit(ELIdentifier node, Object data);
}
