package otservices.mapper.translationrepository;

public class OntTranslation {

    private Integer ID = null;

    private Integer ontRelID = null;

    private String fromConcept = null;

    private String toConcept = null;

    /**
	 * Return the ontology translation ID
	 * 
	 * @param none
	 * @return Ontology translation ID
	 */
    public Integer getId() {
        return this.ID;
    }

    /**
	 * Return ontology relation ID
	 * 
	 * @param none
	 * @return Ontology relation ID
	 */
    public Integer getRelationID() {
        return this.ontRelID;
    }

    /**
	 * Return the from concept
	 * 
	 * @param none
	 * @return From concept
	 */
    public String getFromConcept() {
        return this.fromConcept;
    }

    /**
	 * Return the to concept
	 * 
	 * @param none
	 * @return To concept
	 */
    public String getToConcept() {
        return this.toConcept;
    }

    /**
	 * Set the ontology translation ID
	 * 
	 * @param ID
	 *            Ontology translation ID
	 * @return none
	 */
    public void setId(Integer ID) {
        this.ID = ID;
    }

    /**
	 * Set the ontology relation ID
	 * 
	 * @param ID
	 *            Ontology relation ID
	 * @return none
	 */
    public void setRelationID(Integer ontRelID) {
        this.ontRelID = ontRelID;
    }

    /**
	 * Set the from concept
	 * 
	 * @param fromConcept
	 *            From concept
	 * @return none
	 */
    public void setFromConcept(String fromConcept) {
        this.fromConcept = fromConcept;
    }

    /**
	 * Set the to concept
	 * 
	 * @param toConcept
	 *            To concept
	 * @return none
	 */
    public void setToConcept(String toConcept) {
        this.toConcept = toConcept;
    }
}
