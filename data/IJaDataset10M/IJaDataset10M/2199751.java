package tdmodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import tdmodel.LightArgument;
import tdmodel.Parameter;
import tdmodel.TdmodelPackage;
import tdmodel.Value;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Light Argument</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tdmodel.impl.LightArgumentImpl#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link tdmodel.impl.LightArgumentImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LightArgumentImpl extends EObjectImpl implements LightArgument {

    /**
	 * The cached value of the '{@link #getSymbol() <em>Symbol</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSymbol()
	 * @generated
	 * @ordered
	 */
    protected Parameter symbol;

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected Value value;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected LightArgumentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TdmodelPackage.Literals.LIGHT_ARGUMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Parameter getSymbol() {
        if (symbol != null && symbol.eIsProxy()) {
            InternalEObject oldSymbol = (InternalEObject) symbol;
            symbol = (Parameter) eResolveProxy(oldSymbol);
            if (symbol != oldSymbol) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TdmodelPackage.LIGHT_ARGUMENT__SYMBOL, oldSymbol, symbol));
            }
        }
        return symbol;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Parameter basicGetSymbol() {
        return symbol;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSymbol(Parameter newSymbol) {
        Parameter oldSymbol = symbol;
        symbol = newSymbol;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TdmodelPackage.LIGHT_ARGUMENT__SYMBOL, oldSymbol, symbol));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Value getValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValue(Value newValue, NotificationChain msgs) {
        Value oldValue = value;
        value = newValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TdmodelPackage.LIGHT_ARGUMENT__VALUE, oldValue, newValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(Value newValue) {
        if (newValue != value) {
            NotificationChain msgs = null;
            if (value != null) msgs = ((InternalEObject) value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TdmodelPackage.LIGHT_ARGUMENT__VALUE, null, msgs);
            if (newValue != null) msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TdmodelPackage.LIGHT_ARGUMENT__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TdmodelPackage.LIGHT_ARGUMENT__VALUE, newValue, newValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TdmodelPackage.LIGHT_ARGUMENT__VALUE:
                return basicSetValue(null, msgs);
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
            case TdmodelPackage.LIGHT_ARGUMENT__SYMBOL:
                if (resolve) return getSymbol();
                return basicGetSymbol();
            case TdmodelPackage.LIGHT_ARGUMENT__VALUE:
                return getValue();
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
            case TdmodelPackage.LIGHT_ARGUMENT__SYMBOL:
                setSymbol((Parameter) newValue);
                return;
            case TdmodelPackage.LIGHT_ARGUMENT__VALUE:
                setValue((Value) newValue);
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
            case TdmodelPackage.LIGHT_ARGUMENT__SYMBOL:
                setSymbol((Parameter) null);
                return;
            case TdmodelPackage.LIGHT_ARGUMENT__VALUE:
                setValue((Value) null);
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
            case TdmodelPackage.LIGHT_ARGUMENT__SYMBOL:
                return symbol != null;
            case TdmodelPackage.LIGHT_ARGUMENT__VALUE:
                return value != null;
        }
        return super.eIsSet(featureID);
    }
}
