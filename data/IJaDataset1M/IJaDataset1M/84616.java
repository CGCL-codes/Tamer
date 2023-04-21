package agents;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link agents.Artifact#getSuper <em>Super</em>}</li>
 *   <li>{@link agents.Artifact#getInterface <em>Interface</em>}</li>
 *   <li>{@link agents.Artifact#getEmit <em>Emit</em>}</li>
 * </ul>
 * </p>
 *
 * @see agents.AgentsPackage#getArtifact()
 * @model
 * @generated
 */
public interface Artifact extends Entity {

    /**
	 * Returns the value of the '<em><b>Super</b></em>' reference list.
	 * The list contents are of type {@link agents.Artifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super</em>' reference list.
	 * @see agents.AgentsPackage#getArtifact_Super()
	 * @model
	 * @generated
	 */
    EList<Artifact> getSuper();

    /**
	 * Returns the value of the '<em><b>Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface</em>' reference.
	 * @see #setInterface(Interface)
	 * @see agents.AgentsPackage#getArtifact_Interface()
	 * @model required="true"
	 * @generated
	 */
    Interface getInterface();

    /**
	 * Sets the value of the '{@link agents.Artifact#getInterface <em>Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interface</em>' reference.
	 * @see #getInterface()
	 * @generated
	 */
    void setInterface(Interface value);

    /**
	 * Returns the value of the '<em><b>Emit</b></em>' reference list.
	 * The list contents are of type {@link agents.NoReply}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Emit</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emit</em>' reference list.
	 * @see agents.AgentsPackage#getArtifact_Emit()
	 * @model
	 * @generated
	 */
    EList<NoReply> getEmit();
}
