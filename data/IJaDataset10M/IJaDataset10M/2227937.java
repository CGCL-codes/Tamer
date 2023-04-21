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
 * Object mapping for hibernate-handled table: user_addresses
 * author: auto-generated
 */
@Entity
@Table(name = "user_addresses", catalog = "teachin")
public class UserAddresses implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039798L;

    /** Field mapping */
    private Addresses address;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private Users user;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public UserAddresses() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public UserAddresses(Integer id) {
        this.id = id;
    }

    /** Constructor taking a given ID
	 * @param address Addresses object;
	 * @param id Integer object;
	 * @param user Users object;
	 */
    public UserAddresses(Addresses address, Integer id, Users user) {
        this.address = address;
        this.id = id;
        this.user = user;
    }

    /**
     * Return the value associated with the column: address
	 * @return A Addresses object (this.address)
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    public Addresses getAddress() {
        return this.address;
    }

    /**  
     * Set the value related to the column: address 
	 * @param address the address value you wish to set
	 */
    public void setAddress(final Addresses address) {
        this.address = address;
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
    public UserAddresses clone() throws CloneNotSupportedException {
        super.clone();
        UserAddresses copy = new UserAddresses();
        copy.setAddress(this.getAddress());
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
        if ((aThat == null) || (!(aThat.getClass().equals(UserAddresses.class)))) return false;
        UserAddresses that = (UserAddresses) aThat;
        return (((this.address == null) && (that.address == null)) || (this.address != null && this.address.equals(that.address))) && (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.user == null) && (that.user == null)) || (this.user != null && this.user.equals(that.user)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.address == null ? 0 : this.address.hashCode());
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.user == null ? 0 : this.user.hashCode());
        return result;
    }
}
