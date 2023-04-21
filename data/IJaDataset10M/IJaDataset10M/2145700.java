package org.eclipse.epsilon.dom.eol.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.epsilon.dom.eol.EolPackage;
import org.eclipse.epsilon.dom.eol.Expression;
import org.eclipse.epsilon.dom.eol.RangeCollectionExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Collection Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.dom.eol.impl.RangeCollectionExpressionImpl#getLowerBound <em>Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.epsilon.dom.eol.impl.RangeCollectionExpressionImpl#getUpperBound <em>Upper Bound</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeCollectionExpressionImpl extends CollectionExpressionImpl implements RangeCollectionExpression {

    /**
	 * The cached value of the '{@link #getLowerBound() <em>Lower Bound</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowerBound()
	 * @generated
	 * @ordered
	 */
    protected Expression lowerBound;

    /**
	 * The cached value of the '{@link #getUpperBound() <em>Upper Bound</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperBound()
	 * @generated
	 * @ordered
	 */
    protected Expression upperBound;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RangeCollectionExpressionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EolPackage.Literals.RANGE_COLLECTION_EXPRESSION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Expression getLowerBound() {
        return lowerBound;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLowerBound(Expression newLowerBound, NotificationChain msgs) {
        Expression oldLowerBound = lowerBound;
        lowerBound = newLowerBound;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND, oldLowerBound, newLowerBound);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLowerBound(Expression newLowerBound) {
        if (newLowerBound != lowerBound) {
            NotificationChain msgs = null;
            if (lowerBound != null) msgs = ((InternalEObject) lowerBound).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND, null, msgs);
            if (newLowerBound != null) msgs = ((InternalEObject) newLowerBound).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND, null, msgs);
            msgs = basicSetLowerBound(newLowerBound, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND, newLowerBound, newLowerBound));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Expression getUpperBound() {
        return upperBound;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetUpperBound(Expression newUpperBound, NotificationChain msgs) {
        Expression oldUpperBound = upperBound;
        upperBound = newUpperBound;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND, oldUpperBound, newUpperBound);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpperBound(Expression newUpperBound) {
        if (newUpperBound != upperBound) {
            NotificationChain msgs = null;
            if (upperBound != null) msgs = ((InternalEObject) upperBound).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND, null, msgs);
            if (newUpperBound != null) msgs = ((InternalEObject) newUpperBound).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND, null, msgs);
            msgs = basicSetUpperBound(newUpperBound, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND, newUpperBound, newUpperBound));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND:
                return basicSetLowerBound(null, msgs);
            case EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND:
                return basicSetUpperBound(null, msgs);
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
            case EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND:
                return getLowerBound();
            case EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND:
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
            case EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND:
                setLowerBound((Expression) newValue);
                return;
            case EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND:
                setUpperBound((Expression) newValue);
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
            case EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND:
                setLowerBound((Expression) null);
                return;
            case EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND:
                setUpperBound((Expression) null);
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
            case EolPackage.RANGE_COLLECTION_EXPRESSION__LOWER_BOUND:
                return lowerBound != null;
            case EolPackage.RANGE_COLLECTION_EXPRESSION__UPPER_BOUND:
                return upperBound != null;
        }
        return super.eIsSet(featureID);
    }
}
