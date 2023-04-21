package teachin.domain.teachin.model.obj.teachin;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** 
 * Object mapping for hibernate-handled table: user_contacts
 * author: auto-generated
 */
@Entity
@Table(name = "user_contacts", catalog = "teachin")
public class UserContacts implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039797L;

    /** Field mapping */
    private Contacts contact;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private Users user;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public UserContacts() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public UserContacts(Integer id) {
        this.id = id;
    }

    /** Constructor taking a given ID
	 * @param contact Contacts object;
	 * @param id Integer object;
	 * @param user Users object;
	 */
    public UserContacts(Contacts contact, Integer id, Users user) {
        this.contact = contact;
        this.id = id;
        this.user = user;
    }

    /**
     * Return the value associated with the column: contact
	 * @return A Contacts object (this.contact)
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    public Contacts getContact() {
        return this.contact;
    }

    /**  
     * Set the value related to the column: contact 
	 * @param contact the contact value you wish to set
	 */
    public void setContact(final Contacts contact) {
        this.contact = contact;
    }

    /**
     * Return the value associated with the column: id
	 * @return A Integer object (this.id)
	 */
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**  
     * Set the value related to the column: id 
	 * @param id the id value you wish to set
	 */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: user
	 * @return A Users object (this.user)
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Users getUser() {
        return this.user;
    }

    /**  
     * Set the value related to the column: user 
	 * @param user the user value you wish to set
	 */
    public void setUser(final Users user) {
        this.user = user;
    }

    /**
     * Deep copy
	 * @throws CloneNotSupportedException
     */
    @Override
    public UserContacts clone() throws CloneNotSupportedException {
        super.clone();
        UserContacts copy = new UserContacts();
        copy.setContact(this.getContact());
        copy.setId(this.getId());
        copy.setUser(this.getUser());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id: " + this.id + ", ");
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(UserContacts.class)))) return false;
        UserContacts that = (UserContacts) aThat;
        return (((this.contact == null) && (that.contact == null)) || (this.contact != null && this.contact.equals(that.contact))) && (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.user == null) && (that.user == null)) || (this.user != null && this.user.equals(that.user)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.contact == null ? 0 : this.contact.hashCode());
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.user == null ? 0 : this.user.hashCode());
        return result;
    }
}
