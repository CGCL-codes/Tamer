package org.remus.infomngmnt.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.remus.infomngmnt.DynamicStructure;
import org.remus.infomngmnt.InfomngmntPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dynamic Structure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.impl.DynamicStructureImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.remus.infomngmnt.impl.DynamicStructureImpl#getUpperBound <em>Upper Bound</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DynamicStructureImpl extends InformationStructureItemImpl implements DynamicStructure {

    /**
	 * The default value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
    protected static final int LOWER_BOUND_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getLowerBound() <em>Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
    protected int lowerBound = LOWER_BOUND_EDEFAULT;

    /**
	 * The default value of the '{@link #getUpperBound() <em>Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperBound()
	 * @generated
	 * @ordered
	 */
    protected static final int UPPER_BOUND_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getUpperBound() <em>Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperBound()
	 * @generated
	 * @ordered
	 */
    protected int upperBound = UPPER_BOUND_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DynamicStructureImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return InfomngmntPackage.Literals.DYNAMIC_STRUCTURE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLowerBound(int newLowerBound) {
        int oldLowerBound = lowerBound;
        lowerBound = newLowerBound;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, InfomngmntPackage.DYNAMIC_STRUCTURE__LOWER_BOUND, oldLowerBound, lowerBound));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getUpperBound() {
        return upperBound;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpperBound(int newUpperBound) {
        int oldUpperBound = upperBound;
        upperBound = newUpperBound;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, InfomngmntPackage.DYNAMIC_STRUCTURE__UPPER_BOUND, oldUpperBound, upperBound));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case InfomngmntPackage.DYNAMIC_STRUCTURE__LOWER_BOUND:
                return getLowerBound();
            case InfomngmntPackage.DYNAMIC_STRUCTURE__UPPER_BOUND:
                return getUpperBound();
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
            case InfomngmntPackage.DYNAMIC_STRUCTURE__LOWER_BOUND:
                setLowerBound((Integer) newValue);
                return;
            case InfomngmntPackage.DYNAMIC_STRUCTURE__UPPER_BOUND:
                setUpperBound((Integer) newValue);
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
            case InfomngmntPackage.DYNAMIC_STRUCTURE__LOWER_BOUND:
                setLowerBound(LOWER_BOUND_EDEFAULT);
                return;
            case InfomngmntPackage.DYNAMIC_STRUCTURE__UPPER_BOUND:
                setUpperBound(UPPER_BOUND_EDEFAULT);
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
            case InfomngmntPackage.DYNAMIC_STRUCTURE__LOWER_BOUND:
                return lowerBound != LOWER_BOUND_EDEFAULT;
            case InfomngmntPackage.DYNAMIC_STRUCTURE__UPPER_BOUND:
                return upperBound != UPPER_BOUND_EDEFAULT;
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
        result.append(" (lowerBound: ");
        result.append(lowerBound);
        result.append(", upperBound: ");
        result.append(upperBound);
        result.append(')');
        return result.toString();
    }
}
