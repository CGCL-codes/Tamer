package cz.vse.nest.impl;

import cz.vse.nest.Conclusion;
import cz.vse.nest.Conclusions;
import cz.vse.nest.NestPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conclusions</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.vse.nest.impl.ConclusionsImpl#getConclusion <em>Conclusion</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConclusionsImpl extends EObjectImpl implements Conclusions {

    /**
	 * The cached value of the '{@link #getConclusion() <em>Conclusion</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConclusion()
	 * @generated
	 * @ordered
	 */
    protected EList<Conclusion> conclusion;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ConclusionsImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return NestPackage.Literals.CONCLUSIONS;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Conclusion> getConclusion() {
        if (conclusion == null) {
            conclusion = new EObjectContainmentEList<Conclusion>(Conclusion.class, this, NestPackage.CONCLUSIONS__CONCLUSION);
        }
        return conclusion;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NestPackage.CONCLUSIONS__CONCLUSION:
                return ((InternalEList<?>) getConclusion()).basicRemove(otherEnd, msgs);
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
            case NestPackage.CONCLUSIONS__CONCLUSION:
                return getConclusion();
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
            case NestPackage.CONCLUSIONS__CONCLUSION:
                getConclusion().clear();
                getConclusion().addAll((Collection<? extends Conclusion>) newValue);
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
            case NestPackage.CONCLUSIONS__CONCLUSION:
                getConclusion().clear();
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
            case NestPackage.CONCLUSIONS__CONCLUSION:
                return conclusion != null && !conclusion.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
