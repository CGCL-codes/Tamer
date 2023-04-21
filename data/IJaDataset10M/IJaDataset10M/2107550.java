package jp.ekasi.pms.model.impl;

import jp.ekasi.pms.model.ModelPackage;
import jp.ekasi.pms.model.Resource;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link jp.ekasi.pms.model.impl.ResourceImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link jp.ekasi.pms.model.impl.ResourceImpl#getName <em>Name</em>}</li>
 *   <li>{@link jp.ekasi.pms.model.impl.ResourceImpl#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceImpl extends EObjectImpl implements Resource {

    /**
	 * The default value of the '{@link #getUid() <em>Uid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUid()
	 * @generated
	 * @ordered
	 */
    protected static final int UID_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUid()
	 * @generated
	 * @ordered
	 */
    protected int uid = UID_EDEFAULT;

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
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
    protected static final String NOTES_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
    protected String notes = NOTES_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ResourceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.RESOURCE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getUid() {
        return uid;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUid(int newUid) {
        int oldUid = uid;
        uid = newUid;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE__UID, oldUid, uid));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNotes() {
        return notes;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNotes(String newNotes) {
        String oldNotes = notes;
        notes = newNotes;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.RESOURCE__NOTES, oldNotes, notes));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.RESOURCE__UID:
                return new Integer(getUid());
            case ModelPackage.RESOURCE__NAME:
                return getName();
            case ModelPackage.RESOURCE__NOTES:
                return getNotes();
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
            case ModelPackage.RESOURCE__UID:
                setUid(((Integer) newValue).intValue());
                return;
            case ModelPackage.RESOURCE__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.RESOURCE__NOTES:
                setNotes((String) newValue);
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
            case ModelPackage.RESOURCE__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.RESOURCE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.RESOURCE__NOTES:
                setNotes(NOTES_EDEFAULT);
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
            case ModelPackage.RESOURCE__UID:
                return uid != UID_EDEFAULT;
            case ModelPackage.RESOURCE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.RESOURCE__NOTES:
                return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
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
        result.append(" (uid: ");
        result.append(uid);
        result.append(", name: ");
        result.append(name);
        result.append(", notes: ");
        result.append(notes);
        result.append(')');
        return result.toString();
    }
}
