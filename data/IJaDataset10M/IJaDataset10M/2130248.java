package com.safi.db.server.config.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.safi.db.server.config.ConfigPackage;
import com.safi.db.server.config.SafiServer;
import com.safi.db.server.config.TelephonySubsystem;
import com.safi.db.server.config.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Safi Server</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#getBindIP <em>Bind IP</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#getManagementPort <em>Management Port</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#getUser <em>User</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#getUsers <em>Users</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#isRunning <em>Running</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#isDebug <em>Debug</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafiServerImpl#getDbPort <em>Db Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SafiServerImpl extends ServerResourceImpl implements SafiServer {

    /**
	 * The default value of the '{@link #getBindIP() <em>Bind IP</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getBindIP()
	 * @generated
	 * @ordered
	 */
    protected static final String BIND_IP_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getBindIP() <em>Bind IP</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getBindIP()
	 * @generated
	 * @ordered
	 */
    protected String bindIP = BIND_IP_EDEFAULT;

    /**
	 * The default value of the '{@link #getManagementPort() <em>Management Port</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getManagementPort()
	 * @generated
	 * @ordered
	 */
    protected static final int MANAGEMENT_PORT_EDEFAULT = 7020;

    /**
	 * The cached value of the '{@link #getManagementPort() <em>Management Port</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getManagementPort()
	 * @generated
	 * @ordered
	 */
    protected int managementPort = MANAGEMENT_PORT_EDEFAULT;

    /**
	 * The cached value of the '{@link #getUser() <em>User</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getUser()
	 * @generated
	 * @ordered
	 */
    protected User user;

    /**
	 * The cached value of the '{@link #getUsers() <em>Users</em>}' containment reference list.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getUsers()
	 * @generated
	 * @ordered
	 */
    protected EList<User> users;

    /**
	 * The default value of the '{@link #isRunning() <em>Running</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isRunning()
	 * @generated
	 * @ordered
	 */
    protected static final boolean RUNNING_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isRunning() <em>Running</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isRunning()
	 * @generated
	 * @ordered
	 */
    protected boolean running = RUNNING_EDEFAULT;

    /**
	 * The default value of the '{@link #isDebug() <em>Debug</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isDebug()
	 * @generated
	 * @ordered
	 */
    protected static final boolean DEBUG_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isDebug() <em>Debug</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isDebug()
	 * @generated
	 * @ordered
	 */
    protected boolean debug = DEBUG_EDEFAULT;

    /**
	 * The default value of the '{@link #getDbPort() <em>Db Port</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getDbPort()
	 * @generated
	 * @ordered
	 */
    protected static final int DB_PORT_EDEFAULT = 7021;

    /**
	 * The cached value of the '{@link #getDbPort() <em>Db Port</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getDbPort()
	 * @generated
	 * @ordered
	 */
    protected int dbPort = DB_PORT_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected SafiServerImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ConfigPackage.Literals.SAFI_SERVER;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String getBindIP() {
        return bindIP;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBindIP(String newBindIP) {
        String oldBindIP = bindIP;
        bindIP = newBindIP;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__BIND_IP, oldBindIP, bindIP));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public int getManagementPort() {
        return managementPort;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setManagementPort(int newManagementPort) {
        int oldManagementPort = managementPort;
        managementPort = newManagementPort;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__MANAGEMENT_PORT, oldManagementPort, managementPort));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public User getUser() {
        return user;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetUser(User newUser, NotificationChain msgs) {
        User oldUser = user;
        user = newUser;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__USER, oldUser, newUser);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUser(User newUser) {
        if (newUser != user) {
            NotificationChain msgs = null;
            if (user != null) msgs = ((InternalEObject) user).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.SAFI_SERVER__USER, null, msgs);
            if (newUser != null) msgs = ((InternalEObject) newUser).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.SAFI_SERVER__USER, null, msgs);
            msgs = basicSetUser(newUser, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__USER, newUser, newUser));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<User> getUsers() {
        if (users == null) {
            users = new EObjectContainmentEList<User>(User.class, this, ConfigPackage.SAFI_SERVER__USERS);
        }
        return users;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isRunning() {
        return running;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRunning(boolean newRunning) {
        boolean oldRunning = running;
        running = newRunning;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__RUNNING, oldRunning, running));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isDebug() {
        return debug;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDebug(boolean newDebug) {
        boolean oldDebug = debug;
        debug = newDebug;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__DEBUG, oldDebug, debug));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public int getDbPort() {
        return dbPort;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDbPort(int newDbPort) {
        int oldDbPort = dbPort;
        dbPort = newDbPort;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFI_SERVER__DB_PORT, oldDbPort, dbPort));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ConfigPackage.SAFI_SERVER__USER:
                return basicSetUser(null, msgs);
            case ConfigPackage.SAFI_SERVER__USERS:
                return ((InternalEList<?>) getUsers()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ConfigPackage.SAFI_SERVER__BIND_IP:
                return getBindIP();
            case ConfigPackage.SAFI_SERVER__MANAGEMENT_PORT:
                return getManagementPort();
            case ConfigPackage.SAFI_SERVER__USER:
                return getUser();
            case ConfigPackage.SAFI_SERVER__USERS:
                return getUsers();
            case ConfigPackage.SAFI_SERVER__RUNNING:
                return isRunning();
            case ConfigPackage.SAFI_SERVER__DEBUG:
                return isDebug();
            case ConfigPackage.SAFI_SERVER__DB_PORT:
                return getDbPort();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ConfigPackage.SAFI_SERVER__BIND_IP:
                setBindIP((String) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__MANAGEMENT_PORT:
                setManagementPort((Integer) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__USER:
                setUser((User) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__USERS:
                getUsers().clear();
                getUsers().addAll((Collection<? extends User>) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__RUNNING:
                setRunning((Boolean) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__DEBUG:
                setDebug((Boolean) newValue);
                return;
            case ConfigPackage.SAFI_SERVER__DB_PORT:
                setDbPort((Integer) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case ConfigPackage.SAFI_SERVER__BIND_IP:
                setBindIP(BIND_IP_EDEFAULT);
                return;
            case ConfigPackage.SAFI_SERVER__MANAGEMENT_PORT:
                setManagementPort(MANAGEMENT_PORT_EDEFAULT);
                return;
            case ConfigPackage.SAFI_SERVER__USER:
                setUser((User) null);
                return;
            case ConfigPackage.SAFI_SERVER__USERS:
                getUsers().clear();
                return;
            case ConfigPackage.SAFI_SERVER__RUNNING:
                setRunning(RUNNING_EDEFAULT);
                return;
            case ConfigPackage.SAFI_SERVER__DEBUG:
                setDebug(DEBUG_EDEFAULT);
                return;
            case ConfigPackage.SAFI_SERVER__DB_PORT:
                setDbPort(DB_PORT_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ConfigPackage.SAFI_SERVER__BIND_IP:
                return BIND_IP_EDEFAULT == null ? bindIP != null : !BIND_IP_EDEFAULT.equals(bindIP);
            case ConfigPackage.SAFI_SERVER__MANAGEMENT_PORT:
                return managementPort != MANAGEMENT_PORT_EDEFAULT;
            case ConfigPackage.SAFI_SERVER__USER:
                return user != null;
            case ConfigPackage.SAFI_SERVER__USERS:
                return users != null && !users.isEmpty();
            case ConfigPackage.SAFI_SERVER__RUNNING:
                return running != RUNNING_EDEFAULT;
            case ConfigPackage.SAFI_SERVER__DEBUG:
                return debug != DEBUG_EDEFAULT;
            case ConfigPackage.SAFI_SERVER__DB_PORT:
                return dbPort != DB_PORT_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (bindIP: ");
        result.append(bindIP);
        result.append(", managementPort: ");
        result.append(managementPort);
        result.append(", running: ");
        result.append(running);
        result.append(", debug: ");
        result.append(debug);
        result.append(", dbPort: ");
        result.append(dbPort);
        result.append(')');
        return result.toString();
    }
}
