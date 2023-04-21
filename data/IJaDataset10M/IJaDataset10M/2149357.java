package cz.vse.gebz.impl;

import cz.vse.gebz.GebzPackage;
import cz.vse.gebz.Kontext;
import cz.vse.gebz.Pravidlo;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Kontext</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.vse.gebz.impl.KontextImpl#getPravidlo <em>Pravidlo</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KontextImpl extends AbstraktniLogickySpojImpl implements Kontext {

    /**
	 * The cached value of the '{@link #getPravidlo() <em>Pravidlo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPravidlo()
	 * @generated
	 * @ordered
	 */
    protected Pravidlo pravidlo;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected KontextImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return GebzPackage.Literals.KONTEXT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Pravidlo getPravidlo() {
        if (pravidlo != null && pravidlo.eIsProxy()) {
            InternalEObject oldPravidlo = (InternalEObject) pravidlo;
            pravidlo = (Pravidlo) eResolveProxy(oldPravidlo);
            if (pravidlo != oldPravidlo) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, GebzPackage.KONTEXT__PRAVIDLO, oldPravidlo, pravidlo));
            }
        }
        return pravidlo;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Pravidlo basicGetPravidlo() {
        return pravidlo;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPravidlo(Pravidlo newPravidlo, NotificationChain msgs) {
        Pravidlo oldPravidlo = pravidlo;
        pravidlo = newPravidlo;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GebzPackage.KONTEXT__PRAVIDLO, oldPravidlo, newPravidlo);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPravidlo(Pravidlo newPravidlo) {
        if (newPravidlo != pravidlo) {
            NotificationChain msgs = null;
            if (pravidlo != null) msgs = ((InternalEObject) pravidlo).eInverseRemove(this, GebzPackage.PRAVIDLO__KONTEXT, Pravidlo.class, msgs);
            if (newPravidlo != null) msgs = ((InternalEObject) newPravidlo).eInverseAdd(this, GebzPackage.PRAVIDLO__KONTEXT, Pravidlo.class, msgs);
            msgs = basicSetPravidlo(newPravidlo, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GebzPackage.KONTEXT__PRAVIDLO, newPravidlo, newPravidlo));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case GebzPackage.KONTEXT__PRAVIDLO:
                if (pravidlo != null) msgs = ((InternalEObject) pravidlo).eInverseRemove(this, GebzPackage.PRAVIDLO__KONTEXT, Pravidlo.class, msgs);
                return basicSetPravidlo((Pravidlo) otherEnd, msgs);
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
            case GebzPackage.KONTEXT__PRAVIDLO:
                return basicSetPravidlo(null, msgs);
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
            case GebzPackage.KONTEXT__PRAVIDLO:
                if (resolve) return getPravidlo();
                return basicGetPravidlo();
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
            case GebzPackage.KONTEXT__PRAVIDLO:
                setPravidlo((Pravidlo) newValue);
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
            case GebzPackage.KONTEXT__PRAVIDLO:
                setPravidlo((Pravidlo) null);
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
            case GebzPackage.KONTEXT__PRAVIDLO:
                return pravidlo != null;
        }
        return super.eIsSet(featureID);
    }
}
