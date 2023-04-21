package org.eclipse.epsilon.fptc.expressions.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.epsilon.fptc.expressions.ExpressionsPackage;
import org.eclipse.epsilon.fptc.expressions.Fault;
import org.eclipse.epsilon.fptc.expressions.FaultSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fault Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.fptc.expressions.impl.FaultSetImpl#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FaultSetImpl extends ConcreteAtomImpl implements FaultSet {

    /**
	 * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContents()
	 * @generated
	 * @ordered
	 */
    protected EList<Fault> contents;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected FaultSetImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ExpressionsPackage.Literals.FAULT_SET;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Fault> getContents() {
        if (contents == null) {
            contents = new EObjectContainmentEList<Fault>(Fault.class, this, ExpressionsPackage.FAULT_SET__CONTENTS);
        }
        return contents;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ExpressionsPackage.FAULT_SET__CONTENTS:
                return ((InternalEList<?>) getContents()).basicRemove(otherEnd, msgs);
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
            case ExpressionsPackage.FAULT_SET__CONTENTS:
                return getContents();
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
            case ExpressionsPackage.FAULT_SET__CONTENTS:
                getContents().clear();
                getContents().addAll((Collection<? extends Fault>) newValue);
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
            case ExpressionsPackage.FAULT_SET__CONTENTS:
                getContents().clear();
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
            case ExpressionsPackage.FAULT_SET__CONTENTS:
                return contents != null && !contents.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
