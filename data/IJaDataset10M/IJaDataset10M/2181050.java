package de.fraunhofer.fokus.cttcn;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Stream</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.fraunhofer.fokus.cttcn.InputStream#getCurrentValue <em>Current Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getInputStream()
 * @model
 * @generated
 */
public interface InputStream extends Stream {

    /**
	 * Returns the value of the '<em><b>Current Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Value</em>' attribute.
	 * @see #setCurrentValue(int)
	 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getInputStream_CurrentValue()
	 * @model
	 * @generated
	 */
    int getCurrentValue();

    /**
	 * Sets the value of the '{@link de.fraunhofer.fokus.cttcn.InputStream#getCurrentValue <em>Current Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Value</em>' attribute.
	 * @see #getCurrentValue()
	 * @generated
	 */
    void setCurrentValue(int value);
}
