package EJBTool;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Persistent View</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link EJBTool.PersistentView#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see EJBTool.EJBToolPackage#getPersistentView()
 * @model
 * @generated
 */
public interface PersistentView extends EObject {

    /**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link EJBTool.EntityBean}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see EJBTool.EJBToolPackage#getPersistentView_Children()
	 * @model containment="true"
	 * @generated
	 */
    EList<EntityBean> getChildren();
}
