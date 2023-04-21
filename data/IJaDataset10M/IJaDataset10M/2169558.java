package net.sf.refactorit.audit.rules.j2se5;

import net.sf.refactorit.classmodel.BinPrimitiveType;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.classmodel.BinType;
import net.sf.refactorit.classmodel.BinVariable;
import net.sf.refactorit.classmodel.expressions.BinArrayUseExpression;
import net.sf.refactorit.classmodel.expressions.BinAssignmentExpression;
import net.sf.refactorit.classmodel.expressions.BinExpression;
import net.sf.refactorit.classmodel.expressions.BinFieldInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinLiteralExpression;
import net.sf.refactorit.classmodel.expressions.BinLogicalExpression;
import net.sf.refactorit.classmodel.expressions.BinVariableUseExpression;
import net.sf.refactorit.classmodel.statements.BinForStatement;
import net.sf.refactorit.classmodel.statements.BinLocalVariableDeclaration;
import net.sf.refactorit.classmodel.statements.BinStatementList;
import net.sf.refactorit.parser.JavaTokenTypes;
import net.sf.refactorit.query.BinItemVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juri Reinsalu
 */
public class ForinArrayTraversalCandidateChecker extends ForinForCandidateChecker {

    List arrUses;

    boolean isForinCandidate(BinForStatement forStatement) {
        if (!isIntegerEqualsZeroDeclaration(forStatement.getInitSourceConstruct())) {
            return false;
        }
        BinVariable iteratorVar = getIteratorVariable(forStatement);
        if (!isPropperCondition(forStatement.getCondition(), iteratorVar)) {
            return false;
        }
        BinVariable arrVariable = getArrVariable(forStatement);
        if (arrVariable == null) {
            return false;
        }
        arrUses = new ArrayList();
        if (new BinArrAndIteratorVarsUsageChecker(forStatement.getStatementList(), iteratorVar, arrVariable).isForinConflictUsage()) {
            return false;
        }
        this.forStatement = forStatement;
        return true;
    }

    /**
   * @param forStatement
   * @return
   */
    BinVariable getArrVariable(BinForStatement forStatement) {
        BinExpression expression = ((BinFieldInvocationExpression) getLogicalCondition(forStatement).getRightExpression()).getExpression();
        if (expression instanceof BinVariableUseExpression) {
            return ((BinVariableUseExpression) expression).getVariable();
        } else if (expression instanceof BinFieldInvocationExpression) {
            return ((BinFieldInvocationExpression) expression).getField();
        }
        return null;
    }

    /**
   * @param forStatement
   * @return
   */
    private BinLogicalExpression getLogicalCondition(BinForStatement forStatement) {
        return (BinLogicalExpression) forStatement.getCondition();
    }

    private boolean isPropperCondition(BinExpression forCondition, BinVariable iteratorVariable) {
        if (!isLogicalExpression(forCondition)) {
            return false;
        }
        BinLogicalExpression logicalExpression = (BinLogicalExpression) forCondition;
        if (!isLessThanOrGrThanExpression(logicalExpression)) {
            return false;
        }
        if (!isIteratorvarCheck(iteratorVariable, logicalExpression)) {
            return false;
        }
        if (!isFirstDimensionUse(logicalExpression)) {
            return false;
        }
        return isArrVarLengthCheck(logicalExpression);
    }

    /**
   * @param logicalExpression
   * @return
   */
    private boolean isFirstDimensionUse(BinLogicalExpression forConditionLE) {
        BinExpression rightExpr = forConditionLE.getRightExpression();
        if (!(rightExpr instanceof BinFieldInvocationExpression)) {
            return false;
        }
        BinFieldInvocationExpression rightExprFIE = (BinFieldInvocationExpression) rightExpr;
        BinVariable variable = getVariable(rightExprFIE.getExpression());
        if (variable == null) {
            return false;
        }
        if (!rightExprFIE.getMember().getName().equals("length")) {
            return false;
        }
        return true;
    }

    /**
   * returns either variable from BinVariableUseExpression or field from BinFieldInvocationExpression
   * if expression is none of theese types, then returns null.
   * @param expression: either BinVariableUseExpression or BinFieldInvocationExpression
   */
    private BinVariable getVariable(BinExpression varUseOrFieldInvocationExpression) {
        if (varUseOrFieldInvocationExpression instanceof BinVariableUseExpression) {
            return ((BinVariableUseExpression) varUseOrFieldInvocationExpression).getVariable();
        } else if (varUseOrFieldInvocationExpression instanceof BinFieldInvocationExpression) {
            return ((BinFieldInvocationExpression) varUseOrFieldInvocationExpression).getField();
        }
        return null;
    }

    /**
   * @param forConditionLE
   * @return
   */
    private boolean isArrVarLengthCheck(BinLogicalExpression forConditionLE) {
        BinExpression rightExpr = forConditionLE.getRightExpression();
        if (!(rightExpr instanceof BinFieldInvocationExpression)) {
            return false;
        }
        BinFieldInvocationExpression rightExprFIE = (BinFieldInvocationExpression) rightExpr;
        if (!rightExprFIE.getMember().getName().equals("length")) {
            return false;
        }
        return true;
    }

    /**
   * @param forConditionLE
   * @return
   */
    private boolean isLessThanOrGrThanExpression(BinLogicalExpression forConditionLE) {
        return forConditionLE.getAssigmentType() == JavaTokenTypes.LT;
    }

