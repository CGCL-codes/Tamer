package net.sourceforge.modelintegra.core.metamodel.mimodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.Requirement#getUc <em>Uc</em>}</li>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.Requirement#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getRequirement()
 * @model
 * @generated
 */
public interface Requirement extends TraceabilityElement {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Test";

    /**
	 * Returns the value of the '<em><b>Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.Model#getRequirement <em>Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' container reference.
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getRequirement_Model()
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.Model#getRequirement
	 * @model opposite="requirement" transient="false" changeable="false"
	 * @generated
	 */
    Model getModel();

    /**
	 * Returns the value of the '<em><b>Uc</b></em>' reference list.
	 * The list contents are of type {@link net.sourceforge.modelintegra.core.metamodel.mimodel.UC}.
	 * It is bidirectional and its opposite is '{@link net.sourceforge.modelintegra.core.metamodel.mimodel.UC#getRequirement <em>Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uc</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uc</em>' reference list.
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage#getRequirement_Uc()
	 * @see net.sourceforge.modelintegra.core.metamodel.mimodel.UC#getRequirement
	 * @model type="net.sourceforge.modelintegra.core.metamodel.mimodel.UC" opposite="requirement"
	 * @generated
	 */
    EList getUc();
}
