package net.sf.dlcdb.dbobjects;

import java.util.Set;

/**
 *  POJO class DocumentUser used for storage user data
 *
 *  @hibernate.class
 *    table = "DOCUMENTUSER"
 *
 *  @author $Author: gregor8003 $
 *  @version $Rev: 48 $
 */
public class DocumentUser {

    /** Id of the object */
    private Long id;

    /** User login */
    private String login;

    /** Digest of the user password (password in encoded form) */
    private String passwordDigest;

    /** Collection of permissions granted for this user */
    private Set permissions;

    /**
	 *  Creates a new DocumentUser object.
	 */
    public DocumentUser() {
    }

    /**
	 *  Return ID of the object
	 *
	 *  @hibernate.id
	 *   column = "DOCUMENTUSER_ID"
     *   generator-class = "native"
	 *
	 *  @return ID of the object
	 */
    public Long getId() {
        return id;
    }

    /**
	 *  Return login of the user
	 *
	 *  @hibernate.property
	 *    column = "LOGIN"
	 *
	 *  @return Login of the user
	 */
    public String getLogin() {
        return login;
    }

    /**
	 *  Return digest of the user password
	 *
	 *  @hibernate.property
	 *    column = "PASSWORDDIGEST"
	 *
	 *  @return Digest of the user password
	 */
    public String getPasswordDigest() {
        return passwordDigest;
    }

    /**
	 *  Return collection of user permissions
	 *
	 *  @hibernate.set
	 *  @hibernate.many-to-many
	 *    class = "net.sf.dlcdb.dbobjects.DocumentPermission"
	 *  @hibernate.key
	 *    column = "DOCUMENTUSER_PERMISSION_ID"
	 *
	 *  @return Collection of user permissions
	 */
    public Set getPermissions() {
        return permissions;
    }

    /**
	 *  Set ID of the object
	 *
	 *  @param id ID of the object to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 *  Set login of the user
	 *
	 *  @param login Login of the user to set
	 */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
	 *  Set digest of the user password
	 *
	 *  @param passwordDigest Digest of the user password to set
	 */
    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    /**
	 *  Set collection of the user permissions
	 *
	 *  @param permissions Collection of the user permissions to set
	 */
    public void setPermissions(Set permissions) {
        this.permissions = permissions;
    }
}
