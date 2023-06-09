package org.apache.myfaces.el.convert;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import org.apache.myfaces.shared_impl.util.ClassUtils;

/**
 * Converter for legacy ValueBinding objects. See JSF 1.2 section 5.8.3
 * 
 * ATTENTION: If you make changes to this class, treat javax.faces.component.ValueExpressionToValueBinding accordingly.
 * 
 * @author Stan Silvert
 * @see javax.faces.component.ValueExpressionToValueBinding
 */
@SuppressWarnings("deprecation")
public class ValueExpressionToValueBinding extends ValueBinding implements StateHolder {

    private ValueExpression _valueExpression;

    private boolean isTransient = false;

    public ValueExpressionToValueBinding() {
        _valueExpression = null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((_valueExpression == null) ? 0 : _valueExpression.hashCode());
        result = 31 * result + (isTransient ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ValueExpressionToValueBinding other = (ValueExpressionToValueBinding) obj;
        if (_valueExpression == null) {
            if (other._valueExpression != null) return false;
        } else if (!_valueExpression.equals(other._valueExpression)) return false;
        if (isTransient != other.isTransient) return false;
        return true;
    }

    /**
     * @return the valueExpression
     */
    public ValueExpression getValueExpression() {
        return getNotNullValueExpression();
    }

    /**
     * @return the valueExpression
     */
    private ValueExpression getNotNullValueExpression() {
        if (_valueExpression == null) {
            throw new IllegalStateException("value expression is null");
        }
        return _valueExpression;
    }

    @Override
    public String getExpressionString() {
        return getNotNullValueExpression().getExpressionString();
    }

    /** Creates a new instance of ValueExpressionToValueBinding */
    public ValueExpressionToValueBinding(ValueExpression valueExpression) {
        if (valueExpression == null) {
            throw new IllegalArgumentException("value expression must not be null.");
        }
        _valueExpression = valueExpression;
    }

    @Override
    public void setValue(FacesContext facesContext, Object value) throws EvaluationException, PropertyNotFoundException {
        try {
            getNotNullValueExpression().setValue(facesContext.getELContext(), value);
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public boolean isReadOnly(FacesContext facesContext) throws EvaluationException, PropertyNotFoundException {
        try {
            return getNotNullValueExpression().isReadOnly(facesContext.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public Object getValue(FacesContext facesContext) throws EvaluationException, PropertyNotFoundException {
        try {
            return getNotNullValueExpression().getValue(facesContext.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public Class getType(FacesContext facesContext) throws EvaluationException, PropertyNotFoundException {
        try {
            return getNotNullValueExpression().getType(facesContext.getELContext());
        } catch (javax.el.PropertyNotFoundException e) {
            throw new javax.faces.el.PropertyNotFoundException(e);
        } catch (ELException e) {
            throw new EvaluationException(e);
        }
    }

    public void restoreState(FacesContext facesContext, Object state) {
        if (state != null) {
            if (state instanceof ValueExpression) {
                _valueExpression = (ValueExpression) state;
            } else {
                Object[] stateArray = (Object[]) state;
                _valueExpression = (ValueExpression) ClassUtils.newInstance((String) stateArray[0], ValueExpression.class);
                ((StateHolder) _valueExpression).restoreState(facesContext, stateArray[1]);
            }
        }
    }

    public Object saveState(FacesContext context) {
        if (!isTransient) {
            if (_valueExpression instanceof StateHolder) {
                Object[] state = new Object[2];
                state[0] = _valueExpression.getClass().getName();
                state[1] = ((StateHolder) _valueExpression).saveState(context);
                return state;
            } else {
                return _valueExpression;
            }
        }
        return null;
    }

    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }

    public boolean isTransient() {
        return isTransient;
    }
}
