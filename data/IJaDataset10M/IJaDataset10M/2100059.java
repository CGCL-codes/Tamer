package com.ilog.translator.java2cs.translation.astrewriter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;

/**
 * 
 * 
 * @author afau
 * 
 */
public class NormalizeConditionalExpressionVisitor extends ASTRewriterVisitor {

    public NormalizeConditionalExpressionVisitor(ITranslationContext context) {
        super(context);
        this.transformerName = "Normalize Conditional Expression";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public void endVisit(ConditionalExpression node) {
        Expression thenE = node.getThenExpression();
        Expression elseE = node.getElseExpression();
        Expression test = node.getExpression();
        if (test.getNodeType() != ASTNode.PARENTHESIZED_EXPRESSION) {
            ParenthesizedExpression pExpr = currentRewriter.getAST().newParenthesizedExpression();
            pExpr.setExpression((Expression) currentRewriter.createMoveTarget(test));
            currentRewriter.remove(test, description);
            currentRewriter.replace(test, pExpr, description);
        }
    }
}
