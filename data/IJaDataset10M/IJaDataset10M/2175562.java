package assign;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EObject Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link assign.EObjectReference#getObject <em>Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see assign.AssignPackage#getEObjectReference()
 * @model
 * @generated
 */
public interface EObjectReference extends Expression {

    /**
	 * Returns the value of the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' reference.
	 * @see #setObject(EObject)
	 * @see assign.AssignPackage#getEObjectReference_Object()
	 * @model required="true"
	 * @generated
	 */
    EObject getObject();

    /**
	 * Sets the value of the '{@link assign.EObjectReference#getObject <em>Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' reference.
	 * @see #getObject()
	 * @generated
	 */
    void setObject(EObject value);
}
