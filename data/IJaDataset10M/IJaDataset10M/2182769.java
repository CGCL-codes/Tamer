package org.allesta.wsabi.manage.service.action;

import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.allesta.wsabi.manage.service.ServiceManager;
import org.allesta.wsabi.util.Globals;
import org.allesta.wsabi.util.WebKeys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

/**
 * DOCUMENT ME!
 *
 * @author Allesta, LLC
 * @version $Revision: 1.1 $ 
 */
public class ManageServiceAction extends DispatchAction {

    private static Log logger = LogFactory.getLog(ManageServiceAction.class);

    private ActionMessages _overrideMessages = null;

    /**
     * DOCUMENT ME!
     *
     * @param actionServlet DOCUMENT ME!
     */
    public void setServlet(ActionServlet actionServlet) {
        super.setServlet(actionServlet);
        Boolean isDemoMode = (Boolean) getServlet().getServletContext().getAttribute(Globals.DEMO_MODE);
        Boolean isReadOnly = (Boolean) getServlet().getServletContext().getAttribute(Globals.READ_ONLY);
        if (isReadOnly.booleanValue() && (_overrideMessages != null)) {
            _overrideMessages = new ActionMessages();
            _overrideMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("service.unavailable.readonly"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward disableAuditing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("disableAuditing: " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("disableAuditing(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.disableAuditing(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.disableaudit", "service."));
            if (logger.isDebugEnabled()) {
                logger.debug("Service auditing now disabled for: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward disableWSRM(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("disableWSRM(): " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("disableWSRM(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.disableWSRM(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.disblewsrm", "service."));
            if (logger.isDebugEnabled()) {
                logger.debug("Service now disabled for WS-ReliableMessaging: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward enableAuditing(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("enableAuditing(): entered. " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("enableAuditing(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.enableAuditing(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.enableaudit", "service."));
            if (logger.isDebugEnabled()) {
                logger.debug("Service auditing now enabled for: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward enableWSRM(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("enableWSRM(): " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("enableWSRM(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.enableWSRM(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.enablewsrm", serviceQName));
            if (logger.isDebugEnabled()) {
                logger.debug("enableWSRM(): Service now enabled for WS-ReliableMessaging: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection servicesList = Collections.EMPTY_LIST;
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        try {
            servicesList = serviceManager.getServices();
        } catch (Exception e) {
            logger.error("list(): exception caught while calling getServices(): " + e.getMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(servicesList);
        }
        request.setAttribute(WebKeys.SERVICES, servicesList);
        return mapping.findForward(WebKeys.LIST);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("start: " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("start(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        DynaActionForm dynaForm = (DynaActionForm) form;
        String action = (String) dynaForm.get("action");
        if (action == null) {
            logger.debug("Received null action");
            return list(mapping, form, request, response);
        }
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.startService(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.start", "service."));
            if (logger.isDebugEnabled()) {
                logger.debug("service has been started: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
            logger.error(e.getMessage(), e);
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward stop(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("stop: " + form);
        }
        if (_overrideMessages != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("stop(): exiting with overrideMessages");
            }
            saveMessages(request, _overrideMessages);
            return list(mapping, form, request, response);
        }
        ServiceManager serviceManager = (ServiceManager) getServlet().getServletContext().getAttribute(WebKeys.SERVICE_MANAGER);
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        try {
            String serviceQName = request.getParameter(WebKeys.QNAME);
            serviceManager.stopService(serviceQName);
            success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.stop", "service."));
            if (logger.isDebugEnabled()) {
                logger.debug("Service has been stopped: " + serviceQName);
            }
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e));
            logger.error(e.getMessage(), e);
        }
        if (!success.isEmpty()) {
            saveMessages(request, success);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return list(mapping, form, request, response);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapping DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, form, request, response);
    }
}
