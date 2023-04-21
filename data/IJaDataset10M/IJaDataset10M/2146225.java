package net.sourceforge.nrl.parser.ast.action;

import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.IRuleFile;
import net.sourceforge.nrl.parser.ast.IRuleSetDeclaration;
import net.sourceforge.nrl.parser.ast.constraints.IArithmeticExpression;
import net.sourceforge.nrl.parser.ast.constraints.IBinaryOperatorStatement;
import net.sourceforge.nrl.parser.ast.constraints.IBinaryPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IBooleanLiteral;
import net.sourceforge.nrl.parser.ast.constraints.ICardinalityConstraint;
import net.sourceforge.nrl.parser.ast.constraints.ICastExpression;
import net.sourceforge.nrl.parser.ast.constraints.ICollectionIndex;
import net.sourceforge.nrl.parser.ast.constraints.ICompoundReport;
import net.sourceforge.nrl.parser.ast.constraints.IConcatenatedReport;
import net.sourceforge.nrl.parser.ast.constraints.IConditionalReport;
import net.sourceforge.nrl.parser.ast.constraints.IConstraintRuleDeclaration;
import net.sourceforge.nrl.parser.ast.constraints.IDecimalNumber;
import net.sourceforge.nrl.parser.ast.constraints.IExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IForallStatement;
import net.sourceforge.nrl.parser.ast.constraints.IFunctionalExpression;
import net.sourceforge.nrl.parser.ast.constraints.IGlobalExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IIfThenStatement;
import net.sourceforge.nrl.parser.ast.constraints.IIntegerNumber;
import net.sourceforge.nrl.parser.ast.constraints.IIsInPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IIsNotInPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IIsSubtypePredicate;
import net.sourceforge.nrl.parser.ast.constraints.ILiteralString;
import net.sourceforge.nrl.parser.ast.constraints.IMultipleExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IMultipleNotExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.INRLConstraintDetailVisitor;
import net.sourceforge.nrl.parser.ast.constraints.INotExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IOperatorInvocation;
import net.sourceforge.nrl.parser.ast.constraints.ISelectionExpression;
import net.sourceforge.nrl.parser.ast.constraints.IValidationFragmentApplication;
import net.sourceforge.nrl.parser.ast.constraints.IValidationFragmentDeclaration;
import net.sourceforge.nrl.parser.ast.constraints.IVariableDeclaration;

/**
 * An extension of the basic detail visitor interface that adds the action nodes.
 * <p>
 * To use this interface, supply it to an instance of
 * {@link com.modeltwozero.nrl.parser.ast.action.ActionVisitorDispatcher}. and then pass
 * that instance to the accept method of a node. Pass it to
 * {@link net.sourceforge.nrl.parser.ast.IRuleFile} to visit an entire rule file. For
 * example:
 * 
 * <pre>
 * ruleFile.accept(new ActionVisitorDispatcher(new INRLActionDetailVisitor() { ... } ));
 * </pre>
 * <p>
 * If you don't want to implement the full interface, extend {@link Stub} instead.
 * 
 * @author Christian Nentwich
 */
public interface INRLActionDetailVisitor extends INRLConstraintDetailVisitor {

    public abstract void visitActionFragmentApplicationActionAfter(IActionFragmentApplicationAction action);

    public abstract boolean visitActionFragmentApplicationActionBefore(IActionFragmentApplicationAction action);

    public abstract void visitActionFragmentDeclarationAfter(IActionFragmentDeclaration decl);

    public abstract boolean visitActionFragmentDeclarationBefore(IActionFragmentDeclaration decl);

    public abstract void visitActionRuleDeclarationAfter(IActionRuleDeclaration decl);

    public abstract boolean visitActionRuleDeclarationBefore(IActionRuleDeclaration decl);

    public abstract void visitAddActionAfter(IAddAction add);

    public abstract boolean visitAddActionBefore(IAddAction add);

    public abstract void visitCompoundActionAfter(ICompoundAction action);

    public abstract boolean visitCompoundActionBefore(ICompoundAction action);

    public abstract void visitConditionalActionAfter(IConditionalAction action);

    public abstract boolean visitConditionalActionBefore(IConditionalAction action);

    public abstract void visitCreateActionAfter(ICreateAction action);

    public abstract boolean visitCreateActionBefore(ICreateAction action);

    public abstract void visitRemoveActionAfter(IRemoveAction action);

    public abstract boolean visitRemoveActionBefore(IRemoveAction action);

