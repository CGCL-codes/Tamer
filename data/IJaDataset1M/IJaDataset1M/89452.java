package com.oranda.webofcontacts.struts1.actions;

import com.oranda.webofcontacts.struts1.domain.Contact;
import com.oranda.webofcontacts.struts1.spring.service.ContactService;
import java.util.*;
import org.apache.struts.action.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  Administrator
 * @version 
 */
public class ContactListAction extends org.apache.struts.action.Action {

    private Log log = LogFactory.getLog("com.oranda.webofcontacts.struts1.actions");

    private ContactService contactService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        log.debug("ContactListAction: entering execute()");
        List<Contact> contacts = this.contactService.findContacts();
        request.setAttribute("contacts", contacts);
        log.debug("ContactListAction: ready to forward to view.");
        return mapping.findForward("contactList");
    }

    public ContactService getContactService() {
        return this.contactService;
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Test
     */
    public static void main(String[] args) {
    }
}
