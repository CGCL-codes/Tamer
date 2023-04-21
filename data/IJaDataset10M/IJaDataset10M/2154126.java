package junitmetrics.core.parser;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class StatementModifier extends Modifier {

    public static void modify(Statement statement) {
        switch(statement.getNodeType()) {
            case ASTNode.RETURN_STATEMENT:
                ReturnStatementModifier.modify((ReturnStatement) statement);
                break;
            case ASTNode.VARIABLE_DECLARATION_STATEMENT:
                VariableDeclarationStatementModifier.modify((VariableDeclarationStatement) statement);
                break;
            case ASTNode.SUPER_CONSTRUCTOR_INVOCATION:
                SuperConstructorInvocationModifier.modify((SuperConstructorInvocation) statement);
                break;
            case ASTNode.CONSTRUCTOR_INVOCATION:
                ConstructorInvocationModifier.modify((ConstructorInvocation) statement);
                break;
            case ASTNode.EXPRESSION_STATEMENT:
                ExpressionStatementModifier.modify((ExpressionStatement) statement);
                break;
            case ASTNode.ENHANCED_FOR_STATEMENT:
                EnhancedForStatementModifier.modify((EnhancedForStatement) statement);
                break;
            case ASTNode.BLOCK:
                BlockModifier.modify((Block) statement);
                break;
            case ASTNode.FOR_STATEMENT:
                ForStatementModifier.modify((ForStatement) statement);
                break;
            case ASTNode.IF_STATEMENT:
                IfStatementModifier.modify((IfStatement) statement);
                break;
            case ASTNode.SWITCH_STATEMENT:
                SwitchStatementModifier.modify((SwitchStatement) statement);
                break;
            case ASTNode.WHILE_STATEMENT:
                WhileStatementModifier.modify((WhileStatement) statement);
                break;
            case ASTNode.TRY_STATEMENT:
                TryStatementModifier.modify((TryStatement) statement);
                break;
            case ASTNode.THROW_STATEMENT:
                ThrowStatementModifier.modify((ThrowStatement) statement);
                break;
            case ASTNode.DO_STATEMENT:
                DoStatementModifier.modify((DoStatement) statement);
                break;
            case ASTNode.SWITCH_CASE:
                SwitchCaseModifier.modify((SwitchCase) statement);
                break;
            case ASTNode.SYNCHRONIZED_STATEMENT:
                SynchronizedStatementModifier.modify((SynchronizedStatement) statement);
                break;
            case ASTNode.LABELED_STATEMENT:
                LabeledStatementModifier.modify((LabeledStatement) statement);
                break;
            case ASTNode.BREAK_STATEMENT:
            case ASTNode.CONTINUE_STATEMENT:
            case ASTNode.EMPTY_STATEMENT:
                break;
            case ASTNode.TYPE_DECLARATION_STATEMENT:
                break;
            default:
                throw new RuntimeException("Unknown statement during parsing testing class. Type: " + ASTNode.nodeClassForType(statement.getNodeType()) + statement.toString());
        }
    }
}
