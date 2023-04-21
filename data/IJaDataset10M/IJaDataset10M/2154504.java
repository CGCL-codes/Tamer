package agentgui.math.calculation;

import java.util.HashMap;
import java.util.Iterator;
import org.nfunk.jep.JEP;

/**
 * Class for calculations using the JEP Parser.
 *
 * @author Nils
 */
public class CalcFormula extends CalcExpression {

    private static final long serialVersionUID = -669801540602778489L;

    /** The formula to calculate. */
    private String formula;

    /**
	 * List of parameters (key = parameter name,  value = parameter value). 
	 */
    private HashMap<String, CalcParameter> parameters = new HashMap<String, CalcParameter>();

    /**
	 * Gets the formula.
	 * @return the formula
	 */
    public String getFormula() {
        return formula;
    }

    /**
	 * This method sets the formula.
	 * @param formula the formula to set
	 */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
	 * This method adds a parameter to the formula's parameter list.
	 * @param name The parameter name
	 * @param expression The parameter value
	 */
    public void addParameter(String name, CalcExpression expression) {
        CalcParameter parameter = null;
        if (expression instanceof CalcConstant) {
            parameter = new CalcParameter(CalcParameter.CONSTANT_VALUE, expression);
        } else if (expression instanceof CalcFormula) {
            parameter = new CalcParameter(CalcParameter.FORMULA, expression);
        }
        this.parameters.put(name, parameter);
    }

    /**
	 * Gets the parameters.
	 * @return the parameters
	 */
    public HashMap<String, CalcParameter> getParameters() {
        return this.parameters;
    }

    /**
	 * Sets the parameters.
	 * @param parameters the parameters to set
	 */
    public void setParameters(HashMap<String, CalcParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public double getValue() throws CalcExeption {
        if (formula == null) {
            throw new CalcFormulaNotSetException();
        }
        JEP parser = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        Iterator<String> params = parameters.keySet().iterator();
        while (params.hasNext()) {
            String key = params.next();
            parser.addVariable(key, parameters.get(key).getValue());
        }
        parser.parseExpression(formula);
        parser.getSymbolTable().toString();
        double result = parser.getValue();
        if (Double.isNaN(result)) {
            throw new CalcParameterNotSetException();
        }
        return result;
    }
}
