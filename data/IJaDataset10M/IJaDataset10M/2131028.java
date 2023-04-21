package com.desktopdeveloper.pendulum.samples.contactmanager;

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * Copyright 23-Sep-2004 Meridian Computer Services.
 * Created by: stuart_e
 * Date: 23-Sep-2004
 * Time: 10:47:38
 */
public class ContactDAO implements DAO {

    Vector contacts = new Vector();

    public ContactDAO() {
        Contact kent = new Contact();
        Contact stuart = new Contact();
        kent.setFirstName("Kent");
        kent.setSecondName("Perry");
        stuart.setFirstName("Stuart");
        stuart.setSecondName("Ervine");
        contacts.add(kent);
        contacts.add(stuart);
    }

    public Object load(Object key) {
        Iterator contactsIter = contacts.iterator();
        while (contactsIter.hasNext()) {
            Contact contact = (Contact) contactsIter.next();
            if (contact.getFirstName().equals(key)) {
                return contact;
            }
        }
        return null;
    }
}
