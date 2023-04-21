package org.enml.measures;

/**
 * @author  Pag
 * @model
 */
public interface Calibration extends AcquiredMeasure {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * @return  <code>Calibration expectedLevel</code>
	 * @model  required="true"
	 */
    double getExpectedLevel();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Calibration#getExpectedLevel  <em>Expected Level</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Expected Level</em>' attribute.
	 * @see #getExpectedLevel()
	 * @generated
	 */
    void setExpectedLevel(double value);

    /**
	 * @return  <code>Calibration measuredLevel</code>
	 * @model  required="true"
	 */
    double getMeasuredLevel();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Calibration#getMeasuredLevel  <em>Measured Level</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Measured Level</em>' attribute.
	 * @see #getMeasuredLevel()
	 * @generated
	 */
    void setMeasuredLevel(double value);

    /**
	 * @return  <code>Calibration type</code>
	 * @model  required="true"
	 */
    CalibrationType getType();

    /**
	 * Sets the value of the ' {@link org.enml.measures.Calibration#getType  <em>Type</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Type</em>' attribute.
	 * @see org.enml.measures.CalibrationType
	 * @see #getType()
	 * @generated
	 */
    void setType(CalibrationType value);
}
