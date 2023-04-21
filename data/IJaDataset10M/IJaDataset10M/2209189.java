package iec61970.core;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Irregular Interval Schedule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The schedule has TimePoints where the time between them varies.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.core.IrregularIntervalSchedule#getTimePoints <em>Time Points</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.core.CorePackage#getIrregularIntervalSchedule()
 * @model
 * @generated
 */
public interface IrregularIntervalSchedule extends BasicIntervalSchedule {

    /**
	 * Returns the value of the '<em><b>Time Points</b></em>' reference list.
	 * The list contents are of type {@link iec61970.core.IrregularTimePoint}.
	 * It is bidirectional and its opposite is '{@link iec61970.core.IrregularTimePoint#getIntervalSchedule <em>Interval Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The point data values that define a curve
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time Points</em>' reference list.
	 * @see iec61970.core.CorePackage#getIrregularIntervalSchedule_TimePoints()
	 * @see iec61970.core.IrregularTimePoint#getIntervalSchedule
	 * @model opposite="IntervalSchedule" required="true"
	 * @generated
	 */
    EList<IrregularTimePoint> getTimePoints();
}
