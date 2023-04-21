package javax.faces.component;

import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFComponent;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFProperty;

/**
 * Displays a value to the user.
 */
@JSFComponent(defaultRendererType = "javax.faces.Text")
public class UIOutput extends UIComponentBase implements ValueHolder {

    public static final String COMPONENT_TYPE = "javax.faces.Output";

    public static final String COMPONENT_FAMILY = "javax.faces.Output";

    private Converter _converter;

    /**
     * Construct an instance of the UIOutput.
     */
    public UIOutput() {
        setRendererType("javax.faces.Text");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getLocalValue() {
        return getStateHelper().get(PropertyKeys.value);
    }

    /**
     * Gets The initial value of this component.
     * 
     * @return the new value value
     */
    @JSFProperty
    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value);
    }

    /**
     * The initial value of this component.
     */
    public void setValue(Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    /**
     * An expression that specifies the Converter for this component.
     * <p>
     * The value can either be a static value (ID) or an EL expression. When a static id is
     * specified, an instance of the converter type registered with that id is used. When this
     * is an EL expression, the result of evaluating the expression must be an object that
     * implements the Converter interface.
     * </p>
     */
    @JSFProperty(stateHolder = true)
    public Converter getConverter() {
        if (_converter != null) {
            return _converter;
        }
        ValueExpression expression = getValueExpression("converter");
        if (expression != null) {
            return (Converter) expression.getValue(getFacesContext().getELContext());
        }
        return null;
    }

    public void setConverter(Converter converter) {
        this._converter = converter;
        if (initialStateMarked()) {
            getStateHelper().put(PropertyKeys.converterSet, Boolean.TRUE);
        }
    }

    private boolean _isSetConverter() {
        Boolean value = (Boolean) getStateHelper().get(PropertyKeys.converterSet);
        return value == null ? false : value;
    }

    public void markInitialState() {
        super.markInitialState();
        if (_converter != null && _converter instanceof PartialStateHolder) {
            ((PartialStateHolder) _converter).markInitialState();
        }
    }

    public void clearInitialState() {
        if (initialStateMarked()) {
            super.clearInitialState();
            if (_converter != null && _converter instanceof PartialStateHolder) {
                ((PartialStateHolder) _converter).clearInitialState();
            }
        }
    }

    enum PropertyKeys {

        value, converterSet
    }

    @Override
    public Object saveState(FacesContext facesContext) {
        if (initialStateMarked()) {
            Object parentSaved = super.saveState(facesContext);
            Object converterSaved = null;
            boolean nullDelta = true;
            if (!_isSetConverter() && _converter != null && _converter instanceof PartialStateHolder) {
                StateHolder holder = (StateHolder) _converter;
                if (!holder.isTransient()) {
                    Object attachedState = holder.saveState(facesContext);
                    if (attachedState != null) {
                        nullDelta = false;
                    }
                    converterSaved = new _AttachedDeltaWrapper(_converter.getClass(), attachedState);
                } else {
                    converterSaved = null;
                }
            } else if (_isSetConverter() || _converter != null) {
                converterSaved = saveAttachedState(facesContext, _converter);
                nullDelta = false;
            }
            if (parentSaved == null && nullDelta) {
                return null;
            }
            return new Object[] { parentSaved, converterSaved };
        } else {
            Object[] values = new Object[2];
            values[0] = super.saveState(facesContext);
            values[1] = saveAttachedState(facesContext, _converter);
            return values;
        }
    }

    @Override
    public void restoreState(FacesContext facesContext, Object state) {
        if (state == null) {
            return;
        }
        Object[] values = (Object[]) state;
        super.restoreState(facesContext, values[0]);
        if (values[1] instanceof _AttachedDeltaWrapper) {
            ((StateHolder) _converter).restoreState(facesContext, ((_AttachedDeltaWrapper) values[1]).getWrappedStateObject());
        } else {
            _converter = (javax.faces.convert.Converter) restoreAttachedState(facesContext, values[1]);
        }
    }
}
