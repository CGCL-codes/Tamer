package cn.com.once.deploytool.model.constraint.impl;

import cn.com.once.deploytool.model.constraint.Collision;
import cn.com.once.deploytool.model.constraint.ConstraintPackage;
import cn.com.once.deploytool.model.constraint.DeployPlan;
import cn.com.once.deploytool.model.constraint.ValidationResult;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Validation Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cn.com.once.deploytool.model.constraint.impl.ValidationResultImpl#getDeployPlan <em>Deploy Plan</em>}</li>
 *   <li>{@link cn.com.once.deploytool.model.constraint.impl.ValidationResultImpl#getCollision <em>Collision</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValidationResultImpl extends EObjectImpl implements ValidationResult {

    /**
	 * The cached value of the '{@link #getDeployPlan() <em>Deploy Plan</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeployPlan()
	 * @generated
	 * @ordered
	 */
    protected DeployPlan deployPlan;

    /**
	 * The cached value of the '{@link #getCollision() <em>Collision</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollision()
	 * @generated
	 * @ordered
	 */
    protected Collision collision;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ValidationResultImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ConstraintPackage.Literals.VALIDATION_RESULT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DeployPlan getDeployPlan() {
        if (deployPlan != null && deployPlan.eIsProxy()) {
            InternalEObject oldDeployPlan = (InternalEObject) deployPlan;
            deployPlan = (DeployPlan) eResolveProxy(oldDeployPlan);
            if (deployPlan != oldDeployPlan) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN, oldDeployPlan, deployPlan));
            }
        }
        return deployPlan;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DeployPlan basicGetDeployPlan() {
        return deployPlan;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDeployPlan(DeployPlan newDeployPlan) {
        DeployPlan oldDeployPlan = deployPlan;
        deployPlan = newDeployPlan;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN, oldDeployPlan, deployPlan));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Collision getCollision() {
        if (collision != null && collision.eIsProxy()) {
            InternalEObject oldCollision = (InternalEObject) collision;
            collision = (Collision) eResolveProxy(oldCollision);
            if (collision != oldCollision) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConstraintPackage.VALIDATION_RESULT__COLLISION, oldCollision, collision));
            }
        }
        return collision;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Collision basicGetCollision() {
        return collision;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCollision(Collision newCollision) {
        Collision oldCollision = collision;
        collision = newCollision;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintPackage.VALIDATION_RESULT__COLLISION, oldCollision, collision));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN:
                if (resolve) return getDeployPlan();
                return basicGetDeployPlan();
            case ConstraintPackage.VALIDATION_RESULT__COLLISION:
                if (resolve) return getCollision();
                return basicGetCollision();
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
            case ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN:
                setDeployPlan((DeployPlan) newValue);
                return;
            case ConstraintPackage.VALIDATION_RESULT__COLLISION:
                setCollision((Collision) newValue);
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
            case ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN:
                setDeployPlan((DeployPlan) null);
                return;
            case ConstraintPackage.VALIDATION_RESULT__COLLISION:
                setCollision((Collision) null);
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
            case ConstraintPackage.VALIDATION_RESULT__DEPLOY_PLAN:
                return deployPlan != null;
            case ConstraintPackage.VALIDATION_RESULT__COLLISION:
                return collision != null;
        }
        return super.eIsSet(featureID);
    }
}
