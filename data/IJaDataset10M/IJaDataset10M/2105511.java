package org.enml.measures;

import org.eclipse.emf.ecore.EObject;

/**
 * @author  Pag
 * @model
 */
public interface TimeHistoryParameter extends EObject {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "enml.org (C) 2007";

    /**
	 * @return  <code>TimeHistoryParameter </code>
	 * @model  required="true"
	 */
    String getName();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getName  <em>Name</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * @return  <code>TimeHistoryParameter name</code>
	 * @model  required="true"
	 */
    String getUnit();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getUnit  <em>Unit</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Unit</em>' attribute.
	 * @see #getUnit()
	 * @generated
	 */
    void setUnit(String value);

    /**
	 * @return  <code>TimeHistoryParameter unit</code>
	 * @model  required="true"
	 */
    double getScale();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getScale  <em>Scale</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Scale</em>' attribute.
	 * @see #getScale()
	 * @generated
	 */
    void setScale(double value);

    /**
	 * @return  <code>TimeHistoryParameter scale</code>
	 * @model  required="true"
	 */
    double getNominalFrequency();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getNominalFrequency  <em>Nominal Frequency</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Nominal Frequency</em>' attribute.
	 * @see #getNominalFrequency()
	 * @generated
	 */
    void setNominalFrequency(double value);

    /**
	 * @return  <code>TimeHistoryParameter weighting</code>
	 * @model  required="true"
	 */
    Weighting getWeighting();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getWeighting  <em>Weighting</em>} ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Weighting</em>' attribute.
	 * @see org.enml.measures.Weighting
	 * @see #getWeighting()
	 * @generated
	 */
    void setWeighting(Weighting value);

    /**
	 * @return  <code>TimeHistory</code> reference
	 * @model  required="true" opposite="parameters"
	 */
    TimeHistory getTimeHistory();

    /**
	 * Sets the value of the ' {@link org.enml.measures.TimeHistoryParameter#getTimeHistory  <em>Time History</em>} ' container reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value  the new value of the '<em>Time History</em>' container reference.
	 * @see #getTimeHistory()
	 * @generated
	 */
    void setTimeHistory(TimeHistory value);
}
