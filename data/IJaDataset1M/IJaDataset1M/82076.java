package edu.mobbuzz.facade;

import edu.mobbuzz.bean.Contact;
import edu.mobbuzz.storage.ContactRecordStore;
import java.util.Vector;

public class ContactFacade {

    private ContactRecordStore contactRS;

    private Vector contactArray;

    public ContactRecordStore getContactRS() {
        if (contactRS == null) {
            contactRS = new ContactRecordStore();
        }
        return contactRS;
    }

    public void setContactRS(ContactRecordStore contactRS) {
        this.contactRS = contactRS;
    }

    public Vector displayContactList() {
        contactArray = new Vector();
        if (getContactRS().readRSContact()) {
            for (int i = 0; i < getContactRS().getNbContact(); i++) {
                contactArray.addElement(new Contact(getContactRS().contactArr[i].getRecId(), getContactRS().contactArr[i].getName(), getContactRS().contactArr[i].getId(), getContactRS().contactArr[i].getSex()));
            }
        } else {
            System.out.println("RS Contact Gagal dibuka");
        }
        return contactArray;
    }

    public void addContact(Contact contact) {
        getContactRS().addContact(contact);
    }

    public void updateContact(int index, Contact contact) {
        getContactRS().updateContact(index, contact);
    }

    public void deleteContact(int index) {
        getContactRS().deleteContact(index);
    }

    public void closeContactRS() {
        getContactRS().closeRecStore();
    }
}
