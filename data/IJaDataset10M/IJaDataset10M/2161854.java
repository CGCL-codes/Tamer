package net.sf.copernicus.cclient.model.impl;

import net.sf.copernicus.cclient.model.ModelPackage;
import net.sf.copernicus.cclient.model.Parameter;
import net.sf.copernicus.cclient.model.ValueType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.copernicus.cclient.model.impl.ParameterImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.copernicus.cclient.model.impl.ParameterImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.sf.copernicus.cclient.model.impl.ParameterImpl#isIsIn <em>Is In</em>}</li>
 *   <li>{@link net.sf.copernicus.cclient.model.impl.ParameterImpl#isIsOut <em>Is Out</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterImpl extends EObjectImpl implements Parameter {

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
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final ValueType TYPE_EDEFAULT = ValueType.STRING;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected ValueType type = TYPE_EDEFAULT;

    /**
	 * The default value of the '{@link #isIsIn() <em>Is In</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsIn()
	 * @generated
	 * @ordered
	 */
    protected static final boolean IS_IN_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isIsIn() <em>Is In</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsIn()
	 * @generated
	 * @ordered
	 */
    protected boolean isIn = IS_IN_EDEFAULT;

    /**
	 * The default value of the '{@link #isIsOut() <em>Is Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsOut()
	 * @generated
	 * @ordered
	 */
    protected static final boolean IS_OUT_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isIsOut() <em>Is Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsOut()
	 * @generated
	 * @ordered
	 */
    protected boolean isOut = IS_OUT_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ParameterImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.PARAMETER;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.PARAMETER__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueType getType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(ValueType newType) {
        ValueType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.PARAMETER__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isIsIn() {
        return isIn;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIsIn(boolean newIsIn) {
        boolean oldIsIn = isIn;
        isIn = newIsIn;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.PARAMETER__IS_IN, oldIsIn, isIn));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isIsOut() {
        return isOut;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIsOut(boolean newIsOut) {
        boolean oldIsOut = isOut;
        isOut = newIsOut;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.PARAMETER__IS_OUT, oldIsOut, isOut));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.PARAMETER__NAME:
                return getName();
            case ModelPackage.PARAMETER__TYPE:
                return getType();
            case ModelPackage.PARAMETER__IS_IN:
                return isIsIn() ? Boolean.TRUE : Boolean.FALSE;
            case ModelPackage.PARAMETER__IS_OUT:
                return isIsOut() ? Boolean.TRUE : Boolean.FALSE;
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
            case ModelPackage.PARAMETER__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.PARAMETER__TYPE:
                setType((ValueType) newValue);
                return;
            case ModelPackage.PARAMETER__IS_IN:
                setIsIn(((Boolean) newValue).booleanValue());
                return;
            case ModelPackage.PARAMETER__IS_OUT:
                setIsOut(((Boolean) newValue).booleanValue());
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
            case ModelPackage.PARAMETER__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.PARAMETER__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case ModelPackage.PARAMETER__IS_IN:
                setIsIn(IS_IN_EDEFAULT);
                return;
            case ModelPackage.PARAMETER__IS_OUT:
                setIsOut(IS_OUT_EDEFAULT);
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
            case ModelPackage.PARAMETER__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.PARAMETER__TYPE:
                return type != TYPE_EDEFAULT;
            case ModelPackage.PARAMETER__IS_IN:
                return isIn != IS_IN_EDEFAULT;
            case ModelPackage.PARAMETER__IS_OUT:
                return isOut != IS_OUT_EDEFAULT;
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
        result.append(", type: ");
        result.append(type);
        result.append(", isIn: ");
        result.append(isIn);
        result.append(", isOut: ");
        result.append(isOut);
        result.append(')');
        return result.toString();
    }
}
