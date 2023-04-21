package tinyuser.security;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Implementation of {@link java.security.acl.Group}.
 * 
 * @author Martin Algesten
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class Group extends Principal implements java.security.acl.Group, Serializable {

    private List<java.security.Principal> members = new LinkedList<java.security.Principal>();

    public Group() {
    }

    /**
	 * Constructs a new empty group by setting the group name.
	 * 
	 * @param name
	 *          The name of the group.
	 */
    public Group(String name) {
        super(name);
    }

    /**
	 * Constructs a new group by also providing a list of members.
	 * 
	 * @param name
	 *          The name of the group.
	 * @param members
	 *          The members of the group.
	 */
    public Group(String name, List<Principal> members) {
        this(name);
        if (members == null) throw new IllegalArgumentException("Members can't be null");
        this.members.addAll(members);
    }

    private static final java.security.Principal[] TYPE = new java.security.Principal[] {};

    /**
	 * Returns all principals in this group by adding itself, all members and then recursively adding all members of
	 * groups that are members.
	 * 
	 * @return
	 */
    @Transient
    public java.security.Principal[] getAllPrincipals() {
        HashSet<java.security.Principal> set = new HashSet<java.security.Principal>();
        set.add(this);
        collectPrincipals(set, members());
        return new LinkedList<java.security.Principal>(set).toArray(TYPE);
    }

    protected void collectPrincipals(HashSet<java.security.Principal> set, Enumeration<? extends java.security.Principal> members) {
        while (members.hasMoreElements()) {
            java.security.Principal p = members.nextElement();
            if (set.contains(p)) continue;
            set.add(p);
            if (p instanceof java.security.acl.Group) collectPrincipals(set, ((Group) p).members());
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean addMember(java.security.Principal principal) {
        if (principal == this) throw new IllegalArgumentException("Group can't be added to itself");
        if (principal == null) throw new IllegalArgumentException("User can't be null");
        if (isMember(principal)) return false;
        if (principal instanceof java.security.acl.Group) {
            HashSet<java.security.Principal> set = new HashSet<java.security.Principal>();
            collectPrincipals(set, ((java.security.acl.Group) principal).members());
            if (set.contains(this)) throw new IllegalArgumentException("Circular group dependency not allowed");
        }
        return members.add(principal);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    @Transient
    public boolean isMember(java.security.Principal member) {
        if (member == null) throw new IllegalArgumentException("Member can't be null");
        return members.contains(member);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Enumeration<? extends java.security.Principal> members() {
        return new Vector<java.security.Principal>(members).elements();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean removeMember(java.security.Principal user) {
        if (user == null) throw new IllegalArgumentException("User can't be null");
        return members.remove(user);
    }

    @Override
    public String toString() {
        return "Group[" + getName() + "]";
    }

    protected void setMembers(List<java.security.Principal> members) {
        this.members = members;
    }

    @OneToMany(targetEntity = AbstractPrincipal.class)
    @JoinTable(name = "GroupMembers", joinColumns = @JoinColumn(name = "groupName"), inverseJoinColumns = @JoinColumn(name = "memberName"))
    protected List<java.security.Principal> getMembers() {
        return members;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((members == null) ? 0 : members.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Group other = (Group) obj;
        if (members == null) {
            if (other.members != null) return false;
        } else if (!members.equals(other.members)) return false;
        return true;
    }
}
