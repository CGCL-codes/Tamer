package ru.ifmo.rain.astrans;

import org.eclipse.emf.ecore.EDataType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Existing EData Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ru.ifmo.rain.astrans.ExistingEDataType#getEDataType <em>EData Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see ru.ifmo.rain.astrans.AstransPackage#getExistingEDataType()
 * @model
 * @generated
 */
public interface ExistingEDataType extends EClassifierReference {

    /**
	 * Returns the value of the '<em><b>EData Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EData Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>EData Type</em>' reference.
	 * @see #setEDataType(EDataType)
	 * @see ru.ifmo.rain.astrans.AstransPackage#getExistingEDataType_EDataType()
	 * @model required="true"
	 * @generated
	 */
    EDataType getEDataType();

    /**
	 * Sets the value of the '{@link ru.ifmo.rain.astrans.ExistingEDataType#getEDataType <em>EData Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>EData Type</em>' reference.
	 * @see #getEDataType()
	 * @generated
	 */
    void setEDataType(EDataType value);
}
