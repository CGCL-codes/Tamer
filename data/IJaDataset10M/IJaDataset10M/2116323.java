package org.mov.parser;

public class ExpressionFactory {

    public static Expression newExpression(Token operation) {
        return ExpressionFactory.newExpression(operation, null, null, null);
    }

    public static Expression newExpression(Token operation, Expression arg1) {
        return ExpressionFactory.newExpression(operation, arg1, null, null);
    }

    public static Expression newExpression(Token operation, Expression arg1, Expression arg2) {
        return ExpressionFactory.newExpression(operation, arg1, arg2, null);
    }

    public static Expression newExpression(Token operation, Expression arg1, Expression arg2, Expression arg3) {
        Expression expression = null;
        switch(operation.getType()) {
            case (Token.AND_TOKEN):
                expression = new AndExpression(arg1, arg2);
                break;
            case (Token.OR_TOKEN):
                expression = new OrExpression(arg1, arg2);
                break;
            case (Token.EQUAL_TOKEN):
                expression = new EqualThanExpression(arg1, arg2);
                break;
            case (Token.LESS_THAN_EQUAL_TOKEN):
                expression = new LessThanEqualExpression(arg1, arg2);
                break;
            case (Token.LESS_THAN_TOKEN):
                expression = new LessThanExpression(arg1, arg2);
                break;
            case (Token.GREATER_THAN_TOKEN):
                expression = new GreaterThanExpression(arg1, arg2);
                break;
            case (Token.GREATER_THAN_EQUAL_TOKEN):
                expression = new GreaterThanEqualExpression(arg1, arg2);
                break;
            case (Token.ADD_TOKEN):
                expression = new AddExpression(arg1, arg2);
                break;
            case (Token.SUBTRACT_TOKEN):
                expression = new SubtractExpression(arg1, arg2);
                break;
            case (Token.MULTIPLY_TOKEN):
                expression = new MultiplyExpression(arg1, arg2);
                break;
            case (Token.DIVIDE_TOKEN):
                expression = new DivideExpression(arg1, arg2);
                break;
            case (Token.HELD_TOKEN):
                break;
            case (Token.DAY_OPEN_TOKEN):
                expression = new DayOpenExpression();
                break;
            case (Token.DAY_CLOSE_TOKEN):
                expression = new DayCloseExpression();
                break;
            case (Token.DAY_LOW_TOKEN):
                expression = new DayLowExpression();
                break;
            case (Token.DAY_HIGH_TOKEN):
                expression = new DayHighExpression();
                break;
            case (Token.DAY_VOLUME_TOKEN):
                expression = new DayVolumeExpression();
                break;
            case (Token.NUMBER_TOKEN):
                expression = new NumberExpression(operation.getIntValue());
                break;
            case (Token.LAG_TOKEN):
                expression = new LagExpression(arg1, arg2);
                break;
            case (Token.MIN_TOKEN):
                expression = new MinExpression(arg1, arg2, arg3);
                break;
            case (Token.MAX_TOKEN):
                expression = new MaxExpression(arg1, arg2, arg3);
                break;
            case (Token.AVG_TOKEN):
                expression = new AvgExpression(arg1, arg2, arg3);
                break;
            case (Token.NOT_TOKEN):
                expression = new NotExpression(arg1);
                break;
            case (Token.IF_TOKEN):
                expression = new IfExpression(arg1, arg2, arg3);
                break;
            case (Token.AGE_TOKEN):
                break;
            case (Token.PERCENT_TOKEN):
                expression = new PercentExpression(arg1, arg2);
                break;
            case (Token.NOT_EQUAL_TOKEN):
                expression = new NotEqualExpression(arg1, arg2);
                break;
        }
        if (expression == null) System.out.println("not implemented yet");
        return expression;
    }
}
