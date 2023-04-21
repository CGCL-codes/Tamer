package de.fraunhofer.isst.vts.axlang;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.fraunhofer.isst.vts.axlang.Model#getName <em>Name</em>}</li>
 *   <li>{@link de.fraunhofer.isst.vts.axlang.Model#getApplicationmodels <em>Applicationmodels</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.fraunhofer.isst.vts.axlang.AxlangPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {

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
   * @see de.fraunhofer.isst.vts.axlang.AxlangPackage#getModel_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link de.fraunhofer.isst.vts.axlang.Model#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Applicationmodels</b></em>' containment reference list.
   * The list contents are of type {@link de.fraunhofer.isst.vts.axlang.ApplicationModel}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Applicationmodels</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Applicationmodels</em>' containment reference list.
   * @see de.fraunhofer.isst.vts.axlang.AxlangPackage#getModel_Applicationmodels()
   * @model containment="true"
   * @generated
   */
    EList<ApplicationModel> getApplicationmodels();
}
