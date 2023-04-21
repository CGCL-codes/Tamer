package org.dbe.kb.metamodel.bml.businessprocess;

/**
 * involves_participant association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface InvolvesParticipant extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param commitment Value of the first association end.
     * @param participant Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.bml.businessprocess.Commitment commitment, org.dbe.kb.metamodel.bml.businessprocess.Role participant);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param participant Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.bml.businessprocess.Commitment getCommitment(org.dbe.kb.metamodel.bml.businessprocess.Role participant);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param commitment Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.bml.businessprocess.Role getParticipant(org.dbe.kb.metamodel.bml.businessprocess.Commitment commitment);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param commitment Value of the first association end.
     * @param participant Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.bml.businessprocess.Commitment commitment, org.dbe.kb.metamodel.bml.businessprocess.Role participant);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param commitment Value of the first association end.
     * @param participant Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.bml.businessprocess.Commitment commitment, org.dbe.kb.metamodel.bml.businessprocess.Role participant);
}
