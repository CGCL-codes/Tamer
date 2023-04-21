package com.velocityme.session;

import com.velocityme.entity.ChangeDeltaItemTypeBean;
import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import com.velocityme.interfaces.*;
import com.velocityme.utility.InvalidKeyException;
import com.velocityme.valueobjects.*;
import java.rmi.RemoteException;

/**
 *
 * @author  Robert
 * @ejb.bean
 *           type="Stateless"
 *           cmp-version="2.x"
 *           name="SystemPropertySession"
 *           jndi-name="ejb/SystemPropertySession"
 *           view-type="local"
 *           transaction-type="Container"
 * @ejb.transaction type="Required"
 *
 * @ejb.util generate="physical"
 */
public class SystemPropertySessionBean implements SessionBean {

    private SessionContext m_context;

    /**
     * Get the full list of system property value objects.
     *
     * @param p_keyLocal User's security key
     *
     * @ejb.interface-method view-type="local"
     **/
    public java.util.Collection getAllValueObjects(KeySessionLocal p_keyLocal) throws InvalidKeyException {
        Collection values = new java.util.ArrayList();
        try {
            if (p_keyLocal.isValid()) {
                Collection types = SystemPropertyUtil.getLocalHome().findAll();
                Iterator i = types.iterator();
                while (i.hasNext()) {
                    SystemPropertyLocal systemPropertyLocal = (SystemPropertyLocal) i.next();
                    values.add(systemPropertyLocal.getSystemPropertyValue());
                }
            } else throw new InvalidKeyException();
        } catch (NamingException e) {
            throw new EJBException(e);
        } catch (FinderException e) {
            throw new EJBException(e);
        }
        return values;
    }

    /**
     * Sets up the table of system property types. Value objects with valid IDs
     * will be edited. Those without valid IDs are new and will be created.
     * Existing types that are not in the collection will be deleted.
     *
     * @param p_keyLocal User's security key
     * @param p_values Collection of system property value objects
     *
     * @ejb.interface-method view-type="local"
     **/
    public void setSystemPropertyValueObjects(KeySessionLocal p_keyLocal, java.util.Collection p_values) throws InvalidKeyException {
        try {
            if (p_keyLocal.isValid()) {
                Iterator i = p_values.iterator();
                Collection existingPropertiesLocal = SystemPropertyUtil.getLocalHome().findAll();
                while (i.hasNext()) {
                    SystemPropertyValue value = (SystemPropertyValue) i.next();
                    if (value.systemPropertyIdHasBeenSet()) {
                        SystemPropertyLocal valueLocal = SystemPropertyUtil.getLocalHome().findByPrimaryKey(value.getPrimaryKey());
                        if (valueLocal.getIsEditable()) valueLocal.setSystemPropertyValue(value);
                        existingPropertiesLocal.remove(valueLocal);
                    } else {
                        SystemPropertyUtil.getLocalHome().create(value);
                    }
                }
                i = existingPropertiesLocal.iterator();
                while (i.hasNext()) {
                    SystemPropertyLocal valueLocal = (SystemPropertyLocal) i.next();
                    SystemPropertyUtil.getLocalHome().remove(valueLocal.getPrimaryKey());
                }
            } else throw new InvalidKeyException();
        } catch (NamingException e) {
            throw new EJBException(e);
        } catch (FinderException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        } catch (RemoveException e) {
            throw new EJBException(e);
        }
    }

    /**
     * Create the Session Bean.
     * @throws CreateException 
     * @ejb:create-method view-type="remote"
     */
    public void ejbCreate() throws CreateException {
    }

    public void ejbActivate() throws java.rmi.RemoteException {
    }

    public void ejbPassivate() throws java.rmi.RemoteException {
    }

    public void ejbRemove() throws java.rmi.RemoteException {
    }

    public void setSessionContext(javax.ejb.SessionContext sessionContext) throws java.rmi.RemoteException {
        m_context = sessionContext;
    }
}
