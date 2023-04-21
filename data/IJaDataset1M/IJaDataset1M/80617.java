package net.java.sip.communicator;

import net.java.sip.communicator.gui.imp.*;
import net.java.sip.communicator.sip.simple.*;
import net.java.sip.communicator.common.*;
import net.java.sip.communicator.sip.simple.event.*;
import net.java.sip.communicator.sip.*;

/**
 * <p>Title: SIP Communicator</p>
 * <p>Description: A SIP UA</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Network Research Team, Louis Pasteur University</p>
 * @author Emil Ivov
 * @version 1.0
 */
public class SimpleContactList extends ContactListModel implements ContactListChangeListener {

    private static final Console console = Console.getConsole(SimpleContactList.class);

    private ContactGroup root = null;

    private ContactListController contactListController = null;

    /**
     * Returns <code>true</code> if <code>node</code> is a Contact rather than a
     * Contact group.
     *
     * @param node a node in the tree, obtained from this data source
     * @return true if <code>node</code> is a Contact and not a group
     * @todo Implement this net.java.sip.communicator.gui.imp.ContactListModel
     *   method
     */
    public boolean isGroup(Object node) {
        return node instanceof ContactGroup;
    }

    /**
     * Messaged when the user has altered the value for the item identified by
     * <code>path</code> to <code>newValue</code>.
     *
     * @param path path to the node that the user has altered
     * @param newValue the new value from the TreeCellEditor
     * @todo Implement this net.java.sip.communicator.gui.imp.ContactListModel
     *   method
     */
    public void valueForPathChanged(Object[] path, Object newValue) {
        if (path != null && path.length > 0 && path[path.length] != null && newValue != null && newValue instanceof String && newValue.toString().trim().length() > 0) ((Contact) path[path.length]).setDisplayName(newValue.toString());
    }

    /**
     * Returns the root of the tree.
     *
     * @return the root of the tree
     * @todo Implement this javax.swing.tree.TreeModel method
     */
    public Object getRoot() {
        return root;
    }

    /**
     * Returns the number of children of <code>parent</code>.
     *
     * @param parent a node in the tree, obtained from this data source
     * @return the number of children of the node <code>parent</code>
     * @todo Implement this javax.swing.tree.TreeModel method
     */
    public int getChildCount(Object parent) {
        if (parent == null) return 0;
        if (parent instanceof ContactGroup) return ((ContactGroup) parent).getChildCount(); else return 0;
    }

    /**
     * Returns the child of <code>parent</code> at index <code>index</code> in
     * the parent's child array.
     *
     * @param parent a node in the tree, obtained from this data source
     * @param index int
     * @return the child of <code>parent</code> at index <code>index</code>
     */
    public Object getChild(Object parent, int index) {
        if (getChildCount(parent) <= index) return null;
        return ((ContactGroup) parent).getChild(index);
    }

    /**
     * Returns the index of child in parent.
     *
     * @param parent a note in the tree, obtained from this data source
     * @param child the node we are interested in
     * @return the index of the child in the parent, or -1 if either
     *   <code>child</code> or <code>parent</code> are <code>null</code>
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (getChildCount(parent) == 0) return -1;
        ContactGroup group = (ContactGroup) parent;
        for (int i = 0; i < group.getChildCount(); i++) {
            Contact contact = group.getChild(i);
            if (contact != null && contact.equals(child)) return i;
        }
        return -1;
    }

    /**
     * Sets the root group of the contact list. The root group is the group
     * containing all other contact groups
     * @param rootGroup ContactGroup
     */
    public void setRoot(ContactGroup rootGroup) {
        this.root = rootGroup;
    }

    /**
     * Determines whether the specified name has a status corresponding to online
     * (rather than closed or offline status) for visualisation purposes.
     *
     * @param node the node whose status is to be determined
     * @return true if node is a contact (and not a ContactGroup) and if its
     *   status is Open or Online
     */
    public boolean isOnline(Object node) {
        if (node instanceof Contact) return ((Contact) node).isOnline(); else return false;
    }

    /**
     * Sets the ContactListController instance where we will register ourselves
     * as a listener and get notification messages as well as add and remove
     * contacts we are subscribed to.
     * @param contactListController a valid ContactListController instance.
     */
    public void setContactListController(ContactListController contactListController) {
        this.contactListController = contactListController;
        contactListController.addNotificationListener(this);
    }

    /**
     * Called by the Watcher to indicate the reception of a NOTIFY request.
     *
     * @param evt the event object containing the newly received presence data.
     */
    public void notificationReceived(NotificationReceivedEvent evt) {
        Contact source = evt.getSourceContact();
        ContactGroup[] path = evt.getSourceLocation();
        int childIndex = path[path.length - 1].getChildIndex(source.getPresenceUri());
        fireTreeNodesChanged(source, path, new int[] { childIndex }, new Object[] { source });
    }

    /**
     * Notifies the communications part of of the application that the user
     * wishes to add the contact described by <code>request</code> to their
     * contact list.
     *
     * @param request request that contains details about the contact the the
     *   user wishes to add to their contact list.
     */
    public void requestContactAddition(ContactAdditionRequest request) {
        try {
            ContactGroup path[] = new ContactGroup[request.getLocation().length];
            System.arraycopy(request.getLocation(), 0, path, 0, path.length);
            String presentity = request.getContactIdentifier();
            if (presentity.indexOf(':') == -1) presentity = "pres:" + presentity;
            contactListController.requestContactAddition(path, presentity, request.getAlias(), request.getNotes());
        } catch (CommunicationsException ex) {
            console.showException("Failed to add contact for user " + request.getAlias() + ", " + request.getContactIdentifier(), ex);
        }
    }

    /**
     * Called when a new contact has been added to the contact list.
     * @param evt the event object describing the contact addition.
     */
    public void contactAdded(ContactAddedEvent evt) {
        ContactGroup parent = evt.getSourceLocation()[evt.getSourceLocation().length - 1];
        ContactGroup[] pathToParent = new ContactGroup[evt.getSourceLocation().length - 1];
        System.arraycopy(evt.getSourceLocation(), 0, pathToParent, 0, pathToParent.length);
        super.fireTreeNodesInserted(parent, pathToParent, new int[] { evt.getIndex() }, new Contact[] { evt.getSourceContact() });
    }

    /**
     * Notifies the communications part of of the application that the user
     * wishes to remove the contact described by <code>request</code> from their
     * contact list.
     *
     * @param request request that contains details about the contact the the
     *   user wishes to add to their contact list.
     */
    public void requestContactRemoval(ContactRemovalRequest request) {
        try {
            ContactGroup path[] = new ContactGroup[request.getLocation().length];
            System.arraycopy(request.getLocation(), 0, path, 0, path.length);
            contactListController.requestContactRemoval(path, (Contact) request.getContact());
        } catch (CommunicationsException ex) {
            console.showException("Failed to remove contact for user " + request.getContact().toString(), ex);
        }
    }

    /**
     * Called when a contact has been removed from the contact list.
     * @param evt the event object describing the contact addition.
     */
    public void contactRemoved(ContactRemovedEvent evt) {
        ContactGroup parent = evt.getSourceLocation()[evt.getSourceLocation().length - 1];
        ContactGroup[] pathToParent = new ContactGroup[evt.getSourceLocation().length - 1];
        System.arraycopy(evt.getSourceLocation(), 0, pathToParent, 0, pathToParent.length);
        super.fireTreeNodesRemoved(parent, pathToParent, new int[] { evt.getIndex() }, new Contact[] { evt.getSourceContact() });
    }
}
