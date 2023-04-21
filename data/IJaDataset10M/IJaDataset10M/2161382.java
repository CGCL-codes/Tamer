package org.j2eebuilder.component.ejb;

import java.io.Serializable;

/**
 * 
 * @(#)ComponentStatusPK.java 1.350 01/12/03
 * 
 *                            Primary Key
 * 
 * @version 1.3.1
 * 
 * @since OEC1.2
 */
public class ComponentStatusPK implements java.io.Serializable {

    public ComponentStatusPK(Integer componentID, Integer componentStatusTypeID) {
        this.componentID = componentID;
        this.componentStatusTypeID = componentStatusTypeID;
    }

    public ComponentStatusPK() {
    }

    public String toString() {
        return this.componentID + "-" + this.componentStatusTypeID;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof ComponentStatusPK) return this.hashCode() == ((ComponentStatusPK) pk).hashCode();
        return false;
    }

    public Integer componentID;

    public Integer componentStatusTypeID;

    public Integer getComponentID() {
        return componentID;
    }

    public void setComponentID(Integer componentID) {
        this.componentID = componentID;
    }

    public Integer getComponentStatusTypeID() {
        return componentStatusTypeID;
    }

    public void setComponentStatusTypeID(Integer componentStatusTypeID) {
        this.componentStatusTypeID = componentStatusTypeID;
    }
}
