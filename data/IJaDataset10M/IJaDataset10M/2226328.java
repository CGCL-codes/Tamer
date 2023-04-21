package sk.sigp.eParser;

/**
 * class responsible for parsing expressions
 *   @author Mathew Stafurik
 *   @version 1.0
 */
public class ExpressionParser {

    private PairEvaluator _pairEvaluator;

    private FunctionHolder _functionHolder;

    private VariableHolder _variableHolder;

    public static void main(String[] args) {
        ExpressionParser ep = new ExpressionParser();
        ep.insertVariable("mnozstvo", new Double(1.3f));
        System.out.println((ep.evaluateExpression("if(1==2,1,2)")));
    }

    /**
     * method will solve provided string expression into Double value
     */
    public Double evaluateExpression(String expression) {
        return getPairEvaluator().solvePairs(expression);
    }

    public void insertVariable(String name, Object variable) {
        getVariableHolder().insertVariable(name, variable);
    }

    protected PairEvaluator getPairEvaluator() {
        if (_pairEvaluator == null) {
            _pairEvaluator = new PairEvaluator();
            _pairEvaluator.setVarHolder(getVariableHolder());
            _pairEvaluator.setFunctionHolder(getFunctionHolder());
        }
        return _pairEvaluator;
    }

    protected VariableHolder getVariableHolder() {
        if (_variableHolder == null) {
            _variableHolder = new VariableHolder();
        }
        return _variableHolder;
    }

    protected FunctionHolder getFunctionHolder() {
        if (_functionHolder == null) {
            _functionHolder = new FunctionHolder();
            _functionHolder.setVarHolder(getVariableHolder());
        }
        return _functionHolder;
    }
}
