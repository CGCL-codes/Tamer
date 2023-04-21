package org.emftext.language.owl.impl;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.emftext.language.owl.Individual;
import org.emftext.language.owl.OwlPackage;
import org.emftext.language.owl.SameIndividuals;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Same Individuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftext.language.owl.impl.SameIndividualsImpl#getIndividuals <em>Individuals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SameIndividualsImpl extends MiscImpl implements SameIndividuals {

    /**
	 * The cached value of the '{@link #getIndividuals() <em>Individuals</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndividuals()
	 * @generated
	 * @ordered
	 */
    protected EList<Individual> individuals;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SameIndividualsImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return OwlPackage.Literals.SAME_INDIVIDUALS;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Individual> getIndividuals() {
        if (individuals == null) {
            individuals = new EObjectResolvingEList<Individual>(Individual.class, this, OwlPackage.SAME_INDIVIDUALS__INDIVIDUALS);
        }
        return individuals;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OwlPackage.SAME_INDIVIDUALS__INDIVIDUALS:
                return getIndividuals();
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
            case OwlPackage.SAME_INDIVIDUALS__INDIVIDUALS:
                getIndividuals().clear();
                getIndividuals().addAll((Collection<? extends Individual>) newValue);
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
            case OwlPackage.SAME_INDIVIDUALS__INDIVIDUALS:
                getIndividuals().clear();
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
            case OwlPackage.SAME_INDIVIDUALS__INDIVIDUALS:
                return individuals != null && !individuals.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
