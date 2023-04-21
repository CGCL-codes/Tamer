package org.powerstone.ca.web;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import javax.servlet.ServletException;
import org.powerstone.ca.model.WebModule;
import org.powerstone.ca.service.ResourceManager;

public class WebModuleFormController extends SimpleFormController {

    private final Log log = LogFactory.getLog(WebModuleFormController.class);

    private ResourceManager resourceManager;

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getSuccessView());
        }
        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }
        WebModule webModule = (WebModule) command;
        if (webModule.getWebModuleID().longValue() == -1) {
            resourceManager.createWebModule(webModule);
        } else {
            resourceManager.updateWebModule(webModule);
        }
        request.getSession().setAttribute("message", getText("ca.web_module.saved", webModule.getWebModuleName()));
        return new ModelAndView(getSuccessView());
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        String webModuleID = request.getParameter("webModuleID");
        if ((webModuleID != null) && !webModuleID.equals("-1")) {
            return resourceManager.findWebModule(webModuleID);
        } else {
            WebModule webModule = new WebModule();
            if (log.isDebugEnabled()) {
                log.debug("entering 'formBackingObject' method..." + webModule);
            }
            return webModule;
        }
    }

    public String getText(String msgKey) {
        return getMessageSourceAccessor().getMessage(msgKey);
    }

    public String getText(String msgKey, String arg) {
        return getText(msgKey, new Object[] { arg });
    }

    public String getText(String msgKey, Object[] args) {
        return getMessageSourceAccessor().getMessage(msgKey, args);
    }
}
