package cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.contact;

import java.util.ArrayList;
import java.util.List;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.core.ContactObject;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.BaseInput;

/**
 * @author Jimmy
 * 
 */
public class ContactNewContactInput extends BaseInput {

    /** new contact object. */
    private ContactObject contact;

    /** GroupObject list. */
    private List groups;

    /**
     * 
     */
    public ContactNewContactInput() {
        this.contact = new ContactObject();
        this.groups = new ArrayList();
    }

    /**
     * @return the contact
     */
    public ContactObject getContact() {
        return this.contact;
    }

    /**
     * @param contact
     *            the contact to set
     */
    public void setContact(ContactObject contact) {
        this.contact = contact;
    }

    /**
     * @return the GroupObject list
     */
    public List getGroups() {
        return this.groups;
    }

    /**
     * @param groups
     *            the GroupObject list to set
     */
    public void setGroups(List groups) {
        this.groups = groups;
    }
}
