package tudresden.ocl20.pivot.xocl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import tudresden.ocl20.pivot.xocl.BooleanLiteralExpXS;
import tudresden.ocl20.pivot.xocl.XOCLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Boolean Literal Exp XS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tudresden.ocl20.pivot.xocl.impl.BooleanLiteralExpXSImpl#isBooleanSymbol <em>Boolean Symbol</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BooleanLiteralExpXSImpl extends PrimitiveLiteralExpXSImpl implements BooleanLiteralExpXS {

    /**
   * The default value of the '{@link #isBooleanSymbol() <em>Boolean Symbol</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isBooleanSymbol()
   * @generated
   * @ordered
   */
    protected static final boolean BOOLEAN_SYMBOL_EDEFAULT = false;

    /**
   * The cached value of the '{@link #isBooleanSymbol() <em>Boolean Symbol</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isBooleanSymbol()
   * @generated
   * @ordered
   */
    protected boolean booleanSymbol = BOOLEAN_SYMBOL_EDEFAULT;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected BooleanLiteralExpXSImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return XOCLPackage.Literals.BOOLEAN_LITERAL_EXP_XS;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isBooleanSymbol() {
        return booleanSymbol;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setBooleanSymbol(boolean newBooleanSymbol) {
        boolean oldBooleanSymbol = booleanSymbol;
        booleanSymbol = newBooleanSymbol;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XOCLPackage.BOOLEAN_LITERAL_EXP_XS__BOOLEAN_SYMBOL, oldBooleanSymbol, booleanSymbol));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case XOCLPackage.BOOLEAN_LITERAL_EXP_XS__BOOLEAN_SYMBOL:
                return isBooleanSymbol() ? Boolean.TRUE : Boolean.FALSE;
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
            case XOCLPackage.BOOLEAN_LITERAL_EXP_XS__BOOLEAN_SYMBOL:
                setBooleanSymbol(((Boolean) newValue).booleanValue());
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
            case XOCLPackage.BOOLEAN_LITERAL_EXP_XS__BOOLEAN_SYMBOL:
                setBooleanSymbol(BOOLEAN_SYMBOL_EDEFAULT);
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
            case XOCLPackage.BOOLEAN_LITERAL_EXP_XS__BOOLEAN_SYMBOL:
                return booleanSymbol != BOOLEAN_SYMBOL_EDEFAULT;
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
        result.append(" (booleanSymbol: ");
        result.append(booleanSymbol);
        result.append(')');
        return result.toString();
    }
}
