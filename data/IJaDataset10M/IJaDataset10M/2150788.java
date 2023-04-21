package org.dbe.kb.metamodel.scm.bpel;

/**
 * Flow class proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface FlowClass extends javax.jmi.reflect.RefClass {

    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Flow createFlow();

    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param joinCondition 
     * @param suppressJoinFailure 
     * @return The created instance object.
     */
    public Flow createFlow(java.lang.String name, org.dbe.kb.metamodel.scm.types.BooleanExpr joinCondition, boolean suppressJoinFailure);
}
