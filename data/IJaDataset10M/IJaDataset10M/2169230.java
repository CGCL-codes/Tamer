package ms.jasim.model.items;

import ms.utils.IModelObjectList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Property List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ms.jasim.model.items.ObjectPropertyList#getProperty <em>Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see ms.jasim.model.items.ItemsPackage#getObjectPropertyList()
 * @model superTypes="ms.jasim.model.items.IObjectPropertyList"
 * @generated
 */
public interface ObjectPropertyList extends EObject, IModelObjectList<ObjectProperty> {

    /**
	 * Returns the value of the '<em><b>Property</b></em>' containment reference list.
	 * The list contents are of type {@link ms.jasim.model.items.ObjectProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' containment reference list.
	 * @see ms.jasim.model.items.ItemsPackage#getObjectPropertyList_Property()
	 * @model containment="true"
	 * @generated
	 */
    EList<ObjectProperty> getProperty();
}
