package org.dbe.kb.metamodel.oql.contextdeclarations;

/**
 * OperationParameters_Assoc association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface OperationParametersAssoc extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param operation Value of the first association end.
     * @param parameters Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.oql.contextdeclarations.Operation operation, org.dbe.kb.metamodel.oql.core.expressions.VariableDeclaration parameters);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param parameters Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.oql.contextdeclarations.Operation getOperation(org.dbe.kb.metamodel.oql.core.expressions.VariableDeclaration parameters);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param operation Required value of the first association end.
     * @return List of related objects.
     */
    public java.util.List getParameters(org.dbe.kb.metamodel.oql.contextdeclarations.Operation operation);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param operation Value of the first association end.
     * @param parameters Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.oql.contextdeclarations.Operation operation, org.dbe.kb.metamodel.oql.core.expressions.VariableDeclaration parameters);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param operation Value of the first association end.
     * @param parameters Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.oql.contextdeclarations.Operation operation, org.dbe.kb.metamodel.oql.core.expressions.VariableDeclaration parameters);
}
