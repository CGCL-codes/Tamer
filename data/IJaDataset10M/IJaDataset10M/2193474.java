package org.dbe.kb.metamodel.qml.ocl.expressions;

/**
 * IterateExp object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface IterateExp extends org.dbe.kb.metamodel.qml.ocl.expressions.LoopExp {

    /**
     * Returns the value of reference result.
     * @return Value of reference result.
     */
    public org.dbe.kb.metamodel.qml.ocl.expressions.VariableDeclaration getResult();

    /**
     * Sets the value of reference result. See {@link #getResult} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setResult(org.dbe.kb.metamodel.qml.ocl.expressions.VariableDeclaration newValue);
}
