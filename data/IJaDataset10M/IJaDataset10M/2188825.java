package org.dbe.kb.metamodel.odm.datatypes;

/**
 * oneOf_As association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface OneOfAs extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param enumerator Value of the first association end.
     * @param enumeration Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.odm.datatypes.Literal enumerator, org.dbe.kb.metamodel.odm.datatypes.Enumeration enumeration);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param enumeration Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getEnumerator(org.dbe.kb.metamodel.odm.datatypes.Enumeration enumeration);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param enumerator Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getEnumeration(org.dbe.kb.metamodel.odm.datatypes.Literal enumerator);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param enumerator Value of the first association end.
     * @param enumeration Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.odm.datatypes.Literal enumerator, org.dbe.kb.metamodel.odm.datatypes.Enumeration enumeration);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param enumerator Value of the first association end.
     * @param enumeration Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.odm.datatypes.Literal enumerator, org.dbe.kb.metamodel.odm.datatypes.Enumeration enumeration);
}
