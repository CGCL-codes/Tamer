package com.medcentrex.session;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;
import com.medcentrex.interfaces.*;
import com.medcentrex.util.DS;

/**
 * Session Bean Template
 *
 * ATTENTION: Some of the XDoclet tags are hidden from XDoclet by
 *            adding a "--" between @ and the namespace. Please remove
 *            this "--" to make it active or add a space to make an
 *            active tag inactive.
 *
 * @ejb:bean name="PanscopicSession"
 *           display-name="Bug PanscopicSession Bean"
 *           type="Stateful"
 *           transaction-type="Container"
 *           jndi-name="ejb/com/medcentrex/PanscopicSession"
 *
 * @ejb:ejb-ref ejb-name="PanscopicSession"
 *              ref-name="com/medcentrex/PanscopicSession"
 *
 * @ejb:ejb-ref ejb-name="MyScopesEntity"
 *              ref-name="com/medcentrex/MyScopesEntity"
 *
 **/
public class PanscopicSessionBean implements SessionBean {

    private SessionContext mContext;

    private Integer mCustomerID;

    /**
   * Create the Session Bean
   *
   * @throws CreateException
   *
   * @ejb:create-method view-type="remote"
   **/
    public void ejbCreate() throws CreateException {
        System.out.println("PanscopicSessionBean.ejbCreate()");
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "PanscopicSessionBean [ " + " ]";
    }

    /**
	* Set the CustomerID
	*
	* @throws RemoteException, ServiceUnavailableException
	*
	* @ejb:interface-method view-type="remote"
	*/
    public void setCustomer_ID(Integer pCustomerID) throws EJBException {
        mCustomerID = pCustomerID;
    }

    /**
	* Get the CustomerID
	*
	* @throws RemoteException, ServiceUnavailableException
	*
	* @ejb:interface-method view-type="remote"
	*/
    public Integer getCustomer_ID() throws EJBException {
        return mCustomerID;
    }

    /**
	 * @ejb:interface-method view-type="remote"
	 */
    public Collection getMyScopes(Integer Person_ID) throws ServiceUnavailableException, RemoteException {
        Connection cn = null;
        Statement cmd = null;
        try {
            Context lContext = new InitialContext();
            cn = DS.getConnection(this.getClass());
            cmd = cn.createStatement();
            ResultSet rs = cmd.executeQuery("SELECT myscopes_id,person_id,uri FROM MyScopes WHERE Person_ID=".toLowerCase() + Person_ID);
            Collection list = (Collection) new Vector();
            while (rs.next()) {
                MyScopesEntityData ob = new MyScopesEntityData();
                ob.setMyScopes_ID(new Integer(rs.getString(1)));
                ob.setPerson_ID(new Integer(rs.getString(2)));
                ob.setURI(rs.getString(3));
                list.add(ob);
            }
            return list;
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("JNDI Lookup broken");
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ServiceUnavailableException("SQL Error");
        } finally {
            try {
                if (cmd != null) {
                    cmd.close();
                }
            } catch (Exception e) {
            }
            try {
                if (cn != null) {
                    DS.closeConnection(cn, this.getClass());
                }
            } catch (Exception e) {
            }
        }
    }

    /**
	 * @ejb:interface-method view-type="remote"
	 */
    public boolean insertScope(String URI, Integer Person_ID) throws ServiceUnavailableException, RemoteException {
        Connection cn = null;
        Statement cmd = null;
        try {
            Context lContext = new InitialContext();
            cn = DS.getConnection(this.getClass());
            cmd = cn.createStatement();
            ResultSet rs = cmd.executeQuery(("SELECT myscopes_id FROM MyScopes WHERE Person_ID=" + Person_ID + " AND URI='" + URI + "'").toLowerCase());
            if (rs.next()) return false;
            InitialContext context = new InitialContext();
            MyScopesEntityHome home = (MyScopesEntityHome) context.lookup(MyScopesEntityHome.JNDI_NAME);
            MyScopesEntityData ob = new MyScopesEntityData();
            ob.setPerson_ID(Person_ID);
            ob.setURI(URI);
            home.create(ob);
            return true;
        } catch (NamingException ne) {
            ne.printStackTrace();
            throw new ServiceUnavailableException("JNDI Lookup broken");
        } catch (CreateException ce) {
            throw new ServiceUnavailableException("Could not insert scope.");
        } catch (InvalidValueException ive) {
            throw new ServiceUnavailableException("Could not insert scope.");
        } catch (SQLException se) {
            se.printStackTrace();
            throw new ServiceUnavailableException("SQL Error");
        } finally {
            try {
                if (cmd != null) {
                    cmd.close();
                }
            } catch (Exception e) {
            }
            try {
                if (cn != null) {
                    DS.closeConnection(cn, this.getClass());
                }
            } catch (Exception e) {
            }
        }
    }

    /**
	 * @ejb:interface-method view-type="remote"
	 */
    public void removeScope(Integer MyScopes_ID) throws ServiceUnavailableException, RemoteException {
        try {
            Context context = new InitialContext();
            MyScopesEntityHome home = (MyScopesEntityHome) context.lookup(MyScopesEntityHome.JNDI_NAME);
            MyScopesEntity entity = home.findByMyScopes_ID(MyScopes_ID);
            entity.remove();
        } catch (NamingException ne) {
            ne.printStackTrace();
            throw new ServiceUnavailableException("JNDI Lookup broken");
        } catch (FinderException fe) {
            throw new RemoteException("Could not remove scope");
        } catch (RemoveException re) {
            throw new RemoteException("Could not remove scope");
        }
    }

    public void setSessionContext(SessionContext aContext) throws EJBException {
        mContext = aContext;
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }
}
