package org.dbe.kb.metamodel.bml.core;

/**
 * Attribute class proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface AttributeClass extends javax.jmi.reflect.RefClass {

    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Attribute createAttribute();

    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param multilpicity 
     * @param type 
     * @return The created instance object.
     */
    public Attribute createAttribute(java.lang.String name, org.dbe.kb.metamodel.bml.businessobject.Multiplicity multilpicity, org.dbe.kb.metamodel.bml.businessobject.BusinessObject type);
}
