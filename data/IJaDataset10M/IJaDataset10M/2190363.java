package nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a {@link Role} that can be assigned to a
 * {@link User}.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 24 nov. 2011
 * 
 */
@Entity(name = "ADM_ROLES")
public class HibernateRole extends AbstractUpdatableDomainObject<Long> implements Role {

    private static final long serialVersionUID = -8114881615271153119L;

    /** The name of the role. */
    @BusinessField
    private String name;

    /** Short description of what this role is all about. */
    @BusinessField
    private String description;

    /** Determines if this role is still valid or not. */
    @BusinessField
    private Boolean valid;

    /** Determines if this role is editable or not. */
    @BusinessField
    private Boolean frozen;

    /** The {@link Scope} where this role belongs to. */
    @BusinessField
    private Scope scope;

    /** The privileges that are part of this role. */
    private Set<Privilege> privileges;

    /** The roles a user has. */
    private List<UserRole> userRoles;

    /** The users that are allowed to grant this role to someone else. */
    private Set<User> grantingUsers;

    /**
	 * @return the id
	 */
    @Override
    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "NAME", unique = false, nullable = false)
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    @Column(name = "DESCRIPTION", unique = false, nullable = true)
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "VALID", unique = false, nullable = false)
    public Boolean isValid() {
        return valid;
    }

    @Transient
    public Boolean getValid() {
        return valid;
    }

    @Override
    public void setValid(final Boolean valid) {
        this.valid = valid;
    }

    @Override
    @ManyToOne(targetEntity = HibernateScope.class)
    @JoinColumn(name = "SCP_ID", nullable = false)
    @ForeignKey(name = "FK_SCOPE_ROLE")
    public Scope getScope() {
        return scope;
    }

    @Override
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
	 * @return the privileges
	 */
    @Override
    @ManyToMany(targetEntity = HibernatePrivilege.class, fetch = FetchType.LAZY)
    @JoinTable(name = "ADM_ROLE_PRIVILEGES", joinColumns = @JoinColumn(name = "ROL_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "PRV_ID", referencedColumnName = "ID"))
    @ForeignKey(name = "FK_ROLE_PRIVILEGE")
    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    /**
	 * @param privileges
	 *            the privileges to set
	 */
    @Override
    public void setPrivileges(final Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
	 * @return the users
	 */
    @Override
    @Transient
    public List<User> getUsers() {
        final List<User> users = new ArrayList<User>();
        for (final UserRole userRole : getUserRoles()) {
            users.add(userRole.getUser());
        }
        return users;
    }

    /**
	 * @return the user roles
	 */
    @Override
    @OneToMany(mappedBy = "role", targetEntity = HibernateUserRole.class, fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
	 * @param userRoles
	 *            the user roles to set
	 */
    @Override
    public void setUserRoles(final List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
	 * @return the grantingUsers
	 */
    @Override
    @Transient
    public Set<User> getGrantingUsers() {
        return grantingUsers;
    }

    /**
	 * @param grantingUsers
	 *            the grantingUsers to set
	 */
    @Override
    public void setGrantingUsers(final Set<User> grantingUsers) {
        this.grantingUsers = grantingUsers;
    }

    /**
	 * @return the frozen
	 */
    @Transient
    public Boolean getFrozen() {
        return frozen;
    }

    /**
	 * @param frozen
	 *            the frozen to set
	 */
    @Override
    public void setFrozen(final Boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    @Type(type = "yes_no")
    @Column(name = "FROZEN", unique = false, nullable = false)
    public Boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        if (object instanceof Role) {
            final Role role = (Role) object;
            this.name = role.getName();
            this.description = role.getDescription();
            this.frozen = role.isFrozen();
            this.valid = role.isValid();
            final Scope originalScope = role.getScope();
            if (originalScope != null) {
                this.scope = new HibernateScope();
                this.scope.copy(role.getScope());
            }
            final Set<Privilege> originalPrivileges = role.getPrivileges();
            this.privileges = new HashSet<Privilege>();
            if (originalPrivileges != null) {
                for (final Privilege privilege : originalPrivileges) {
                    final HibernatePrivilege hibernatePrivilege = new HibernatePrivilege();
                    hibernatePrivilege.copy(privilege);
                    privileges.add(hibernatePrivilege);
                }
            }
            final List<UserRole> originalUserRoles = role.getUserRoles();
            this.userRoles = new ArrayList<UserRole>();
            if (originalUserRoles != null) {
                for (final UserRole userRole : originalUserRoles) {
                    final HibernateUserRole hibernateUserRole = new HibernateUserRole();
                    hibernateUserRole.copy(userRole);
                    userRoles.add(hibernateUserRole);
                }
            }
        }
        super.copy(object);
    }
}
