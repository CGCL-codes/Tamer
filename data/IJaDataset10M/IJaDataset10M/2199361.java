package uk.org.ogsadai.dqp.lqp.optimiser.implosion;

import java.util.List;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ArithmeticExpressionOperand;
import uk.org.ogsadai.expression.EqualExpression;
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
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpressionVisitor;

public class SetSourceExpressionVisitor implements ExpressionVisitor {

    private ArithmeticExpressionVisitor mVisitor;

    public SetSourceExpressionVisitor(ArithmeticExpressionVisitor aeVisitor) {
        mVisitor = aeVisitor;
    }

    @Override
    public void visitAndExpression(AndExpression expression) {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitEqualExpression(EqualExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitGreaterThanExpression(GreaterThanExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitGreaterThanOrEqualExpression(GreaterThanOrEqualExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitIsNullExpression(IsNullExpression expression) {
        Operand operand = expression.getOperand();
        if (operand instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) operand).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitLessThanExpression(LessThanExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitLessThanOrEqualExpression(LessThanOrEqualExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitLikeExpression(LikeExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitNotEqualExpression(NotEqualExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        Operand right = expression.getRightOperand();
        if (right instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
        }
    }

    @Override
    public void visitNotExpression(NotExpression expression) {
        expression.getChildExpression().accept(this);
    }

    @Override
    public void visitOrExpression(OrExpression expression) {
        expression.getLeftExpression().accept(this);
        expression.getRightExpression().accept(this);
    }

    @Override
    public void visitInExpression(InExpression expression) {
        Operand left = expression.getLeftOperand();
        if (left instanceof ArithmeticExpressionOperand) {
            ((ArithmeticExpressionOperand) left).getExpression().accept(mVisitor);
        }
        List<Operand> rights = expression.getRightOperands();
        for (Operand right : rights) {
            if (right instanceof ArithmeticExpressionOperand) {
                ((ArithmeticExpressionOperand) right).getExpression().accept(mVisitor);
            }
        }
    }
}
