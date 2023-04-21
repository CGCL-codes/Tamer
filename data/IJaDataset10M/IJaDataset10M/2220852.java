package uk.org.ogsadai.expression.visitors;

import java.util.ArrayList;
import java.util.List;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.BooleanExpression;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.GreaterThanExpression;
import uk.org.ogsadai.expression.GreaterThanOrEqualExpression;
import uk.org.ogsadai.expression.InExpression;
import uk.org.ogsadai.expression.IsNullExpression;
import uk.org.ogsadai.expression.LessThanExpression;
import uk.org.ogsadai.expression.LessThanOrEqualExpression;
import uk.org.ogsadai.expression.LikeExpression;
import uk.org.ogsadai.expression.NotEqualExpression;
import uk.org.ogsadai.expression.NotExpression;
import uk.org.ogsadai.expression.Operand;
import uk.org.ogsadai.expression.OrExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.CloneArithmeticExprVisitor;

/**
 * Cloning expression visitor.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CloneExpressionVisitor implements ExpressionVisitor {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Cloned expression. */
    private Expression mClonedExpression;

    /** Current left expression. */
    private Expression mLeftExpression;

    /** Current right expression. */
    private Expression mRightExpression;

    /** Current left operand. */
    private Operand mLeftOperand;

    /** Current right operand. */
    private Operand mRightOperand;

    /**
     * Visit boolean expression.
     * 
     * @param expression
     *            boolean expression
     */
    private void visitBoolean(BooleanExpression expression) {
        expression.getLeftExpression().accept(this);
        mLeftExpression = mClonedExpression;
        expression.getRightExpression().accept(this);
        mRightExpression = mClonedExpression;
    }

    /**
     * Visit comparison expression.
     * 
     * @param expression
     *            comparison expression
     */
    private void visitComparison(ComparisonExpression expression) {
        mLeftOperand = cloneOperand(expression.getLeftOperand());
        mRightOperand = cloneOperand(expression.getRightOperand());
    }

    /**
     * {@inheritDoc}
     */
    public void visitAndExpression(AndExpression expression) {
        visitBoolean(expression);
        mClonedExpression = new AndExpression(mLeftExpression, mRightExpression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitEqualExpression(EqualExpression expression) {
        visitComparison(expression);
        mClonedExpression = new EqualExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanExpression(GreaterThanExpression expression) {
        visitComparison(expression);
        mClonedExpression = new GreaterThanExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression) {
        visitComparison(expression);
        mClonedExpression = new GreaterThanOrEqualExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitIsNullExpression(IsNullExpression expression) {
        Operand operand = cloneOperand(expression.getOperand());
        mClonedExpression = new IsNullExpression(operand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanExpression(LessThanExpression expression) {
        visitComparison(expression);
        mClonedExpression = new LessThanExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression) {
        visitComparison(expression);
        mClonedExpression = new LessThanOrEqualExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitLikeExpression(LikeExpression expression) {
        visitComparison(expression);
        mClonedExpression = new LikeExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitInExpression(InExpression expression) {
        Operand leftOperand = cloneOperand(expression.getLeftOperand());
        List<Operand> rightOperands = new ArrayList<Operand>(expression.getRightOperands().size());
        for (Operand rightOperand : expression.getRightOperands()) {
            rightOperands.add(cloneOperand(rightOperand));
        }
        mClonedExpression = new InExpression(leftOperand, rightOperands);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotEqualExpression(NotEqualExpression expression) {
        visitComparison(expression);
        mClonedExpression = new NotEqualExpression(mLeftOperand, mRightOperand);
    }

    /**
     * {@inheritDoc}
     */
    public void visitNotExpression(NotExpression expression) {
        expression.getChildExpression().accept(this);
        mClonedExpression = new NotExpression(mClonedExpression);
    }

    /**
     * {@inheritDoc}
     */
    public void visitOrExpression(OrExpression expression) {
        visitBoolean(expression);
        mClonedExpression = new OrExpression(mLeftExpression, mRightExpression);
    }

    /**
     * Gets cloned expression.
     * 
     * @return expression clone
     */
    public Expression getClonedExpression() {
        return mClonedExpression;
    }

    /**
     * Clones an operand.
     * 
     * @param operand operand to be clones
     * 
     * @return the clone
     */
    private Operand cloneOperand(Operand operand) {
        return new ArithmeticExpressionOperand(CloneArithmeticExprVisitor.cloneExpression(((ArithmeticExpressionOperand) operand).getExpression()));
    }
}
