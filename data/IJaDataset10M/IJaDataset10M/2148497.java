package org.openjf.usergroup;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openjf.util.EntityBasic;
import org.openjf.util.ObjectUtil;

public class Groupx implements EntityBasic {

    private int id;

    private String name;

    /**
     * Is this group data (user list and other) obtained from an 
     * external database such as LDAP?
     */
    private boolean external;

    public Groupx() {
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public boolean equalsId(Object o) {
        if ((o == null) || (!ObjectUtil.isInstance(getClass(), o))) {
            return false;
        }
        Groupx oo = (Groupx) o;
        return getId() == oo.getId();
    }

    protected boolean equalsProperties(Groupx o) {
        return equalsId(o) && ObjectUtil.equals(getName(), o.getName()) && isExternal() == o.isExternal();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (!ObjectUtil.sameClass(getClass(), o))) {
            return false;
        }
        return equalsProperties((Groupx) o);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }
}
