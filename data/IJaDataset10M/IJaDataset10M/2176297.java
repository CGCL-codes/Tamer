package org.parallelj.mda.controlflow.model.controlflow;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Meta Information Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.MetaInformationContainer#getMetaInformation <em>Meta Information</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage#getMetaInformationContainer()
 * @model
 * @generated
 */
public interface MetaInformationContainer extends EObject {

    /**
	 * Returns the value of the '<em><b>Meta Information</b></em>' containment reference list.
	 * The list contents are of type {@link org.parallelj.mda.controlflow.model.controlflow.MetaInformation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Meta Information</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Meta Information</em>' containment reference list.
	 * @see org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage#getMetaInformationContainer_MetaInformation()
	 * @model containment="true"
	 * @generated
	 */
    EList<MetaInformation> getMetaInformation();
}
