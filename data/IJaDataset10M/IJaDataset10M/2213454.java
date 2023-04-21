package org.dbe.kb.metamodel.qml.contextdeclarations;

/**
 * OperationDefinition object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface OperationDefinition extends javax.jmi.reflect.RefObject {

    /**
     * Returns the value of attribute simpleName.
     * @return Value of attribute simpleName.
     */
    public java.lang.String getSimpleName();

    /**
     * Sets the value of simpleName attribute. See {@link #getSimpleName} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setSimpleName(java.lang.String newValue);

    /**
     * Returns the value of reference parameters.
     * @return Value of reference parameters. Element type: {@link org.dbe.kb.metamodel.qml.ocl.expressions.VariableDeclaration}
     */
    public java.util.List getParameters();

    /**
     * Returns the value of reference type.
     * @return Value of reference type.
     */
    public javax.jmi.model.Classifier getType();

    /**
     * Sets the value of reference type. See {@link #getType} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setType(javax.jmi.model.Classifier newValue);

    /**
     * Returns the value of reference bodyExpression.
     * @return Value of reference bodyExpression.
     */
    public org.dbe.kb.metamodel.qml.ocl.expressions.OclExpression getBodyExpression();

    /**
     * Sets the value of reference bodyExpression. See {@link #getBodyExpression} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setBodyExpression(org.dbe.kb.metamodel.qml.ocl.expressions.OclExpression newValue);
}
