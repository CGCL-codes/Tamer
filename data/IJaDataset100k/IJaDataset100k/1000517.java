package org.authorsite.bib.ejb.entity;

import javax.ejb.*;
import java.util.*;

/**
 * @ejb:bean name="PersonEJB"
 *              type="CMP"
 *              reentrant="false"
 *              cmp-version="2.x"
 *              schema="Person"
 *              primkey-field="personID"
 *              view-type="local"
 *              local-jndi-name="ejb/PersonLocalEJB"
 * @ejb:interface local-class="org.authorsite.bib.ejb.entity.PersonLocal"
 *                generate="local"
 * @ejb:home    local-class="org.authorsite.bib.ejb.entity.PersonLocalHome"
 *              generate="local"
 * @ejb:pk class="java.lang.Integer"
 *
 * @ejb:transaction type="supports"
 *
 * @ejb:finder signature="java.util.Collection findByMainName(java.lang.String mainName)"
 *             query=    "select Object(P) from Person as P
 *                        where P.mainName = ?1"
 *
 * @ejb:finder signature="java.util.Collection findByMainNameAndGivenName(java.lang.String mainName, java.lang.String givenName)"
 *             query=    "select Object(P) from Person as P
 *                        where P.mainName = ?1 and P.givenName = ?2"
 *
 * @ejb:finder signature="java.util.Collection findLikeMainName(java.lang.String mainName)"
 *             query=    "select Object(P) from Person as P
 *                        where P.mainName like '?1' "
 *
 * @jboss:query signature="java.util.Collection findLikeMainName(java.lang.String mainName)"
 *              query=     "select Object(P) from Person as P
 *                        where P.mainName like ?1 "
 *
 * @ejb:finder signature="java.util.Collection findLikeMainNameAndGivenName(java.lang.String mainName, java.lang.String givenName)"
 *             query=    "select Object(P) from Person as P
 *                        where P.mainName like '?1' and P.givenName like '?2'"
 *
 * @jboss:query signature="java.util.Collection findLikeMainNameAndGivenName(java.lang.String mainName, java.lang.String givenName)"
 *             query=    "select Object(P) from Person as P
 *                        where P.mainName like ?1 and P.givenName like ?2"
 *
 * @ejb:finder signature="java.util.Collection findWithoutProductionRels()"
 *             query=    "select Object(P) from Person as P
 *                         where P.mediaProductionRelationships is empty"
 *
 * @ejb:finder  signature="java.util.Collection findWithUnlinkedProductionRels()"
 *              query=    "select Object (P) from Person as P, in (P.mediaProductionRelationships) as MPR
 *                         where MPR.mediaItem is null"
 * 
 * @jboss:table-name table-name="Person"
 * @jboss:create create="false"
 * @jboss:remove remove="false"
 * @jboss:cmp-field field-name="personID" column-name="personID"
 * @jboss:cmp-field field-name="prefix" column-name="prefix"
 * @jboss:cmp-field field-name="givenName" column-name="givenName"
 * @jboss:cmp-field field-name="mainName" column-name="mainName"
 * @jboss:cmp-field field-name="otherNames" column-name="otherNames"
 * @jboss:cmp-field field-name="suffix" column-name="suffix"
 * @jboss:cmp-field field-name="genderCode" column-name="genderCode"
 *
 * @jboss.read-ahead strategy="on-load" page-size="10" eager-load-group="Main"
 *
 * @author  jejking
 * @version $Revision: 1.8 $
 */
public abstract class PersonBean implements EntityBean {

    private EntityContext ctx;

    /**
     * @ejb:create-method
     */
    public Integer ejbCreate(int newPersonID, String newMainName) throws CreateException {
        setPersonID(new Integer(newPersonID));
        setMainName(newMainName);
        return null;
    }

    public void ejbPostCreate(int newPersonID, String newMainName) {
    }

    /**
     * @ejb:create-method
     */
    public Integer ejbCreate(int newPersonID, String newMainName, String newGivenName) throws CreateException {
        setPersonID(new Integer(newPersonID));
        setMainName(newMainName);
        setGivenName(newGivenName);
        return null;
    }

    public void ejbPostCreate(int newPersonID, String newMainName, String newGivenName) {
    }

    /**
     * @ejb:create-method
     */
    public Integer ejbCreate(int newPersonID, String newMainName, String newGivenName, String newOtherNames) throws CreateException {
        setPersonID(new Integer(newPersonID));
        setMainName(newMainName);
        setGivenName(newGivenName);
        setOtherNames(newOtherNames);
        return null;
    }

    public void ejbPostCreate(int newPersonID, String newMainName, String newGivenName, String newOtherNames) {
    }

    /**
     * @ejb:create-method
     */
    public Integer ejbCreate(int newPersonID, String newMainName, String newGivenName, String newOtherNames, String newSuffix) throws CreateException {
        setPersonID(new Integer(newPersonID));
        setMainName(newMainName);
        setGivenName(newGivenName);
        setOtherNames(newOtherNames);
        setSuffix(newSuffix);
        return null;
    }

    public void ejbPostCreate(int newPersonID, String newMainName, String newGivenName, String newOtherNames, String newSuffix) {
    }

    /**
     * @ejb:persistent-field
     * @ejb:pk-field
     * @ejb:interface-method view-type="local"
     */
    public abstract Integer getPersonID();

    /**
     * @ejb:persistent-field
     */
    public abstract void setPersonID(Integer newPersonID);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract String getPrefix();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPrefix(String newPrefix);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract String getGivenName();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setGivenName(String newGivenName);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract String getMainName();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMainName(String newMainName);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract String getOtherNames();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOtherNames(String newOtherNames);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract String getSuffix();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setSuffix(String newSuffix);

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract int getGenderCode();

    /**
     * @ejb:persistent-field
     * @ejb:interface-method view-type="local"
     */
    public abstract void setGenderCode(int newGenderCode);

    /**
     * @ejb:relation    name="productionRelationships-people"
     *                  role-name="people are involved in productionRelationships"
     * @ejb:interface-method view-type="local"
     *
     * @jboss:relation-table table-name="personMediaProdRel"
     * @jboss:relation related-pk-field="mediaProductionRelationshipID" fk-column="mediaProductionRelationshipID"
     */
    public abstract Set getMediaProductionRelationships();

    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMediaProductionRelationships(Set newMediaProductionRelationships);

    /** Creates a new instance of personPersonBean */
    public PersonBean() {
    }

    public void ejbActivate() throws javax.ejb.EJBException {
    }

    public void ejbLoad() throws javax.ejb.EJBException {
    }

    public void ejbPassivate() throws javax.ejb.EJBException {
    }

    public void ejbRemove() throws javax.ejb.RemoveException, javax.ejb.EJBException {
    }

    public void ejbStore() throws javax.ejb.EJBException {
    }

    public void setEntityContext(javax.ejb.EntityContext entityContext) throws javax.ejb.EJBException {
        ctx = entityContext;
    }

    public void unsetEntityContext() throws javax.ejb.EJBException, java.rmi.RemoteException {
        ctx = null;
    }
}
