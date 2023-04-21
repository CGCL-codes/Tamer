package org.nightlabs.jfire.security;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nick
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class RoleSet implements Group, Serializable {

    /**
	 * The serial version of this class.
	 */
    private static final long serialVersionUID = 1L;

    private Set<Principal> members = new HashSet<Principal>();

    private static final String NAME = "Roles";

    /**
	 * Create a new empty RoleSet.
	 */
    public RoleSet() {
    }

    public boolean addMember(Principal user) {
        return members.add(user);
    }

    public boolean isMember(Principal member) {
        return members.contains(member);
    }

    public boolean removeMember(Principal user) {
        return members.remove(user);
    }

    public String getName() {
        return NAME;
    }

    public Enumeration<? extends Principal> members() {
        return Collections.enumeration(members);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Principal principal : members) {
            if (sb.length() > 0) sb.append(',');
            sb.append(principal);
        }
        return this.getClass().getName() + '[' + sb.toString() + ']';
    }
}
