package de.mywines.model.impl;

import de.mywines.model.Bestandteil;
import de.mywines.model.ModelPackage;
import de.mywines.model.Rebsorte;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bestandteil</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.mywines.model.impl.BestandteilImpl#getProzent <em>Prozent</em>}</li>
 *   <li>{@link de.mywines.model.impl.BestandteilImpl#getRebsorte <em>Rebsorte</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BestandteilImpl extends ElementImpl implements Bestandteil {

    /**
	 * The default value of the '{@link #getProzent() <em>Prozent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProzent()
	 * @generated
	 * @ordered
	 */
    protected static final int PROZENT_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getProzent() <em>Prozent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProzent()
	 * @generated
	 * @ordered
	 */
    protected int prozent = PROZENT_EDEFAULT;

    /**
	 * The cached value of the '{@link #getRebsorte() <em>Rebsorte</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRebsorte()
	 * @generated
	 * @ordered
	 */
    protected Rebsorte rebsorte;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected BestandteilImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.BESTANDTEIL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getProzent() {
        return prozent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProzent(int newProzent) {
        int oldProzent = prozent;
        prozent = newProzent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BESTANDTEIL__PROZENT, oldProzent, prozent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Rebsorte getRebsorte() {
        if (rebsorte != null && rebsorte.eIsProxy()) {
            InternalEObject oldRebsorte = (InternalEObject) rebsorte;
            rebsorte = (Rebsorte) eResolveProxy(oldRebsorte);
            if (rebsorte != oldRebsorte) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.BESTANDTEIL__REBSORTE, oldRebsorte, rebsorte));
            }
        }
        return rebsorte;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Rebsorte basicGetRebsorte() {
        return rebsorte;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRebsorte(Rebsorte newRebsorte) {
        Rebsorte oldRebsorte = rebsorte;
        rebsorte = newRebsorte;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BESTANDTEIL__REBSORTE, oldRebsorte, rebsorte));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.BESTANDTEIL__PROZENT:
                return getProzent();
            case ModelPackage.BESTANDTEIL__REBSORTE:
                if (resolve) return getRebsorte();
                return basicGetRebsorte();
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
            case ModelPackage.BESTANDTEIL__PROZENT:
                setProzent((Integer) newValue);
                return;
            case ModelPackage.BESTANDTEIL__REBSORTE:
                setRebsorte((Rebsorte) newValue);
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
            case ModelPackage.BESTANDTEIL__PROZENT:
                setProzent(PROZENT_EDEFAULT);
                return;
            case ModelPackage.BESTANDTEIL__REBSORTE:
                setRebsorte((Rebsorte) null);
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
            case ModelPackage.BESTANDTEIL__PROZENT:
                return prozent != PROZENT_EDEFAULT;
            case ModelPackage.BESTANDTEIL__REBSORTE:
                return rebsorte != null;
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
        result.append(" (Prozent: ");
        result.append(prozent);
        result.append(')');
        return result.toString();
    }
}
