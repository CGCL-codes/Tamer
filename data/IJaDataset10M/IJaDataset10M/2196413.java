package net.sf.gwtruts.client.integration.spring;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Reza Alavi
 */
public class RemoteResponse implements IsSerializable {

    private Map<Serializable, Serializable> model;

    private String viewName;

    private Map<String, String> errors;

    public RemoteResponse() {
    }

    public RemoteResponse(String viewName, Map<Serializable, Serializable> model, Map<String, String> errors) {
        setViewName(viewName);
        setModel(model);
        setErrors(errors);
    }

    public Map<Serializable, Serializable> getModel() {
        return model;
    }

    public void setModel(Map model) {
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
