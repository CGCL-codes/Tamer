package org.dbe.kb.metamodel.scm.bpel;

/**
 * MessageActivity object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface MessageActivity extends org.dbe.kb.metamodel.scm.bpel.Activity {

    /**
     * Returns the value of attribute portType.
     * @return Value of attribute portType.
     */
    public java.lang.String getPortType();

    /**
     * Sets the value of portType attribute. See {@link #getPortType} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setPortType(java.lang.String newValue);

    /**
     * Returns the value of attribute operation.
     * @return Value of attribute operation.
     */
    public java.lang.String getOperation();

    /**
     * Sets the value of operation attribute. See {@link #getOperation} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setOperation(java.lang.String newValue);

    /**
     * Returns the value of reference partnerLink.
     * @return Value of reference partnerLink.
     */
    public org.dbe.kb.metamodel.scm.bpel.PartnerLink getPartnerLink();

    /**
     * Sets the value of reference partnerLink. See {@link #getPartnerLink} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setPartnerLink(org.dbe.kb.metamodel.scm.bpel.PartnerLink newValue);

    /**
     * Returns the value of reference correlations.
     * @return Value of reference correlations. Element type: {@link org.dbe.kb.metamodel.scm.bpel.Correlation}
     */
    public java.util.Collection getCorrelations();
}
