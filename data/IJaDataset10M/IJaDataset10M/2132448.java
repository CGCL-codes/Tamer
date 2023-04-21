package org.nexopenframework.security.model.ri;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.nexopenframework.domain.entity.BaseEntityImpl;
import org.nexopenframework.security.model.Role;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of the {@link Role} interface for dealing with <code>ORM</code> solutions</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @see org.nexopenframework.security.model.Role
 * @see org.nexopenframework.domain.entity.BaseEntityImpl
 * @version $Revision $,$Date: 2008-04-15 22:02:44 +0100 $
 * @since 2.0.0.GA
 */
public class RoleEntity extends BaseEntityImpl implements Serializable, Role {

    private static final long serialVersionUID = 1L;

    /**parent role, accepts a aprent-child relationship*/
    private Role parent;

    /**Roles which are children of this one*/
    private Set<Role> children;

    /**Name of this role*/
    private String name;

    /**Optional description of this {@link Role}*/
    private String description;

    /**
	 * <p>Default constructor</p>
	 */
    public RoleEntity() {
        super();
    }

    /**
	 * <p>It creates a Role with a given name</p>
	 * 
	 * @param name
	 */
    public RoleEntity(final String name) {
        this.name = name;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#getParent()
	 */
    public Role getParent() {
        return parent;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#setParent(org.nexopenframework.security.model.ri.Role)
	 */
    public void setParent(final Role parent) {
        this.parent = parent;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#getName()
	 */
    public String getName() {
        return name;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#setName(java.lang.String)
	 */
    public void setName(final String name) {
        this.name = name;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#getDescription()
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#setDescription(java.lang.String)
	 */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#addRole(org.nexopenframework.security.model.ri.Role)
	 */
    public void addRole(final Role role) {
        if (this.children == null) {
            this.children = new HashSet<Role>();
        }
        role.setParent(this);
        this.children.add(role);
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#getChildren()
	 */
    public Set<Role> getChildren() {
        return children;
    }

    /**
	 * 
	 * @see org.nexopenframework.security.model.ri.Role#setChildren(java.util.Set)
	 */
    public void setChildren(final Set<Role> children) {
        this.children = children;
    }
}
