package org.xtext.example.swrtj.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.xtext.example.swrtj.DottedExpression;
import org.xtext.example.swrtj.Expression;
import org.xtext.example.swrtj.SwrtjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.impl.ExpressionImpl#getSign <em>Sign</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.impl.ExpressionImpl#getTermList <em>Term List</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.impl.ExpressionImpl#getOperatorList <em>Operator List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExpressionImpl extends GenericExpressionImpl implements Expression {

    /**
   * The default value of the '{@link #getSign() <em>Sign</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSign()
   * @generated
   * @ordered
   */
    protected static final String SIGN_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getSign() <em>Sign</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSign()
   * @generated
   * @ordered
   */
    protected String sign = SIGN_EDEFAULT;

    /**
   * The cached value of the '{@link #getTermList() <em>Term List</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTermList()
   * @generated
   * @ordered
   */
    protected EList<DottedExpression> termList;

    /**
   * The cached value of the '{@link #getOperatorList() <em>Operator List</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperatorList()
   * @generated
   * @ordered
   */
    protected EList<String> operatorList;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ExpressionImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return SwrtjPackage.Literals.EXPRESSION;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getSign() {
        return sign;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setSign(String newSign) {
        String oldSign = sign;
        sign = newSign;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SwrtjPackage.EXPRESSION__SIGN, oldSign, sign));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<DottedExpression> getTermList() {
        if (termList == null) {
            termList = new EObjectContainmentEList<DottedExpression>(DottedExpression.class, this, SwrtjPackage.EXPRESSION__TERM_LIST);
        }
        return termList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<String> getOperatorList() {
        if (operatorList == null) {
            operatorList = new EDataTypeEList<String>(String.class, this, SwrtjPackage.EXPRESSION__OPERATOR_LIST);
        }
        return operatorList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SwrtjPackage.EXPRESSION__TERM_LIST:
                return ((InternalEList<?>) getTermList()).basicRemove(otherEnd, msgs);
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
            case SwrtjPackage.EXPRESSION__SIGN:
                return getSign();
            case SwrtjPackage.EXPRESSION__TERM_LIST:
                return getTermList();
            case SwrtjPackage.EXPRESSION__OPERATOR_LIST:
                return getOperatorList();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case SwrtjPackage.EXPRESSION__SIGN:
                setSign((String) newValue);
                return;
            case SwrtjPackage.EXPRESSION__TERM_LIST:
                getTermList().clear();
                getTermList().addAll((Collection<? extends DottedExpression>) newValue);
                return;
            case SwrtjPackage.EXPRESSION__OPERATOR_LIST:
                getOperatorList().clear();
                getOperatorList().addAll((Collection<? extends String>) newValue);
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
            case SwrtjPackage.EXPRESSION__SIGN:
                setSign(SIGN_EDEFAULT);
                return;
            case SwrtjPackage.EXPRESSION__TERM_LIST:
                getTermList().clear();
                return;
            case SwrtjPackage.EXPRESSION__OPERATOR_LIST:
                getOperatorList().clear();
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
            case SwrtjPackage.EXPRESSION__SIGN:
                return SIGN_EDEFAULT == null ? sign != null : !SIGN_EDEFAULT.equals(sign);
            case SwrtjPackage.EXPRESSION__TERM_LIST:
                return termList != null && !termList.isEmpty();
            case SwrtjPackage.EXPRESSION__OPERATOR_LIST:
                return operatorList != null && !operatorList.isEmpty();
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
        result.append(" (sign: ");
        result.append(sign);
        result.append(", operatorList: ");
        result.append(operatorList);
        result.append(')');
        return result.toString();
    }
}
