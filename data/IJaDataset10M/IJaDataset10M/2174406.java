package eln.server.event;

import java.util.Vector;

public class AdminActionSource {

    private Vector adminActionListeners = new Vector();

    public AdminActionSource() {
    }

    public synchronized void addAdminActionListener(AdminActionListener aal) {
        if (!adminActionListeners.contains(aal)) {
            adminActionListeners.addElement(aal);
        }
    }

    public synchronized void removeAdminActionListener(AdminActionListener aal) {
        if (!adminActionListeners.contains(aal)) {
            adminActionListeners.removeElement(aal);
        }
    }

    public void fireAdminAction(int actionType) {
        AdminActionEvent evt = new AdminActionEvent(this, actionType);
        Vector v;
        synchronized (this) {
            v = (Vector) adminActionListeners.clone();
        }
        int count = v.size();
        for (int i = 0; i < count; i++) {
            AdminActionListener client = (AdminActionListener) v.elementAt(i);
            client.performAdminAction(evt);
        }
    }
}
