package org.emftext.language.owl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.owl.DataProperty#getRange <em>Range</em>}</li>
 *   <li>{@link org.emftext.language.owl.DataProperty#getCharacteristic <em>Characteristic</em>}</li>
 *   <li>{@link org.emftext.language.owl.DataProperty#getSuperProperties <em>Super Properties</em>}</li>
 *   <li>{@link org.emftext.language.owl.DataProperty#getEquivalentProperties <em>Equivalent Properties</em>}</li>
 *   <li>{@link org.emftext.language.owl.DataProperty#getDisjointProperties <em>Disjoint Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.owl.OwlPackage#getDataProperty()
 * @model
 * @generated
 */
public interface DataProperty extends Feature, Annotateable {

    /**
	 * Returns the value of the '<em><b>Range</b></em>' containment reference list.
	 * The list contents are of type {@link org.emftext.language.owl.DataRange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' containment reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDataProperty_Range()
	 * @model containment="true"
	 * @generated
	 */
    EList<DataRange> getRange();

    /**
	 * Returns the value of the '<em><b>Characteristic</b></em>' attribute list.
	 * The list contents are of type {@link org.emftext.language.owl.Characteristic}.
	 * The literals are from the enumeration {@link org.emftext.language.owl.Characteristic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Characteristic</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Characteristic</em>' attribute list.
	 * @see org.emftext.language.owl.Characteristic
	 * @see org.emftext.language.owl.OwlPackage#getDataProperty_Characteristic()
	 * @model
	 * @generated
	 */
    EList<Characteristic> getCharacteristic();

    /**
	 * Returns the value of the '<em><b>Super Properties</b></em>' reference list.
	 * The list contents are of type {@link org.emftext.language.owl.DataProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Properties</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Properties</em>' reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDataProperty_SuperProperties()
	 * @model
	 * @generated
	 */
    EList<DataProperty> getSuperProperties();

    /**
	 * Returns the value of the '<em><b>Equivalent Properties</b></em>' reference list.
	 * The list contents are of type {@link org.emftext.language.owl.DataProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Equivalent Properties</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Equivalent Properties</em>' reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDataProperty_EquivalentProperties()
	 * @model
	 * @generated
	 */
    EList<DataProperty> getEquivalentProperties();

    /**
	 * Returns the value of the '<em><b>Disjoint Properties</b></em>' reference list.
	 * The list contents are of type {@link org.emftext.language.owl.DataProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Disjoint Properties</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disjoint Properties</em>' reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDataProperty_DisjointProperties()
	 * @model
	 * @generated
	 */
    EList<DataProperty> getDisjointProperties();
}
