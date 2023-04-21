package de.forsthaus.backend.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for <b>SecUserrole</b>.<br>
 * DE: Model Klasse fuer <b>UserRolle</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecUserrole implements java.io.Serializable, Entity {

    private static final long serialVersionUID = 6613720067926409622L;

    private long id = Long.MIN_VALUE;

    private int version;

    private SecUser secUser;

    private SecRole secRole;

    public boolean isNew() {
        return (getId() == Long.MIN_VALUE);
    }

    public SecUserrole() {
    }

    public SecUserrole(long id) {
        this.setId(id);
    }

    public SecUserrole(long id, SecUser secUser, SecRole secRole) {
        this.setId(id);
        this.secUser = secUser;
        this.secRole = secRole;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
	 * EN: Hibernate version field. Do not touch this!.<br>
	 * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
	 */
    public int getVersion() {
        return this.version;
    }

    /**
	 * EN: Hibernate version field. Do not touch this!.<br>
	 * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
	 */
    public void setVersion(int version) {
        this.version = version;
    }

    public SecUser getSecUser() {
        return this.secUser;
    }

    public void setSecUser(SecUser secUser) {
        this.secUser = secUser;
    }

    public SecRole getSecRole() {
        return this.secRole;
    }

    public void setSecRole(SecRole secRole) {
        this.secRole = secRole;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    public boolean equals(SecUserrole secUserrole) {
        return getId() == secUserrole.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SecUserrole) {
            SecUserrole secUserrole = (SecUserrole) obj;
            return equals(secUserrole);
        }
        return false;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
}
