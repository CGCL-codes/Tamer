package net.jonbuck.tassoo.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statuses</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.jonbuck.tassoo.model.Statuses#getStatus <em>Status</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.jonbuck.tassoo.model.TassooPackage#getStatuses()
 * @model
 * @generated
 */
public interface Statuses extends EObject {

    /**
	 * Returns the value of the '<em><b>Status</b></em>' containment reference list.
	 * The list contents are of type {@link net.jonbuck.tassoo.model.Status}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' containment reference list.
	 * @see net.jonbuck.tassoo.model.TassooPackage#getStatuses_Status()
	 * @model containment="true"
	 * @generated
	 */
    EList<Status> getStatus();
}
