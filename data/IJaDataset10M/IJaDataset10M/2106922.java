package genetic.component.statementlist.mutator;

import genetic.BuildException;
import genetic.Foundation;
import genetic.GeneticComponent;
import genetic.MutatorAction;
import genetic.component.expression.Expression;
import genetic.component.expression.function.VariableExpressionFunction;
import genetic.component.statement.Statement;
import genetic.component.statement.function.AssignmentStatementFunction;
import genetic.component.statementlist.StatementList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lccstudent
 */
public class ExtractVariable extends MutatorAction<StatementList> {

    public ExtractVariable() {
    }

    private static class Result {

        Expression expression;

        Statement statement;

        int index;

        public Result(Expression expression, Statement statement, int index) {
            this.expression = expression;
            this.statement = statement;
            this.index = index;
        }
    }

    @Override
    public boolean mutate(StatementList target) throws BuildException {
        Result expressionResult = findExpression(target);
        if (expressionResult == null) {
            return false;
        }
        Class variableType = expressionResult.expression.getOutputType();
        String variableName = target.getContextModel().declareVariable(variableType, false);
        AssignmentStatementFunction assignment = new AssignmentStatementFunction();
        assignment.setVariable(variableName, variableType);
        Statement declaration = new Statement(assignment, target);
        declaration.setInput(0, expressionResult.expression);
        List<Statement> newStatements = new ArrayList(target.getStatements());
        newStatements.add(0, declaration);
        target.setStatements(newStatements.toArray(new Statement[newStatements.size()]));
        VariableExpressionFunction variable = new VariableExpressionFunction(variableName, variableType);
        Expression variableExpression = new Expression(variable, expressionResult.statement);
        expressionResult.statement.setInput(expressionResult.index, variableExpression);
        return true;
    }

    private static int NUMBER_ATTEMPTS = 5;

    private Result findExpression(StatementList target) {
        for (int i = 0; i < NUMBER_ATTEMPTS; i++) {
            int numberStatements = target.getStatements().size();
            Random rand = Foundation.getInstance().getBuilderRandom();
            Statement statement = target.getStatements().get(rand.nextInt(numberStatements));
            for (int j = 0; j < NUMBER_ATTEMPTS; j++) {
                int numberInputs = statement.getFunction().getNumberInputs();
                int index = rand.nextInt(numberInputs);
                GeneticComponent input = statement.getInput(index);
                if (input instanceof Expression) {
                    return new Result((Expression) input, statement, index);
                }
            }
        }
        return null;
    }
}
