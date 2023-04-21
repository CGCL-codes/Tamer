package SBVR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Role</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link SBVR.Role#getObjectType <em>Object Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see SBVR.SBVRPackage#getRole()
 * @model
 * @generated
 */
public interface Role extends NounConcept {

    /**
	 * Returns the value of the '<em><b>Object Type</b></em>' reference list.
	 * The list contents are of type {@link SBVR.ObjectType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Type</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Type</em>' reference list.
	 * @see SBVR.SBVRPackage#getRole_ObjectType()
	 * @model ordered="false"
	 * @generated
	 */
    EList<ObjectType> getObjectType();
}
