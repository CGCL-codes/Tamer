package com.velocityme.session;

import com.velocityme.utility.InvalidKeyException;
import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import com.velocityme.interfaces.*;
import com.velocityme.valueobjects.*;
import java.rmi.RemoteException;

/**
 *
 * @author  Robert Crida Work
 * @ejb.bean
 *           type="Stateless"
 *           cmp-version="2.x"
 *           name="ContactDetailSession"
 *           jndi-name="ejb/ContactDetailSession"
 *           view-type="local"
 *           transaction-type="Container"
 * @ejb.transaction type="Required"
 *
 * @ejb.util generate="physical"
 */
public class ContactDetailSessionBean implements SessionBean {

    private SessionContext m_context;

    /**
     * Get the full list of contact detail type value objects sorted by the
     * sequence number
     *
     * @param p_keyLocal User's security key
     *
     * @ejb.interface-method view-type="local"
     **/
    public java.util.Collection getAllValueObjects(KeySessionLocal p_keyLocal) throws InvalidKeyException {
        Collection values = new java.util.TreeSet();
        try {
            if (p_keyLocal.isValid()) {
                Collection types = ContactDetailTypeUtil.getLocalHome().findAll();
                Iterator i = types.iterator();
                while (i.hasNext()) {
                    ContactDetailTypeLocal contactDetailTypeLocal = (ContactDetailTypeLocal) i.next();
                    values.add(new ContactDetailTypeValueToString(contactDetailTypeLocal.getContactDetailTypeValue()));
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
     * Sets up the table of contact detail types. Value objects with valid IDs
     * will be edited. Those without valid IDs are new and will be created.
     * Existing types that are not in the collection will be deleted. Note that
     * all contact details of the types deleted will be deleted too.
     *
     * @param p_keyLocal User's security key
     * @param p_values Collection of contact detail type value objects
     *
     * @ejb.interface-method view-type="local"
     **/
    public void setContactDetailTypeValueObjects(KeySessionLocal p_keyLocal, java.util.Collection p_values) throws InvalidKeyException {
        try {
            if (p_keyLocal.isValid()) {
                Iterator i = p_values.iterator();
                Collection existingTypesLocal = ContactDetailTypeUtil.getLocalHome().findAll();
                while (i.hasNext()) {
                    ContactDetailTypeValue value = (ContactDetailTypeValue) i.next();
                    if (value.contactDetailTypeIdHasBeenSet()) {
                        ContactDetailTypeLocal typeLocal = ContactDetailTypeUtil.getLocalHome().findByPrimaryKey(value.getPrimaryKey());
                        typeLocal.setContactDetailTypeValue(value);
                        existingTypesLocal.remove(typeLocal);
                    } else {
                        ContactDetailTypeUtil.getLocalHome().create(value);
                    }
                }
                i = existingTypesLocal.iterator();
                while (i.hasNext()) {
                    ContactDetailTypeLocal typeLocal = (ContactDetailTypeLocal) i.next();
                    ContactDetailTypeUtil.getLocalHome().remove(typeLocal.getPrimaryKey());
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
