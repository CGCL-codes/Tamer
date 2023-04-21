package net.sourceforge.ondex.webservice;

import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;

/**
 * Defines a Relation between two concepts and possibly a qualifier concept.
 * 
 * @author David Withers
 */
public class WSRelation {

    /**
	 * The ID of this Relation
	 */
    private Integer id;

    /**
	 * The "from" Concept of this Relation
	 */
    private WSConcept fromConcept;

    /**
	 * The "to" Concept of this Relation
	 */
    private WSConcept toConcept;

    /**
	 * The "qualifier" Concept of this Relation for ternary
	 * relations (Optional)
	 */
    private WSConcept qualifier;

    /**
	 * The RelationType of this Relation
	 */
    private WSRelationType ofType;

    public WSRelation() {
    }

    public WSRelation(ONDEXRelation relation) {
        id = relation.getId();
        ONDEXConcept concept = relation.getFromConcept();
        if (concept != null) {
            fromConcept = new WSConcept(concept);
        }
        concept = relation.getToConcept();
        if (concept != null) {
            toConcept = new WSConcept(concept);
        }
        concept = relation.getQualifier();
        if (concept != null) {
            qualifier = new WSConcept(concept);
        }
        RelationType relationType = relation.getOfType();
        if (relationType != null) {
            ofType = new WSRelationType(relationType);
        }
    }

    /**
	 * Returns the id.
	 * 
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * Returns the fromConcept.
	 * 
	 * @return the fromConcept
	 */
    public WSConcept getFromConcept() {
        return fromConcept;
    }

    /**
	 * Sets the fromConcept.
	 * 
	 * @param fromConcept
	 *            the new fromConcept
	 */
    public void setFromConcept(WSConcept fromConcept) {
        this.fromConcept = fromConcept;
    }

    /**
	 * Returns the toConcept.
	 * 
	 * @return the toConcept
	 */
    public WSConcept getToConcept() {
        return toConcept;
    }

    /**
	 * Sets the toConcept.
	 * 
	 * @param toConcept
	 *            the new toConcept
	 */
    public void setToConcept(WSConcept toConcept) {
        this.toConcept = toConcept;
    }

    /**
	 * Returns the qualifier.
	 * 
	 * @return the qualifier
	 */
    public WSConcept getQualifier() {
        return qualifier;
    }

    /**
	 * Sets the qualifier.
	 * 
	 * @param qualifier
	 *            the new qualifier
	 */
    public void setQualifier(WSConcept qualifier) {
        this.qualifier = qualifier;
    }

    /**
	 * Returns the ofTypeSet.
	 * 
	 * @return the ofTypeSet
	 */
    public WSRelationType getOfType() {
        return ofType;
    }

    /**
	 * Sets the ofType.
	 * 
	 * @param ofType
	 *            the new ofType
	 */
    public void setOfType(WSRelationType ofType) {
        this.ofType = ofType;
    }
}
