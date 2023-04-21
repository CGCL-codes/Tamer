package uk.ac.cam.caret.minibix.qtibank.webutil;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class SimpleELEvaluatorUtil {

    private ExpressionEvaluator evaluator;

    private VariableResolver varResolver;

    private Class defaultExpectedType;

    public SimpleELEvaluatorUtil(PageContext pageContext, Class defaultExpectedType) {
        this.evaluator = pageContext.getExpressionEvaluator();
        this.varResolver = pageContext.getVariableResolver();
        this.defaultExpectedType = defaultExpectedType;
    }

    public SimpleELEvaluatorUtil(PageContext pageContext) {
        this(pageContext, Object.class);
    }

    public Object eval(String expression, Class expectedType) throws ELException {
        return evaluator.evaluate(expression, expectedType, varResolver, null);
    }

    public Object eval(String expression) throws ELException {
        return this.eval(expression, defaultExpectedType);
    }

    public String asStr(String expression) throws ELException {
        return (String) this.eval(expression, defaultExpectedType);
    }
}
