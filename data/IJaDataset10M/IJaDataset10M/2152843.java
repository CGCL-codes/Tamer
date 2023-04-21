package org.enml.net.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.enml.geo.CoordinateReferenceSystem;
import org.enml.net.NetPackage;
import org.enml.net.Network;
import org.enml.net.SubNetwork;
import org.enml.subjects.NoiseOwner;
import org.enml.subjects.SubjectsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Network</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.enml.net.impl.NetworkImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.enml.net.impl.NetworkImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.enml.net.impl.NetworkImpl#getCoordinateReferenceSystem <em>Coordinate Reference System</em>}</li>
 *   <li>{@link org.enml.net.impl.NetworkImpl#getSubNetworks <em>Sub Networks</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NetworkImpl extends EObjectImpl implements Network {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "enml.org (C) 2007";

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
	 * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwner()
	 * @generated
	 * @ordered
	 */
    protected NoiseOwner owner;

    /**
	 * The cached value of the '{@link #getCoordinateReferenceSystem() <em>Coordinate Reference System</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoordinateReferenceSystem()
	 * @generated
	 * @ordered
	 */
    protected CoordinateReferenceSystem coordinateReferenceSystem;

    /**
	 * The cached value of the '{@link #getSubNetworks() <em>Sub Networks</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubNetworks()
	 * @generated
	 * @ordered
	 */
    protected EList<SubNetwork> subNetworks;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected NetworkImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return NetPackage.Literals.NETWORK;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NetPackage.NETWORK__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NoiseOwner getOwner() {
        if (owner != null && owner.eIsProxy()) {
            InternalEObject oldOwner = (InternalEObject) owner;
            owner = (NoiseOwner) eResolveProxy(oldOwner);
            if (owner != oldOwner) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, NetPackage.NETWORK__OWNER, oldOwner, owner));
            }
        }
        return owner;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NoiseOwner basicGetOwner() {
        return owner;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOwner(NoiseOwner newOwner, NotificationChain msgs) {
        NoiseOwner oldOwner = owner;
        owner = newOwner;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NetPackage.NETWORK__OWNER, oldOwner, newOwner);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOwner(NoiseOwner newOwner) {
        if (newOwner != owner) {
            NotificationChain msgs = null;
            if (owner != null) msgs = ((InternalEObject) owner).eInverseRemove(this, SubjectsPackage.NOISE_OWNER__NETWORKS, NoiseOwner.class, msgs);
            if (newOwner != null) msgs = ((InternalEObject) newOwner).eInverseAdd(this, SubjectsPackage.NOISE_OWNER__NETWORKS, NoiseOwner.class, msgs);
            msgs = basicSetOwner(newOwner, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NetPackage.NETWORK__OWNER, newOwner, newOwner));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCoordinateReferenceSystem(CoordinateReferenceSystem newCoordinateReferenceSystem, NotificationChain msgs) {
        CoordinateReferenceSystem oldCoordinateReferenceSystem = coordinateReferenceSystem;
        coordinateReferenceSystem = newCoordinateReferenceSystem;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM, oldCoordinateReferenceSystem, newCoordinateReferenceSystem);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem newCoordinateReferenceSystem) {
        if (newCoordinateReferenceSystem != coordinateReferenceSystem) {
            NotificationChain msgs = null;
            if (coordinateReferenceSystem != null) msgs = ((InternalEObject) coordinateReferenceSystem).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM, null, msgs);
            if (newCoordinateReferenceSystem != null) msgs = ((InternalEObject) newCoordinateReferenceSystem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM, null, msgs);
            msgs = basicSetCoordinateReferenceSystem(newCoordinateReferenceSystem, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM, newCoordinateReferenceSystem, newCoordinateReferenceSystem));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<SubNetwork> getSubNetworks() {
        if (subNetworks == null) {
            subNetworks = new EObjectWithInverseResolvingEList<SubNetwork>(SubNetwork.class, this, NetPackage.NETWORK__SUB_NETWORKS, NetPackage.SUB_NETWORK__NETWORK);
        }
        return subNetworks;
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
            case NetPackage.NETWORK__OWNER:
                if (owner != null) msgs = ((InternalEObject) owner).eInverseRemove(this, SubjectsPackage.NOISE_OWNER__NETWORKS, NoiseOwner.class, msgs);
                return basicSetOwner((NoiseOwner) otherEnd, msgs);
            case NetPackage.NETWORK__SUB_NETWORKS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubNetworks()).basicAdd(otherEnd, msgs);
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
            case NetPackage.NETWORK__OWNER:
                return basicSetOwner(null, msgs);
            case NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM:
                return basicSetCoordinateReferenceSystem(null, msgs);
            case NetPackage.NETWORK__SUB_NETWORKS:
                return ((InternalEList<?>) getSubNetworks()).basicRemove(otherEnd, msgs);
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
            case NetPackage.NETWORK__NAME:
                return getName();
            case NetPackage.NETWORK__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM:
                return getCoordinateReferenceSystem();
            case NetPackage.NETWORK__SUB_NETWORKS:
                return getSubNetworks();
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
            case NetPackage.NETWORK__NAME:
                setName((String) newValue);
                return;
            case NetPackage.NETWORK__OWNER:
                setOwner((NoiseOwner) newValue);
                return;
            case NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM:
                setCoordinateReferenceSystem((CoordinateReferenceSystem) newValue);
                return;
            case NetPackage.NETWORK__SUB_NETWORKS:
                getSubNetworks().clear();
                getSubNetworks().addAll((Collection<? extends SubNetwork>) newValue);
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
            case NetPackage.NETWORK__NAME:
                setName(NAME_EDEFAULT);
                return;
            case NetPackage.NETWORK__OWNER:
                setOwner((NoiseOwner) null);
                return;
            case NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM:
                setCoordinateReferenceSystem((CoordinateReferenceSystem) null);
                return;
            case NetPackage.NETWORK__SUB_NETWORKS:
                getSubNetworks().clear();
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
            case NetPackage.NETWORK__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case NetPackage.NETWORK__OWNER:
                return owner != null;
            case NetPackage.NETWORK__COORDINATE_REFERENCE_SYSTEM:
                return coordinateReferenceSystem != null;
            case NetPackage.NETWORK__SUB_NETWORKS:
                return subNetworks != null && !subNetworks.isEmpty();
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
        result.append(')');
        return result.toString();
    }
}
