package net.jforum.security;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author Rafael Steil
 * @version $Id: RoleCollection.java,v 1.11 2006/08/24 01:07:02 rafaelsteil Exp $
 */
public class RoleCollection extends LinkedHashMap implements Serializable {

    public void add(Role role) {
        super.put(role.getName(), role);
    }

    /**
	 * Gets a role.
	 * 
	 * @param name The role's name
	 * @return <code>Role</code> object if a role with a name equals to the name passed
	 * as argument is found, or <code>null</code> otherwise.
	 */
    public Role get(String name) {
        return (Role) super.get(name);
    }

    /** 
	 * @see java.util.AbstractCollection#toString()
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer(512);
        for (Iterator iter = this.values().iterator(); iter.hasNext(); ) {
            sb.append(iter.next()).append('\n');
        }
        return sb.toString();
    }
}
