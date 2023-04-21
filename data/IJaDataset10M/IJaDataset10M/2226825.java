package de.fraunhofer.isst.eastadl.functionmodeling;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import de.fraunhofer.isst.eastadl.elements.EAElement;

/**
 * <strong>AllocateableElement �instanceRef�</strong>
 * <p>
 * An �instanceRef� of an AllocateableElement.<br>
 * Used by member <strong>allocatedElement</strong> in class <strong>FunctionAllocation</strong>
 * <p>
 * A AllocateableElement �instanceRef� consists of<br>
 * - the path (List &lt EAElement &gt ) leading to the referenced object and<br>
 * - the type (AllocateableElement) of the referenced object.
 * 
 * @author dprenzel
 *
 * @model
 */
public interface AllocateableElementInstanceRef extends EObject {

    /**
	 * Returns the value of the '<em><b>Path</b></em>' reference list.
	 * The list contents are of type {@link de.fraunhofer.isst.eastadl.elements.EAElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>path : EAElement [0..*]</strong>
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' reference list.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionmodelingPackage#getAllocateableElementInstanceRef_Path()
	 * @model
	 */
    EList<EAElement> getPath();

    /**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>type : AllocateableElement [1]</strong>
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(AllocateableElement)
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionmodelingPackage#getAllocateableElementInstanceRef_Type()
	 * @model required="true"
	 */
    AllocateableElement getType();

    /**
	 * Sets the value of the '{@link de.fraunhofer.isst.eastadl.functionmodeling.AllocateableElementInstanceRef#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>type : AllocateableElement [1]</strong>
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
    void setType(AllocateableElement value);
}
