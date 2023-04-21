package astcentric.structure.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Evaluator of a {@link Function}.
 */
public class FunctionEvaluator extends Evaluator {

    private final ArgumentEvaluator _functionProvider;

    private final List<ArgumentEvaluator> _argumentEvaluators;

    public FunctionEvaluator(ArgumentEvaluator functionProvider, ArgumentEvaluator... argumentEvaluators) {
        this(functionProvider, Arrays.asList(argumentEvaluators));
    }

    /**
   * Creates an instance for the specified provider of the function which
   * will be calculated and a list of evaluators for the arguments of the
   * function call.
   */
    public FunctionEvaluator(ArgumentEvaluator functionProvider, List<ArgumentEvaluator> argumentEvaluators) {
        if (functionProvider == null) {
            throw new IllegalArgumentException("Unspecified function provider.");
        }
        _functionProvider = functionProvider;
        _argumentEvaluators = Util.createUnmodifiableList(argumentEvaluators);
    }

    Data evaluate(CalculationContext calculationContext, PatternBindings bindings) {
        List<Function> arguments = new ArrayList<Function>();
        for (ArgumentEvaluator evaluator : _argumentEvaluators) {
            arguments.add(evaluator.evaluate(calculationContext, bindings));
        }
        Function function = _functionProvider.evaluate(calculationContext, bindings);
        return function.calculate(calculationContext, arguments);
    }
}
