package com.kwoksys.action.contacts;

import com.kwoksys.action.base.BaseTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.contacts.ContactSearch;
import com.kwoksys.biz.contacts.ContactService;
import com.kwoksys.biz.contacts.dto.Contact;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.exception.DatabaseException;
import com.kwoksys.framework.system.ObjectTypes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContactAssociateTemplate
 */
public class ContactAssociateTemplate extends BaseTemplate {

    private String contactId;

    private String formSearchAction;

    private String formSaveAction;

    private String formCancelAction;

    public ContactAssociateTemplate() {
        super(ContactAssociateTemplate.class);
    }

    public void applyTemplate() throws DatabaseException {
        ContactService contactService = ServiceProvider.getContactService();
        List contactList = new ArrayList();
        if (!contactId.isEmpty()) {
            ContactSearch contactSearch = new ContactSearch();
            contactSearch.put(ContactSearch.CONTACT_ID_EQUALS, contactId);
            contactSearch.put(ContactSearch.CONTACT_TYPE, ObjectTypes.COMPANY_EMPLOYEE_CONTACT);
            QueryBits query = new QueryBits(contactSearch);
            List<Contact> contacts = contactService.getContacts(query);
            if (!contacts.isEmpty()) {
                Contact contact = contacts.iterator().next();
                Map map = new HashMap();
                map.put("contactId", contact.getId());
                StringBuilder contactDisplay = new StringBuilder();
                contactDisplay.append(contact.getLastName()).append(", ").append(contact.getFirstName());
                if (!contact.getEmailPrimary().isEmpty()) {
                    contactDisplay.append(" (").append(contact.getEmailPrimary()).append(")");
                }
                map.put("contactName", contactDisplay.toString());
                contactList.add(map);
                request.setAttribute("contactList", contactList);
            }
        }
        if (contactId.isEmpty()) {
            request.setAttribute("selectContactMessage", "form.noSearchInput");
        } else if (contactList.isEmpty()) {
            request.setAttribute("selectContactMessage", "form.noSearchResult");
        }
        request.setAttribute("disableSaveButton", contactList.isEmpty());
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setFormSearchAction(String formSearchAction) {
        this.formSearchAction = formSearchAction;
    }

    public void setFormCancelAction(String formCancelAction) {
        this.formCancelAction = formCancelAction;
    }

    public void setFormSaveAction(String formSaveAction) {
        this.formSaveAction = formSaveAction;
    }

    public String getFormSearchAction() {
        return formSearchAction;
    }

    public String getFormSaveAction() {
        return formSaveAction;
    }

    public String getFormCancelAction() {
        return formCancelAction;
    }
}