    public abstract void visitForEachActionAfter(IForEachAction action);

    public abstract boolean visitForEachActionBefore(IForEachAction action);

    public abstract void visitOperatorActionAfter(IOperatorAction action);

    public abstract boolean visitOperatorActionBefore(IOperatorAction action);

    public abstract void visitRemoveFromCollectionActionAfter(IRemoveFromCollectionAction action);

    public abstract boolean visitRemoveFromCollectionActionBefore(IRemoveFromCollectionAction action);

    public abstract void visitSetActionAfter(ISetAction action);

    public abstract boolean visitSetActionBefore(ISetAction action);

    public abstract void visitVariableDeclarationActionAfter(IVariableDeclarationAction action);

    public abstract boolean visitVariableDeclarationActionBefore(IVariableDeclarationAction action);

    /**
	 * Default implementation that does nothing. Sub-class from this if you don't want to
	 * implement all the interface methods.
	 */
    public static class Stub implements INRLActionDetailVisitor {

        public void visitActionFragmentApplicationActionAfter(IActionFragmentApplicationAction action) {
        }

        public boolean visitActionFragmentApplicationActionBefore(IActionFragmentApplicationAction action) {
            return true;
        }

        public void visitActionFragmentDeclarationAfter(IActionFragmentDeclaration decl) {
        }

        public boolean visitActionFragmentDeclarationBefore(IActionFragmentDeclaration decl) {
            return true;
        }

        public void visitActionRuleDeclarationAfter(IActionRuleDeclaration decl) {
        }

        public boolean visitActionRuleDeclarationBefore(IActionRuleDeclaration decl) {
            return true;
        }

        public void visitAddActionAfter(IAddAction add) {
        }

        public boolean visitAddActionBefore(IAddAction add) {
            return true;
        }

        public void visitCompoundActionAfter(ICompoundAction action) {
        }

        public boolean visitCompoundActionBefore(ICompoundAction action) {
            return true;
        }

        public void visitConditionalActionAfter(IConditionalAction action) {
        }

        public boolean visitConditionalActionBefore(IConditionalAction action) {
            return true;
        }

        public void visitCreateActionAfter(ICreateAction action) {
        }

        public boolean visitCreateActionBefore(ICreateAction action) {
            return true;
        }

        public void visitForEachActionAfter(IForEachAction action) {
        }

        public boolean visitForEachActionBefore(IForEachAction action) {
            return true;
        }

        public void visitOperatorActionAfter(IOperatorAction action) {
        }

        public boolean visitOperatorActionBefore(IOperatorAction action) {
            return true;
        }

        public void visitRemoveActionAfter(IRemoveAction action) {
        }

        public boolean visitRemoveActionBefore(IRemoveAction action) {
            return true;
        }

        public void visitRemoveFromCollectionActionAfter(IRemoveFromCollectionAction action) {
        }

        public boolean visitRemoveFromCollectionActionBefore(IRemoveFromCollectionAction action) {
            return true;
        }

        public void visitSetActionAfter(ISetAction action) {
        }

        public boolean visitSetActionBefore(ISetAction action) {
            return true;
        }

        public void visitVariableDeclarationActionAfter(IVariableDeclarationAction action) {
        }

        public boolean visitVariableDeclarationActionBefore(IVariableDeclarationAction action) {
            return true;
        }

        public void visitArithmeticExpressionAfter(IArithmeticExpression expr) {
        }

        public boolean visitArithmeticExpressionBefore(IArithmeticExpression expr) {
            return true;
        }

        public void visitBinaryOperatorStatementAfter(IBinaryOperatorStatement statement) {
        }

        public boolean visitBinaryOperatorStatementBefore(IBinaryOperatorStatement statement) {
            return true;
        }

        public void visitBinaryPredicateAfter(IBinaryPredicate predicate) {
        }

        public boolean visitBinaryPredicateBefore(IBinaryPredicate predicate) {
            return true;
        }

        public void visitBooleanLiteral(IBooleanLiteral bool) {
        }

        public void visitCardinalityConstraint(ICardinalityConstraint constraint) {
        }

        public void visitCastExpressionAfter(ICastExpression expr) {
        }

        public boolean visitCastExpressionBefore(ICastExpression expr) {
            return true;
        }

        public void visitCollectionIndexAfter(ICollectionIndex index) {
        }

        public boolean visitCollectionIndexBefore(ICollectionIndex index) {
            return true;
        }

        public void visitCompoundReportAfter(ICompoundReport report) {
        }

