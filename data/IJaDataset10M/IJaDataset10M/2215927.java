package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Claim_LocationEntity;
import com.medcentrex.bridge.interfaces.Claim_LocationEntityData;
import com.medcentrex.bridge.interfaces.Claim_LocationEntityHome;
import com.medcentrex.bridge.interfaces.Claim_LocationEntityPK;
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
 * The Entity bean represents a Claim_LocationEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Claim_LocationEntity"
 *           display-name="Claim_LocationEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Claim_LocationEntity"
 *           local-jndi-name="ejb/com/medcentrex/bridge/Claim_LocationEntityLocal"
 * 			schema="claim_location"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="claim_location"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Claim_LocationEntity findByClaim_Location_ID( java.lang.Integer Claim_Location_ID )"
 * query="SELECT OBJECT(ob) FROM claim_location ob WHERE ob.claim_Location_ID=?1"
 *
 * @jboss:table-name table-name="claim_location"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Claim_LocationEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
	 * Store the data within the provided data object into this bean.
	 * @param Claim_Location The Value Object containing the Claim_LocationEntity values
	 * @ejb:interface-method view-type="remote"
	 **/
    public void setValueObject(Claim_LocationEntityData claim_location) throws InvalidValueException {
        if (claim_location == null) {
            throw new InvalidValueException("object.undefined", "Claim_LocationEntity");
        }
        setClaim_Location_ID(claim_location.getClaim_Location_ID());
        setPlace_ID(claim_location.getPlace_ID());
        setClaim_Block_ID(claim_location.getClaim_Block_ID());
        setClaim_Item_ID(claim_location.getClaim_Item_ID());
        setCustomer_ID(claim_location.getCustomer_ID());
        setLocation_Address1(claim_location.getLocation_Address1());
        setLocation_Address2(claim_location.getLocation_Address2());
        setLocation_City(claim_location.getLocation_City());
        setLocation_Country(claim_location.getLocation_Country());
        setLocation_Name(claim_location.getLocation_Name());
        setLocation_State(claim_location.getLocation_State());
        setLocation_Zip(claim_location.getLocation_Zip());
    }

    /**
	 * Store the data within the provided data object into this bean.
	 * @param Claim_Location The Value Object containing the Claim_LocationEntity values
	 * @ejb:interface-method view-type="remote"
	 **/
    public void updateValueObject(Claim_LocationEntityData claim_location) throws InvalidValueException {
        if (claim_location == null) {
            throw new InvalidValueException("object.undefined", "Claim_LocationEntity");
        }
        setPlace_ID(claim_location.getPlace_ID());
        setClaim_Block_ID(claim_location.getClaim_Block_ID());
        setClaim_Item_ID(claim_location.getClaim_Item_ID());
        setCustomer_ID(claim_location.getCustomer_ID());
        setLocation_Address1(claim_location.getLocation_Address1());
        setLocation_Address2(claim_location.getLocation_Address2());
        setLocation_City(claim_location.getLocation_City());
        setLocation_Country(claim_location.getLocation_Country());
        setLocation_Name(claim_location.getLocation_Name());
        setLocation_State(claim_location.getLocation_State());
        setLocation_Zip(claim_location.getLocation_Zip());
    }

    /**
	 * Create and return a Claim_LocationEntity data object populated with the data from
	 * this bean.
	 * @return Returns a Claim_LocationEntityData object containing the data within this
	 *  bean.
	 * @ejb:interface-method view-type="remote"
	 **/
    public Claim_LocationEntityData getValueObject() {
        Claim_LocationEntityData claim_location = new Claim_LocationEntityData();
        claim_location.setClaim_Location_ID(getClaim_Location_ID());
        claim_location.setPlace_ID(getPlace_ID());
        claim_location.setClaim_Block_ID(getClaim_Block_ID());
        claim_location.setClaim_Item_ID(getClaim_Item_ID());
        claim_location.setCustomer_ID(getCustomer_ID());
        claim_location.setLocation_Address1(getLocation_Address1());
        claim_location.setLocation_Address2(getLocation_Address2());
        claim_location.setLocation_City(getLocation_City());
        claim_location.setLocation_Country(getLocation_Country());
        claim_location.setLocation_Name(getLocation_Name());
        claim_location.setLocation_State(getLocation_State());
        claim_location.setLocation_Zip(getLocation_Zip());
        return claim_location;
    }

    /**
	 * Describes the instance and its content for debugging purpose
	 * @return Debugging information about the instance and its content
	 **/
    public String toString() {
        return "Claim_LocationEntityBean [ " + getValueObject() + " ]";
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
            String sql = "select max(claim_location_id)+1 from claim_location";
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
	 * @jboss:column-name name="Claim_Location_ID"
	 */
    public abstract Integer getClaim_Location_ID();

    public abstract void setClaim_Location_ID(java.lang.Integer Claim_Location_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Place_ID"
	 */
    public abstract Integer getPlace_ID();

    public abstract void setPlace_ID(java.lang.Integer Place_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Claim_Block_ID"
	 */
    public abstract Integer getClaim_Block_ID();

    public abstract void setClaim_Block_ID(java.lang.Integer Claim_Block_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Customer_ID"
	 */
    public abstract Integer getCustomer_ID();

    public abstract void setCustomer_ID(java.lang.Integer Customer_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Claim_Item_ID"
	 */
    public abstract Integer getClaim_Item_ID();

    public abstract void setClaim_Item_ID(java.lang.Integer Claim_Item_ID);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_Name"
	 */
    public abstract String getLocation_Name();

    public abstract void setLocation_Name(java.lang.String Location_Name);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_Address1"
	 */
    public abstract String getLocation_Address1();

    public abstract void setLocation_Address1(java.lang.String Location_Address1);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_Address2"
	 */
    public abstract String getLocation_Address2();

    public abstract void setLocation_Address2(java.lang.String Location_Address2);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_City"
	 */
    public abstract String getLocation_City();

    public abstract void setLocation_City(java.lang.String Location_City);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_State"
	 */
    public abstract String getLocation_State();

    public abstract void setLocation_State(java.lang.String Location_State);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_Zip"
	 */
    public abstract String getLocation_Zip();

    public abstract void setLocation_Zip(java.lang.String Location_Zip);

    /**
	 * @ejb:persistent-field
	 * @jboss:column-name name="Location_Country"
	 */
    public abstract String getLocation_Country();

    public abstract void setLocation_Country(java.lang.String Location_Country);

    /**
	 * Create a Claim_LocationEntity based on the supplied Claim_LocationEntity Value Object.
	 *
	 * @param Claim_Location The data used to create the Claim_LocationEntity.
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
    public Claim_LocationEntityPK ejbCreate(Claim_LocationEntityData Claim_Location) throws InvalidValueException, EJBException, CreateException {
        Claim_LocationEntityData lData = (Claim_LocationEntityData) Claim_Location.clone();
        try {
            lData.setClaim_Location_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Claim_LocationEntityData Claim_Location) {
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
