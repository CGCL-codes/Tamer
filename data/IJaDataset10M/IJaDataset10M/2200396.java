package net.sf.refactorit.audit.rules.j2se5;

import net.sf.refactorit.classmodel.BinItemVisitable;
import net.sf.refactorit.classmodel.BinLocalVariable;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.classmodel.BinVariable;
import net.sf.refactorit.classmodel.expressions.BinExpression;
import net.sf.refactorit.classmodel.expressions.BinFieldInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinMethodInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinVariableUseExpression;
import net.sf.refactorit.classmodel.statements.BinForStatement;
import net.sf.refactorit.classmodel.statements.BinLocalVariableDeclaration;

/**
 * @author Juri Reinsalu
 */
public class ForinForIteratorTraversalCandidateChecker extends ForinForCandidateChecker {

    private BinLocalVariable iteratorVar;

    private BinVariable iterableVariable;

    private BinExpression iterableAccessExpr;

    private BinMethodInvocationExpression nextCallExpression;

    boolean isForinCandidate(BinForStatement forStatement) {
        if (!isIteratorDeclaration(forStatement.getInitSourceConstruct())) return false;
        if (!isPropperCondition(forStatement.getCondition())) return false;
        if (hasIteratorExpressions(forStatement)) return false;
        if (hasConflictingUseOfIterator(forStatement, iteratorVar)) {
            return false;
        }
        this.forStatement = forStatement;
        return true;
    }

    private boolean hasIteratorExpressions(BinForStatement forStatement) {
        return forStatement.iteratorExpressionList() != null;
    }

    BinVariable getIteratorVariable() {
        return this.iteratorVar;
    }

    /**
   * @param forStatement
   * @param iteratorVar2
   * @return
   */
    private boolean hasConflictingUseOfIterator(BinForStatement forStatement, BinLocalVariable iteratorVar2) {
        IteratorUseAnalyzer iteratorUseAnalyzer = new IteratorUseAnalyzer(iteratorVar2);
        forStatement.getStatementList().defaultTraverse(iteratorUseAnalyzer);
        if (iteratorUseAnalyzer.isUseConflict()) {
            return true;
        }
        setNextCallExpression(iteratorUseAnalyzer.getNextCallExpression());
        return false;
    }

    private boolean isIteratorDeclaration(BinSourceConstruct initSourceConstruct) {
        if (!isLocalVariableDeclaration(initSourceConstruct)) {
            return false;
        }
        BinLocalVariableDeclaration localVarDeclaration = (BinLocalVariableDeclaration) initSourceConstruct;
        if (!isOfIteratorType(localVarDeclaration)) {
            return false;
        }
        BinLocalVariable iteratorVar = (BinLocalVariable) localVarDeclaration.getVariables()[0];
        BinExpression iteratorInitExpr = iteratorVar.getExpression();
        if (!isMethodInvocation(iteratorInitExpr)) {
            return false;
        }
        BinMethodInvocationExpression callForIterator = (BinMethodInvocationExpression) iteratorInitExpr;
        if (!isPropperIteratorCall(callForIterator.getMethod())) {
            return false;
        }
        BinVariable iterable = extractIterableVar(callForIterator);
        if (iterable == null) return false;
        this.iteratorVar = iteratorVar;
        this.iterableVariable = iterable;
        this.iterableAccessExpr = callForIterator.getExpression();
        return true;
    }

    /**
   * @param callForIterator
   * @return
   */
    private BinVariable extractIterableVar(BinMethodInvocationExpression callForIterator) {
        if (callForIterator.getExpression() instanceof BinVariableUseExpression) return ((BinVariableUseExpression) callForIterator.getExpression()).getVariable();
        if (callForIterator.getExpression() instanceof BinFieldInvocationExpression) return ((BinFieldInvocationExpression) callForIterator.getExpression()).getField();
        return null;
    }

    /**
   * @param iteratorInitExpr
   * @return
   */
    private boolean isMethodInvocation(BinItemVisitable expression) {
        return (expression instanceof BinMethodInvocationExpression);
    }

    /**
   * @param localVarDeclaration
   * @return
   */
    private boolean isOfIteratorType(BinLocalVariableDeclaration localVarDeclaration) {
        return "java.util.Iterator".equals(localVarDeclaration.getVariables()[0].getTypeRef().getQualifiedName());
    }

    /**
   * @param initSourceConstruct
   * @return
   */
    private boolean isLocalVariableDeclaration(BinSourceConstruct initSourceConstruct) {
        return (initSourceConstruct instanceof BinLocalVariableDeclaration);
    }

    /**
   * @param callForIterator
   */
    private boolean isPropperIteratorCall(BinMethod method) {
        if (!("iterator".equals(method.getName()) && (method.getParameters().length == 0))) {
            return false;
        }
        return method.getReturnType().isDerivedFrom(method.getProject().getTypeRefForName("java.util.Iterator"));
    }

    private boolean isPropperCondition(BinExpression condition) {
        if (!isMethodInvocation(condition)) {
            return false;
        }
        BinMethodInvocationExpression binMethodInvocationExpression = (BinMethodInvocationExpression) condition;
        if (!"hasNext".equals(binMethodInvocationExpression.getMethod().getName())) {
            return false;
        }
        return isIteratorVarUse(binMethodInvocationExpression);
    }

    /**
   * @param binMethodInvocationExpression
   * @return
   */
    private boolean isIteratorVarUse(BinMethodInvocationExpression binMethodInvocationExpression) {
        if (!isVariableUse(binMethodInvocationExpression.getExpression())) {
            return false;
        }
        if (!this.iteratorVar.isSame(((BinVariableUseExpression) binMethodInvocationExpression.getExpression()).getVariable())) {
            return false;
        }
        return true;
    }

    private boolean isVariableUse(BinExpression expression) {
        return expression instanceof BinVariableUseExpression;
    }

    /**
   * @return
   */
    public BinVariable getIterableVariable() {
        return this.iterableVariable;
    }

    /**
   * @return
   */
    public BinExpression getIterableExpression() {
        return this.iterableAccessExpr;
    }

    /**
   * @return
   */
    public BinMethodInvocationExpression getNextCallExpression() {
        return this.nextCallExpression;
    }

    public void setNextCallExpression(BinMethodInvocationExpression nextCallExpression) {
        this.nextCallExpression = nextCallExpression;
    }
}
