package cz.fi.muni.xkremser.editor.shared.rpc;

import java.io.Serializable;

/**
 * The Class RecentlyModifiedItem.
 */
public class UserInfoItem implements Serializable {

    private static final long serialVersionUID = 6046736915835497481L;

    /** The uuid. */
    private String name;

    /** The name. */
    private String surname;

    /** The description. */
    private String sex;

    /** The id. */
    private String id;

    /**
     * Instantiates a new recently modified item.
     */
    public UserInfoItem() {
    }

    /**
     * Instantiates a new user info item.
     * 
     * @param name
     *        the name
     * @param surname
     *        the surname
     * @param sex
     *        the sex
     * @param id
     *        the id
     */
    public UserInfoItem(String name, String surname, String sex, String id) {
        super();
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *        the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the surname.
     * 
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname.
     * 
     * @param surname
     *        the new surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the sex.
     * 
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the sex.
     * 
     * @param sex
     *        the new sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *        the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserInfoItem other = (UserInfoItem) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
