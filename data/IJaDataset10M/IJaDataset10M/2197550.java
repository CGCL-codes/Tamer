package org.jquery4jsf.application.ajax;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.servlet.ServletResponse;
import org.jquery4jsf.application.AjaxPhaseListener;
import org.jquery4jsf.component.ComponentUtilities;
import org.jquery4jsf.custom.AjaxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultAjaxRequestHandler implements AjaxRequestHandler {

    private static Logger logger = LoggerFactory.getLogger(DefaultAjaxRequestHandler.class);

    public void handleAjaxRequest(PhaseEvent event) throws IOException {
        FacesContext context = event.getFacesContext();
        Map params = context.getExternalContext().getRequestParameterMap();
        boolean isAjaxRequest = params.containsKey(AjaxPhaseListener.AJAX_VIEW_ID);
        if (isAjaxRequest) {
            boolean isPartialSubmit = params.containsKey(AjaxPhaseListener.AJAX_PS_ID);
            if (isPartialSubmit) {
                String updateIds = (String) params.get(AjaxPhaseListener.AJAX_PS_ID);
                ServletResponse response = (ServletResponse) context.getExternalContext().getResponse();
                response.setContentType("text/html");
                try {
                    String values[] = updateIds.split(":");
                    int y = values.length - 1;
                    updateIds = values[y];
                    UIComponent component = ComponentUtilities.findComponentInRoot(updateIds);
                    if (component != null) {
                        ComponentUtilities.encodeAll(context, component);
                    } else {
                        logger.error("Component not found to be updated");
                    }
                } catch (IOException e) {
                    logger.error("ajaxRequest error: " + e.getMessage());
                }
            } else {
                String id = (String) params.get(AjaxPhaseListener.AJAX_VIEW_ID);
                if (id != null && !id.equalsIgnoreCase("")) {
                    String values[] = id.split(":");
                    int i = values.length - 1;
                    id = values[i];
                    UIComponent component = ComponentUtilities.findComponentInRoot(id);
                    if (component != null && component instanceof AjaxComponent) {
                        ((AjaxComponent) component).encodePartially(context);
                    } else {
                        logger.error("Component not found to be updated");
                    }
                }
            }
        }
    }
}
