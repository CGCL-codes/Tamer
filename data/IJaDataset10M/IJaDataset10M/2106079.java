package tdmodel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tdmodel.Project#getModelProvider <em>Model Provider</em>}</li>
 *   <li>{@link tdmodel.Project#getTestSuites <em>Test Suites</em>}</li>
 *   <li>{@link tdmodel.Project#getScenarios <em>Scenarios</em>}</li>
 * </ul>
 * </p>
 *
 * @see tdmodel.TdmodelPackage#getProject()
 * @model
 * @generated
 */
public interface Project extends EObject {

    /**
	 * Returns the value of the '<em><b>Model Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Provider</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Provider</em>' containment reference.
	 * @see #setModelProvider(ActionModelProvider)
	 * @see tdmodel.TdmodelPackage#getProject_ModelProvider()
	 * @model containment="true" required="true" ordered="false"
	 *        extendedMetaData="name='model'"
	 * @generated
	 */
    ActionModelProvider getModelProvider();

    /**
	 * Sets the value of the '{@link tdmodel.Project#getModelProvider <em>Model Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Provider</em>' containment reference.
	 * @see #getModelProvider()
	 * @generated
	 */
    void setModelProvider(ActionModelProvider value);

    /**
	 * Returns the value of the '<em><b>Test Suites</b></em>' containment reference list.
	 * The list contents are of type {@link tdmodel.Suite}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test Suites</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Test Suites</em>' containment reference list.
	 * @see tdmodel.TdmodelPackage#getProject_TestSuites()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Suite> getTestSuites();

    /**
	 * Returns the value of the '<em><b>Scenarios</b></em>' containment reference list.
	 * The list contents are of type {@link tdmodel.Scenario}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenarios</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenarios</em>' containment reference list.
	 * @see tdmodel.TdmodelPackage#getProject_Scenarios()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Scenario> getScenarios();
}
