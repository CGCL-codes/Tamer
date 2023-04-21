package eu.medeia.ecore.apmm.tscm;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.tscm.PortAction#getPortChanges <em>Port Changes</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.tscm.PortAction#getMaterialflowChanges <em>Materialflow Changes</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.tscm.PortAction#getKinematicChanges <em>Kinematic Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.ecore.apmm.tscm.TscmPackage#getPortAction()
 * @model
 * @generated
 */
public interface PortAction extends Action {

    /**
	 * Returns the value of the '<em><b>Port Changes</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.tscm.PortChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Changes</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.tscm.TscmPackage#getPortAction_PortChanges()
	 * @model containment="true"
	 * @generated
	 */
    EList<PortChange> getPortChanges();

    /**
	 * Returns the value of the '<em><b>Materialflow Changes</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.tscm.MaterialflowChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Materialflow Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Materialflow Changes</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.tscm.TscmPackage#getPortAction_MaterialflowChanges()
	 * @model containment="true"
	 * @generated
	 */
    EList<MaterialflowChange> getMaterialflowChanges();

    /**
	 * Returns the value of the '<em><b>Kinematic Changes</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.tscm.KinematicChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kinematic Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kinematic Changes</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.tscm.TscmPackage#getPortAction_KinematicChanges()
	 * @model containment="true"
	 * @generated
	 */
    EList<KinematicChange> getKinematicChanges();
}
