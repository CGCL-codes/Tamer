package org.remus.infomngmnt.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.remus.infomngmnt.AvailableRuleDefinitions;
import org.remus.infomngmnt.InfomngmntPackage;
import org.remus.infomngmnt.NewElementRules;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Available Rule Definitions</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.impl.AvailableRuleDefinitionsImpl#getNewElementRules <em>New Element Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AvailableRuleDefinitionsImpl extends EObjectImpl implements AvailableRuleDefinitions {

    /**
	 * The cached value of the '{@link #getNewElementRules() <em>New Element Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewElementRules()
	 * @generated
	 * @ordered
	 */
    protected EList<NewElementRules> newElementRules;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AvailableRuleDefinitionsImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return InfomngmntPackage.Literals.AVAILABLE_RULE_DEFINITIONS;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<NewElementRules> getNewElementRules() {
        if (newElementRules == null) {
            newElementRules = new EObjectContainmentEList<NewElementRules>(NewElementRules.class, this, InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES);
        }
        return newElementRules;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES:
                return ((InternalEList<?>) getNewElementRules()).basicRemove(otherEnd, msgs);
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
            case InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES:
                return getNewElementRules();
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
            case InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES:
                getNewElementRules().clear();
                getNewElementRules().addAll((Collection<? extends NewElementRules>) newValue);
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
            case InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES:
                getNewElementRules().clear();
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
            case InfomngmntPackage.AVAILABLE_RULE_DEFINITIONS__NEW_ELEMENT_RULES:
                return newElementRules != null && !newElementRules.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
