package agents;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Artifact Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link agents.ArtifactInstance#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see agents.AgentsPackage#getArtifactInstance()
 * @model
 * @generated
 */
public interface ArtifactInstance extends Instance {

    /**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(Artifact)
	 * @see agents.AgentsPackage#getArtifactInstance_Type()
	 * @model required="true"
	 * @generated
	 */
    Artifact getType();

    /**
	 * Sets the value of the '{@link agents.ArtifactInstance#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
    void setType(Artifact value);
}
