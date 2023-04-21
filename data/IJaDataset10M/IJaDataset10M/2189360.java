package net.sourceforge.olympos.dsl.platform.platform.impl;

import net.sourceforge.olympos.dsl.platform.platform.AbstractController;
import net.sourceforge.olympos.dsl.platform.platform.Actions;
import net.sourceforge.olympos.dsl.platform.platform.PlatformPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Controller</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sourceforge.olympos.dsl.platform.platform.impl.AbstractControllerImpl#getTriggered <em>Triggered</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AbstractControllerImpl extends ElementsImpl implements AbstractController {

    /**
	 * The cached value of the '{@link #getTriggered() <em>Triggered</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTriggered()
	 * @generated
	 * @ordered
	 */
    protected Actions triggered;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AbstractControllerImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return PlatformPackage.Literals.ABSTRACT_CONTROLLER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Actions getTriggered() {
        return triggered;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTriggered(Actions newTriggered, NotificationChain msgs) {
        Actions oldTriggered = triggered;
        triggered = newTriggered;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED, oldTriggered, newTriggered);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTriggered(Actions newTriggered) {
        if (newTriggered != triggered) {
            NotificationChain msgs = null;
            if (triggered != null) msgs = ((InternalEObject) triggered).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED, null, msgs);
            if (newTriggered != null) msgs = ((InternalEObject) newTriggered).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED, null, msgs);
            msgs = basicSetTriggered(newTriggered, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED, newTriggered, newTriggered));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED:
                return basicSetTriggered(null, msgs);
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
            case PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED:
                return getTriggered();
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
            case PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED:
                setTriggered((Actions) newValue);
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
            case PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED:
                setTriggered((Actions) null);
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
            case PlatformPackage.ABSTRACT_CONTROLLER__TRIGGERED:
                return triggered != null;
        }
        return super.eIsSet(featureID);
    }
}
