package net.sf.sail.emf.sailuserdata.impl;

import java.util.Collection;
import net.sf.sail.emf.sailuserdata.ESockEntry;
import net.sf.sail.emf.sailuserdata.ESockPart;
import net.sf.sail.emf.sailuserdata.SailuserdataPackage;
import org.doomdark.uuid.UUID;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ESock Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.sail.emf.sailuserdata.impl.ESockPartImpl#getSockEntries <em>Sock Entries</em>}</li>
 *   <li>{@link net.sf.sail.emf.sailuserdata.impl.ESockPartImpl#getPodId <em>Pod Id</em>}</li>
 *   <li>{@link net.sf.sail.emf.sailuserdata.impl.ESockPartImpl#getRimName <em>Rim Name</em>}</li>
 *   <li>{@link net.sf.sail.emf.sailuserdata.impl.ESockPartImpl#getRimShape <em>Rim Shape</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ESockPartImpl extends EObjectImpl implements ESockPart {

    /**
	 * The cached value of the '{@link #getSockEntries() <em>Sock Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSockEntries()
	 * @generated
	 * @ordered
	 */
    protected EList sockEntries;

    /**
	 * The default value of the '{@link #getPodId() <em>Pod Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPodId()
	 * @generated
	 * @ordered
	 */
    protected static final UUID POD_ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPodId() <em>Pod Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPodId()
	 * @generated
	 * @ordered
	 */
    protected UUID podId = POD_ID_EDEFAULT;

    /**
	 * The default value of the '{@link #getRimName() <em>Rim Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRimName()
	 * @generated
	 * @ordered
	 */
    protected static final String RIM_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRimName() <em>Rim Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRimName()
	 * @generated
	 * @ordered
	 */
    protected String rimName = RIM_NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getRimShape() <em>Rim Shape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRimShape()
	 * @generated
	 * @ordered
	 */
    protected static final Class RIM_SHAPE_EDEFAULT = String.class;

    /**
	 * The cached value of the '{@link #getRimShape() <em>Rim Shape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRimShape()
	 * @generated
	 * @ordered
	 */
    protected Class rimShape = RIM_SHAPE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ESockPartImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return SailuserdataPackage.Literals.ESOCK_PART;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getSockEntries() {
        if (sockEntries == null) {
            sockEntries = new EObjectContainmentEList(ESockEntry.class, this, SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES);
        }
        return sockEntries;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UUID getPodId() {
        return podId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPodId(UUID newPodId) {
        UUID oldPodId = podId;
        podId = newPodId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SailuserdataPackage.ESOCK_PART__POD_ID, oldPodId, podId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRimName() {
        return rimName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRimName(String newRimName) {
        String oldRimName = rimName;
        rimName = newRimName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SailuserdataPackage.ESOCK_PART__RIM_NAME, oldRimName, rimName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Class getRimShape() {
        return rimShape;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRimShape(Class newRimShape) {
        Class oldRimShape = rimShape;
        rimShape = newRimShape;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SailuserdataPackage.ESOCK_PART__RIM_SHAPE, oldRimShape, rimShape));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES:
                return ((InternalEList) getSockEntries()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES:
                return getSockEntries();
            case SailuserdataPackage.ESOCK_PART__POD_ID:
                return getPodId();
            case SailuserdataPackage.ESOCK_PART__RIM_NAME:
                return getRimName();
            case SailuserdataPackage.ESOCK_PART__RIM_SHAPE:
                return getRimShape();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES:
                getSockEntries().clear();
                getSockEntries().addAll((Collection) newValue);
                return;
            case SailuserdataPackage.ESOCK_PART__POD_ID:
                setPodId((UUID) newValue);
                return;
            case SailuserdataPackage.ESOCK_PART__RIM_NAME:
                setRimName((String) newValue);
                return;
            case SailuserdataPackage.ESOCK_PART__RIM_SHAPE:
                setRimShape((Class) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES:
                getSockEntries().clear();
                return;
            case SailuserdataPackage.ESOCK_PART__POD_ID:
                setPodId(POD_ID_EDEFAULT);
                return;
            case SailuserdataPackage.ESOCK_PART__RIM_NAME:
                setRimName(RIM_NAME_EDEFAULT);
                return;
            case SailuserdataPackage.ESOCK_PART__RIM_SHAPE:
                setRimShape(RIM_SHAPE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case SailuserdataPackage.ESOCK_PART__SOCK_ENTRIES:
                return sockEntries != null && !sockEntries.isEmpty();
            case SailuserdataPackage.ESOCK_PART__POD_ID:
                return POD_ID_EDEFAULT == null ? podId != null : !POD_ID_EDEFAULT.equals(podId);
            case SailuserdataPackage.ESOCK_PART__RIM_NAME:
                return RIM_NAME_EDEFAULT == null ? rimName != null : !RIM_NAME_EDEFAULT.equals(rimName);
            case SailuserdataPackage.ESOCK_PART__RIM_SHAPE:
                return RIM_SHAPE_EDEFAULT == null ? rimShape != null : !RIM_SHAPE_EDEFAULT.equals(rimShape);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (podId: ");
        result.append(podId);
        result.append(", rimName: ");
        result.append(rimName);
        result.append(", rimShape: ");
        result.append(rimShape);
        result.append(')');
        return result.toString();
    }
}
