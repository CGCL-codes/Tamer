package org.nexopenframework.modules.users.support;

import java.io.Serializable;
import java.util.List;
import org.nexopenframework.security.model.Group;
import org.nexopenframework.security.model.Role;

/**
 * <p>NexOpen Framework Modules</p>
 * 
 * <p>Holder of roles and groups information. It will be have only a few roles and groups so
 * serialization/deserialization it will not be problematic.</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version $Revision  ,$Date 13/06/2009 21:23:58
 * @since 1.3.0
 */
public class SecurityModelHolder implements Serializable {

    /**serialization stuff*/
    private static final long serialVersionUID = 1L;

    /***/
    private final List<Role> roles;

    /***/
    private final List<Group> groups;

    /**
	 * <p></p>
	 * 
	 * @param roles
	 * @param groups
	 */
    public SecurityModelHolder(final List<Role> roles, final List<Group> groups) {
        super();
        this.roles = roles;
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
