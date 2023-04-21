package urban.urban;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Initialise</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link urban.urban.Initialise#getMultiple <em>Multiple</em>}</li>
 *   <li>{@link urban.urban.Initialise#getAgents <em>Agents</em>}</li>
 * </ul>
 * </p>
 *
 * @see urban.urban.UrbanPackage#getInitialise()
 * @model
 * @generated
 */
public interface Initialise extends EObject {

    /**
   * Returns the value of the '<em><b>Multiple</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Multiple</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Multiple</em>' attribute.
   * @see #setMultiple(int)
   * @see urban.urban.UrbanPackage#getInitialise_Multiple()
   * @model
   * @generated
   */
    int getMultiple();

    /**
   * Sets the value of the '{@link urban.urban.Initialise#getMultiple <em>Multiple</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Multiple</em>' attribute.
   * @see #getMultiple()
   * @generated
   */
    void setMultiple(int value);

    /**
   * Returns the value of the '<em><b>Agents</b></em>' containment reference list.
   * The list contents are of type {@link urban.urban.Expression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Agents</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Agents</em>' containment reference list.
   * @see urban.urban.UrbanPackage#getInitialise_Agents()
   * @model containment="true"
   * @generated
   */
    EList<Expression> getAgents();
}
