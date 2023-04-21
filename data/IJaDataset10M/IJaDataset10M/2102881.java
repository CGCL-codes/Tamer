package org.dbe.kb.metamodel.qml.ocl.types;

/**
 * BagType class proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface BagTypeClass extends javax.jmi.reflect.RefClass {

    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public BagType createBagType();

    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param annotation 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param visibility 
     * @return The created instance object.
     */
    public BagType createBagType(java.lang.String name, java.lang.String annotation, boolean isRoot, boolean isLeaf, boolean isAbstract, javax.jmi.model.VisibilityKind visibility);
}
