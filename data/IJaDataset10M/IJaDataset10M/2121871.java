package org.dbe.kb.metamodel.upm.core;

/**
 * Core package interface.
 *  
 * <p><em><strong>Note:</strong> This type should not be subclassed or implemented 
 * by clients. It is generated from a MOF metamodel and automatically implemented 
 * by MDR (see <a href="http://mdr.netbeans.org/">mdr.netbeans.org</a>).</em></p>
 */
public interface CorePackage extends javax.jmi.reflect.RefPackage {

    /**
     * Returns UserInformation class proxy object.
     * @return UserInformation class proxy object.
     */
    public org.dbe.kb.metamodel.upm.core.UserInformationClass getUserInformation();

    /**
     * Returns UserIdentity class proxy object.
     * @return UserIdentity class proxy object.
     */
    public org.dbe.kb.metamodel.upm.core.UserIdentityClass getUserIdentity();

    /**
     * Returns UserPreferencesAssoc association proxy object.
     * @return UserPreferencesAssoc association proxy object.
     */
    public org.dbe.kb.metamodel.upm.core.UserPreferencesAssoc getUserPreferencesAssoc();

    /**
     * Returns UserIdentityUserInformationAssoc association proxy object.
     * @return UserIdentityUserInformationAssoc association proxy object.
     */
    public org.dbe.kb.metamodel.upm.core.UserIdentityUserInformationAssoc getUserIdentityUserInformationAssoc();
}
