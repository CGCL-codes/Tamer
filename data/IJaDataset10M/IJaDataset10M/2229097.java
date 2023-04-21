package hub.sam.lang.mf2b.impl;

import hub.sam.lang.mf2b.Decision;
import hub.sam.lang.mf2b.Evaluation;
import hub.sam.lang.mf2b.Expression;
import hub.sam.lang.mf2b.Mf2bPackage;
import hub.sam.lang.mf2b.Value;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Decision</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.sam.lang.mf2b.impl.DecisionImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.impl.DecisionImpl#getTrueBranch <em>True Branch</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.impl.DecisionImpl#getFalseBranch <em>False Branch</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DecisionImpl extends ExpressionImpl implements Decision {

    /**
     * The cached value of the '{@link #getCondition() <em>Condition</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getCondition()
     * @generated
     * @ordered
     */
    protected Expression condition;

    /**
     * The cached value of the '{@link #getTrueBranch() <em>True Branch</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTrueBranch()
     * @generated
     * @ordered
     */
    protected Expression trueBranch;

    /**
     * The cached value of the '{@link #getFalseBranch() <em>False Branch</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFalseBranch()
     * @generated
     * @ordered
     */
    protected Expression falseBranch;

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    protected DecisionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Mf2bPackage.Literals.DECISION;
    }

    @Override
    public EList<Expression> getOperands() {
        if (operands == null) {
            operands = new DerivedUnionEObjectEList<Expression>(Expression.class, this, Mf2bPackage.DECISION__OPERANDS, new int[] { Mf2bPackage.DECISION__CONDITION, Mf2bPackage.DECISION__TRUE_BRANCH, Mf2bPackage.DECISION__FALSE_BRANCH });
        }
        return operands;
    }

    @Override
    public String getSymbol() {
        return "if";
    }

    @Override
    public Value compute(Evaluation context) {
        Evaluation caseEval = context.getOperands().get(1);
        return (Value) EcoreUtil.copy(caseEval.getValue());
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public Expression getCondition() {
        return condition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCondition(Expression newCondition, NotificationChain msgs) {
        Expression oldCondition = condition;
        condition = newCondition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__CONDITION, oldCondition, newCondition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCondition(Expression newCondition) {
        if (newCondition != condition) {
            NotificationChain msgs = null;
            if (condition != null) msgs = ((InternalEObject) condition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__CONDITION, null, msgs);
            if (newCondition != null) msgs = ((InternalEObject) newCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__CONDITION, null, msgs);
            msgs = basicSetCondition(newCondition, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__CONDITION, newCondition, newCondition));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public Expression getTrueBranch() {
        return trueBranch;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTrueBranch(Expression newTrueBranch, NotificationChain msgs) {
        Expression oldTrueBranch = trueBranch;
        trueBranch = newTrueBranch;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__TRUE_BRANCH, oldTrueBranch, newTrueBranch);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTrueBranch(Expression newTrueBranch) {
        if (newTrueBranch != trueBranch) {
            NotificationChain msgs = null;
            if (trueBranch != null) msgs = ((InternalEObject) trueBranch).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__TRUE_BRANCH, null, msgs);
            if (newTrueBranch != null) msgs = ((InternalEObject) newTrueBranch).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__TRUE_BRANCH, null, msgs);
            msgs = basicSetTrueBranch(newTrueBranch, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__TRUE_BRANCH, newTrueBranch, newTrueBranch));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public Expression getFalseBranch() {
        return falseBranch;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFalseBranch(Expression newFalseBranch, NotificationChain msgs) {
        Expression oldFalseBranch = falseBranch;
        falseBranch = newFalseBranch;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__FALSE_BRANCH, oldFalseBranch, newFalseBranch);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFalseBranch(Expression newFalseBranch) {
        if (newFalseBranch != falseBranch) {
            NotificationChain msgs = null;
            if (falseBranch != null) msgs = ((InternalEObject) falseBranch).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__FALSE_BRANCH, null, msgs);
            if (newFalseBranch != null) msgs = ((InternalEObject) newFalseBranch).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Mf2bPackage.DECISION__FALSE_BRANCH, null, msgs);
            msgs = basicSetFalseBranch(newFalseBranch, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Mf2bPackage.DECISION__FALSE_BRANCH, newFalseBranch, newFalseBranch));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case Mf2bPackage.DECISION__CONDITION:
                return basicSetCondition(null, msgs);
            case Mf2bPackage.DECISION__TRUE_BRANCH:
                return basicSetTrueBranch(null, msgs);
            case Mf2bPackage.DECISION__FALSE_BRANCH:
                return basicSetFalseBranch(null, msgs);
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
            case Mf2bPackage.DECISION__CONDITION:
                return getCondition();
            case Mf2bPackage.DECISION__TRUE_BRANCH:
                return getTrueBranch();
            case Mf2bPackage.DECISION__FALSE_BRANCH:
                return getFalseBranch();
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
            case Mf2bPackage.DECISION__CONDITION:
                setCondition((Expression) newValue);
                return;
            case Mf2bPackage.DECISION__TRUE_BRANCH:
                setTrueBranch((Expression) newValue);
                return;
            case Mf2bPackage.DECISION__FALSE_BRANCH:
                setFalseBranch((Expression) newValue);
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
            case Mf2bPackage.DECISION__CONDITION:
                setCondition((Expression) null);
                return;
            case Mf2bPackage.DECISION__TRUE_BRANCH:
                setTrueBranch((Expression) null);
                return;
            case Mf2bPackage.DECISION__FALSE_BRANCH:
                setFalseBranch((Expression) null);
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
            case Mf2bPackage.DECISION__CONDITION:
                return condition != null;
            case Mf2bPackage.DECISION__TRUE_BRANCH:
                return trueBranch != null;
            case Mf2bPackage.DECISION__FALSE_BRANCH:
                return falseBranch != null;
        }
        return super.eIsSet(featureID);
    }
}
