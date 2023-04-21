package au.gov.nla.aons.mvc.actions;

import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import au.gov.nla.aons.format.domain.AonsFormat;

public class FormatRemovePreviousAction implements Action {

    public Event execute(RequestContext context) throws Exception {
        AonsFormat format = (AonsFormat) context.getFlowScope().get("format");
        format.setPreviousVersion(null);
        return new Event(this, "success");
    }
}