        public boolean visitCompoundReportBefore(ICompoundReport report) {
            return true;
        }

        public void visitConcatenatedReportAfter(IConcatenatedReport report) {
        }

        public boolean visitConcatenatedReportBefore(IConcatenatedReport report) {
            return true;
        }

        public void visitConditionalReportAfter(IConditionalReport report) {
        }

        public boolean visitConditionalReportBefore(IConditionalReport report) {
            return true;
        }

        public void visitConstraintRuleDeclarationAfter(IConstraintRuleDeclaration decl) {
        }

        public boolean visitConstraintRuleDeclarationBefore(IConstraintRuleDeclaration decl) {
            return true;
        }

        public void visitDecimalNumber(IDecimalNumber number) {
        }

        public void visitExistsStatementAfter(IExistsStatement exists) {
        }

        public boolean visitExistsStatementBefore(IExistsStatement exists) {
            return true;
        }

        public void visitForallStatementAfter(IForallStatement forall) {
        }

        public boolean visitForallStatementBefore(IForallStatement forall) {
            return true;
        }

        public void visitFunctionalExpressionAfter(IFunctionalExpression expr) {
        }

        public boolean visitFunctionalExpressionBefore(IFunctionalExpression expr) {
            return true;
        }

        public void visitGlobalExistsStatementAfter(IGlobalExistsStatement exists) {
        }

        public boolean visitGlobalExistsStatementBefore(IGlobalExistsStatement exists) {
            return true;
        }

        public void visitIfThenStatementAfter(IIfThenStatement ifThen) {
        }

        public boolean visitIfThenStatementBefore(IIfThenStatement ifThen) {
            return true;
        }

        public void visitIntegerNumber(IIntegerNumber number) {
        }

        public void visitIsInPredicateAfter(IIsInPredicate isIn) {
        }

        public boolean visitIsInPredicateBefore(IIsInPredicate isIn) {
            return true;
        }

        public void visitIsNotInPredicateAfter(IIsNotInPredicate isNotIn) {
        }

        public boolean visitIsNotInPredicateBefore(IIsNotInPredicate isNotIn) {
            return true;
        }

        public void visitIsSubtypePredicateAfter(IIsSubtypePredicate subType) {
        }

        public boolean visitIsSubtypePredicateBefore(IIsSubtypePredicate subType) {
            return true;
        }

        public void visitLiteralString(ILiteralString literal) {
        }

        public void visitModelReferenceAfter(IModelReference ref) {
        }

        public boolean visitModelReferenceBefore(IModelReference ref) {
            return true;
        }

        public void visitMultipleExistsStatementAfter(IMultipleExistsStatement statement) {
        }

        public boolean visitMultipleExistsStatementBefore(IMultipleExistsStatement statement) {
            return true;
        }

        public void visitMultipleNotExistsStatementAfter(IMultipleNotExistsStatement statement) {
        }

        public boolean visitMultipleNotExistsStatementBefore(IMultipleNotExistsStatement statement) {
            return true;
        }

        public void visitNotExistsStatementAfter(INotExistsStatement exists) {
        }

        public boolean visitNotExistsStatementBefore(INotExistsStatement exists) {
            return true;
        }

        public void visitOperatorInvocationAfter(IOperatorInvocation op) {
        }

        public boolean visitOperatorInvocationBefore(IOperatorInvocation op) {
            return true;
        }

        public void visitRuleFileAfter(IRuleFile file) {
        }

        public boolean visitRuleFileBefore(IRuleFile file) {
            return true;
        }

        public void visitRuleSetDeclarationAfter(IRuleSetDeclaration decl) {
        }

        public boolean visitRuleSetDeclarationBefore(IRuleSetDeclaration decl) {
            return true;
        }

        public void visitSelectionExpressionAfter(ISelectionExpression expr) {
        }

        public boolean visitSelectionExpressionBefore(ISelectionExpression expr) {
            return true;
        }

        public void visitValidationFragmentApplicationAfter(IValidationFragmentApplication app) {
        }

        public boolean visitValidationFragmentApplicationBefore(IValidationFragmentApplication app) {
            return true;
        }

        public void visitValidationFragmentDeclarationAfter(IValidationFragmentDeclaration decl) {
        }

        public boolean visitValidationFragmentDeclarationBefore(IValidationFragmentDeclaration decl) {
            return true;
        }

        public void visitVariableDeclarationAfter(IVariableDeclaration decl) {
        }

        public boolean visitVariableDeclarationBefore(IVariableDeclaration decl) {
            return true;
        }
    }
}
