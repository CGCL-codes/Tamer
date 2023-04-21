package org.hl7.v3;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TS1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A quantity specifying a point on the axis of natural time.
 *             A point in time is most often represented as a calendar
 *             expression.
 *          
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.TS1#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getTS1()
 * @model extendedMetaData="name='TS' kind='empty'"
 * @generated
 */
public interface TS1 extends QTY {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.hl7.v3.V3Package#getTS1_Value()
	 * @model dataType="org.hl7.v3.Ts"
	 *        extendedMetaData="kind='attribute' name='value'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link org.hl7.v3.TS1#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);
}
