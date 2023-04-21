package hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl;

import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.AtomicImplementationNotationPackage;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NExpression;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NSet;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>NSet</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl.NSetImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl.NSetImpl#getExp <em>Exp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NSetImpl extends NImplStepImpl implements NSet {

    /**
   * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTarget()
   * @generated
   * @ordered
   */
    protected NVariable target;

    /**
   * The cached value of the '{@link #getExp() <em>Exp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExp()
   * @generated
   * @ordered
   */
    protected NExpression exp;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected NSetImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return AtomicImplementationNotationPackage.Literals.NSET;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NVariable getTarget() {
        if (target != null && target.eIsProxy()) {
            InternalEObject oldTarget = (InternalEObject) target;
            target = (NVariable) eResolveProxy(oldTarget);
            if (target != oldTarget) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, AtomicImplementationNotationPackage.NSET__TARGET, oldTarget, target));
            }
        }
        return target;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NVariable basicGetTarget() {
        return target;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setTarget(NVariable newTarget) {
        NVariable oldTarget = target;
        target = newTarget;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AtomicImplementationNotationPackage.NSET__TARGET, oldTarget, target));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NExpression getExp() {
        return exp;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetExp(NExpression newExp, NotificationChain msgs) {
        NExpression oldExp = exp;
        exp = newExp;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AtomicImplementationNotationPackage.NSET__EXP, oldExp, newExp);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setExp(NExpression newExp) {
        if (newExp != exp) {
            NotificationChain msgs = null;
            if (exp != null) msgs = ((InternalEObject) exp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AtomicImplementationNotationPackage.NSET__EXP, null, msgs);
            if (newExp != null) msgs = ((InternalEObject) newExp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AtomicImplementationNotationPackage.NSET__EXP, null, msgs);
            msgs = basicSetExp(newExp, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AtomicImplementationNotationPackage.NSET__EXP, newExp, newExp));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AtomicImplementationNotationPackage.NSET__EXP:
                return basicSetExp(null, msgs);
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
            case AtomicImplementationNotationPackage.NSET__TARGET:
                if (resolve) return getTarget();
                return basicGetTarget();
            case AtomicImplementationNotationPackage.NSET__EXP:
                return getExp();
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
            case AtomicImplementationNotationPackage.NSET__TARGET:
                setTarget((NVariable) newValue);
                return;
            case AtomicImplementationNotationPackage.NSET__EXP:
                setExp((NExpression) newValue);
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
            case AtomicImplementationNotationPackage.NSET__TARGET:
                setTarget((NVariable) null);
                return;
            case AtomicImplementationNotationPackage.NSET__EXP:
                setExp((NExpression) null);
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
            case AtomicImplementationNotationPackage.NSET__TARGET:
                return target != null;
            case AtomicImplementationNotationPackage.NSET__EXP:
                return exp != null;
        }
        return super.eIsSet(featureID);
    }
}
