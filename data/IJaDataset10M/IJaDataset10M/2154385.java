package kiff.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import kiff.dao.GlobalAclDAO;
import kiff.security.Acl;
import kiff.security.AclEntry;
import kiff.security.Permission;
import kiff.security.Principal;

/**
 * Global Access Control List.
 * @author Adam
 * @version $Id: GlobalAcl.java 71 2008-11-26 21:03:06Z a.ruggles $
 * 
 * Created on Oct 21, 2008 at 9:52:24 PM 
 */
@Entity
@Table(name = "acls")
@NamedQueries({ @NamedQuery(name = GlobalAclDAO.FIND_BY_NAME, query = "select acl from GlobalAcl acl where acl.name = :name") })
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalAcl implements Acl, CoreEntity<Integer> {

    /**
	 * Default Global Acl Name.
	 */
    public static final String DEFAULT_NAME = "Default";

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = 7813526387338102834L;

    /**
	 * The set of access control entries.
	 */
    private Set<AclEntry> entries = new HashSet<AclEntry>();

    /**
	 * The unique identifier.
	 */
    private Integer id;

    /**
	 * The name of the Forum Access Control list.
	 */
    private String name;

    /**
	 * Default Constructor.
	 */
    public GlobalAcl() {
        this.name = java.util.UUID.randomUUID().toString();
    }

    /**
	 * Constructs a Global Acl with a name.
	 * @param name The name of the Global Acl.
	 */
    public GlobalAcl(final String name) {
        this.name = name;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#addEntry(kiff.security.AclEntry)
	 */
    public void addEntry(final AclEntry entry) {
        this.entries.add(entry);
        entry.setAcl(this);
    }

    /**
	 * {@inheritDoc}
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GlobalAcl)) {
            return false;
        }
        GlobalAcl rhs = (GlobalAcl) obj;
        return new EqualsBuilder().append(name, rhs.getName()).isEquals();
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#getAclEntries()
	 */
    @Transient
    public Enumeration<AclEntry> getAclEntries() {
        return Collections.enumeration(entries);
    }

    /**
	 * Returns entries.
	 * @return the entries.
	 */
    @OneToMany(targetEntity = GlobalAclEntry.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "acl")
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    public Set<AclEntry> getEntries() {
        return entries;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.entity.CoreEntity#getId()
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#getName()
	 */
    @Column(length = 50, unique = true)
    public String getName() {
        return name;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#getOwner()
	 */
    @Transient
    public Principal getOwner() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#getPermission(kiff.security.Principal)
	 */
    @Transient
    public Permission getPermission(final Principal principal) {
        for (AclEntry entry : entries) {
            if (entry.getPrincipal().equals(principal)) {
                return entry.getPermission();
            }
        }
        return null;
    }

    /**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 87).append(name).toHashCode();
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#hasPermission(java.util.Collection, kiff.security.Permission)
	 */
    public boolean hasPermission(final Collection<Principal> principals, final Permission permission) {
        for (Principal principal : principals) {
            if (hasPermission(principal, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#hasPermission(kiff.security.Principal, kiff.security.Permission)
	 */
    public boolean hasPermission(final Principal principal, final Permission permission) {
        for (AclEntry entry : entries) {
            if (entry.getPrincipal().equals(principal)) {
                return entry.hasPermission(permission);
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#hasPermission(kiff.security.Principal[], kiff.security.Permission)
	 */
    public boolean hasPermission(final Principal[] principals, final Permission permission) {
        for (Principal principal : principals) {
            if (hasPermission(principal, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#removeEntry(kiff.security.AclEntry)
	 */
    public void removeEntry(final AclEntry entry) {
        this.entries.remove(entry);
        entry.setAcl(null);
    }

    /**
	 * Sets entries.
	 * @param entries the entries to set.
	 */
    public void setEntries(final Set<AclEntry> entries) {
        this.entries = entries;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.entity.CoreEntity#setId(java.io.Serializable)
	 */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#setName(java.lang.String)
	 */
    public void setName(final String name) {
        this.name = name;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.security.Acl#setOwner(kiff.security.Principal)
	 */
    public void setOwner(final Principal owner) {
    }
}
