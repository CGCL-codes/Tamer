package com.grooveapp.taglib;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;

public class JSPUtils {

    public static Object eval(String expr, Class type, PageContext jspContext) throws JspException {
        try {
            if (expr.indexOf("${") == -1) return expr;
            ExpressionEvaluator evaluator = jspContext.getExpressionEvaluator();
            return evaluator.evaluate(expr, type, jspContext.getVariableResolver(), null);
        } catch (ELException e) {
            throw new JspException(e);
        }
    }

    public static Object eval(String expr, Class type, JspContext jspContext) throws JspException {
        try {
            if (expr.indexOf("${") == -1) return expr;
            ExpressionEvaluator evaluator = jspContext.getExpressionEvaluator();
            return evaluator.evaluate(expr, type, jspContext.getVariableResolver(), null);
        } catch (ELException e) {
            throw new JspException(e);
        }
    }
}
