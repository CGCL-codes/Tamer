package net.smplmathparser;

/**
 * Class representing a binary operator node in a evaluation tree
 * 
 * @author Alex Barfoot
 * 
 */
public class BinaryNode extends EvaluationNode {

    /**
	 * Serial UID
	 */
    private static final long serialVersionUID = 1891013317357691674L;

    /**
	 * The BinaryOperator for the operator to be performed
	 */
    private BinaryOperator operator;

    /**
	 * The first parameter for this binary operator
	 */
    private EvaluationNode parameter1;

    /**
	 * The second parameter for this binary operator
	 */
    private EvaluationNode parameter2;

    /**
	 * Construct a binary node with the given operator
	 * 
	 * @param operator
	 *            The binary operator for this node
	 * @param pos
	 *            The position of the binary operator in the function string
	 * @param function
	 *            The function string that contains this binary operator
	 * @param parent
	 *            The parent tree of this operator node
	 * @throws MathParserException
	 */
    public BinaryNode(BinaryOperator operator, int pos, String function, EvaluationTree parent) throws MathParserException {
        this.operator = operator;
        String leftStr = function.substring(0, pos);
        String rightStr = function.substring(pos + 1, function.length());
        parameter1 = parent.parse(leftStr);
        parameter2 = parent.parse(rightStr);
    }

    @Override
    public double evaluate() {
        double parameter1Value = parameter1.evaluate();
        double parameter2Value = parameter2.evaluate();
        double value = operator.evaluate(parameter1Value, parameter2Value);
        return value;
    }
}
