package org.gbif.portal.model;

import java.util.Date;

/**
 * Agent model object
 * @author trobertson
 */
public class Agent extends ModelObject {

    /**
	 * Generated
	 */
    private static final long serialVersionUID = -7232356677909853863L;

    protected String name;

    protected String address;

    protected String email;

    protected String telephone;

    protected Date created;

    protected Date modified;

    protected Date deleted;

    @Override
    public String toString() {
        return "Agent id[" + id + "] name[" + name + "] email[" + email + "] address[" + address + "] telephone[" + telephone + "]";
    }

    /**
	 * @return Returns the deleted.
	 */
    public Date getDeleted() {
        return deleted;
    }

    /**
	 * @param deleted The deleted to set.
	 */
    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    /**
	 * @return Returns the address.
	 */
    public String getAddress() {
        return address;
    }

    /**
	 * @param address The address to set.
	 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
	 * @return Returns the created.
	 */
    public Date getCreated() {
        return created;
    }

    /**
	 * @param created The created to set.
	 */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
	 * @return Returns the email.
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email The email to set.
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return Returns the modified.
	 */
    public Date getModified() {
        return modified;
    }

    /**
	 * @param modified The modified to set.
	 */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the telephone.
	 */
    public String getTelephone() {
        return telephone;
    }

    /**
	 * @param telephone The telephone to set.
	 */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
