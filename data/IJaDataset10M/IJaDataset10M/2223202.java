package com.velocityme.session;

import javax.ejb.*;
import com.velocityme.interfaces.KeySessionLocal;
import com.velocityme.interfaces.PermissionLocal;
import com.velocityme.interfaces.PermissionUtil;
import com.velocityme.utility.InvalidKeyException;
import com.velocityme.valueobjects.PermissionValueToString;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;

/**
 *
 * @author  Robert Crida Work
 * @ejb.bean
 *           type="Stateless"
 *           cmp-version="2.x"
 *           name="PermissionSession"
 *           jndi-name="ejb/PermissionSession"
 *           view-type="local"
 *           transaction-type="Container"
 * @ejb.transaction type="Required"
 *
 * @ejb.util generate="physical"
 */
public class PermissionSessionBean implements SessionBean {

    private SessionContext m_context;

    /**
     * Get the full list of permission value objects.
     *
     * @param p_key User's security key
     *
     * @ejb.interface-method view-type="local"
     **/
    public java.util.Set getAllValueObjects(KeySessionLocal p_keyLocal) throws InvalidKeyException {
        Set values = new java.util.TreeSet();
        try {
            if (p_keyLocal.isValid()) {
                Collection permissions = PermissionUtil.getLocalHome().findAll();
                Iterator i = permissions.iterator();
                while (i.hasNext()) {
                    PermissionLocal permissionLocal = (PermissionLocal) i.next();
                    values.add(new PermissionValueToString(permissionLocal.getPermissionValue()));
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
     * Create the Session Bean.
     * @throws CreateException 
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
