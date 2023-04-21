package org.dbe.kb.metamodel.qml.ocl.expressions;

/**
 * AttributeCallExp object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface AttributeCallExp extends org.dbe.kb.metamodel.qml.ocl.expressions.ModelPropertyCallExp {

    /**
     * Returns the value of reference referredAttribute.
     * @return Value of reference referredAttribute.
     */
    public javax.jmi.model.Attribute getReferredAttribute();

    /**
     * Sets the value of reference referredAttribute. See {@link #getReferredAttribute} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setReferredAttribute(javax.jmi.model.Attribute newValue);
}
