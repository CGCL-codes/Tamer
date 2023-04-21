package tudresden.ocl20.pivot.essentialocl.types;

import org.eclipse.emf.ecore.EObject;
import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tuple Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tudresden.ocl20.pivot.essentialocl.types.TupleType#getOclLibrary <em>Ocl Library</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface TupleType extends Type {

    /**
	 * Returns the value of the '<em><b>Ocl Library</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ocl Library</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * The reference to the {@link OclLibrary} facade class is necessary
	 * for determining type conformance and common super types. This
	 * implementation uses a dependency injection approach. Whenever
	 * a <code>TupleType</code> is created, the reference to the 
	 * <code>OclLibrary</code> should be set. Note that the old toolkit
	 * ised a  Singleton approach which is not repeated here to maintain
	 * clear interfaces.
	 * </p>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ocl Library</em>' reference.
	 * @see #setOclLibrary(OclLibrary)
	 * @generated
	 */
    OclLibrary getOclLibrary();

    /**
	 * Sets the value of the '{@link tudresden.ocl20.pivot.essentialocl.types.TupleType#getOclLibrary <em>Ocl Library</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ocl Library</em>' reference.
	 * @see #getOclLibrary()
	 * @generated
	 */
    void setOclLibrary(OclLibrary value);
}
