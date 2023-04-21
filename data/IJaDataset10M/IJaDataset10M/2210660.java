package org.dbe.kb.metamodel.odm.ontology;

/**
 * Ontology class proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface OntologyClass extends javax.jmi.reflect.RefClass {

    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Ontology createOntology();

    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param id 
     * @return The created instance object.
     */
    public Ontology createOntology(java.lang.String name, java.lang.String id);
}