    /**
   * @param iteratorVariable
   * @param forConditionLE
   * @return
   */
    private boolean isIteratorvarCheck(BinVariable iteratorVariable, BinLogicalExpression forConditionLE) {
        if (!(forConditionLE.getLeftExpression() instanceof BinVariableUseExpression)) {
            return false;
        }
        if (!iteratorVariable.isSame(((BinVariableUseExpression) forConditionLE.getLeftExpression()).getVariable())) {
            return false;
        }
        return true;
    }

    /**
   * @param forCondition
   * @return
   */
    private boolean isLogicalExpression(BinExpression forCondition) {
        return (forCondition instanceof BinLogicalExpression);
    }

    private class BinArrAndIteratorVarsUsageChecker extends BinItemVisitor {

        private BinVariable iteratorVariable;

        private BinVariable arrayVariable;

        private boolean forinConflictUsage = false;

        public BinArrAndIteratorVarsUsageChecker(BinStatementList list, BinVariable iteratorVar, BinVariable arrayVar) {
            iteratorVariable = iteratorVar;
            arrayVariable = arrayVar;
            list.defaultTraverse(this);
        }

        public void visit(BinVariableUseExpression varUse) {
            if (!isIteratorVariableUse(varUse) || isInIteratedArrDimension(varUse)) {
                super.visit(varUse);
                return;
            }
            forinConflictUsage = true;
        }

        public void visit(BinArrayUseExpression arrUse) {
            if (!isArrVarInInterest(arrUse)) {
                super.visit(arrUse);
                return;
            }
            if (isReadOnlyUseOfCurrentOnlyArrElement(arrUse)) {
                arrUses.add(arrUse);
                super.visit(arrUse);
                return;
            }
            forinConflictUsage = true;
        }

        /**
     * @param arrUse
     * @return
     */
        private boolean isReadOnlyUseOfCurrentOnlyArrElement(BinArrayUseExpression arrUse) {
            return isPropperlyIndexed(arrUse) && !isAssignmentExpression(arrUse);
        }

        private boolean isAssignmentExpression(BinArrayUseExpression arrUse) {
            return arrUse.getParent() instanceof BinAssignmentExpression;
        }

        /**
     * @param varUse
     * @return
     */
        private boolean isIteratorVariableUse(BinVariableUseExpression varUse) {
            return varUse.getVariable().isSame(iteratorVariable);
        }

        private boolean isInIteratedArrDimension(BinVariableUseExpression varUse) {
            if (varUse.getParent() instanceof BinArrayUseExpression) {
                BinArrayUseExpression arrUseExpr = (BinArrayUseExpression) varUse.getParent();
                if (isArrVarInInterest(arrUseExpr)) {
                    return true;
                }
            }
            return false;
        }

        /**
     * @param arrUse
     * @return
     */
        private boolean isArrVarInInterest(BinArrayUseExpression arrUse) {
            boolean isBinVarUseExpr = arrUse.getArrayExpression() instanceof BinVariableUseExpression;
            if (isBinVarUseExpr) {
                return arrayVariable.isSame(((BinVariableUseExpression) arrUse.getArrayExpression()).getVariable());
            }
            if (arrUse.getArrayExpression() instanceof BinFieldInvocationExpression) {
                return arrayVariable.isSame(((BinFieldInvocationExpression) arrUse.getArrayExpression()).getField());
            }
            return false;
        }

        /**
     * @param arrUse
     * @return
     */
        private boolean isPropperlyIndexed(BinArrayUseExpression arrUse) {
            return arrUse.getDimensionExpression() instanceof BinVariableUseExpression && iteratorVariable.isSame(((BinVariableUseExpression) arrUse.getDimensionExpression()).getVariable());
        }

        public boolean isForinConflictUsage() {
            return forinConflictUsage;
        }
    }

    /**
   * @return
   */
    public BinArrayUseExpression[] getArrUses() {
        return (BinArrayUseExpression[]) arrUses.toArray(new BinArrayUseExpression[arrUses.size()]);
    }

    /**
   * @param forVariable
   * @return
   */
    protected boolean isIntegerEqualsZeroDeclaration(BinSourceConstruct forInitConstruct) {
        if (!(forInitConstruct instanceof BinLocalVariableDeclaration)) {
            return false;
        }
        BinVariable[] forVariables = ((BinLocalVariableDeclaration) forInitConstruct).getVariables();
        if (forVariables.length != 1) {
            return false;
        }
        BinVariable forVariable = forVariables[0];
        BinType varType = forVariable.getTypeRef().getBinType();
        if (!(varType instanceof BinPrimitiveType)) {
            return false;
        }
        if (!((BinPrimitiveType) varType).isIntegerType()) {
            return false;
        }
        BinExpression expression = forVariable.getExpression();
        if (!(expression instanceof BinLiteralExpression)) {
            return false;
        }
        if (!((BinLiteralExpression) expression).getLiteral().equals("0")) {
            return false;
        }
        return true;
    }

    /**
   * @param forStatement
   * @return
   */
    protected BinVariable getIteratorVariable(BinForStatement forStatement) {
        return ((BinLocalVariableDeclaration) forStatement.getInitSourceConstruct()).getVariables()[0];
    }
}
