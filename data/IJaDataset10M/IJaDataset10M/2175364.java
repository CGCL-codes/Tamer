package org.slaatsoi.business.schema.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.slaatsoi.business.schema.RepeatDailyType;
import org.slaatsoi.business.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repeat Daily Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.slaatsoi.business.schema.impl.RepeatDailyTypeImpl#getRepeatEvery <em>Repeat Every</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RepeatDailyTypeImpl extends PeriodicFrequencyTypeImpl implements RepeatDailyType {

    /**
     * The default value of the '{@link #getRepeatEvery() <em>Repeat Every</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepeatEvery()
     * @generated
     * @ordered
     */
    protected static final int REPEAT_EVERY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getRepeatEvery() <em>Repeat Every</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepeatEvery()
     * @generated
     * @ordered
     */
    protected int repeatEvery = REPEAT_EVERY_EDEFAULT;

    /**
     * This is true if the Repeat Every attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean repeatEveryESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RepeatDailyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SchemaPackage.Literals.REPEAT_DAILY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getRepeatEvery() {
        return repeatEvery;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRepeatEvery(int newRepeatEvery) {
        int oldRepeatEvery = repeatEvery;
        repeatEvery = newRepeatEvery;
        boolean oldRepeatEveryESet = repeatEveryESet;
        repeatEveryESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY, oldRepeatEvery, repeatEvery, !oldRepeatEveryESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetRepeatEvery() {
        int oldRepeatEvery = repeatEvery;
        boolean oldRepeatEveryESet = repeatEveryESet;
        repeatEvery = REPEAT_EVERY_EDEFAULT;
        repeatEveryESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY, oldRepeatEvery, REPEAT_EVERY_EDEFAULT, oldRepeatEveryESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetRepeatEvery() {
        return repeatEveryESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY:
                return getRepeatEvery();
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
            case SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY:
                setRepeatEvery((Integer) newValue);
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
            case SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY:
                unsetRepeatEvery();
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
            case SchemaPackage.REPEAT_DAILY_TYPE__REPEAT_EVERY:
                return isSetRepeatEvery();
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
        result.append(" (repeatEvery: ");
        if (repeatEveryESet) result.append(repeatEvery); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
