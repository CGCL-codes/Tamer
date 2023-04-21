package com.webstate.customTags;

import java.util.Map;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class PrintErrorsTag extends UIComponentTag {

    private String errors;

    private String styleClass;

    public void release() {
        super.release();
        errors = null;
        styleClass = null;
    }

    /*****
     * 
     */
    @SuppressWarnings("unchecked")
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        Map<String, Object> map = component.getAttributes();
        if (styleClass != null) map.put("styleClass", styleClass);
        if (errors != null) {
            if (isValueReference(errors)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application application = context.getApplication();
                ValueBinding binding = application.createValueBinding(errors);
                component.setValueBinding("errors", binding);
                map.put("errors", errors);
            } else {
                map.put("errors", errors);
            }
        }
    }

    public String getComponentType() {
        return "printErrors";
    }

    public String getRendererType() {
        return null;
    }

    /**
     * @return Returns the errors.
     */
    public String getErrors() {
        return errors;
    }

    /**
     * @param errors
     *            The errors to set.
     */
    public void setErrors(String myPresence) {
        this.errors = myPresence;
    }

    /**
     * @return Returns the styleClass.
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * @param styleClass The styleClass to set.
     */
    public void setStyleClass(String name) {
        this.styleClass = name;
    }
}
