package com.ctb.diagram.views;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import com.ctb.Application;
import com.ctb.diagram.testmodel.Contact;
import com.ctb.diagram.testmodel.ContactsEntry;
import com.ctb.diagram.testmodel.ContactsGroup;
import com.ctb.diagram.testmodel.Presence;

public class HyperbolaAdapterFactory implements IAdapterFactory {

    private IWorkbenchAdapter groupAdapter = new IWorkbenchAdapter() {

        public Object getParent(Object o) {
            return ((ContactsGroup) o).getParent();
        }

        public String getLabel(Object o) {
            ContactsGroup group = ((ContactsGroup) o);
            int available = 0;
            Contact[] entries = group.getEntries();
            for (int i = 0; i < entries.length; i++) {
                Contact contact = entries[i];
                if (contact instanceof ContactsEntry) {
                    if (((ContactsEntry) contact).getPresence() != Presence.INVISIBLE) available++;
                }
            }
            return group.getName() + " (" + available + "/" + entries.length + ")";
        }

        public Object[] getChildren(Object o) {
            return ((ContactsGroup) o).getEntries();
        }

        @Override
        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }
    };

    private IWorkbenchAdapter entryAdapter = new IWorkbenchAdapter() {

        public Object getParent(Object o) {
            return ((ContactsEntry) o).getParent();
        }

        public String getLabel(Object o) {
            ContactsEntry entry = ((ContactsEntry) o);
            return entry.getNickname() + " (" + entry.getName() + "@" + entry.getServer() + ")";
        }

        @Override
        public Object[] getChildren(Object o) {
            return null;
        }

        @Override
        public ImageDescriptor getImageDescriptor(Object object) {
            return null;
        }
    };

    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof ContactsGroup) return groupAdapter;
        if (adapterType == IWorkbenchAdapter.class && adaptableObject instanceof ContactsEntry) return entryAdapter;
        return null;
    }

    public Class[] getAdapterList() {
        return new Class[] { IWorkbenchAdapter.class };
    }
}
