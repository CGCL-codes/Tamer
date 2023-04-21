package agents;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link agents.Entity#getName <em>Name</em>}</li>
 *   <li>{@link agents.Entity#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link agents.Entity#getSupport <em>Support</em>}</li>
 *   <li>{@link agents.Entity#getArtRefs <em>Art Refs</em>}</li>
 * </ul>
 * </p>
 *
 * @see agents.AgentsPackage#getEntity()
 * @model abstract="true"
 * @generated
 */
public interface Entity extends EObject {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see agents.AgentsPackage#getEntity_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link agents.Entity#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link agents.Attribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see agents.AgentsPackage#getEntity_Attribute()
	 * @model containment="true"
	 * @generated
	 */
    EList<Attribute> getAttribute();

    /**
	 * Returns the value of the '<em><b>Support</b></em>' containment reference list.
	 * The list contents are of type {@link agents.Support}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Support</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Support</em>' containment reference list.
	 * @see agents.AgentsPackage#getEntity_Support()
	 * @model containment="true"
	 * @generated
	 */
    EList<Support> getSupport();

    /**
	 * Returns the value of the '<em><b>Art Refs</b></em>' reference list.
	 * The list contents are of type {@link agents.Artifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Art Refs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Art Refs</em>' reference list.
	 * @see agents.AgentsPackage#getEntity_ArtRefs()
	 * @model
	 * @generated
	 */
    EList<Artifact> getArtRefs();
}
