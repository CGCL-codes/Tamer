package xpath.functions;

import xpath.BooleanExpression;
import xpath.StringExpression;

public class StartsWithFunction extends BooleanExpression {

    public StartsWithFunction() {
    }

    public boolean evaluate() {
        StringExpression firstArgument = (StringExpression) arguments.get(0);
        StringExpression secondArgument = (StringExpression) arguments.get(1);
        return firstArgument.evaluate().startsWith(secondArgument.evaluate());
    }
}
