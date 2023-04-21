package org.jma.app.ewisdom.entities;

import java.io.Serializable;

/**
 * RolEntityPair class represents a pair formed by a ROL and an Entity, a role could be any String
 * that represents the Role given to an entity, an Entity could be a String reference to a group or user, 
 * groupName and userName. <br>
 * It's used by EntityWithACL class<br> 
 * @author jesmari@ono.com
 * @version 1.0
 * @since 1.0
 */
public class RolEntityPair implements Serializable {

    String rol;

    String entity;

    public RolEntityPair(String rol, String entity) {
        this.rol = rol;
        this.entity = entity;
    }

    public boolean equals(Object o) {
        return (((RolEntityPair) o).rol.equals(this.rol) && ((RolEntityPair) o).entity.equals(this.entity));
    }
}
