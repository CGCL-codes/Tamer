package sessionBeans.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import entityBeans.ejb.UserBean;
import entityBeans.ejb.UserCMP;
import entityBeans.interfaces.UserInventoryLocal;
import entityBeans.interfaces.UserLocal;
import entityBeans.interfaces.UserLocalHome;
import fatClient.ServiceLocatorManager;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition --> 
 * @ejb.bean name="Hello"	
 *           description="An EJB named Hello"
 *           display-name="Hello"
 *           jndi-name="Hello"
 *           type="Stateless" 
 *           transaction-type="Container"
 * 
 * <!-- end-xdoclet-definition --> 
 * @generated
 */
public abstract class HelloBean implements javax.ejb.SessionBean {

    private static final String STATIC_UserLocalHome_REF_NAME = "ejb/User";

    private static final Class STATIC_UserLocalHome_CLASS = UserLocalHome.class;

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.create-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
    public void ejbCreate() {
    }

    /** 
	 *
	 * <!-- begin-xdoclet-definition --> 
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition --> 
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
    public String sayHello(String param) throws FinderException {
        String aaa = "";
        Collection usInvCollection = null;
        UserLocalHome userHome = (UserLocalHome) ServiceLocatorManager.getLocalHome(STATIC_UserLocalHome_REF_NAME, STATIC_UserLocalHome_CLASS);
        UserLocal userLocal = null;
        try {
            userLocal = (UserLocal) userHome.findByPrimaryKey(new Integer(1));
        } catch (FinderException e) {
            e.printStackTrace();
        }
        UserInventoryLocal tempUserInventoryLocal = null;
        usInvCollection = userLocal.getUserInventory();
        Iterator it = usInvCollection.iterator();
        while (it.hasNext()) {
            tempUserInventoryLocal = (UserInventoryLocal) it.next();
            aaa = tempUserInventoryLocal.getQuantity().toString();
        }
        return aaa;
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
    }

    /**
	 * 
	 */
    public HelloBean() {
    }
}
