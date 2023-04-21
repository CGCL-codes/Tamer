package edu.upmc.opi.caBIG.caTIES.database.domain.impl;

import edu.upmc.opi.caBIG.caTIES.database.domain.*;

/**
 * The Class TopologyImpl.
 */
public class TopologyImpl implements java.io.Serializable, Topology {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1234567890L;

    /**
     * The id.
     */
    protected java.lang.Long id;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public java.lang.Long getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * The version.
     */
    protected java.lang.Long version;

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public java.lang.Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * 
     * @param version
     *            the version
     */
    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    /**
     * The uuid.
     */
    protected java.lang.String uuid;

    /**
     * Gets the uuid.
     * 
     * @return the uuid
     */
    public java.lang.String getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid.
     * 
     * @param uuid
     *            the uuid
     */
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    /**
     * The concept referent collection.
     */
    private java.util.Collection conceptReferentCollection = new java.util.HashSet();

    /**
     * Gets the concept referent collection.
     * 
     * @return the concept referent collection
     */
    public java.util.Collection getConceptReferentCollection() {
        return conceptReferentCollection;
    }

    /**
     * Sets the concept referent collection.
     * 
     * @param conceptReferentCollection
     *            the concept referent collection
     */
    public void setConceptReferentCollection(java.util.Collection conceptReferentCollection) {
        this.conceptReferentCollection = conceptReferentCollection;
    }

    /**
     * Adds the concept referent.
     * 
     * @param conceptReferent
     *            the concept referent
     */
    public void addConceptReferent(edu.upmc.opi.caBIG.caTIES.database.domain.ConceptReferent conceptReferent) {
        this.conceptReferentCollection.add(conceptReferent);
        conceptReferent.setTopology(this);
    }

    /**
     * Equals.
     * 
     * @param obj
     *            the obj
     * 
     * @return true, if equals
     */
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj instanceof Topology) {
            Topology c = (Topology) obj;
            String thisUUID = getUuid();
            if (this.uuid != null && thisUUID.equals(c.getUuid())) {
                eq = true;
            }
        }
        return eq;
    }

    /**
     * Hash code.
     * 
     * @return the int
     */
    public int hashCode() {
        int h = 0;
        if (getUuid() != null) {
            h += getUuid().hashCode();
        }
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName() + "\n");
        sb.append("ID ==> " + this.id + "\n");
        sb.append("UUID ==> " + this.uuid + "\n");
        return sb.toString();
    }
}
