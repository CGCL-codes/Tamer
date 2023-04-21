package com.myopa.logic.contacts;

import com.myopa.vo.ContactVo.ContactType;

/**
 * This is a factory class for the ContactManager interface/implementations.
 *
 * @author Clint Burns <c1burns@users.sourceforge.net>
 */
public class ContactManagerFactory {

    private static ContactManager individualCM = new ContactManagerImpl_Individual();

    private static ContactManager organizationCM = new ContactManagerImpl_Organization();

    public static ContactManager getInstance(ContactType type) {
        switch(type) {
            case INDIVIDUAL:
                return individualCM;
            case ORGANIZATION:
                return organizationCM;
            default:
                return individualCM;
        }
    }
}
