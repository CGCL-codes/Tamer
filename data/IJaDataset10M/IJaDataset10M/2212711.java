package org.greenscape.opencontact.api;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author smsajid
 */
@Entity
@Table(name = "m_contact_group")
@NamedQueries(value = { @NamedQuery(name = "findAllGroups", query = "select g from ContactGroup g") })
public class ContactGroup {

    protected Long id;

    protected String name;

    protected List<GroupAccount> groupAccounts;

    protected List<Contact> contacts;

    /**
     * Returns the primary key id of this group.
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objid")
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of this group
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the group name
     * @return the group name
     */
    @Column(name = "name", length = 60)
    public String getName() {
        return name;
    }

    /**
     * Sets the group name
     * @param name the group name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the groupAccount
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    public List<GroupAccount> getGroupAccounts() {
        return groupAccounts;
    }

    /**
     * @param groupAccounts the groupAccounts to set
     */
    public void setGroupAccounts(List<GroupAccount> groupAccounts) {
        this.groupAccounts = groupAccounts;
    }

    /**
     * @return the contacts
     */
    @ManyToMany(mappedBy = "groups")
    public List<Contact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContactGroup)) {
            return false;
        }
        ContactGroup other = (ContactGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.greenscape.opencontact.ContactGroup[" + id + "," + getName() + "]";
    }
}
