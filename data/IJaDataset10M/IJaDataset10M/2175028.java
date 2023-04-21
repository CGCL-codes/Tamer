package org.eclipse.epsilon.fptc.expressions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.fptc.expressions.Expression;
import org.eclipse.epsilon.fptc.expressions.ExpressionsPackage;
import org.eclipse.epsilon.fptc.expressions.FaultBehaviour;
import org.eclipse.epsilon.fptc.expressions.Tuple;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.fptc.expressions.impl.ExpressionImpl#getFaultBehaviour <em>Fault Behaviour</em>}</li>
 *   <li>{@link org.eclipse.epsilon.fptc.expressions.impl.ExpressionImpl#getRhs <em>Rhs</em>}</li>
 *   <li>{@link org.eclipse.epsilon.fptc.expressions.impl.ExpressionImpl#getLhs <em>Lhs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExpressionImpl extends EObjectImpl implements Expression {

    /**
	 * The cached value of the '{@link #getRhs() <em>Rhs</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRhs()
	 * @generated
	 * @ordered
	 */
    protected Tuple rhs;

    /**
	 * The cached value of the '{@link #getLhs() <em>Lhs</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLhs()
	 * @generated
	 * @ordered
	 */
    protected Tuple lhs;

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
        return ExpressionsPackage.Literals.EXPRESSION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FaultBehaviour getFaultBehaviour() {
        if (eContainerFeatureID != ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR) return null;
        return (FaultBehaviour) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFaultBehaviour(FaultBehaviour newFaultBehaviour, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newFaultBehaviour, ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFaultBehaviour(FaultBehaviour newFaultBehaviour) {
        if (newFaultBehaviour != eInternalContainer() || (eContainerFeatureID != ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR && newFaultBehaviour != null)) {
            if (EcoreUtil.isAncestor(this, newFaultBehaviour)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newFaultBehaviour != null) msgs = ((InternalEObject) newFaultBehaviour).eInverseAdd(this, ExpressionsPackage.FAULT_BEHAVIOUR__EXPRESSIONS, FaultBehaviour.class, msgs);
            msgs = basicSetFaultBehaviour(newFaultBehaviour, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR, newFaultBehaviour, newFaultBehaviour));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Tuple getRhs() {
        return rhs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRhs(Tuple newRhs, NotificationChain msgs) {
        Tuple oldRhs = rhs;
        rhs = newRhs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.EXPRESSION__RHS, oldRhs, newRhs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRhs(Tuple newRhs) {
        if (newRhs != rhs) {
            NotificationChain msgs = null;
            if (rhs != null) msgs = ((InternalEObject) rhs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.EXPRESSION__RHS, null, msgs);
            if (newRhs != null) msgs = ((InternalEObject) newRhs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.EXPRESSION__RHS, null, msgs);
            msgs = basicSetRhs(newRhs, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.EXPRESSION__RHS, newRhs, newRhs));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Tuple getLhs() {
        return lhs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLhs(Tuple newLhs, NotificationChain msgs) {
        Tuple oldLhs = lhs;
        lhs = newLhs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.EXPRESSION__LHS, oldLhs, newLhs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLhs(Tuple newLhs) {
        if (newLhs != lhs) {
            NotificationChain msgs = null;
            if (lhs != null) msgs = ((InternalEObject) lhs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.EXPRESSION__LHS, null, msgs);
            if (newLhs != null) msgs = ((InternalEObject) newLhs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.EXPRESSION__LHS, null, msgs);
            msgs = basicSetLhs(newLhs, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.EXPRESSION__LHS, newLhs, newLhs));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetFaultBehaviour((FaultBehaviour) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                return basicSetFaultBehaviour(null, msgs);
            case ExpressionsPackage.EXPRESSION__RHS:
                return basicSetRhs(null, msgs);
            case ExpressionsPackage.EXPRESSION__LHS:
                return basicSetLhs(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID) {
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                return eInternalContainer().eInverseRemove(this, ExpressionsPackage.FAULT_BEHAVIOUR__EXPRESSIONS, FaultBehaviour.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                return getFaultBehaviour();
            case ExpressionsPackage.EXPRESSION__RHS:
                return getRhs();
            case ExpressionsPackage.EXPRESSION__LHS:
                return getLhs();
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
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                setFaultBehaviour((FaultBehaviour) newValue);
                return;
            case ExpressionsPackage.EXPRESSION__RHS:
                setRhs((Tuple) newValue);
                return;
            case ExpressionsPackage.EXPRESSION__LHS:
                setLhs((Tuple) newValue);
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
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                setFaultBehaviour((FaultBehaviour) null);
                return;
            case ExpressionsPackage.EXPRESSION__RHS:
                setRhs((Tuple) null);
                return;
            case ExpressionsPackage.EXPRESSION__LHS:
                setLhs((Tuple) null);
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
            case ExpressionsPackage.EXPRESSION__FAULT_BEHAVIOUR:
                return getFaultBehaviour() != null;
            case ExpressionsPackage.EXPRESSION__RHS:
                return rhs != null;
            case ExpressionsPackage.EXPRESSION__LHS:
                return lhs != null;
        }
        return super.eIsSet(featureID);
    }
}
