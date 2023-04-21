package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnGroup;

/**
 * MsnContactListListener adapter.
 * 
 * @author Roger Chen
 */
public class MsnContactListAdapter implements MsnContactListListener {

    public void contactListInitCompleted(MsnMessenger messenger) {
    }

    public void contactListSyncCompleted(MsnMessenger messenger) {
    }

    public void contactStatusChanged(MsnMessenger messenger, MsnContact contact) {
    }

    public void ownerStatusChanged(MsnMessenger messenger) {
    }

    public void contactAddedMe(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactRemovedMe(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactAddCompleted(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactRemoveCompleted(MsnMessenger messenger, MsnContact contact) {
    }

    public void groupAddCompleted(MsnMessenger messenger, MsnGroup group) {
    }

    public void groupRemoveCompleted(MsnMessenger messenger, MsnGroup group) {
    }
}
