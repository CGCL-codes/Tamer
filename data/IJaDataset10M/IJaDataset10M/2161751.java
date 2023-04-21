package urban.kappa;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Number</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link urban.kappa.Number#getNint <em>Nint</em>}</li>
 *   <li>{@link urban.kappa.Number#getNdecimal <em>Ndecimal</em>}</li>
 * </ul>
 * </p>
 *
 * @see urban.kappa.KappaPackage#getNumber()
 * @model
 * @generated
 */
public interface Number extends EObject {

    /**
   * Returns the value of the '<em><b>Nint</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Nint</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Nint</em>' attribute.
   * @see #setNint(int)
   * @see urban.kappa.KappaPackage#getNumber_Nint()
   * @model
   * @generated
   */
    int getNint();

    /**
   * Sets the value of the '{@link urban.kappa.Number#getNint <em>Nint</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Nint</em>' attribute.
   * @see #getNint()
   * @generated
   */
    void setNint(int value);

    /**
   * Returns the value of the '<em><b>Ndecimal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ndecimal</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ndecimal</em>' attribute.
   * @see #setNdecimal(String)
   * @see urban.kappa.KappaPackage#getNumber_Ndecimal()
   * @model
   * @generated
   */
    String getNdecimal();

    /**
   * Sets the value of the '{@link urban.kappa.Number#getNdecimal <em>Ndecimal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ndecimal</em>' attribute.
   * @see #getNdecimal()
   * @generated
   */
    void setNdecimal(String value);
}
