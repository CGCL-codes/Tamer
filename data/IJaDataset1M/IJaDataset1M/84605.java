package com.intel.gpe.client2.admin.panels.tables;

import com.intel.gpe.clients.api.User;
import com.intel.gui.controls2.configurable.ITableEntry;

/**
 * The user entry.
 * @author Alexander Lukichev
 * @version $Id: UserEntry.java,v 1.4 2006/10/19 13:40:49 dizhigul Exp $
 *
 */
public class UserEntry implements ITableEntry, AdminTableEntry {

    private User user;

    public UserEntry(User user) {
        this.user = user;
    }

    public void set(int i, Object o) {
    }

    public Comparable get(int i) {
        switch(i) {
            case 0:
                return user.getCommonName();
            case 1:
                return user.getSubject();
            default:
                return null;
        }
    }

    /**
     * Get user
     * @return the wrapped user object
     */
    public User getUser() {
        return user;
    }

    public String getIconPath() {
        return "images/user.gif";
    }
}
