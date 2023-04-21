package odm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equivalent Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link odm.EquivalentClass#getDescription <em>Description</em>}</li>
 *   <li>{@link odm.EquivalentClass#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @see odm.OdmPackage#getEquivalentClass()
 * @model
 * @generated
 */
public interface EquivalentClass extends ClassAxiom {

    /**
	 * Returns the value of the '<em><b>Description</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' reference.
	 * @see #setDescription(Description)
	 * @see odm.OdmPackage#getEquivalentClass_Description()
	 * @model required="true"
	 * @generated
	 */
    Description getDescription();

    /**
	 * Sets the value of the '{@link odm.EquivalentClass#getDescription <em>Description</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' reference.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(Description value);

    /**
	 * Returns the value of the '<em><b>Tag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tag</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tag</em>' attribute.
	 * @see #setTag(String)
	 * @see odm.OdmPackage#getEquivalentClass_Tag()
	 * @model required="true"
	 * @generated
	 */
    String getTag();

    /**
	 * Sets the value of the '{@link odm.EquivalentClass#getTag <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tag</em>' attribute.
	 * @see #getTag()
	 * @generated
	 */
    void setTag(String value);
}
