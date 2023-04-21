package com.apelon.dts.client.association;

import com.apelon.dts.client.common.DTSObject;

/**
 * An association relates a concept or term to another concept or term.
 * Associations fall into three main categories:  concept
 * associations, term assocations, and synonyms which are concept-term
 * associations.
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon, Inc.
 * @version    DTS 3.0.0
 * @since      DTS 3.0.0
 */
public abstract class Association {

    private DTSObject fromItem;

    private AssociationType assocType;

    private DTSObject toItem;

    public Association() {
    }

    /**
   * Constructs an association from the "from" item, association type, and "to"
   * item.
   *
   * @param      fromItem     the from item of the association
   * @param      assocType    the association type
   * @param      toItem       the to item of the association
   */
    protected Association(DTSObject fromItem, AssociationType assocType, DTSObject toItem) {
        this.fromItem = fromItem;
        this.assocType = assocType;
        this.toItem = toItem;
    }

    /**
   * Constructs an association from the association type and "to" item.  This
   * would normally be used to construct an association that is part of a
   * {@link com.apelon.dts.client.concept.DTSConcept} or
   * {@link com.apelon.dts.client.term.Term}.  When using this constructor you
   * will also need to issue {@link #setFromItem} before using the object.
   *
   * @param      assocType        the association type
   * @param      toItem           the to item of the association
   */
    protected Association(AssociationType assocType, DTSObject toItem) {
        this.assocType = assocType;
        this.toItem = toItem;
    }

    /**
   * Constructs an association from the "from" item and association type.  This
   * would normally be used to construct an inverse association that is part of
   * a {@link com.apelon.dts.client.concept.DTSConcept} or
   * {@link com.apelon.dts.client.term.Term}.  When using this constructor you
   * will also need to issue {@link #setToItem} before using the object.
   *
   *
   * @param      fromItem         the from item of the association
   * @param      assocType        the association type
   */
    protected Association(DTSObject fromItem, AssociationType assocType) {
        this.fromItem = fromItem;
        this.assocType = assocType;
    }

    /**
   * Set "from" item of association.
   *
   * @param      fromItem         from item of association
   */
    protected void setFromItem(DTSObject fromItem) {
        this.fromItem = fromItem;
    }

    /**
   * Get "from" item of association.
   *
   * @return     from item.
   */
    public DTSObject getFromItem() {
        return fromItem;
    }

    /**
   * Gets type of association.
   *
   * @return     association type
   */
    public AssociationType getAssociationType() {
        return assocType;
    }

    /**
   * Set type of association
   * 
   * @param type  the type of association
   */
    public void setAssociationType(AssociationType type) {
        assocType = type;
    }

    /**
   * Gets the name of the association type of this <code>Association</code>.
   *
   * @return     the name of this <code>Association</code>.
   */
    public String getName() {
        return getAssociationType().getName();
    }

    /**
   * Set "to" item of association.
   *
   * @param      toItem           to item of association
   */
    protected void setToItem(DTSObject toItem) {
        this.toItem = toItem;
    }

    /**
   * Get "to" item of association.
   *
   * @return     from item.
   */
    protected DTSObject getToItem() {
        return toItem;
    }

    /**
   * Gets the <code>String</code> value of this <code>Association</code>.
   *
   * @return     the value of this <code>Association</code>.
   */
    public String getValue() {
        return getToItem().getName();
    }

    /**
   * Compares this association to the specified object.
   *
   * @param   obj   the object to compare this <code>String</code>
   *                     against.
   * @return  <code>true</code> if the <code>String </code>are equal;
   *          <code>false</code> otherwise.
   */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Association) {
            Association assc = (Association) obj;
            DTSObject from = assc.getFromItem();
            if (from != null && !from.equals(this.getFromItem())) {
                return false;
            }
            AssociationType asscType = assc.getAssociationType();
            if (!asscType.equals(this.getAssociationType())) {
                return false;
            }
            DTSObject to = assc.getToItem();
            if (to != null && !to.equals(this.getToItem())) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
   * Returns a hashcode for this association.
   * @return  a hash code value for this object.
   */
    public int hashCode() {
        if (getFromItem() == null) {
            return getAssociationType().hashCode() ^ getToItem().hashCode();
        } else if (getToItem() == null) {
            return getFromItem().hashCode() ^ getAssociationType().hashCode();
        } else {
            return getFromItem().hashCode() ^ getAssociationType().hashCode() ^ getToItem().hashCode();
        }
    }
}
