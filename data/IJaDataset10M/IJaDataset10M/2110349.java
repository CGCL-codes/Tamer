package org.openemed.CTSVAPI;

public class ConceptId implements java.io.Serializable {

    private String codeSystem_id;

    private String concept_code;

    public ConceptId() {
    }

    public ConceptId(String codeSystem_id, String concept_code) {
        this.codeSystem_id = codeSystem_id;
        this.concept_code = concept_code;
    }

    /**
     * Gets the codeSystem_id value for this ConceptId.
     *
     * @return codeSystem_id
     */
    public String getCodeSystem_id() {
        return codeSystem_id;
    }

    /**
     * Sets the codeSystem_id value for this ConceptId.
     *
     * @param codeSystem_id
     */
    public void setCodeSystem_id(String codeSystem_id) {
        this.codeSystem_id = codeSystem_id;
    }

    /**
     * Gets the concept_code value for this ConceptId.
     *
     * @return concept_code
     */
    public String getConcept_code() {
        return concept_code;
    }

    /**
     * Sets the concept_code value for this ConceptId.
     *
     * @param concept_code
     */
    public void setConcept_code(String concept_code) {
        this.concept_code = concept_code;
    }

    public String toString() {
        return codeSystem_id + "/" + concept_code;
    }
}
