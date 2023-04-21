package genetictest;

import genetic.component.context.Context;
import genetic.GeneticComponent;
import genetic.component.expression.Expression;
import genetic.component.statement.function.StatementFunction;
import java.util.List;

public class IntExpressionStatement extends StatementFunction {

    @Override
    public int getNumberInputs() {
        return 1;
    }

    @Override
    public String getInputName(int i) {
        return "expressionInput";
    }

    @Override
    public InputSignature getInputSignature(int i) {
        return new ExpressionInputSignature(Integer.TYPE);
    }

    public void execute(Context context, List<GeneticComponent> inputs) {
        Expression expression = (Expression) inputs.get(0);
        System.out.println(expression.evaluate(context));
    }
}
