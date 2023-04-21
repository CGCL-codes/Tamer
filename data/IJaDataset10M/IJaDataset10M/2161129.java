package de.forsthaus.backend.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * EN: Model class for <b>SecGroupright</b>.<br>
 * DE: Model Klasse fuer <b>Gruppenrechte</b>.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class SecGroupright implements java.io.Serializable, Entity {

    private static final long serialVersionUID = 9206102047641563556L;

    private long id = Long.MIN_VALUE;

    private int version;

    private SecGroup secGroup;

    private SecRight secRight;

    public boolean isNew() {
        return (getId() == Long.MIN_VALUE);
    }

    public SecGroupright() {
    }

    public SecGroupright(long id) {
        this.setId(id);
    }

    public SecGroupright(long id, SecGroup secGroup, SecRight secRight) {
        this.setId(id);
        this.secGroup = secGroup;
        this.secRight = secRight;
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

    public SecGroup getSecGroup() {
        return this.secGroup;
    }

    public void setSecGroup(SecGroup secGroup) {
        this.secGroup = secGroup;
    }

    public SecRight getSecRight() {
        return this.secRight;
    }

    public void setSecRight(SecRight secRight) {
        this.secRight = secRight;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    public boolean equals(SecGroupright secGroupright) {
        return getId() == secGroupright.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SecGroupright) {
            SecGroupright secGroupright = (SecGroupright) obj;
            return equals(secGroupright);
        }
        return false;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
}
