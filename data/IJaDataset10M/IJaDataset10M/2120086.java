package org.allesta.wsabi.axis.configure.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import org.allesta.wsabi.axis.configure.DeploymentManager;
import org.allesta.wsabi.axis.configure.action.util.ActionUtils;
import org.allesta.wsabi.util.WebKeys;
import org.apache.axis.deployment.wsdd.WSDDHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * DOCUMENT ME!
 *
 * @author Allesta, LLC
 * @version $Revision: 1.1 $ 
 */
public class CRUDHandlers extends AbstractCRUDAction {

    private static final Log logger = LogFactory.getLog(CRUDHandlers.class);

    /**
     * Creates a new CRUDHandlers object.
     */
    public CRUDHandlers() {
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
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("add: " + form);
        }
        DynaActionForm dynaForm = (DynaActionForm) form;
        String action = (String) dynaForm.get("action");
        if ((action == null) || !(WebKeys.SUBMIT.equals(action))) {
            logger.debug("Received null or cancel action");
            return list(mapping, form, request, response);
        }
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        DeploymentManager dm = DeploymentManager.getInstance();
        String[] checkbox = (String[]) dynaForm.get("checkbox");
        String[] name = (String[]) dynaForm.get("name");
        String[] type = (String[]) dynaForm.get("type");
        if ((checkbox != null) && (checkbox.length > 0)) {
            for (int i = 0; i < checkbox.length; i++) {
                if ((name[i] == null) || (name[i].length() == 0)) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.name"));
                }
                if ((type[i] == null) || (type[i].length() == 0)) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.type"));
                }
                try {
                    WSDDHandler handler = dm.createHandler(name[i], type[i]);
                    if (errors.isEmpty() && (handler != null)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Added " + handler.getQName());
                        }
                        dm.deployHandler(handler);
                        success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.add", handler.getQName()));
                    }
                } catch (Exception e) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e.getMessage()));
                    logger.error(e.getMessage(), e);
                }
            }
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
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("delete: " + form);
        }
        DynaActionForm dynaForm = (DynaActionForm) form;
        String action = (String) dynaForm.get("action");
        if ((action == null) || !(WebKeys.SUBMIT.equals(action))) {
            logger.debug("Received null or cancel action");
            return list(mapping, form, request, response);
        }
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        DeploymentManager dm = DeploymentManager.getInstance();
        String[] checkbox = (String[]) dynaForm.get("checkbox");
        for (int i = 0; i < checkbox.length; i++) {
            try {
                WSDDHandler h = dm.getHandler(QName.valueOf(checkbox[i]));
                dm.undeployHandler(h.getQName());
                if (logger.isDebugEnabled()) {
                    logger.debug("Deleted " + h.getQName());
                }
                success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.delete", h.getQName()));
            } catch (Exception e) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e.getMessage()));
                logger.error(e.getMessage(), e);
            }
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
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("edit: " + form);
        }
        DynaActionForm dynaForm = (DynaActionForm) form;
        String action = (String) dynaForm.get("action");
        if ((action == null) || !(WebKeys.SUBMIT.equals(action))) {
            logger.debug("Received null or cancel action");
            return list(mapping, form, request, response);
        }
        ActionMessages errors = new ActionMessages();
        ActionMessages success = new ActionMessages();
        DeploymentManager dm = DeploymentManager.getInstance();
        String[] checkbox = (String[]) dynaForm.get("checkbox");
        String[] name = (String[]) dynaForm.get("name");
        String[] type = (String[]) dynaForm.get("type");
        for (int i = 0; i < checkbox.length; i++) {
            try {
                QName qname = QName.valueOf(checkbox[i]);
                WSDDHandler handler = dm.getHandler(qname);
                qname = handler.getQName();
                handler.setQName(new QName(qname.getNamespaceURI(), name[i]));
                qname = handler.getType();
                handler.setType(new QName(qname.getNamespaceURI(), type[i]));
                dm.updateHandler(QName.valueOf(checkbox[i]), handler);
                if (logger.isDebugEnabled()) {
                    logger.debug("Updated " + handler.getQName());
                }
                success.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("success.edit", QName.valueOf(checkbox[i])));
            } catch (Exception e) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.exception", e.getMessage()));
                logger.error(e.getMessage(), e);
            }
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
     * @param request DOCUMENT ME!
     * @param form DOCUMENT ME!
     */
    protected void storeItemsList(HttpServletRequest request, ActionForm form) {
        DynaActionForm dynaForm = (DynaActionForm) form;
        String action = (String) dynaForm.get(WebKeys.ACTION);
        ActionUtils.storeHandlersList(request, WebKeys.ADD.equals(action));
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param form DOCUMENT ME!
     */
    protected void storeItemsMap(HttpServletRequest request, ActionForm form) {
    }
}
