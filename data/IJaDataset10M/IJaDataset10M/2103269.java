package org.dbe.kb.metamodel.odm.individuals;

/**
 * distinctMembers_As association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface DistinctMembersAs extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param member Value of the first association end.
     * @param distinctSet Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.odm.individuals.Thing member, org.dbe.kb.metamodel.odm.individuals.AllDifferent distinctSet);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param distinctSet Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMember(org.dbe.kb.metamodel.odm.individuals.AllDifferent distinctSet);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param member Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getDistinctSet(org.dbe.kb.metamodel.odm.individuals.Thing member);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param member Value of the first association end.
     * @param distinctSet Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.odm.individuals.Thing member, org.dbe.kb.metamodel.odm.individuals.AllDifferent distinctSet);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param member Value of the first association end.
     * @param distinctSet Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.odm.individuals.Thing member, org.dbe.kb.metamodel.odm.individuals.AllDifferent distinctSet);
}
