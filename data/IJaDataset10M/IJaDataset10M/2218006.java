package fi.arcusys.acj.util.web;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

/**
 * Facade for {@link FacesContext} operations.
 * 
 * @version 1.0 $Rev$
 * @author mikko
 * Copyright (C) 2007 Arcusys Oy
 */
public class FacesHelper {

    /**
	 * Return the current <code>FacesContext</code>.
	 * @return the current <code>FacesContext</code> instance
	 */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
	 * Return the current <code>Application</code> instance.
	 * 
	 * <p>This method is shortcut to calling 
	 * <code>getFacesContext().getApplication()</code>.</p>
	 * @return the current <code>Application</code> instance.
	 */
    public static Application getApplication() {
        return getFacesContext().getApplication();
    }

    /**
	 * Return the current <code>VariableResolver</code> instance.
	 * 
	 * <p>This method is a shortcut to calling
	 * <code>getApplication().getVariableResolver()</code>.</p>
	 * 
	 * @return the current <code>VariableResolver</code> instance
	 */
    public static VariableResolver getVariableResolver() {
        return getApplication().getVariableResolver();
    }

    /**
	 * Get a named bean from the current context.
	 * @param name name of the bean
	 * @return the bean or <code>null</code> if no such bean exists
	 */
    public static Object getBean(String name) {
        return getVariableResolver().resolveVariable(getFacesContext(), name);
    }
}
