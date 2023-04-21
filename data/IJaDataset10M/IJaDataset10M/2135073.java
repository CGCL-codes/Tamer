package library.impl;

import library.LibraryPackage;
import library.Renter;
import library.RenterState;
import library.Restriction;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Renter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link library.impl.RenterImpl#getName <em>Name</em>}</li>
 * <li>{@link library.impl.RenterImpl#getQuota <em>Quota</em>}</li>
 * <li>{@link library.impl.RenterImpl#getRenterState <em>Renter State</em>}</li>
 * <li>{@link library.impl.RenterImpl#getRestriction <em>Restriction</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class RenterImpl extends EObjectImpl implements Renter {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getQuota() <em>Quota</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getQuota()
	 * @generated
	 * @ordered
	 */
    protected static final int QUOTA_EDEFAULT = 3;

    /**
	 * The cached value of the '{@link #getQuota() <em>Quota</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getQuota()
	 * @generated
	 * @ordered
	 */
    protected int quota = QUOTA_EDEFAULT;

    /**
	 * The default value of the '{@link #getRenterState() <em>Renter State</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRenterState()
	 * @generated
	 * @ordered
	 */
    protected static final RenterState RENTER_STATE_EDEFAULT = RenterState.ACTIVATED;

    /**
	 * The cached value of the '{@link #getRenterState() <em>Renter State</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRenterState()
	 * @generated
	 * @ordered
	 */
    protected RenterState renterState = RENTER_STATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getRestriction() <em>Restriction</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRestriction()
	 * @generated
	 * @ordered
	 */
    protected Restriction restriction;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected RenterImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected EClass eStaticClass() {
        return LibraryPackage.Literals.RENTER;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LibraryPackage.RENTER__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public int getQuota() {
        return quota;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setQuota(int newQuota) {
        int oldQuota = quota;
        quota = newQuota;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LibraryPackage.RENTER__QUOTA, oldQuota, quota));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public RenterState getRenterState() {
        return renterState;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setRenterState(RenterState newRenterState) {
        RenterState oldRenterState = renterState;
        renterState = newRenterState == null ? RENTER_STATE_EDEFAULT : newRenterState;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LibraryPackage.RENTER__RENTER_STATE, oldRenterState, renterState));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public Restriction getRestriction() {
        return restriction;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public NotificationChain basicSetRestriction(Restriction newRestriction, NotificationChain msgs) {
        Restriction oldRestriction = restriction;
        restriction = newRestriction;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LibraryPackage.RENTER__RESTRICTION, oldRestriction, newRestriction);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setRestriction(Restriction newRestriction) {
        if (newRestriction != restriction) {
            NotificationChain msgs = null;
            if (restriction != null) msgs = ((InternalEObject) restriction).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LibraryPackage.RENTER__RESTRICTION, null, msgs);
            if (newRestriction != null) msgs = ((InternalEObject) newRestriction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LibraryPackage.RENTER__RESTRICTION, null, msgs);
            msgs = basicSetRestriction(newRestriction, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LibraryPackage.RENTER__RESTRICTION, newRestriction, newRestriction));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case LibraryPackage.RENTER__RESTRICTION:
                return basicSetRestriction(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case LibraryPackage.RENTER__NAME:
                return getName();
            case LibraryPackage.RENTER__QUOTA:
                return new Integer(getQuota());
            case LibraryPackage.RENTER__RENTER_STATE:
                return getRenterState();
            case LibraryPackage.RENTER__RESTRICTION:
                return getRestriction();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case LibraryPackage.RENTER__NAME:
                setName((String) newValue);
                return;
            case LibraryPackage.RENTER__QUOTA:
                setQuota(((Integer) newValue).intValue());
                return;
            case LibraryPackage.RENTER__RENTER_STATE:
                setRenterState((RenterState) newValue);
                return;
            case LibraryPackage.RENTER__RESTRICTION:
                setRestriction((Restriction) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case LibraryPackage.RENTER__NAME:
                setName(NAME_EDEFAULT);
                return;
            case LibraryPackage.RENTER__QUOTA:
                setQuota(QUOTA_EDEFAULT);
                return;
            case LibraryPackage.RENTER__RENTER_STATE:
                setRenterState(RENTER_STATE_EDEFAULT);
                return;
            case LibraryPackage.RENTER__RESTRICTION:
                setRestriction((Restriction) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case LibraryPackage.RENTER__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case LibraryPackage.RENTER__QUOTA:
                return quota != QUOTA_EDEFAULT;
            case LibraryPackage.RENTER__RENTER_STATE:
                return renterState != RENTER_STATE_EDEFAULT;
            case LibraryPackage.RENTER__RESTRICTION:
                return restriction != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(", quota: ");
        result.append(quota);
        result.append(", renterState: ");
        result.append(renterState);
        result.append(')');
        return result.toString();
    }
}
