package br.unb.unbiquitous.ubiquitos.uos.ontology;

/**
 * This interface contains the methods related to object properties that could
 * be invoked at the deploy of an application.
 * 
 * @author anaozaki
 */
public interface DeployObjectProperty {

    /**
     * Add object property to ontology. 
     * 
     * @param objectPropertyName Name of the object property.
     */
    public void addObjectProperty(String objectPropertyName);

    /**
     * Add sub object property to ontology. 
     * 
     * @param subObjectPropertyName Name of the sub object property.
     * @param objectPropertyName Name of the object property.
     */
    public void addSubObjectProperty(String subObjectPropertyName, String objectPropertyName);

    /**
     * Add object property domain, which refers to the subject. 
     * 
     * @param objectPropertyName Name of the object property.
     * @param domainName Name of the concept that will be the domain.
     */
    public void addObjectPropertyDomain(String objectPropertyName, String domainName);

    /**
     * Add object property range, which refers to the object. 
     * 
     * @param objectPropertyName Name of the object property.
     * @param rangeName Name of the concept that will be the range.
     */
    public void addObjectPropertyRange(String objectPropertyName, String rangeName);

    /**
     * Set object property as being transitive. 
     * 
     * @param objectPropertyName Name of the object property.
     */
    public void addTransitiveProperty(String objectPropertyName);

    public boolean hasSubObjectProperty(String subObjectPropertyName, String objectPropertyName);

    public boolean hasObjectProperty(String objectPropertyName);

    public boolean hasObjectPropertyRange(String objectPropertyName, String rangeName);

    public boolean hasObjectPropertyDomain(String objectPropertyName, String domainName);

    public boolean hasTransitiveProperty(String objectPropertyName);
}
