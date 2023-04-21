package com.ohioedge.j2ee.api.org.mechanism.ejb;

import java.io.Serializable;

/**
 * @(#)MechanismPK.java	1.350 01/12/03
 * Primary Key
 * @version 1.3.1
 * @since OEC1.2
 */
public class MechanismPK implements java.io.Serializable {

    public MechanismPK(Integer mechanismID) {
        this.mechanismID = mechanismID;
    }

    public MechanismPK() {
    }

    public String toString() {
        return String.valueOf(this.mechanismID);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if ((pk != null) && (pk instanceof MechanismPK)) return this.toString().equals(((MechanismPK) pk).toString());
        return false;
    }

    public Integer mechanismID;

    public Integer getMechanismID() {
        return mechanismID;
    }

    public void setMechanismID(Integer mechanismID) {
        this.mechanismID = mechanismID;
    }
}
