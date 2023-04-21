package DeftDataModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Specific Format</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link DeftDataModel.SpecificFormat#getParentCodesnippet <em>Parent Codesnippet</em>}</li>
 * </ul>
 * </p>
 *
 * @see DeftDataModel.DeftDataModelPackage#getSpecificFormat()
 * @model
 * @generated
 */
public interface SpecificFormat extends Format {

    /**
	 * Returns the value of the '<em><b>Parent Codesnippet</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Codesnippet</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Codesnippet</em>' reference.
	 * @see #setParentCodesnippet(CodeSnippet)
	 * @see DeftDataModel.DeftDataModelPackage#getSpecificFormat_ParentCodesnippet()
	 * @model required="true"
	 * @generated
	 */
    CodeSnippet getParentCodesnippet();

    /**
	 * Sets the value of the '{@link DeftDataModel.SpecificFormat#getParentCodesnippet <em>Parent Codesnippet</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Codesnippet</em>' reference.
	 * @see #getParentCodesnippet()
	 * @generated
	 */
    void setParentCodesnippet(CodeSnippet value);
}
