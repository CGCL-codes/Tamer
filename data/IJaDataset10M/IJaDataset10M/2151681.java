package net.sourceforge.xconf.toolbox.spring.action;

import java.util.HashMap;
import java.util.Map;

/**
 * A result object returned by all ActionCommandController actions.
 * 
 * @author Tom Czarniecki
 */
public class ActionResult {

    private Map model;

    private String viewName;

    private boolean appendErrors = true;

    public ActionResult(String viewName) {
        this.viewName = viewName;
        this.model = new HashMap();
    }

    public ActionResult(String viewName, Map model) {
        this.viewName = viewName;
        this.model = model;
    }

    public ActionResult(String viewName, String key, Object value) {
        this.viewName = viewName;
        this.model = new HashMap();
        this.model.put(key, value);
    }

    public void setAppendErrors(boolean appendErrors) {
        this.appendErrors = appendErrors;
    }

    /**
     * If <code>true</code>, the ActionCommandController will create a ModelAndView instance that contains both
     * the model returned by this class and the BindException model containing any validation errors,
     * otherwise only the model returned by this class will be used.
     * <p>
     * Default is <code>true</code> and should ONLY be changed to <code>false</code> if you intend for your
     * result view to be a redirect view, and therefore don't require the command or bind errors to be shown
     * in the redirect query string.
     * 
     * @see ActionCommandController#createModelAndView(ActionResult, org.springframework.validation.BindException)
     */
    public boolean isAppendErrors() {
        return appendErrors;
    }

    /**
     * Returns the name of the view to be used by the ActionCommandController to create a ModelAndView instance.
     * 
     * @see ActionCommandController#createModelAndView(ActionResult, org.springframework.validation.BindException)
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Returns the model to be used by the ActionCommandController to create a ModelAndView instance.
     * 
     * @see ActionCommandController#createModelAndView(ActionResult, org.springframework.validation.BindException)
     */
    public Map getModel() {
        return model;
    }
}
