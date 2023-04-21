package fr.inria.papyrus.uml4tst.emftext.ocl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Path Name CS</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.inria.papyrus.uml4tst.emftext.ocl.PathNameCS#getSimpleName <em>Simple Name</em>}</li>
 *   <li>{@link fr.inria.papyrus.uml4tst.emftext.ocl.PathNameCS#getPathName <em>Path Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.inria.papyrus.uml4tst.emftext.ocl.OclPackage#getPathNameCS()
 * @model
 * @generated
 */
public interface PathNameCS extends EObject {

    /**
	 * Returns the value of the '<em><b>Simple Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Simple Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Name</em>' containment reference.
	 * @see #setSimpleName(SimpleNameCS)
	 * @see fr.inria.papyrus.uml4tst.emftext.ocl.OclPackage#getPathNameCS_SimpleName()
	 * @model containment="true" required="true"
	 * @generated
	 */
    SimpleNameCS getSimpleName();

    /**
	 * Sets the value of the '{@link fr.inria.papyrus.uml4tst.emftext.ocl.PathNameCS#getSimpleName <em>Simple Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Name</em>' containment reference.
	 * @see #getSimpleName()
	 * @generated
	 */
    void setSimpleName(SimpleNameCS value);

    /**
	 * Returns the value of the '<em><b>Path Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path Name</em>' containment reference.
	 * @see #setPathName(PathNameCS)
	 * @see fr.inria.papyrus.uml4tst.emftext.ocl.OclPackage#getPathNameCS_PathName()
	 * @model containment="true"
	 * @generated
	 */
    PathNameCS getPathName();

    /**
	 * Sets the value of the '{@link fr.inria.papyrus.uml4tst.emftext.ocl.PathNameCS#getPathName <em>Path Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path Name</em>' containment reference.
	 * @see #getPathName()
	 * @generated
	 */
    void setPathName(PathNameCS value);
}
