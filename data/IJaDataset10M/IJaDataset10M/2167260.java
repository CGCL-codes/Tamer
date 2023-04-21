package au.gov.nla.aons.mvc.actions.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class CheckRepositoryTypeChosenValidAction implements org.springframework.webflow.execution.Action {

    List<String> validTypes = new ArrayList<String>();

    public Event execute(RequestContext context) throws Exception {
        String type = context.getRequestParameters().get("type");
        if (type == null || type.trim().equals("")) {
            context.getFlashScope().put("typeError", "Type Cannot Be Null");
            return new Event(this, "failure");
        }
        if (!validTypes.contains(type)) {
            context.getFlashScope().put("typeError", "Invalid Type Chosen [" + type.replaceAll("([a-z])([A-Z0-9])", "$1 $2") + "]");
            return new Event(this, "failure");
        }
        return new Event(this, "success");
    }

    public List<String> getValidTypes() {
        return validTypes;
    }

    public void setValidTypes(List<String> validTypes) {
        this.validTypes = validTypes;
    }
}
