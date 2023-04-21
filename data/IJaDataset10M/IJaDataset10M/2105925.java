package com.safi.db.impl;

import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import com.safi.db.DBResource;
import com.safi.db.DbPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DB Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.db.impl.DBResourceImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.safi.db.impl.DBResourceImpl#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link com.safi.db.impl.DBResourceImpl#getLastUpdated <em>Last Updated</em>}</li>
 *   <li>{@link com.safi.db.impl.DBResourceImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DBResourceImpl extends EObjectImpl implements DBResource {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getLastModified() <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getLastModified()
	 * @generated
	 * @ordered
	 */
    protected static final Date LAST_MODIFIED_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLastModified() <em>Last Modified</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getLastModified()
	 * @generated
	 * @ordered
	 */
    protected Date lastModified = LAST_MODIFIED_EDEFAULT;

    /**
	 * The default value of the '{@link #getLastUpdated() <em>Last Updated</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getLastUpdated()
	 * @generated
	 * @ordered
	 */
    protected static final Date LAST_UPDATED_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLastUpdated() <em>Last Updated</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getLastUpdated()
	 * @generated
	 * @ordered
	 */
    protected Date lastUpdated = LAST_UPDATED_EDEFAULT;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final int ID_EDEFAULT = -1;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected int id = ID_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected DBResourceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DbPackage.Literals.DB_RESOURCE;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DbPackage.DB_RESOURCE__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getLastModified() {
        return lastModified;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLastModified(Date newLastModified) {
        Date oldLastModified = lastModified;
        lastModified = newLastModified;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DbPackage.DB_RESOURCE__LAST_MODIFIED, oldLastModified, lastModified));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLastUpdated(Date newLastUpdated) {
        Date oldLastUpdated = lastUpdated;
        lastUpdated = newLastUpdated;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DbPackage.DB_RESOURCE__LAST_UPDATED, oldLastUpdated, lastUpdated));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public int getId() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(int newId) {
        int oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DbPackage.DB_RESOURCE__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DbPackage.DB_RESOURCE__NAME:
                return getName();
            case DbPackage.DB_RESOURCE__LAST_MODIFIED:
                return getLastModified();
            case DbPackage.DB_RESOURCE__LAST_UPDATED:
                return getLastUpdated();
            case DbPackage.DB_RESOURCE__ID:
                return getId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DbPackage.DB_RESOURCE__NAME:
                setName((String) newValue);
                return;
            case DbPackage.DB_RESOURCE__LAST_MODIFIED:
                setLastModified((Date) newValue);
                return;
            case DbPackage.DB_RESOURCE__LAST_UPDATED:
                setLastUpdated((Date) newValue);
                return;
            case DbPackage.DB_RESOURCE__ID:
                setId((Integer) newValue);
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
            case DbPackage.DB_RESOURCE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case DbPackage.DB_RESOURCE__LAST_MODIFIED:
                setLastModified(LAST_MODIFIED_EDEFAULT);
                return;
            case DbPackage.DB_RESOURCE__LAST_UPDATED:
                setLastUpdated(LAST_UPDATED_EDEFAULT);
                return;
            case DbPackage.DB_RESOURCE__ID:
                setId(ID_EDEFAULT);
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
            case DbPackage.DB_RESOURCE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case DbPackage.DB_RESOURCE__LAST_MODIFIED:
                return LAST_MODIFIED_EDEFAULT == null ? lastModified != null : !LAST_MODIFIED_EDEFAULT.equals(lastModified);
            case DbPackage.DB_RESOURCE__LAST_UPDATED:
                return LAST_UPDATED_EDEFAULT == null ? lastUpdated != null : !LAST_UPDATED_EDEFAULT.equals(lastUpdated);
            case DbPackage.DB_RESOURCE__ID:
                return id != ID_EDEFAULT;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", lastModified: ");
        result.append(lastModified);
        result.append(", lastUpdated: ");
        result.append(lastUpdated);
        result.append(", id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }
}
