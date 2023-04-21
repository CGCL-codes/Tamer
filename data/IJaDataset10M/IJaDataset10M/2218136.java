package net.sourceforge.arguscodewatch.quickfixes;

import java.util.List;
import net.sourceforge.arguscodewatch.CodewatchCompletionProposal;
import net.sourceforge.arguscodewatch.builders.EqualsComparisonCheck;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

/**
 * Provides the QuickFix for the {@link EqualsComparisonCheck}.
 */
public class EqualsComparisonFix extends CodewatchCompletionProposal {

    @SuppressWarnings("unchecked")
    @Override
    public TextEdit doBuildReplacement(IInvocationContext context, IMarker currentMarker) {
        ASTNode node = context.getCoveredNode();
        if (node == null) {
            node = context.getCoveringNode();
        }
        while (node.getNodeType() != ASTNode.INFIX_EXPRESSION) {
            node = node.getParent();
            if (node == null) {
                return null;
            }
        }
        InfixExpression infix = (InfixExpression) node;
        AST ast = infix.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        MethodInvocation replacementMethod = ast.newMethodInvocation();
        replacementMethod.setExpression((Expression) ASTNode.copySubtree(ast, infix.getLeftOperand()));
        replacementMethod.setName(ast.newSimpleName("equals"));
        List<Expression> args = replacementMethod.arguments();
        args.add((Expression) ASTNode.copySubtree(ast, infix.getRightOperand()));
        Expression replacement;
        if (infix.getOperator().equals(InfixExpression.Operator.NOT_EQUALS)) {
            PrefixExpression replacementPrefix = ast.newPrefixExpression();
            replacementPrefix.setOperator(PrefixExpression.Operator.NOT);
            replacementPrefix.setOperand(replacementMethod);
            replacement = replacementPrefix;
        } else {
            replacement = replacementMethod;
        }
        rewriter.replace(infix, replacement, null);
        try {
            return rewriter.rewriteAST(new Document(context.getCompilationUnit().getSource()), null);
        } catch (JavaModelException e) {
        }
        return null;
    }
}
