package org.dbe.kb.metamodel.scm.bpel;

/**
 * Source object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface Source extends javax.jmi.reflect.RefObject {

    /**
     * Returns the value of attribute transitionCondition.
     * @return Value of attribute transitionCondition.
     */
    public org.dbe.kb.metamodel.scm.types.BooleanExpr getTransitionCondition();

    /**
     * Sets the value of transitionCondition attribute. See {@link #getTransitionCondition} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setTransitionCondition(org.dbe.kb.metamodel.scm.types.BooleanExpr newValue);

    /**
     * Returns the value of reference linkName.
     * @return Value of reference linkName.
     */
    public org.dbe.kb.metamodel.scm.bpel.Link getLinkName();

    /**
     * Sets the value of reference linkName. See {@link #getLinkName} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setLinkName(org.dbe.kb.metamodel.scm.bpel.Link newValue);
}
