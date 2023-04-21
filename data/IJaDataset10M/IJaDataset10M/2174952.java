package org.dbe.kb.metamodel.upm.core;

/**
 * UserPreferencesAssoc association proxy interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface UserPreferencesAssoc extends javax.jmi.reflect.RefAssociation {

    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param userIdentity Value of the first association end.
     * @param userPreferences Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.dbe.kb.metamodel.upm.core.UserIdentity userIdentity, org.dbe.kb.metamodel.upm.preferences.UserPreference userPreferences);

    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param userPreferences Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.dbe.kb.metamodel.upm.core.UserIdentity getUserIdentity(org.dbe.kb.metamodel.upm.preferences.UserPreference userPreferences);

    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param userIdentity Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getUserPreferences(org.dbe.kb.metamodel.upm.core.UserIdentity userIdentity);

    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param userIdentity Value of the first association end.
     * @param userPreferences Value of the second association end.
     */
    public boolean add(org.dbe.kb.metamodel.upm.core.UserIdentity userIdentity, org.dbe.kb.metamodel.upm.preferences.UserPreference userPreferences);

    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param userIdentity Value of the first association end.
     * @param userPreferences Value of the second association end.
     */
    public boolean remove(org.dbe.kb.metamodel.upm.core.UserIdentity userIdentity, org.dbe.kb.metamodel.upm.preferences.UserPreference userPreferences);
}
