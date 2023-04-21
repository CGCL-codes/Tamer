package agentgui.math.calculation;

/**
 * The Class CalcParameter.
 */
public class CalcParameter extends CalcExpression {

    private static final long serialVersionUID = 1444566766356515442L;

    public static final int CONSTANT_VALUE = 0;

    public static final int FORMULA = 1;

    /** The parameter type. */
    private int parameterType = 0;

    /** 
	 * The current CalcExpression, which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 */
    private CalcExpression calcExpression = null;

    /**
	 * Instantiates a new parameter for calculations.
	 *
	 * @param parameterType the parameter type
	 * @param calcExpression the CalcExpression
	 */
    public CalcParameter(int parameterType, CalcExpression calcExpression) {
        this.setParameterType(parameterType);
        this.setCalcExpression(calcExpression);
    }

    /**
	 * Sets the parameter type.
	 * @param parameterType the parameterType to set
	 */
    public void setParameterType(int parameterType) {
        this.parameterType = parameterType;
    }

    /**
	 * Gets the parameter type.
	 * @return the parameterType
	 */
    public int getParameterType() {
        return parameterType;
    }

    /**
	 * Sets the current CalcExpression which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 * @param calcExpression the calcExpression to set
	 */
    public void setCalcExpression(CalcExpression calcExpression) {
        if (calcExpression instanceof CalcConstant) {
            setParameterType(CalcParameter.CONSTANT_VALUE);
        } else if (calcExpression instanceof CalcFormula) {
            setParameterType(CalcParameter.FORMULA);
        }
        this.calcExpression = calcExpression;
    }

    /**
	 * Gets the current CalcExpression.
	 * @return the calcExpression
	 */
    public CalcExpression getCalcExpression() {
        return this.calcExpression;
    }

    @Override
    public double getValue() throws CalcExeption {
        return this.calcExpression.getValue();
    }
}
