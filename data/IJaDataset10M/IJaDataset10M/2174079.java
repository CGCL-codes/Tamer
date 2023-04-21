package org.dengues.model.project.impl;

import java.util.Collection;
import org.dengues.model.project.CDCConnType;
import org.dengues.model.project.ConnPropertyAttriType;
import org.dengues.model.project.ConnPropertySetType;
import org.dengues.model.project.ProjectPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conn Property Set Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.dengues.model.project.impl.ConnPropertySetTypeImpl#getKeys <em>Keys</em>}</li>
 *   <li>{@link org.dengues.model.project.impl.ConnPropertySetTypeImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link org.dengues.model.project.impl.ConnPropertySetTypeImpl#getCdcConn <em>Cdc Conn</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnPropertySetTypeImpl extends AbstractProjectObjectImpl implements ConnPropertySetType {

    /**
     * The default value of the '{@link #getKeys() <em>Keys</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeys()
     * @generated
     * @ordered
     */
    protected static final String KEYS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getKeys() <em>Keys</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeys()
     * @generated
     * @ordered
     */
    protected String keys = KEYS_EDEFAULT;

    /**
     * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAttributes()
     * @generated
     * @ordered
     */
    protected EList<ConnPropertyAttriType> attributes;

    /**
     * The cached value of the '{@link #getCdcConn() <em>Cdc Conn</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCdcConn()
     * @generated
     * @ordered
     */
    protected CDCConnType cdcConn;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConnPropertySetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ProjectPackage.Literals.CONN_PROPERTY_SET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getKeys() {
        return keys;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKeys(String newKeys) {
        String oldKeys = keys;
        keys = newKeys;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ProjectPackage.CONN_PROPERTY_SET_TYPE__KEYS, oldKeys, keys));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ConnPropertyAttriType> getAttributes() {
        if (attributes == null) {
            attributes = new EObjectContainmentWithInverseEList<ConnPropertyAttriType>(ConnPropertyAttriType.class, this, ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES, ProjectPackage.CONN_PROPERTY_ATTRI_TYPE__PROPERTY_SET);
        }
        return attributes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CDCConnType getCdcConn() {
        if (cdcConn != null && cdcConn.eIsProxy()) {
            InternalEObject oldCdcConn = (InternalEObject) cdcConn;
            cdcConn = (CDCConnType) eResolveProxy(oldCdcConn);
            if (cdcConn != oldCdcConn) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN, oldCdcConn, cdcConn));
            }
        }
        return cdcConn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CDCConnType basicGetCdcConn() {
        return cdcConn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCdcConn(CDCConnType newCdcConn, NotificationChain msgs) {
        CDCConnType oldCdcConn = cdcConn;
        cdcConn = newCdcConn;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN, oldCdcConn, newCdcConn);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCdcConn(CDCConnType newCdcConn) {
        if (newCdcConn != cdcConn) {
            NotificationChain msgs = null;
            if (cdcConn != null) msgs = ((InternalEObject) cdcConn).eInverseRemove(this, ProjectPackage.CDC_CONN_TYPE__CONN_SET, CDCConnType.class, msgs);
            if (newCdcConn != null) msgs = ((InternalEObject) newCdcConn).eInverseAdd(this, ProjectPackage.CDC_CONN_TYPE__CONN_SET, CDCConnType.class, msgs);
            msgs = basicSetCdcConn(newCdcConn, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN, newCdcConn, newCdcConn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getAttributes()).basicAdd(otherEnd, msgs);
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                if (cdcConn != null) msgs = ((InternalEObject) cdcConn).eInverseRemove(this, ProjectPackage.CDC_CONN_TYPE__CONN_SET, CDCConnType.class, msgs);
                return basicSetCdcConn((CDCConnType) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                return ((InternalEList<?>) getAttributes()).basicRemove(otherEnd, msgs);
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                return basicSetCdcConn(null, msgs);
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
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__KEYS:
                return getKeys();
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                return getAttributes();
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                if (resolve) return getCdcConn();
                return basicGetCdcConn();
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
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__KEYS:
                setKeys((String) newValue);
                return;
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                getAttributes().clear();
                getAttributes().addAll((Collection<? extends ConnPropertyAttriType>) newValue);
                return;
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                setCdcConn((CDCConnType) newValue);
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
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__KEYS:
                setKeys(KEYS_EDEFAULT);
                return;
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                getAttributes().clear();
                return;
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                setCdcConn((CDCConnType) null);
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
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__KEYS:
                return KEYS_EDEFAULT == null ? keys != null : !KEYS_EDEFAULT.equals(keys);
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__ATTRIBUTES:
                return attributes != null && !attributes.isEmpty();
            case ProjectPackage.CONN_PROPERTY_SET_TYPE__CDC_CONN:
                return cdcConn != null;
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
        result.append(" (keys: ");
        result.append(keys);
        result.append(')');
        return result.toString();
    }
}
