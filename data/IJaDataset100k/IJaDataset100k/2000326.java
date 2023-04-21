package fi.hip.gb.gridlib.idff12.database.spfedbase;

import org.sourceid.idff12.adapter.FederationInfo;

/**
 * This interface represents a federation database of the GridSP service.
 *
 * @author Henri Mikkonen
 * @version $Id: FederationDatabase.java,v 1.2 2006/05/22 12:16:02 mikkonen Exp $
 */
public interface FederationDatabase {

    /**
     * Checks whether the user is federated with a provider.
     * @param id The user's identifier as <code>String</code>.
     * @param providerId The provider id as <code>String</code>.
     * @return <code>true</code> if the federation exists, <code>false</code>
     * otherwise.
     */
    public boolean isFederatedWith(String id, String providerId);

    /**
     * Gets all the federation infos by user identifier.
     * @param identifier The user's identifier as <code>String</code>.
     * @return All the federations in an array.
     */
    public FederationInfo[] getFederationInfosByUid(String identifier);

    /**
     * Gets a federation by the name identifer at the IDB.
     * @param nameId The user's pseudonym identifier as <code>String</code>.
     * @param providerId The provider (IDB) id as <code>String</code>.
     * @return The corresponding federation info, null if no federation exists.
     */
    public FederationInfo getFederationInfoByIdp(String nameId, String providerId);

    /**
     * Gets a federation by the name identifer at the SP.
     * @param nameId The user's pseudonym identifier as <code>String</code>.
     * @param providerId The provider (SP) id as <code>String</code>.
     * @return The corresponding federation info, null if no federation exists.
     */
    public FederationInfo getFederationInfoBySp(String nameId, String providerId);

    public FederationInfo addFederation(String ide, String principalId, String providerId, String idpNameId);

    /**
     * Removes a federation.
     * @param principalId The user's identifier as <code>String</code>.
     * @param providerId The provider id as <code>String</code>.
     */
    public void removeFederation(String principalId, String providerId);

    /**
     * Updates the SP's pseudonym id.
     * @param providerId The provider id as <code>String</code>.
     * @param idpNameId The user's IDP pseudonym identifier as 
     * <code>String</code>.
     * @param oldSpNameId The old SP pseudonym identifier as 
     * <code>String</code>.
     * @param newSpNameId The new SP pseudonym identifier as 
     * <code>String</code>.
     */
    public void updateSpNameId(String providerId, String idpNameId, String oldSpNameId, String newSpNameId);

    /**
     * Updates the IDB's pseudonym id.
     * @param providerId The provider id as <code>String</code>.
     * @param oldIdpNameId The old IDB pseudonym identifier as 
     * <code>String</code>.
     * @param newIdpNameId The new IDB pseudonym identifier as 
     * <code>String</code>.
     */
    public void updateIdpNameId(String providerId, String oldIdpNameId, String newIdpNameId);

    /**
     * Gets the allowed proxy certificate subject DN for a specified provider.
     * @param principalId The user's identifer as <code>String</code>.
     * @param providerId The provider identifier as <code>String</code>.
     * @return The authorized proxy certificate subject DN as 
     * <code>String</code>.
     */
    public String getProxyCertificateSubjectDN(String principalId, String providerId);

    /**
     * Sets the allowed proxy certificate subject DN for a specified provider.
     * @param principalId The user's identifer as <code>String</code>.
     * @param providerId The provider identifier as <code>String</code>.
     * @param subjectDN The authorized proxy certificate subject DN as 
     * <code>String</code>.
     */
    public void setProxyCertificateSubjectDN(String principalId, String providerId, String subjectDN);

    /**
     * Gets the user proxy saved in the database.
     * @param identifier The user's identifier as <code>String</code>.
     * @return The Base64-encoded user proxy.
     */
    public String getUserProxy(String identifier);

    public void setUserProxy(String nameId, String providerId, String newProxy);
}
