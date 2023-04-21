package tudresden.ocl20.pivot.essentialocl.types;

import org.eclipse.emf.ecore.EObject;
import tudresden.ocl20.pivot.essentialocl.expressions.CollectionKind;
import java.util.List;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.TypeParameter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getElementType <em>Element Type</em>}</li>
 *   <li>{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getOclLibrary <em>Ocl Library</em>}</li>
 *   <li>{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface CollectionType extends Type {

    /**
	 * Returns the value of the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Type</em>' reference.
	 * @see #setElementType(Type)
	 * @generated
	 */
    Type getElementType();

    /**
	 * Sets the value of the '{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getElementType <em>Element Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element Type</em>' reference.
	 * @see #getElementType()
	 * @generated
	 */
    void setElementType(Type value);

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
	 * a <code>CollectionType</code> is created, the reference to the 
	 * <code>OclLibrary</code> should be set. Note that the old toolkit
	 * used a  Singleton approach which is not repeated here to maintain
	 * clear interfaces.
	 * </p>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ocl Library</em>' reference.
	 * @see #setOclLibrary(OclLibrary)
	 * @generated
	 */
    OclLibrary getOclLibrary();

    /**
	 * Sets the value of the '{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getOclLibrary <em>Ocl Library</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ocl Library</em>' reference.
	 * @see #getOclLibrary()
	 * @generated
	 */
    void setOclLibrary(OclLibrary value);

    /**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link tudresden.ocl20.pivot.essentialocl.expressions.CollectionKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * <p>
	 * Identifies the kind of this <code>CollectionType</code>. This is necessary
	 * to clearly differentiate an instance of <code>CollectionType</code> from
	 * instances of its subclasses. This is required when determining type conformance
	 * of one collection type with another (see the documentation of {@link #conformsTo}).
	 * Unfortunately, there is no equivalent in Java for the OCL <code>oclIsTypeOf</code>
	 * operation. The <code>instanceof</code> operator cannot be used since it would
	 * return <code>true</code> for subclasses as well.
	 * </p>
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.essentialocl.expressions.CollectionKind
	 * @see #setKind(CollectionKind)
	 * @generated
	 */
    CollectionKind getKind();

    /**
	 * Sets the value of the '{@link tudresden.ocl20.pivot.essentialocl.types.CollectionType#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see tudresden.ocl20.pivot.essentialocl.expressions.CollectionKind
	 * @see #getKind()
	 * @generated
	 */
    void setKind(CollectionKind value);

    /**
	 * Overridden to specialize the co-variant return type to <code>CollectionType</code>.
	 * 
	 * @see tudresden.ocl20.pivot.pivotmodel.GenericElement#bindTypeParameter(java.util.List, java.util.List)
	 */
    CollectionType bindTypeParameter(List<TypeParameter> parameters, List<? extends Type> types);
}
