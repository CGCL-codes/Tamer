package org.in4ama.documentengine.evaluator.handlers;

import org.in4ama.documentengine.evaluator.EvaluationContext;
import org.in4ama.documentengine.evaluator.EvaluationHelper;
import org.in4ama.documentengine.evaluator.TagHandler;
import org.in4ama.documentengine.exception.EvaluationException;

/** Basic parameter handling */
public class ParameterHandler {

    public static final String EMPTY_VALUE = "";

    /** Underlying evaluation context */
    private EvaluationContext context;

    /**
	 * Creates a new instance of ParameterHandler class
	 * 
	 * @param context
	 *            owning evaluation context.
	 */
    public ParameterHandler(EvaluationContext context) {
        this.context = context;
    }

    /** Handles parameter tags 
	 * @throws EvaluationException */
    @TagHandler("P")
    public String handle(String parameterName) throws EvaluationException {
        Object param = context.getParameter(parameterName);
        if ((param == null) && context.getProjectContext().getSettingsConfigurationMgr().getForceParams()) {
            String msg = "The required parameter '" + parameterName + "' " + "hasn't been provided.";
            throw new EvaluationException(msg);
        }
        EvaluationHelper evaluator = context.getProjectContext().getInforamaContext().getEvaluationHelper();
        return (param == null) ? EMPTY_VALUE : evaluator.evaluate(param.toString(), context);
    }

    /** Handles the parameters tag, the second argument specifies the value
	 * that, after being evaluated, will be returned when the parameter doesn't exist. */
    @TagHandler("P")
    public String handle(String parameterName, String defaultValue) throws EvaluationException {
        Object param = context.getParameter(parameterName);
        String value = (param != null) ? param.toString() : defaultValue;
        EvaluationHelper evaluator = context.getProjectContext().getInforamaContext().getEvaluationHelper();
        return (value != null) ? evaluator.evaluate(value, context) : EMPTY_VALUE;
    }

    /** Indicates whether the parameter exists. The method returns either a 
	 * string 'true' or 'false'*/
    @TagHandler("hasParam")
    public String hasParam(String parameterName) {
        return context.getParameter(parameterName) != null ? "true" : "false";
    }
}
