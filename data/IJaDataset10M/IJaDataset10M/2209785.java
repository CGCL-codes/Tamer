package net.sourceforge.ondex.webservice;

import net.sourceforge.ondex.core.RelationType;

/**
 * Specifies the RelationType of a Relation. In addition to the mandatory field
 * id and the optional field description, an inverse name and several properties
 * can be defined. A RelationType can be a specialization of another
 * RelationType.
 * 
 * @author David Withers
 */
public class WSRelationType extends WSMetaData {

    /**
	 * The inverse name for this RelationType (Optional)
	 */
    private String inverseName;

    /**
	 * The antisymmetric property for this RelationType. The default is false. (Optional)
	 */
    private Boolean isAntisymmetric;

    /**
	 * The reflexive property for this RelationType. The default is false. (Optional)
	 */
    private Boolean isReflexive;

    /**
	 * The symmetric property for this RelationType. The default is false. (Optional)
	 */
    private Boolean isSymmetric;

    /**
	 * The transitive property for this RelationType. The default is false. (Optional)
	 */
    private Boolean isTransitiv;

    /**
	 * Parent RelationType for this RelationType (Optional)
	 */
    private WSRelationType specialisationOf;

    public WSRelationType() {
    }

    public WSRelationType(RelationType relationType) {
        super(relationType);
        inverseName = relationType.getInverseName();
        isAntisymmetric = relationType.isAntisymmetric();
        isReflexive = relationType.isReflexive();
        isSymmetric = relationType.isSymmetric();
        isTransitiv = relationType.isTransitiv();
        RelationType specialisationOfRelationType = relationType.getSpecialisationOf();
        if (specialisationOfRelationType != null) {
            specialisationOf = new WSRelationType(specialisationOfRelationType);
        }
    }

    /**
	 * Returns the inverseName.
	 * 
	 * @return the inverseName
	 */
    public String getInverseName() {
        return inverseName;
    }

    /**
	 * Sets the inverseName.
	 * 
	 * @param inverseName
	 *            the new inverseName
	 */
    public void setInverseName(String inverseName) {
        this.inverseName = inverseName;
    }

    /**
	 * Returns the isAntisymmetric.
	 * 
	 * @return the isAntisymmetric
	 */
    public boolean isAntisymmetric() {
        return isAntisymmetric;
    }

    /**
	 * Sets the isAntisymmetric.
	 * 
	 * @param isAntisymmetric
	 *            the new isAntisymmetric
	 */
    public void setAntisymmetric(boolean isAntisymmetric) {
        this.isAntisymmetric = isAntisymmetric;
    }

    /**
	 * Returns the isReflexive.
	 * 
	 * @return the isReflexive
	 */
    public boolean isReflexive() {
        return isReflexive;
    }

    /**
	 * Sets the isReflexive.
	 * 
	 * @param isReflexive
	 *            the new isReflexive
	 */
    public void setReflexive(boolean isReflexive) {
        this.isReflexive = isReflexive;
    }

    /**
	 * Returns the isSymmetric.
	 * 
	 * @return the isSymmetric
	 */
    public boolean isSymmetric() {
        return isSymmetric;
    }

    /**
	 * Sets the isSymmetric.
	 * 
	 * @param isSymmetric
	 *            the new isSymmetric
	 */
    public void setSymmetric(boolean isSymmetric) {
        this.isSymmetric = isSymmetric;
    }

    /**
	 * Returns the isTransitiv.
	 * 
	 * @return the isTransitiv
	 */
    public boolean isTransitiv() {
        return isTransitiv;
    }

    /**
	 * Sets the isTransitiv.
	 * 
	 * @param isTransitiv
	 *            the new isTransitiv
	 */
    public void setTransitiv(boolean isTransitiv) {
        this.isTransitiv = isTransitiv;
    }

    /**
	 * 
	 * 
	 * @return
	 */
    public WSRelationType getSpecialisationOf() {
        return specialisationOf;
    }

    /**
	 * Sets the specialisationOf.
	 * 
	 * @param specialisationOf
	 *            the new specialisationOf
	 */
    public void setSpecialisationOf(WSRelationType specialisationOf) {
        this.specialisationOf = specialisationOf;
    }

    /*********************************************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
