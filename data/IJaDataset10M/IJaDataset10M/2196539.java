package fr.expression4j.core.predefine;

import java.util.List;
import fr.expression4j.core.Catalog;
import fr.expression4j.core.Expression;
import fr.expression4j.core.Parameters;
import fr.expression4j.core.exception.EvalException;
import fr.expression4j.core.exception.ParametersException;
import fr.expression4j.util.ParameterUtil;

public class SqrtFunction implements Expression {

    public Catalog getCatalog() {
        return null;
    }

    public double evaluate(Parameters parameters) throws EvalException {
        try {
            double x = parameters.getParameter("x");
            return Math.sqrt(x);
        } catch (ParametersException pe) {
            throw new EvalException("Could not find parameter x for function " + getName());
        }
    }

    public List<String> getParameters() {
        return ParameterUtil.generateXParameters();
    }

    public String getName() {
        return "sqrt";
    }
}
