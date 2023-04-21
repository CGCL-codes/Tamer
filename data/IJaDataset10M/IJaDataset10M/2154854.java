package jolie.xtext.jolie;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jolie.xtext.jolie.Program#getMain <em>Main</em>}</li>
 * </ul>
 * </p>
 *
 * @see jolie.xtext.jolie.JoliePackage#getProgram()
 * @model
 * @generated
 */
public interface Program extends EObject {

    /**
   * Returns the value of the '<em><b>Main</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Main</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Main</em>' containment reference.
   * @see #setMain(Main)
   * @see jolie.xtext.jolie.JoliePackage#getProgram_Main()
   * @model containment="true"
   * @generated
   */
    Main getMain();

    /**
   * Sets the value of the '{@link jolie.xtext.jolie.Program#getMain <em>Main</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Main</em>' containment reference.
   * @see #getMain()
   * @generated
   */
    void setMain(Main value);
}
