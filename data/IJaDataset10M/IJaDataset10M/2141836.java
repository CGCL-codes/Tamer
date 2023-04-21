package ms.jasim.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Goal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ms.jasim.model.Goal#getDecomposition <em>Decomposition</em>}</li>
 *   <li>{@link ms.jasim.model.Goal#getRequires <em>Requires</em>}</li>
 *   <li>{@link ms.jasim.model.Goal#getRequiredBy <em>Required By</em>}</li>
 * </ul>
 * </p>
 *
 * @see ms.jasim.model.ModelPackage#getGoal()
 * @model
 * @generated
 */
public interface Goal extends CustomizableObject {

    /**
	 * Returns the value of the '<em><b>Decomposition</b></em>' containment reference list.
	 * The list contents are of type {@link ms.jasim.model.Decomposition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Decomposition</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Decomposition</em>' containment reference list.
	 * @see ms.jasim.model.ModelPackage#getGoal_Decomposition()
	 * @model containment="true"
	 * @generated
	 */
    EList<Decomposition> getDecomposition();

    /**
	 * Returns the value of the '<em><b>Requires</b></em>' reference list.
	 * The list contents are of type {@link ms.jasim.model.Goal}.
	 * It is bidirectional and its opposite is '{@link ms.jasim.model.Goal#getRequiredBy <em>Required By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Requires</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Requires</em>' reference list.
	 * @see ms.jasim.model.ModelPackage#getGoal_Requires()
	 * @see ms.jasim.model.Goal#getRequiredBy
	 * @model opposite="requiredBy"
	 * @generated
	 */
    EList<Goal> getRequires();

    /**
	 * Returns the value of the '<em><b>Required By</b></em>' reference list.
	 * The list contents are of type {@link ms.jasim.model.Goal}.
	 * It is bidirectional and its opposite is '{@link ms.jasim.model.Goal#getRequires <em>Requires</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Required By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required By</em>' reference list.
	 * @see ms.jasim.model.ModelPackage#getGoal_RequiredBy()
	 * @see ms.jasim.model.Goal#getRequires
	 * @model opposite="requires"
	 * @generated
	 */
    EList<Goal> getRequiredBy();
}
