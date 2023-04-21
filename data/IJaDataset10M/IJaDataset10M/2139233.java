package edu.upmc.opi.caBIG.caTIES.database.domain.impl;

import edu.upmc.opi.caBIG.caTIES.database.domain.*;

/**
 * The Class ConceptImpl
 * @see edu.upmc.opi.caBIG.caTIES.database.domain.Concept 
 */
public class ConceptImpl implements java.io.Serializable, Concept {

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
     * The cui.
     */
    protected java.lang.String cui;

    /**
     * Gets the cui.
     * 
     * @return the cui
     */
    public java.lang.String getCui() {
        return cui;
    }

    /**
     * Sets the cui.
     * 
     * @param cui
     *            the cui
     */
    public void setCui(java.lang.String cui) {
        this.cui = cui;
    }

    /**
     * The tui.
     */
    protected java.lang.String tui;

    /**
     * Gets the tui.
     * 
     * @return the tui
     */
    public java.lang.String getTui() {
        return tui;
    }

    /**
     * Sets the tui.
     * 
     * @param tui
     *            the tui
     */
    public void setTui(java.lang.String tui) {
        this.tui = tui;
    }

    /**
     * The name.
     */
    protected java.lang.String name;

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * The semantic type.
     */
    protected java.lang.String semanticType;

    /**
     * Gets the semantic type.
     * 
     * @return the semantic type
     */
    public java.lang.String getSemanticType() {
        return semanticType;
    }

    /**
     * Sets the semantic type.
     * 
     * @param semanticType
     *            the semantic type
     */
    public void setSemanticType(java.lang.String semanticType) {
        this.semanticType = semanticType;
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
        conceptReferent.setConcept(this);
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
        if (obj instanceof Concept) {
            Concept c = (Concept) obj;
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
        sb.append("CUI ==> " + this.cui + "\n");
        sb.append("TUI ==> " + this.tui + "\n");
        sb.append("NAME ==> " + this.name + "\n");
        sb.append("SEMANTIC_TYPE ==> " + this.semanticType + "\n");
        return sb.toString();
    }
}
