package ProjectWizard;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Info</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ProjectWizard.ProjectInfo#getExampleType <em>Example Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see ProjectWizard.ProjectWizardPackage#getProjectInfo()
 * @model
 * @generated
 */
public interface ProjectInfo extends EObject {

    /**
	 * Returns the value of the '<em><b>Example Type</b></em>' attribute.
	 * The literals are from the enumeration {@link ProjectWizard.ProjectType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Example Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Example Type</em>' attribute.
	 * @see ProjectWizard.ProjectType
	 * @see #setExampleType(ProjectType)
	 * @see ProjectWizard.ProjectWizardPackage#getProjectInfo_ExampleType()
	 * @model
	 * @generated
	 */
    ProjectType getExampleType();

    /**
	 * Sets the value of the '{@link ProjectWizard.ProjectInfo#getExampleType <em>Example Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Example Type</em>' attribute.
	 * @see ProjectWizard.ProjectType
	 * @see #getExampleType()
	 * @generated
	 */
    void setExampleType(ProjectType value);
}
