package eu.medeia.caex.model.CAEXClassModelV215.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package;
import eu.medeia.caex.model.CAEXClassModelV215.ChangeMode;
import eu.medeia.caex.model.CAEXClassModelV215.DescriptionType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.impl.DescriptionTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.impl.DescriptionTypeImpl#getChangeMode <em>Change Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescriptionTypeImpl extends EObjectImpl implements DescriptionType {

    /**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected static final String VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected String value = VALUE_EDEFAULT;

    /**
	 * The default value of the '{@link #getChangeMode() <em>Change Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeMode()
	 * @generated
	 * @ordered
	 */
    protected static final ChangeMode CHANGE_MODE_EDEFAULT = ChangeMode.STATE;

    /**
	 * The cached value of the '{@link #getChangeMode() <em>Change Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeMode()
	 * @generated
	 * @ordered
	 */
    protected ChangeMode changeMode = CHANGE_MODE_EDEFAULT;

    /**
	 * This is true if the Change Mode attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean changeModeESet;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DescriptionTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CAEXClassModelV215Package.Literals.DESCRIPTION_TYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CAEXClassModelV215Package.DESCRIPTION_TYPE__VALUE, oldValue, value));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ChangeMode getChangeMode() {
        return changeMode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setChangeMode(ChangeMode newChangeMode) {
        ChangeMode oldChangeMode = changeMode;
        changeMode = newChangeMode == null ? CHANGE_MODE_EDEFAULT : newChangeMode;
        boolean oldChangeModeESet = changeModeESet;
        changeModeESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE, oldChangeMode, changeMode, !oldChangeModeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetChangeMode() {
        ChangeMode oldChangeMode = changeMode;
        boolean oldChangeModeESet = changeModeESet;
        changeMode = CHANGE_MODE_EDEFAULT;
        changeModeESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE, oldChangeMode, CHANGE_MODE_EDEFAULT, oldChangeModeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetChangeMode() {
        return changeModeESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__VALUE:
                return getValue();
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE:
                return getChangeMode();
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
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__VALUE:
                setValue((String) newValue);
                return;
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE:
                setChangeMode((ChangeMode) newValue);
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
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE:
                unsetChangeMode();
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
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case CAEXClassModelV215Package.DESCRIPTION_TYPE__CHANGE_MODE:
                return isSetChangeMode();
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
        result.append(" (value: ");
        result.append(value);
        result.append(", changeMode: ");
        if (changeModeESet) result.append(changeMode); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
