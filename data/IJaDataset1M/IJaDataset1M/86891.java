package org.dbe.kb.metamodel.scm.types;

/**
 * Boolean-Expr class proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface BooleanExprClass extends javax.jmi.reflect.RefClass {

    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public BooleanExpr createBooleanExpr();

    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param Expression 
     * @return The created instance object.
     */
    public BooleanExpr createBooleanExpr(java.lang.String expression);
}
