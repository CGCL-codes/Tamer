package net.sf.jml.example;

import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnList;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnContactListAdapter;

/**
 * @auther Daniel Zhou (danithaca@gmail.com)
 * @organization School of Information, University of Michigan
 * Date: Dec 5, 2007
 */
public class ListenMessenger extends BasicMessenger {

    protected void initMessenger(MsnMessenger messenger) {
        messenger.addContactListListener(new MsnContactListAdapter() {

            public void contactListInitCompleted(MsnMessenger messenger) {
                MsnContact[] contacts = messenger.getContactList().getContactsInList(MsnList.AL);
                for (MsnContact contact : contacts) {
                    if (contact.getStatus() != MsnUserStatus.OFFLINE) {
                        System.out.println(contact.getDisplayName() + " : " + contact.getStatus());
                    }
                }
            }

            public void contactStatusChanged(MsnMessenger messenger, MsnContact contact) {
                System.out.println(contact.getDisplayName() + " : " + contact.getOldStatus() + "->" + contact.getStatus());
                System.out.println(contact.getPersonalMessage());
            }
        });
    }
}
