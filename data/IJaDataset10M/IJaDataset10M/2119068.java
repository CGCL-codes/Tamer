package org.dbe.kb.metamodel.odm.individuals;

/**
 * OPTRange_As association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface OptrangeAs extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param theOpinstance Value of the first association end.
     * @param theOprange Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.odm.individuals.ObjectPropertyThing theOpinstance, org.dbe.kb.metamodel.odm.individuals.ClassThing theOprange);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param theOprange Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getTheOpinstance(org.dbe.kb.metamodel.odm.individuals.ClassThing theOprange);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param theOpinstance Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.odm.individuals.ClassThing getTheOprange(org.dbe.kb.metamodel.odm.individuals.ObjectPropertyThing theOpinstance);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param theOpinstance Value of the first association end.
     * @param theOprange Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.odm.individuals.ObjectPropertyThing theOpinstance, org.dbe.kb.metamodel.odm.individuals.ClassThing theOprange);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param theOpinstance Value of the first association end.
     * @param theOprange Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.odm.individuals.ObjectPropertyThing theOpinstance, org.dbe.kb.metamodel.odm.individuals.ClassThing theOprange);
}
