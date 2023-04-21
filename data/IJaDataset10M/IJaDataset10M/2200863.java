package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Claim_Location_IdentifierEntity;
import com.medcentrex.bridge.interfaces.Claim_Location_IdentifierEntityData;
import com.medcentrex.bridge.interfaces.Claim_Location_IdentifierEntityHome;
import com.medcentrex.bridge.interfaces.Claim_Location_IdentifierEntityPK;
import com.medcentrex.bridge.interfaces.ServiceUnavailableException;
import com.medcentrex.bridge.interfaces.SequenceGenerator;
import com.medcentrex.bridge.interfaces.SequenceGeneratorHome;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

/**
 * The Entity bean represents a Claim_Location_IdentifierEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Claim_Location_IdentifierEntity"
 *           display-name="Claim_Location_IdentifierEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Claim_Location_IdentifierEntity"
 *           local-jndi-name="ejb/com/medcentrex/bridge/Claim_Location_IdentifierEntityLocal"
 * 			schema="Claim_Location_Identifier"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="claim_location_identifier"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Claim_Location_IdentifierEntity findByClaim_Location_Identifier_ID( java.lang.Integer Claim_Location_Identifier_ID )"
 * query="SELECT OBJECT(ob) FROM Claim_Location_Identifier ob WHERE ob.claim_Location_Identifier_ID=?1"
 *
 * @jboss:table-name table-name="claim_location_identifier"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Claim_Location_IdentifierEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
	 * Store the data within the provided data object into this bean.
	 * @param Claim_Location_Identifier The Value Object containing the Claim_Location_IdentifierEntity values
	 * @ejb:interface-method view-type="remote"
	 **/
    public void setValueObject(Claim_Location_IdentifierEntityData claim_location_identifier) throws InvalidValueException {
        if (claim_location_identifier == null) {
            throw new InvalidValueException("object.undefined", "Claim_Location_IdentifierEntity");
        }
        setClaim_Location_Identifier_ID(claim_location_identifier.getClaim_Location_Identifier_ID());
        setClaim_Location_ID(claim_location_identifier.getClaim_Location_ID());
        setIdentifier_Code(claim_location_identifier.getIdentifier_Code());
        setIdentifier(claim_location_identifier.getIdentifier());
    }

    /**
	 * Store the data within the provided data object into this bean.
	 * @param Claim_Location_Identifier The Value Object containing the Claim_Location_IdentifierEntity values
	 * @ejb:interface-method view-type="remote"
	 **/
    public void updateValueObject(Claim_Location_IdentifierEntityData claim_location_identifier) throws InvalidValueException {
        if (claim_location_identifier == null) {
            throw new InvalidValueException("object.undefined", "Claim_Location_IdentifierEntity");
        }
        setClaim_Location_ID(claim_location_identifier.getClaim_Location_ID());
        setIdentifier_Code(claim_location_identifier.getIdentifier_Code());
        setIdentifier(claim_location_identifier.getIdentifier());
    }

    /**
	 * Create and return a Claim_Location_IdentifierEntity data object populated with the data from
	 * this bean.
	 * @return Returns a Claim_Location_IdentifierEntityData object containing the data within this
	 *  bean.
	 * @ejb:interface-method view-type="remote"
	 **/
    public Claim_Location_IdentifierEntityData getValueObject() {
        Claim_Location_IdentifierEntityData claim_location_identifier = new Claim_Location_IdentifierEntityData();
        claim_location_identifier.setClaim_Location_Identifier_ID(getClaim_Location_Identifier_ID());
        claim_location_identifier.setClaim_Location_ID(getClaim_Location_ID());
        claim_location_identifier.setIdentifier_Code(getIdentifier_Code());
        claim_location_identifier.setIdentifier(getIdentifier());
        return claim_location_identifier;
    }

    /**
	 * Describes the instance and its content for debugging purpose
	 * @return Debugging information about the instance and its content
	 **/
    public String toString() {
        return "Claim_Location_IdentifierEntityBean [ " + getValueObject() + " ]";
    }

    /**
	 * Retrive a unique creation id to use for this bean.  This will end up
	 * demarcating this bean from others when it is stored as a record
	 * in the database.
	 * @return Returns an integer that can be used as a unique creation id.
	 * @throws ServiceUnavailableException Indicating that it was not possible
	 *                                     to retrieve a new unqiue ID because
	 *                                     the service is not available
	 **/
    private Integer generateUniqueId() throws ServiceUnavailableException {
        Connection cn = null;
        Statement cmd = null;
        try {
            Context context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:/medcentrex.bridge-DS");
            cn = ds.getConnection();
            cmd = cn.createStatement();
            String sql = "select max(claim_location_identifier_id)+1 from claim_location_identifier";
            ResultSet rs = cmd.executeQuery(sql);
            if (rs.next()) {
                return new Integer(rs.getInt(1));
            } else {
                return new Integer(1);
            }
        } catch (SQLException se) {
            throw new ServiceUnavailableException("Sequence number is broken" + se.getMessage());
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("JNDI Lookup broken");
        } finally {
            try {
                cmd.close();
            } catch (Exception e) {
            }
            try {
                cn.close();
            } catch (Exception e) {
            }
        }
    }

    /**
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name name="Claim_Location_Identifier_ID"
	 */
    public abstract Integer getClaim_Location_Identifier_ID();

    public abstract void setClaim_Location_Identifier_ID(java.lang.Integer Claim_Location_Identifier_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Claim_Location_ID"
	 */
    public abstract Integer getClaim_Location_ID();

    public abstract void setClaim_Location_ID(java.lang.Integer Claim_Location_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Identifier_Code"
	 */
    public abstract String getIdentifier_Code();

    public abstract void setIdentifier_Code(java.lang.String Identifier_Code);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Identifier"
	 */
    public abstract String getIdentifier();

    public abstract void setIdentifier(java.lang.String Identifier);

    /**
	 * Create a Claim_Location_IdentifierEntity based on the supplied Claim_Location_IdentifierEntity Value Object.
	 *
	 * @param Claim_Location_Identifier The data used to create the Claim_Location_IdentifierEntity.
	 *
	 * @throws InvalidValueException If one of the values are not correct,
	 *                               this will not roll back the transaction
	 *                               because the caller has the chance to
	 *                               fix the problem and try again
	 * @throws EJBException If no new unique ID could be retrieved this will
	 *                      rollback the transaction because there is no
	 *                      hope to try again
	 * @throws CreateException Because we have to do so (EJB spec.)
	 *
	 * @ejb:create-method view-type="remote"
	 **/
    public Claim_Location_IdentifierEntityPK ejbCreate(Claim_Location_IdentifierEntityData Claim_Location_Identifier) throws InvalidValueException, EJBException, CreateException {
        Claim_Location_IdentifierEntityData lData = (Claim_Location_IdentifierEntityData) Claim_Location_Identifier.clone();
        try {
            lData.setClaim_Location_Identifier_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Claim_Location_IdentifierEntityData Claim_Location_Identifier) {
    }

    public void setEntityContext(EntityContext lContext) {
        mContext = lContext;
    }

    public void unsetEntityContext() {
        mContext = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() throws RemoveException {
    }
}
