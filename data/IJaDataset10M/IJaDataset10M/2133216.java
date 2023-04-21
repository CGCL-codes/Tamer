package org.opennms.web.admin.roles;

import java.util.Collection;

public class WebGroup {

    private String m_name;

    private Collection m_users;

    public WebGroup(String name) {
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public String toString() {
        return "" + getName();
    }

    public Collection getUsers() {
        return m_users;
    }

    protected void setUsers(Collection users) {
        m_users = users;
    }

    public boolean equals(Object obj) {
        if (obj instanceof WebGroup) {
            WebGroup u = (WebGroup) obj;
            return m_name.equals(u.m_name);
        }
        return false;
    }

    public int hashCode() {
        return m_name.hashCode();
    }
}
