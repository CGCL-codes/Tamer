package org.apache.myfaces.trinidadinternal.uinode;

import javax.faces.context.FacesContext;
import javax.el.ValueExpression;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.data.BoundValue;

class ValueExpressionBoundValue implements BoundValue {

    public ValueExpressionBoundValue(ValueExpression expression) {
        if (expression == null) throw new NullPointerException();
        _expression = expression;
    }

    /**
   * @todo Better way to retrieve FacesContext
   */
    public Object getValue(UIXRenderingContext rContext) {
        FacesContext fContext = FacesContext.getCurrentInstance();
        return _expression.getValue(fContext.getELContext());
    }

    private final ValueExpression _expression;
}
