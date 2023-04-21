package de.hu_berlin.sam.mmunit.coverage.cgc.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import de.hu_berlin.sam.mmunit.coverage.TestGoal;
import de.hu_berlin.sam.mmunit.coverage.cgc.CgcPackage;
import de.hu_berlin.sam.mmunit.coverage.cgc.ContextGeneralizationCoverage;
import de.hu_berlin.sam.mmunit.coverage.gc.impl.GeneralizationCoverageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Context Generalization Coverage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.cgc.impl.ContextGeneralizationCoverageImpl#getReference <em>Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContextGeneralizationCoverageImpl extends GeneralizationCoverageImpl implements ContextGeneralizationCoverage {

    /**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
    protected EReference reference;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContextGeneralizationCoverageImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CgcPackage.Literals.CONTEXT_GENERALIZATION_COVERAGE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getReference() {
        if (reference != null && reference.eIsProxy()) {
            InternalEObject oldReference = (InternalEObject) reference;
            reference = (EReference) eResolveProxy(oldReference);
            if (reference != oldReference) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE, oldReference, reference));
            }
        }
        return reference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference basicGetReference() {
        return reference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReference(EReference newReference) {
        EReference oldReference = reference;
        reference = newReference;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE, oldReference, reference));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public boolean equals(TestGoal tg) {
        if (tg instanceof ContextGeneralizationCoverage) {
            ContextGeneralizationCoverage cc = (ContextGeneralizationCoverage) tg;
            return getSupertype().getName().equals(cc.getSupertype().getName()) && getSubtype().getName().equals(cc.getSubtype().getName()) && getReference().getName().equals(cc.getReference().getName());
        } else {
            return false;
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE:
                if (resolve) return getReference();
                return basicGetReference();
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
            case CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE:
                setReference((EReference) newValue);
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
            case CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE:
                setReference((EReference) null);
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
            case CgcPackage.CONTEXT_GENERALIZATION_COVERAGE__REFERENCE:
                return reference != null;
        }
        return super.eIsSet(featureID);
    }
}
