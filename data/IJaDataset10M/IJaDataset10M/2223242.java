package org.dbe.kb.metamodel.oql.core.expressions;

/**
 * AppliedPropertySource_Assoc association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface AppliedPropertySourceAssoc extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param appliedProperty Value of the first association end.
     * @param source Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.oql.core.expressions.PropertyCallExp appliedProperty, org.dbe.kb.metamodel.oql.core.expressions.OclExpression source);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param source Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.oql.core.expressions.PropertyCallExp getAppliedProperty(org.dbe.kb.metamodel.oql.core.expressions.OclExpression source);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param appliedProperty Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.oql.core.expressions.OclExpression getSource(org.dbe.kb.metamodel.oql.core.expressions.PropertyCallExp appliedProperty);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param appliedProperty Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.oql.core.expressions.PropertyCallExp appliedProperty, org.dbe.kb.metamodel.oql.core.expressions.OclExpression source);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param appliedProperty Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.oql.core.expressions.PropertyCallExp appliedProperty, org.dbe.kb.metamodel.oql.core.expressions.OclExpression source);
}
