package de.hu_berlin.sam.mmunit.coverage.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import de.hu_berlin.sam.mmunit.coverage.CoverageCriterion;
import de.hu_berlin.sam.mmunit.coverage.CoveragePackage;
import de.hu_berlin.sam.mmunit.coverage.TestGoal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Criterion</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.impl.CoverageCriterionImpl#getTestgoals <em>Testgoals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CoverageCriterionImpl extends EObjectImpl implements CoverageCriterion {

    /**
	 * The cached value of the '{@link #getTestgoals() <em>Testgoals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTestgoals()
	 * @generated
	 * @ordered
	 */
    protected EList<TestGoal> testgoals;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CoverageCriterionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CoveragePackage.Literals.COVERAGE_CRITERION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<TestGoal> getTestgoals() {
        if (testgoals == null) {
            testgoals = new EObjectContainmentWithInverseEList<TestGoal>(TestGoal.class, this, CoveragePackage.COVERAGE_CRITERION__TESTGOALS, CoveragePackage.TEST_GOAL__CRITERION);
        }
        return testgoals;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public boolean subsumes(CoverageCriterion cc) {
        CoverageCriterion cc1 = cc;
        CoverageCriterion cc2 = this;
        boolean result = false;
        if (cc1 == cc2) {
            result = true;
        } else {
            for (int i = 0; i < cc1.getTestgoals().size(); i++) {
                boolean first = false;
                TestGoal tg1 = cc1.getTestgoals().get(i);
                for (int j = 0; j < cc2.getTestgoals().size(); j++) {
                    TestGoal tg2 = cc2.getTestgoals().get(j);
                    first = tg1.equals(tg2);
                    if (first) {
                        result = first;
                        break;
                    }
                }
                if (!first) {
                    return false;
                }
            }
        }
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case CoveragePackage.COVERAGE_CRITERION__TESTGOALS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTestgoals()).basicAdd(otherEnd, msgs);
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
            case CoveragePackage.COVERAGE_CRITERION__TESTGOALS:
                return ((InternalEList<?>) getTestgoals()).basicRemove(otherEnd, msgs);
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
            case CoveragePackage.COVERAGE_CRITERION__TESTGOALS:
                return getTestgoals();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case CoveragePackage.COVERAGE_CRITERION__TESTGOALS:
                return testgoals != null && !testgoals.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
