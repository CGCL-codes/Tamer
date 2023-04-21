package de.hu_berlin.sam.mmunit.coverage.mc;

import org.eclipse.emf.common.util.EList;
import de.hu_berlin.sam.mmunit.coverage.CoverageCriterion;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multiplicity Coverage Criterion</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.coverage.mc.MultiplicityCoverageCriterion#getPartitions <em>Partitions</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hu_berlin.sam.mmunit.coverage.mc.McPackage#getMultiplicityCoverageCriterion()
 * @model
 * @generated
 */
public interface MultiplicityCoverageCriterion extends CoverageCriterion {

    /**
	 * Returns the value of the '<em><b>Partitions</b></em>' containment reference list.
	 * The list contents are of type {@link de.hu_berlin.sam.mmunit.coverage.mc.MultiplicityPartition}.
	 * It is bidirectional and its opposite is '{@link de.hu_berlin.sam.mmunit.coverage.mc.MultiplicityPartition#getCriterion <em>Criterion</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Partitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Partitions</em>' containment reference list.
	 * @see de.hu_berlin.sam.mmunit.coverage.mc.McPackage#getMultiplicityCoverageCriterion_Partitions()
	 * @see de.hu_berlin.sam.mmunit.coverage.mc.MultiplicityPartition#getCriterion
	 * @model opposite="criterion" containment="true"
	 * @generated
	 */
    EList<MultiplicityPartition> getPartitions();
}
