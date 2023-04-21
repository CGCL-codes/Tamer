package de.hu_berlin.sam.mmunit.coverage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Goal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.TestGoal#isCovered <em>Covered</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.TestGoal#getCoveredBy <em>Covered By</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.TestGoal#getCriterion <em>Criterion</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hu_berlin.sam.mmunit.coverage.CoveragePackage#getTestGoal()
 * @model abstract="true"
 * @generated
 */
public interface TestGoal extends EObject {

    /**
	 * Returns the value of the '<em><b>Covered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Covered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Covered</em>' attribute.
	 * @see #setCovered(boolean)
	 * @see de.hu_berlin.sam.mmunit.coverage.CoveragePackage#getTestGoal_Covered()
	 * @model derived="true"
	 * @generated
	 */
    boolean isCovered();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.coverage.TestGoal#isCovered <em>Covered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Covered</em>' attribute.
	 * @see #isCovered()
	 * @generated
	 */
    void setCovered(boolean value);

    /**
	 * Returns the value of the '<em><b>Covered By</b></em>' containment reference list.
	 * The list contents are of type {@link de.hu_berlin.sam.mmunit.coverage.CoveringElement}.
	 * It is bidirectional and its opposite is '{@link de.hu_berlin.sam.mmunit.coverage.CoveringElement#getTestgoal <em>Testgoal</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Covered By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Covered By</em>' containment reference list.
	 * @see de.hu_berlin.sam.mmunit.coverage.CoveragePackage#getTestGoal_CoveredBy()
	 * @see de.hu_berlin.sam.mmunit.coverage.CoveringElement#getTestgoal
	 * @model opposite="testgoal" containment="true"
	 * @generated
	 */
    EList<CoveringElement> getCoveredBy();

    /**
	 * Returns the value of the '<em><b>Criterion</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link de.hu_berlin.sam.mmunit.coverage.CoverageCriterion#getTestgoals <em>Testgoals</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Criterion</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Criterion</em>' container reference.
	 * @see #setCriterion(CoverageCriterion)
	 * @see de.hu_berlin.sam.mmunit.coverage.CoveragePackage#getTestGoal_Criterion()
	 * @see de.hu_berlin.sam.mmunit.coverage.CoverageCriterion#getTestgoals
	 * @model opposite="testgoals" required="true" transient="false"
	 * @generated
	 */
    CoverageCriterion getCriterion();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.coverage.TestGoal#getCriterion <em>Criterion</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Criterion</em>' container reference.
	 * @see #getCriterion()
	 * @generated
	 */
    void setCriterion(CoverageCriterion value);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
    boolean equals(TestGoal tg);
}
