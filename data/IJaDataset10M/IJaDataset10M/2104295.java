package org.emftext.language.owl.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.emftext.language.owl.Description;
import org.emftext.language.owl.EquivalentClasses;
import org.emftext.language.owl.OwlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Equivalent Classes</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftext.language.owl.impl.EquivalentClassesImpl#getDescriptions <em>Descriptions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EquivalentClassesImpl extends MiscImpl implements EquivalentClasses {

    /**
	 * The cached value of the '{@link #getDescriptions() <em>Descriptions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescriptions()
	 * @generated
	 * @ordered
	 */
    protected EList<Description> descriptions;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EquivalentClassesImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return OwlPackage.Literals.EQUIVALENT_CLASSES;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Description> getDescriptions() {
        if (descriptions == null) {
            descriptions = new EObjectContainmentEList<Description>(Description.class, this, OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS);
        }
        return descriptions;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS:
                return ((InternalEList<?>) getDescriptions()).basicRemove(otherEnd, msgs);
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
            case OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS:
                return getDescriptions();
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
            case OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS:
                getDescriptions().clear();
                getDescriptions().addAll((Collection<? extends Description>) newValue);
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
            case OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS:
                getDescriptions().clear();
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
            case OwlPackage.EQUIVALENT_CLASSES__DESCRIPTIONS:
                return descriptions != null && !descriptions.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
