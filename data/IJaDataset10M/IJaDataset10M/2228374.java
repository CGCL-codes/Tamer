package odm.impl;

import odm.Description;
import odm.OdmPackage;
import odm.SubClassOf;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sub Class Of</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.SubClassOfImpl#getSubclass <em>Subclass</em>}</li>
 *   <li>{@link odm.impl.SubClassOfImpl#getSuperclass <em>Superclass</em>}</li>
 *   <li>{@link odm.impl.SubClassOfImpl#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SubClassOfImpl extends ClassAxiomImpl implements SubClassOf {

    /**
	 * The cached value of the '{@link #getSubclass() <em>Subclass</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubclass()
	 * @generated
	 * @ordered
	 */
    protected Description subclass = null;

    /**
	 * The cached value of the '{@link #getSuperclass() <em>Superclass</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSuperclass()
	 * @generated
	 * @ordered
	 */
    protected Description superclass = null;

    /**
	 * The default value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected static final String TAG_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected String tag = TAG_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SubClassOfImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.SUB_CLASS_OF;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description getSubclass() {
        if (subclass != null && subclass.eIsProxy()) {
            InternalEObject oldSubclass = (InternalEObject) subclass;
            subclass = (Description) eResolveProxy(oldSubclass);
            if (subclass != oldSubclass) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.SUB_CLASS_OF__SUBCLASS, oldSubclass, subclass));
            }
        }
        return subclass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description basicGetSubclass() {
        return subclass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSubclass(Description newSubclass) {
        Description oldSubclass = subclass;
        subclass = newSubclass;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.SUB_CLASS_OF__SUBCLASS, oldSubclass, subclass));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description getSuperclass() {
        if (superclass != null && superclass.eIsProxy()) {
            InternalEObject oldSuperclass = (InternalEObject) superclass;
            superclass = (Description) eResolveProxy(oldSuperclass);
            if (superclass != oldSuperclass) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.SUB_CLASS_OF__SUPERCLASS, oldSuperclass, superclass));
            }
        }
        return superclass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description basicGetSuperclass() {
        return superclass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSuperclass(Description newSuperclass) {
        Description oldSuperclass = superclass;
        superclass = newSuperclass;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.SUB_CLASS_OF__SUPERCLASS, oldSuperclass, superclass));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTag(String newTag) {
        String oldTag = tag;
        tag = newTag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.SUB_CLASS_OF__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.SUB_CLASS_OF__SUBCLASS:
                if (resolve) return getSubclass();
                return basicGetSubclass();
            case OdmPackage.SUB_CLASS_OF__SUPERCLASS:
                if (resolve) return getSuperclass();
                return basicGetSuperclass();
            case OdmPackage.SUB_CLASS_OF__TAG:
                return getTag();
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
            case OdmPackage.SUB_CLASS_OF__SUBCLASS:
                setSubclass((Description) newValue);
                return;
            case OdmPackage.SUB_CLASS_OF__SUPERCLASS:
                setSuperclass((Description) newValue);
                return;
            case OdmPackage.SUB_CLASS_OF__TAG:
                setTag((String) newValue);
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
            case OdmPackage.SUB_CLASS_OF__SUBCLASS:
                setSubclass((Description) null);
                return;
            case OdmPackage.SUB_CLASS_OF__SUPERCLASS:
                setSuperclass((Description) null);
                return;
            case OdmPackage.SUB_CLASS_OF__TAG:
                setTag(TAG_EDEFAULT);
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
            case OdmPackage.SUB_CLASS_OF__SUBCLASS:
                return subclass != null;
            case OdmPackage.SUB_CLASS_OF__SUPERCLASS:
                return superclass != null;
            case OdmPackage.SUB_CLASS_OF__TAG:
                return TAG_EDEFAULT == null ? tag != null : !TAG_EDEFAULT.equals(tag);
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
        result.append(" (tag: ");
        result.append(tag);
        result.append(')');
        return result.toString();
    }

    public boolean equals(Object o) {
        if ((o instanceof SubClassOfImpl) && ((SubClassOfImpl) o).getSuperclass().equals(this.getSuperclass()) && (((SubClassOfImpl) o).getSubclass() == null || this.getSubclass() == null || ((SubClassOfImpl) o).getSubclass().equals(this.getSubclass()))) return true; else return false;
    }
}
