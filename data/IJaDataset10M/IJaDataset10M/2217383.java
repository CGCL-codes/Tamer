package org.jquery4jsf.taglib.html.ext;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;
import org.jquery4jsf.component.ComponentUtilities;

public abstract class UIComponentTagBase extends UIComponentTag {

    public static void setIntegerProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setIntegerProperty(context, component, propertyName, value);
    }

    public static void setStringProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setStringProperty(context, component, propertyName, value);
    }

    public static void setBooleanProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setBooleanProperty(context, component, propertyName, value);
    }

    public static void setFloatProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setFloatProperty(context, component, propertyName, value);
    }

    public static void setDoubleProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setDoubleProperty(context, component, propertyName, value);
    }

    public static void setConverterProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setConverterProperty(context, component, propertyName, value);
    }

    public static void setValueProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setValueProperty(context, component, propertyName, value);
    }

    public static void setActionListenerProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setActionListenerProperty(context, component, value);
    }

    public static void setActionProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setActionProperty(context, component, value);
    }

    public static void setMethodBindingProperty(FacesContext context, UIComponent component, String propertyName, String value, Class[] param) {
        ComponentUtilities.setMethodBindingProperty(context, component, propertyName, value, param);
    }

    public static void setValidatorProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setValidatorProperty(context, component, propertyName, value);
    }

    public static void setValueChangeListenerProperty(FacesContext context, UIComponent component, String propertyName, String value) {
        ComponentUtilities.setValueChangeListenerProperty(context, component, propertyName, value);
    }
}
