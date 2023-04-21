package org.dbe.kb.metamodel.scm.bpel;

/**
 * Property object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface Property extends javax.jmi.reflect.RefObject {

    /**
     * Returns the value of attribute name.
     * @return Value of attribute name.
     */
    public java.lang.String getName();

    /**
     * Sets the value of name attribute. See {@link #getName} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setName(java.lang.String newValue);

    /**
     * Returns the value of attribute Type.
     * @return Value of attribute Type.
     */
    public org.dbe.kb.metamodel.scm.types.XsdataType getType();

    /**
     * Sets the value of Type attribute. See {@link #getType} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setType(org.dbe.kb.metamodel.scm.types.XsdataType newValue);
}
