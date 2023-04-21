package org.modelversioning.core.diff.copydiff;

import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Copy Element Left Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A "LeftTarget" element copy describes a difference involving the left element/resource. In the case of CopyElements, this can describe either the copy of an element or the remote removal of the copy (for three way comparisons).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.modelversioning.core.diff.copydiff.CopyElementLeftTarget#getCopiedRightElement <em>Copied Right Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.modelversioning.core.diff.copydiff.CopyDiffPackage#getCopyElementLeftTarget()
 * @model
 * @generated
 */
public interface CopyElementLeftTarget extends ModelElementChangeLeftTarget {

    /**
	 * Returns the value of the '<em><b>Copied Right Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Copied Right Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Copied Right Element</em>' reference.
	 * @see #setCopiedRightElement(EObject)
	 * @see org.modelversioning.core.diff.copydiff.CopyDiffPackage#getCopyElementLeftTarget_CopiedRightElement()
	 * @model
	 * @generated
	 */
    EObject getCopiedRightElement();

    /**
	 * Sets the value of the '{@link org.modelversioning.core.diff.copydiff.CopyElementLeftTarget#getCopiedRightElement <em>Copied Right Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Copied Right Element</em>' reference.
	 * @see #getCopiedRightElement()
	 * @generated
	 */
    void setCopiedRightElement(EObject value);
}
