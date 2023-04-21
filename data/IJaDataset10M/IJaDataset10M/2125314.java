package com.apelon.dts.client.association;

import com.apelon.dts.client.concept.DTSConcept;
import com.apelon.dts.client.term.Term;

/**
 * A Synonym associates a term to a concept.  It relates a term which is a
 * synonym of a concept.
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Apelon, Inc.</p>
 * @author     Apelon, Inc.
 * @version    DTS 3.0.0
 * @since      DTS 3.0.0
 */
public class Synonym extends Association {

    private boolean preferred;

    private boolean fLocalAddition;

    /**
   * Constructs a synonym from a concept, association type, and term.
   *
   * @param      concept          Synonym's concept
   * @param      assocType        Synonym's association type, i.e. the type of synonym
   * @param      term             Synonym term
   * @param      preferred        indicates this synonym is the preferred term
   *                              for this concept
   */
    public Synonym(DTSConcept concept, AssociationType assocType, Term term, boolean preferred) {
        super(concept, assocType, term);
        this.preferred = preferred;
    }

    /**
   * Constructs a synonym from the association type and synonym term.  This
   * would normally be used to construct a synonym that is part of a
   * {@link com.apelon.dts.client.concept.DTSConcept}.  When using this
   * constructor you will also need to issue {@link #setConcept} before
   * using the object.
   *
   * @param      assocType        the association type
   * @param      term             the synonym term of the association
   */
    public Synonym(AssociationType assocType, Term term) {
        super(assocType, term);
    }

    /**
   * Constructs a synonym from the association type and synonym term.  This
   * would normally be used to construct a synonym that is part of a
   * {@link com.apelon.dts.client.concept.DTSConcept}.  When using this
   * constructor you will also need to issue {@link #setConcept} before
   * using the object.
   *
   * @param      assocType        the association type
   * @param      term             the synonym term of the association
   * @param      localAddition    indicator whether this is a local addition to
   *                              subscription content
   */
    public Synonym(AssociationType assocType, Term term, boolean localAddition) {
        super(assocType, term);
        this.fLocalAddition = localAddition;
    }

    /**
   * Set concept of synonym association.
   *
   * @param      concept          concept of synonym association
   */
    public void setConcept(DTSConcept concept) {
        setFromItem(concept);
    }

    /**
   * Get synonym's concept.
   *
   * @return     Synonym's concept
   */
    public DTSConcept getConcept() {
        return (DTSConcept) getFromItem();
    }

    /**
   * Get synonym's term.
   *
   * @return     Synonym's term
   */
    public Term getTerm() {
        return (Term) getToItem();
    }

    /**
   * Indicates whether this synonym is the preferred term for the concept
   * @return     true if this is the preferred term; false if not
   */
    public boolean isPreferred() {
        return preferred;
    }

    /**
   * Set the preferred term indicator.  Normally exactly one synonym should be
   * so designated per concept.
   * @param      newPreferred     true if this synonym is preferred, false otherwise
   */
    public void setPreferred(boolean newPreferred) {
        preferred = newPreferred;
    }

    /**
   * Indicates whether this synonym is a local addition, i.e. the concept is in
   * a subscription namespace, but the synonym type is in a local namespace.
   *
   * @return     <code>true</code> if this synonym is a local addition to a
   *             subscription concept; <code>false</code> if not.
   */
    public boolean isLocalAddition() {
        return fLocalAddition;
    }

    /**
   * Sets the local addition indicator.
   *
   * @param      localAddition    <code>true</code> if this synonym is a local
   *                              addition to a subscription concept;
   *                              <code>false</code> if not.
   */
    public void setLocalAddition(boolean localAddition) {
        fLocalAddition = localAddition;
    }

    /**
   * Compares this Synonym to the specified object.
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
        if (obj instanceof Synonym) {
            Synonym syn = (Synonym) obj;
            if (!getConcept().equals(syn.getConcept())) {
                return false;
            }
            if (!getAssociationType().equals(syn.getAssociationType())) {
                return false;
            }
            if (!getTerm().equals(syn.getTerm())) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
    * Returns a hashcode for this DTS concept.
    * @return  a hash code value for this object.
    */
    public int hashCode() {
        return getConcept().hashCode() ^ getAssociationType().hashCode() ^ getTerm().hashCode();
    }

    /**
   * Returns name of synonym term.
   *
   * @return synonym term
   */
    public String toString() {
        return getTerm().getName();
    }
}
