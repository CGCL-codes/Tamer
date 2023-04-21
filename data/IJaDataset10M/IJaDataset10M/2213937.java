package org.dbe.kb.metamodel.scm.bpel;

/**
 * BPELProcess object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface Bpelprocess extends javax.jmi.reflect.RefObject {

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
     * Returns the value of attribute targetNamespace.
     * @return Value of attribute targetNamespace.
     */
    public org.dbe.kb.metamodel.scm.types.AnyUri getTargetNamespace();

    /**
     * Sets the value of targetNamespace attribute. See {@link #getTargetNamespace} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setTargetNamespace(org.dbe.kb.metamodel.scm.types.AnyUri newValue);

    /**
     * Returns the value of attribute queryLanguage.
     * @return Value of attribute queryLanguage.
     */
    public org.dbe.kb.metamodel.scm.types.AnyUri getQueryLanguage();

    /**
     * Sets the value of queryLanguage attribute. See {@link #getQueryLanguage} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setQueryLanguage(org.dbe.kb.metamodel.scm.types.AnyUri newValue);

    /**
     * Returns the value of attribute expressionLanguage.
     * @return Value of attribute expressionLanguage.
     */
    public org.dbe.kb.metamodel.scm.types.AnyUri getExpressionLanguage();

    /**
     * Sets the value of expressionLanguage attribute. See {@link #getExpressionLanguage} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setExpressionLanguage(org.dbe.kb.metamodel.scm.types.AnyUri newValue);

    /**
     * Returns the value of attribute suppressJoinFailure.
     * @return Value of attribute suppressJoinFailure.
     */
    public boolean isSuppressJoinFailure();

    /**
     * Sets the value of suppressJoinFailure attribute. See {@link #isSuppressJoinFailure} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setSuppressJoinFailure(boolean newValue);

    /**
     * Returns the value of attribute enableInstanceCompensation.
     * @return Value of attribute enableInstanceCompensation.
     */
    public boolean isEnableInstanceCompensation();

    /**
     * Sets the value of enableInstanceCompensation attribute. See {@link #isEnableInstanceCompensation} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setEnableInstanceCompensation(boolean newValue);

    /**
     * Returns the value of attribute abstractProcess.
     * @return Value of attribute abstractProcess.
     */
    public boolean isAbstractProcess();

    /**
     * Sets the value of abstractProcess attribute. See {@link #isAbstractProcess} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setAbstractProcess(boolean newValue);

    /**
     * Returns the value of reference correlationSets.
     * @return Value of reference correlationSets. Element type: {@link org.dbe.kb.metamodel.scm.bpel.CorrelationSet}
     */
    public java.util.Collection getCorrelationSets();

    /**
     * Returns the value of reference faultHandlers.
     * @return Value of reference faultHandlers.
     */
    public org.dbe.kb.metamodel.scm.bpel.FaultHandlers getFaultHandlers();

    /**
     * Sets the value of reference faultHandlers. See {@link #getFaultHandlers} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setFaultHandlers(org.dbe.kb.metamodel.scm.bpel.FaultHandlers newValue);

    /**
     * Returns the value of reference partnerLinks.
     * @return Value of reference partnerLinks. Element type: {@link org.dbe.kb.metamodel.scm.bpel.PartnerLink}
     */
    public java.util.Collection getPartnerLinks();

    /**
     * Returns the value of reference Activities.
     * @return Value of reference Activities. Element type: {@link org.dbe.kb.metamodel.scm.bpel.Activity}
     */
    public java.util.Collection getActivities();

    /**
     * Returns the value of reference partners.
     * @return Value of reference partners. Element type: {@link org.dbe.kb.metamodel.scm.bpel.Partner}
     */
    public java.util.Collection getPartners();

    /**
     * Returns the value of reference cHandler.
     * @return Value of reference cHandler.
     */
    public org.dbe.kb.metamodel.scm.bpel.CompensationHandler getCHandler();

    /**
     * Sets the value of reference cHandler. See {@link #getCHandler} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setCHandler(org.dbe.kb.metamodel.scm.bpel.CompensationHandler newValue);

    /**
     * Returns the value of reference eventHandlers.
     * @return Value of reference eventHandlers.
     */
    public org.dbe.kb.metamodel.scm.bpel.EventHandlers getEventHandlers();

    /**
     * Sets the value of reference eventHandlers. See {@link #getEventHandlers} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setEventHandlers(org.dbe.kb.metamodel.scm.bpel.EventHandlers newValue);

    /**
     * Returns the value of reference variables.
     * @return Value of reference variables. Element type: {@link org.dbe.kb.metamodel.scm.bpel.Variable}
     */
    public java.util.Collection getVariables();
}
