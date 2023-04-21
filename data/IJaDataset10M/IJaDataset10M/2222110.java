package org.suafe.core;

import java.util.List;

/**
 * The Interface AuthzGroupMember.
 */
public interface AuthzGroupMember extends AuthzPermissionable {

    /**
	 * Returns an immutable list of AuthzGroupIF objects.
	 * 
	 * @return Immutable list of AuthzGroupIF object.
	 */
    List<AuthzGroup> getGroups();
}
