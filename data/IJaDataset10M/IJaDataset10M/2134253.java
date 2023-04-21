package UMLModelMM;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link UMLModelMM.Class#isFinal <em>Is Final</em>}</li>
 * </ul>
 * </p>
 *
 * @see UMLModelMM.UMLModelMMPackage#getClass_()
 * @model
 * @generated
 */
public interface Class extends Classifier {

    /**
	 * Returns the value of the '<em><b>Is Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Final</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Final</em>' attribute.
	 * @see #setIsFinal(boolean)
	 * @see UMLModelMM.UMLModelMMPackage#getClass_IsFinal()
	 * @model unique="false" required="true" ordered="false"
	 * @generated
	 */
    boolean isFinal();

    /**
	 * Sets the value of the '{@link UMLModelMM.Class#isFinal <em>Is Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Final</em>' attribute.
	 * @see #isFinal()
	 * @generated
	 */
    void setIsFinal(boolean value);
}
