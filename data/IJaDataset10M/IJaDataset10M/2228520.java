package edu.tufts.osidimpl.repository.google.global;

/**
 This class holds a set of Query-related properties and provides accessors.  
 Values are initialized from what is passed into the constructor.
 */
public class Query implements edu.tufts.vue.fsm.Query {

    private org.osid.repository.Repository repository = null;

    private java.io.Serializable searchCriteria = null;

    private org.osid.shared.Type searchType = null;

    private org.osid.shared.Properties searchProperties = null;

    private String foreignIdString = null;

    protected Query(String foreignIdString, org.osid.repository.Repository repository, java.io.Serializable searchCriteria, org.osid.shared.Type searchType, org.osid.shared.Properties searchProperties) {
        this.foreignIdString = foreignIdString;
        this.repository = repository;
        this.searchCriteria = searchCriteria;
        this.searchType = searchType;
        this.searchProperties = searchProperties;
    }

    public String getForeignIdString() {
        return this.foreignIdString;
    }

    public void setForeignIdString(String foreignIdString) {
        this.foreignIdString = foreignIdString;
    }

    protected Query(org.osid.repository.Repository repository, java.io.Serializable searchCriteria, org.osid.shared.Type searchType, org.osid.shared.Properties searchProperties) {
        this.repository = repository;
        this.searchCriteria = searchCriteria;
        this.searchType = searchType;
        this.searchProperties = searchProperties;
    }

    public org.osid.repository.Repository getRepository() {
        return this.repository;
    }

    public void setRepository(org.osid.repository.Repository repository) {
        this.repository = repository;
    }

    public java.io.Serializable getSearchCriteria() {
        return this.searchCriteria;
    }

    public void setSearchCriteria(java.io.Serializable searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public org.osid.shared.Type getSearchType() {
        return this.searchType;
    }

    public void setSearchType(org.osid.shared.Type searchType) {
        this.searchType = searchType;
    }

    public org.osid.shared.Properties getSearchProperties() {
        return this.searchProperties;
    }

    public void setSearchProperties(org.osid.shared.Properties searchProperties) {
        this.searchProperties = searchProperties;
    }
}
