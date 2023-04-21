package org.dbe.kb.metamodel.qml.ocl.types;

/**
 * TupleType object instance interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface TupleType extends javax.jmi.model.DataType {

    /**
     * Returns the value of reference variables.
     * @return Value of reference variables. Element type: {@link org.dbe.kb.metamodel.qml.ocl.expressions.VariableDeclaration}
     */
    public java.util.List getVariables();
}
