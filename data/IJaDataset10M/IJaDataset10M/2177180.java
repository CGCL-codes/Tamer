package org.netbeans.modules.exceptions.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Jindrich Sedek
 */
@Table(name = "directuser")
@NamedQueries({ @NamedQuery(name = "Directuser.findByName", query = "SELECT d FROM Directuser d WHERE d.name = :name") })
@Entity
public class Directuser implements Serializable {

    @Id
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "MESSAGE_PREFIX", nullable = true)
    private String messagePrefix;

    /** Creates a new instance of Directuser */
    public Directuser() {
    }

    /**
     * Creates a new instance of Directuser with the specified values.
     * @param name the name of the Directuser
     */
    public Directuser(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this Directuser.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this Directuser to the specified value.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public void setMessagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    /**
     * Determines whether another object is equal to this Directuser.  The result is 
     * <code>true</code> if and only if the argument is not null and is a Directuser object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Directuser)) {
            return false;
        }
        Directuser other = (Directuser) object;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ff.Directuser[name=" + name + "]";
    }

    public boolean hasValidPrefix() {
        return (getMessagePrefix() != null) && (getMessagePrefix().length() > 0);
    }
}
