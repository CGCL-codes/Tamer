package net.sourceforge.modelintegra.core.metamodel.mimodel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Traceability Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#getId <em>Id</em>}</li>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isFoundInSpec <em>Found In Spec</em>}</li>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isFoundInModel <em>Found In Model</em>}</li>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isObsolete <em>Obsolete</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getTraceabilityElement()
 * @model
 * @generated
 */
public interface TraceabilityElement extends MIClassifier {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Test";

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getTraceabilityElement_Id()
	 * @model
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Found In Spec</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Found In Spec</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Found In Spec</em>' attribute.
	 * @see #setFoundInSpec(boolean)
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getTraceabilityElement_FoundInSpec()
	 * @model
	 * @generated
	 */
    boolean isFoundInSpec();

    /**
	 * Sets the value of the '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isFoundInSpec <em>Found In Spec</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Found In Spec</em>' attribute.
	 * @see #isFoundInSpec()
	 * @generated
	 */
    void setFoundInSpec(boolean value);

    /**
	 * Returns the value of the '<em><b>Found In Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Found In Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Found In Model</em>' attribute.
	 * @see #setFoundInModel(boolean)
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getTraceabilityElement_FoundInModel()
	 * @model
	 * @generated
	 */
    boolean isFoundInModel();

    /**
	 * Sets the value of the '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isFoundInModel <em>Found In Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Found In Model</em>' attribute.
	 * @see #isFoundInModel()
	 * @generated
	 */
    void setFoundInModel(boolean value);

    /**
	 * Returns the value of the '<em><b>Obsolete</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Obsolete</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Obsolete</em>' attribute.
	 * @see #setObsolete(boolean)
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getTraceabilityElement_Obsolete()
	 * @model
	 * @generated
	 */
    boolean isObsolete();

    /**
	 * Sets the value of the '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.TraceabilityElement#isObsolete <em>Obsolete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Obsolete</em>' attribute.
	 * @see #isObsolete()
	 * @generated
	 */
    void setObsolete(boolean value);
}
