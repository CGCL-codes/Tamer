package biz.modelx.precise.usecase.ui.model.eusecase.impl;

import biz.modelx.precise.ui.model.eprecise.impl.PNameableImpl;
import biz.modelx.precise.usecase.ui.model.eusecase.EusecasePackage;
import biz.modelx.precise.usecase.ui.model.eusecase.SystemBoundery;
import biz.modelx.precise.usecase.ui.model.eusecase.Usecase;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>System Boundery</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link biz.modelx.precise.usecase.ui.model.eusecase.impl.SystemBounderyImpl#getSbusecases <em>Sbusecases</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SystemBounderyImpl extends PNameableImpl implements SystemBoundery {

    /**
	 * The cached value of the '{@link #getSbusecases() <em>Sbusecases</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSbusecases()
	 * @generated
	 * @ordered
	 */
    protected EList<Usecase> sbusecases;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SystemBounderyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EusecasePackage.Literals.SYSTEM_BOUNDERY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Usecase> getSbusecases() {
        if (sbusecases == null) {
            sbusecases = new EObjectContainmentEList<Usecase>(Usecase.class, this, EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES);
        }
        return sbusecases;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES:
                return ((InternalEList<?>) getSbusecases()).basicRemove(otherEnd, msgs);
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
            case EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES:
                return getSbusecases();
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
            case EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES:
                getSbusecases().clear();
                getSbusecases().addAll((Collection<? extends Usecase>) newValue);
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
            case EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES:
                getSbusecases().clear();
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
            case EusecasePackage.SYSTEM_BOUNDERY__SBUSECASES:
                return sbusecases != null && !sbusecases.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
