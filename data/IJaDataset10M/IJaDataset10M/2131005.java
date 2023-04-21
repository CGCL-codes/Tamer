package com.germinus.merlin.controller.administration;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class AdministrationViewController extends AbstractController implements InitializingBean {

    private static final Log log = LogFactory.getLog(AdministrationViewController.class);

    /**
     * Process the action request. There is nothing to return.
     */
    @Override
    protected void handleActionRequestInternal(ActionRequest request, ActionResponse response) throws Exception {
        log.debug("handleActionRequestInternal");
    }

    /**
     * Process the render request and return a ModelAndView object which the
     * DispatcherPortlet will render.
     */
    public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        log.debug("handleRenderRequestInternal");
        Map<String, Object> model = new HashMap<String, Object>();
        return new ModelAndView("administration/viewAdministration", "model", model);
    }

    public void afterPropertiesSet() throws Exception {
    }
}
