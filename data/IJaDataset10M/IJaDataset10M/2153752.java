package org.apache.myfaces.trinidadinternal.taglib.util;

import java.io.Serializable;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;

public class MethodExpressionMethodBinding extends MethodBinding implements Serializable {

    public MethodExpressionMethodBinding(MethodExpression expression) {
        if (expression == null) throw new NullPointerException();
        _expression = expression;
    }

    public String getExpressionString() {
        return _expression.getExpressionString();
    }

    public Class getType(FacesContext context) {
        return _expression.getMethodInfo(context.getELContext()).getReturnType();
    }

    public Object invoke(FacesContext context, Object[] params) {
        try {
            return _expression.invoke(context.getELContext(), params);
        } catch (ELException ee) {
            throw new EvaluationException(ee.getMessage(), ee.getCause());
        }
    }

    private MethodExpression _expression;
}
