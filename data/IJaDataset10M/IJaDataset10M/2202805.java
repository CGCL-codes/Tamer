package com.oranda.webofcontacts.struts1.actions;

import com.oranda.webofcontacts.struts1.domain.Contact;
import com.oranda.webofcontacts.struts1.forms.ContactForm;
import com.oranda.webofcontacts.struts1.spring.service.ContactService;
import org.apache.struts.action.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  Administrator
 * @version 
 */
public class ContactDecPriorityAction extends org.apache.struts.action.Action {

    private Log log = LogFactory.getLog("com.oranda.webofcontacts.struts1.actions");

    private ContactService contactService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        log.debug("ContactDeleteAction: entering execute()");
        Integer id = Integer.valueOf(request.getParameter("id"));
        this.contactService.decrementPriority(id);
        return mapping.findForward("success");
    }

    public ContactService getContactService() {
        return this.contactService;
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
}
