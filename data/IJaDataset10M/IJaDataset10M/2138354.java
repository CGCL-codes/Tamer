package de.morknet.mrw.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import de.morknet.mrw.metamodel.Anschluss;
import de.morknet.mrw.metamodel.Licht;
import de.morknet.mrw.metamodel.ModelrailwayPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Licht</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.morknet.mrw.metamodel.impl.LichtImpl#getAnschluss <em>Anschluss</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LichtImpl extends BeleuchtungsmittelImpl implements Licht {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected LichtImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelrailwayPackage.Literals.LICHT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Anschluss getAnschluss() {
        if (eContainerFeatureID() != ModelrailwayPackage.LICHT__ANSCHLUSS) return null;
        return (Anschluss) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAnschluss(Anschluss newAnschluss, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newAnschluss, ModelrailwayPackage.LICHT__ANSCHLUSS, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAnschluss(Anschluss newAnschluss) {
        if (newAnschluss != eInternalContainer() || (eContainerFeatureID() != ModelrailwayPackage.LICHT__ANSCHLUSS && newAnschluss != null)) {
            if (EcoreUtil.isAncestor(this, newAnschluss)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newAnschluss != null) msgs = ((InternalEObject) newAnschluss).eInverseAdd(this, ModelrailwayPackage.ANSCHLUSS__LICHTER, Anschluss.class, msgs);
            msgs = basicSetAnschluss(newAnschluss, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelrailwayPackage.LICHT__ANSCHLUSS, newAnschluss, newAnschluss));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetAnschluss((Anschluss) otherEnd, msgs);
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
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                return basicSetAnschluss(null, msgs);
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
        switch(eContainerFeatureID()) {
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                return eInternalContainer().eInverseRemove(this, ModelrailwayPackage.ANSCHLUSS__LICHTER, Anschluss.class, msgs);
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
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                return getAnschluss();
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
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                setAnschluss((Anschluss) newValue);
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
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                setAnschluss((Anschluss) null);
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
            case ModelrailwayPackage.LICHT__ANSCHLUSS:
                return getAnschluss() != null;
        }
        return super.eIsSet(featureID);
    }
}
