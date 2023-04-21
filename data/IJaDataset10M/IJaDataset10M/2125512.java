package org.apache.jetspeed.services.forward.configuration.impl;

import org.apache.jetspeed.services.forward.configuration.Page;

/**
 * Page Implementation for Forward
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: PageImpl.java,v 1.2 2004/02/23 03:50:10 jford Exp $
 */
public class PageImpl implements Page, java.io.Serializable {

    private String name;

    private String user;

    private String role;

    private String group;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
