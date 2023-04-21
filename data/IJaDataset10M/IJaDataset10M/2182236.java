package com.rarebrick.tools.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.rarebrick.tools.Constants;
import com.rarebrick.tools.model.Client;
import com.rarebrick.tools.service.ClientManager;
import com.rarebrick.tools.webapp.form.ClientForm;

/**
 * Action class to handle CRUD on a Client object
 * 
 * @struts.action name="clientForm" path="/clients" scope="request"
 *                validate="false" parameter="method" input="mainMenu"
 *                roles="admin"
 * @struts.action name="clientForm" path="/editClient" scope="request"
 *                validate="false" parameter="method" input="list"
 *                roles="admin"
 * @struts.action name="clientForm" path="/saveClient" scope="request"
 *                validate="false" parameter="method" input="edit"
 *                roles="admin"
 * @struts.action-set-property property="cancellable" value="true"
 * @struts.action-forward name="edit"
 *                        path="/WEB-INF/pages/translator/clientForm.jsp"
 * @struts.action-forward name="list"
 *                        path="/WEB-INF/pages/translator/clientList.jsp"
 * @struts.action-forward name="search" path="/clients.html" redirect="true"
 */
public final class ClientAction extends BaseAction {

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("search");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'delete' method");
        }
        ActionMessages messages = new ActionMessages();
        ClientForm clientForm = (ClientForm) form;
        ClientManager mgr = (ClientManager) getBean("clientManager");
        Client client = (Client) convert(clientForm);
        try {
            mgr.removeClient(client.getId());
        } catch (Exception e) {
            ActionMessages errors = getErrors(request);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.delete.client", client.getName()));
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("client.deleted"));
        saveMessages(request.getSession(), messages);
        return mapping.findForward("search");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'edit' method");
        }
        ClientForm clientForm = (ClientForm) form;
        if (clientForm.getId() != null && clientForm.getId().length() != 0) {
            ClientManager mgr = (ClientManager) getBean("clientManager");
            Client client = (Client) mgr.getClient(Long.valueOf(clientForm.getId()));
            clientForm = (ClientForm) convert(client);
            clientForm.setConfirmPassword(clientForm.getPassword());
            updateFormBean(mapping, request, clientForm);
        }
        return mapping.findForward("edit");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'save' method");
        }
        ActionMessages errors = form.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }
        ActionMessages messages = new ActionMessages();
        ClientForm clientForm = (ClientForm) form;
        boolean isNew = ("".equals(clientForm.getId()) || clientForm.getId() == null);
        ClientManager mgr = (ClientManager) getBean("clientManager");
        Client client = (Client) convert(clientForm);
        boolean newPassword = StringUtils.equals(request.getParameter("encryptPass"), "true");
        try {
            client = mgr.saveClient(client, newPassword);
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.existing.client", client.getName()));
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }
        if (isNew) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("client.added", new String[] { client.getName(), client.getPassword() }));
            saveMessages(request.getSession(), messages);
            return mapping.findForward("search");
        } else {
            BeanUtils.copyProperties(clientForm, convert(client));
            clientForm.setConfirmPassword(clientForm.getPassword());
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("client.updated"));
            saveMessages(request, messages);
            return mapping.findForward("edit");
        }
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'search' method");
        }
        ClientManager mgr = (ClientManager) getBean("clientManager");
        request.setAttribute(Constants.CLIENT_LIST, mgr.getClients());
        return mapping.findForward("list");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return search(mapping, form, request, response);
    }
}
