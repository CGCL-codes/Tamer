package au.gov.nla.aons.mvc.actions;

import java.util.Map;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Allows for the delegation of type specific form action objects
 * for different implementations of a domain object.
 * 
 * @author DLEVY
 */
public class TypeSpecificFormAction extends MultiAction {

    private Map<String, FormAction> typeFormActionMap;

    private String flowTypeName = "type";

    /**
	 * Retrieves the type specifier from the request parameters.
	 */
    public Event initMultiTypeForm(RequestContext context) throws Exception {
        String typeName = context.getRequestParameters().get(flowTypeName);
        context.getFlowScope().put(flowTypeName, typeName);
        return new Event(this, "success");
    }

    /**
	 * Method re-used throughout to retrieve the desired FormAction
	 * object which corresponds to the type specifier in FlowScope.
	 */
    protected FormAction retrieveFormAction(RequestContext context) {
        String typeName = (String) context.getFlowScope().get(flowTypeName);
        if (typeName == null) {
            throw new RuntimeException("You must call the method [initMultiTypeForm] before calling other form methods");
        }
        FormAction formAction = typeFormActionMap.get(typeName);
        if (formAction == null) {
            throw new RuntimeException("There is no FormAction for the given type: " + typeName);
        }
        return formAction;
    }

    public Event bind(RequestContext context) throws Exception {
        return retrieveFormAction(context).bind(context);
    }

    public Event bindAndValidate(RequestContext context) throws Exception {
        return retrieveFormAction(context).bindAndValidate(context);
    }

    public Event resetForm(RequestContext context) throws Exception {
        return retrieveFormAction(context).resetForm(context);
    }

    public Event setupForm(RequestContext context) throws Exception {
        return retrieveFormAction(context).setupForm(context);
    }

    public Event validate(RequestContext context) throws Exception {
        return retrieveFormAction(context).validate(context);
    }

    public String getFlowTypeName() {
        return flowTypeName;
    }

    public void setFlowTypeName(String flowTypeName) {
        this.flowTypeName = flowTypeName;
    }

    public Map<String, FormAction> getTypeFormActionMap() {
        return typeFormActionMap;
    }

    public void setTypeFormActionMap(Map<String, FormAction> typeFormActionMap) {
        this.typeFormActionMap = typeFormActionMap;
    }
}
