package org.dbe.kb.metamodel.scm.bpel;

/**
 * BPELCatchAll object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface BpelcatchAll extends javax.jmi.reflect.RefObject {

    /**
     * Returns the value of reference compensate.
     * @return Value of reference compensate.
     */
    public org.dbe.kb.metamodel.scm.bpel.Compensate getCompensate();

    /**
     * Sets the value of reference compensate. See {@link #getCompensate} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setCompensate(org.dbe.kb.metamodel.scm.bpel.Compensate newValue);

    /**
     * Returns the value of reference Activity.
     * @return Value of reference Activity. Element type: {@link org.dbe.kb.metamodel.scm.bpel.Activity}
     */
    public java.util.Collection getActivity();
}
