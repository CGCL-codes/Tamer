package net.sf.jsfcomp.ajaxslider.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.util.Map;

/**
 * UIComponentTagSupport provides common helper methods for
 * JavaServer Faces UIComponent tag handlers.
 */
public abstract class UIComponentTagSupport extends UIComponentTag {

    /**
   * Sets a component string property as a value binding, or string literal.
   *
   * @param component  the Faces component
   * @param attrName   the attribute name
   * @param value      the attribute value
   */
    protected void setStringProperty(UIComponent component, String attrName, String value) {
        if (value == null) return;
        if (isValueReference(value)) {
            component.setValueBinding(attrName, createValueBinding(value));
        } else {
            component.getAttributes().put(attrName, value);
        }
    }

    protected void setIntegerProperty(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue != null) {
            if (isValueReference(attributeValue)) {
                component.setValueBinding(attributeName, getValueBinding(attributeValue));
            } else {
                component.getAttributes().put(attributeName, Integer.valueOf(attributeValue));
            }
        }
    }

    protected ValueBinding getValueBinding(String expression) {
        return FacesContext.getCurrentInstance().getApplication().createValueBinding(expression);
    }

    /**
   * Sets a component boolean property as a value binding, or boolean literal.
   *
   * @param component  the Faces component
   * @param attrName   the attribute name
   * @param value      the attribute value
   */
    protected void setBooleanProperty(UIComponent component, String attrName, String value) {
        if (value == null) return;
        if (isValueReference(value)) {
            component.setValueBinding(attrName, createValueBinding(value));
        } else {
            component.getAttributes().put(attrName, Boolean.valueOf(value));
        }
    }

    /**
   * Sets a component property as a value binding.
   *
   * @param component  the Faces component
   * @param attrName   the attribute name
   * @param value      the attribute value
   */
    protected void setValueBindingProperty(UIComponent component, String attrName, String value) {
        if (value == null) return;
        component.setValueBinding(attrName, createValueBinding(value));
    }

    /**
   * Sets a component property as a method binding.
   *
   * @param component  the Faces component
   * @param attrName   the attribute name
   * @param value      the attribute value
   * @param signature  the method signature
   */
    protected void setMethodBindingProperty(UIComponent component, String attrName, String value, Class[] signature) {
        if (value == null) return;
        Map attrs = component.getAttributes();
        attrs.put(attrName, createMethodBinding(value, signature));
    }

    /**
   * Returns a ValueBinding for the string value.
   *
   * @param value  the attribute string value
   *
   * @return  a parsed ValueBinding
   */
    protected ValueBinding createValueBinding(String value) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        return application.createValueBinding(value);
    }

    /**
   * Returns a MethodBinding for the string value.
   *
   * @param value      the attribute string value
   * @param signature  the method binding signature
   *
   * @return  a parsed MethodBinding
   */
    protected MethodBinding createMethodBinding(String value, Class[] signature) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        return application.createMethodBinding(value, signature);
    }
}
