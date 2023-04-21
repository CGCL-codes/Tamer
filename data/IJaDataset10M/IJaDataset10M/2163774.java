package jolie.xtext.jolie;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assign Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jolie.xtext.jolie.AssignStatement#getAssignStatement <em>Assign Statement</em>}</li>
 *   <li>{@link jolie.xtext.jolie.AssignStatement#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see jolie.xtext.jolie.JoliePackage#getAssignStatement()
 * @model
 * @generated
 */
public interface AssignStatement extends BasicStatement {

    /**
   * Returns the value of the '<em><b>Assign Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Assign Statement</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Assign Statement</em>' containment reference.
   * @see #setAssignStatement(AssignStatement)
   * @see jolie.xtext.jolie.JoliePackage#getAssignStatement_AssignStatement()
   * @model containment="true"
   * @generated
   */
    AssignStatement getAssignStatement();

    /**
   * Sets the value of the '{@link jolie.xtext.jolie.AssignStatement#getAssignStatement <em>Assign Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Assign Statement</em>' containment reference.
   * @see #getAssignStatement()
   * @generated
   */
    void setAssignStatement(AssignStatement value);

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
   * @see jolie.xtext.jolie.JoliePackage#getAssignStatement_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link jolie.xtext.jolie.AssignStatement#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);
}
