package SBVR;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Projecting Formulation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link SBVR.ProjectingFormulation#getProjection <em>Projection</em>}</li>
 *   <li>{@link SBVR.ProjectingFormulation#getBindableTarget <em>Bindable Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see SBVR.SBVRPackage#getProjectingFormulation()
 * @model
 * @generated
 */
public interface ProjectingFormulation extends LogicalFormulation {

    /**
	 * Returns the value of the '<em><b>Projection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projection</em>' reference.
	 * @see #setProjection(Projection)
	 * @see SBVR.SBVRPackage#getProjectingFormulation_Projection()
	 * @model required="true"
	 * @generated
	 */
    Projection getProjection();

    /**
	 * Sets the value of the '{@link SBVR.ProjectingFormulation#getProjection <em>Projection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Projection</em>' reference.
	 * @see #getProjection()
	 * @generated
	 */
    void setProjection(Projection value);

    /**
	 * Returns the value of the '<em><b>Bindable Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bindable Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bindable Target</em>' reference.
	 * @see #setBindableTarget(BindableTarget)
	 * @see SBVR.SBVRPackage#getProjectingFormulation_BindableTarget()
	 * @model required="true"
	 * @generated
	 */
    BindableTarget getBindableTarget();

    /**
	 * Sets the value of the '{@link SBVR.ProjectingFormulation#getBindableTarget <em>Bindable Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bindable Target</em>' reference.
	 * @see #getBindableTarget()
	 * @generated
	 */
    void setBindableTarget(BindableTarget value);
}
