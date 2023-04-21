package odm;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link odm.PropertyExpression#getDomain <em>Domain</em>}</li>
 *   <li>{@link odm.PropertyExpression#getRange <em>Range</em>}</li>
 * </ul>
 * </p>
 *
 * @see odm.OdmPackage#getPropertyExpression()
 * @model
 * @generated
 */
public interface PropertyExpression extends EObject {

    /**
	 * Returns the value of the '<em><b>Domain</b></em>' containment reference list.
	 * The list contents are of type {@link odm.PropertyDomain}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domain</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Domain</em>' containment reference list.
	 * @see odm.OdmPackage#getPropertyExpression_Domain()
	 * @model type="odm.PropertyDomain" containment="true"
	 * @generated
	 */
    EList getDomain();

    /**
	 * Returns the value of the '<em><b>Range</b></em>' containment reference list.
	 * The list contents are of type {@link odm.PropertyRange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' containment reference list.
	 * @see odm.OdmPackage#getPropertyExpression_Range()
	 * @model type="odm.PropertyRange" containment="true"
	 * @generated
	 */
    EList getRange();
}
