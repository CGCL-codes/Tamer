package org.dbe.kb.metamodel.ssl.core;

/**
 * ContactInformation object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface ContactInformation extends org.dbe.kb.metamodel.ssl.core.ServiceConcept {

    /**
     * Returns the value of attribute Type.
     * @return Value of attribute Type.
     */
    public org.dbe.kb.metamodel.ssl.types.TypeUri getType();

    /**
     * Sets the value of Type attribute. See {@link #getType} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setType(org.dbe.kb.metamodel.ssl.types.TypeUri newValue);
}
