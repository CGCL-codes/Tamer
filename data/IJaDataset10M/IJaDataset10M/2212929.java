package jolie.xtext.jolie;

import java.lang.String;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jolie.xtext.jolie.Type#getName <em>Name</em>}</li>
 *   <li>{@link jolie.xtext.jolie.Type#getTypedef <em>Typedef</em>}</li>
 * </ul>
 * </p>
 *
 * @see jolie.xtext.jolie.JoliePackage#getType()
 * @model
 * @generated
 */
public interface Type extends EObject {

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
   * @see jolie.xtext.jolie.JoliePackage#getType_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link jolie.xtext.jolie.Type#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Typedef</b></em>' containment reference list.
   * The list contents are of type {@link jolie.xtext.jolie.Typedef}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Typedef</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Typedef</em>' containment reference list.
   * @see jolie.xtext.jolie.JoliePackage#getType_Typedef()
   * @model containment="true"
   * @generated
   */
    EList<Typedef> getTypedef();
}
