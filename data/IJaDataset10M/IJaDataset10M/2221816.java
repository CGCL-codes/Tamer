package tdmodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Suite</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tdmodel.Suite#getName <em>Name</em>}</li>
 *   <li>{@link tdmodel.Suite#getUid <em>Uid</em>}</li>
 *   <li>{@link tdmodel.Suite#getDefinition <em>Definition</em>}</li>
 *   <li>{@link tdmodel.Suite#getDescription <em>Description</em>}</li>
 *   <li>{@link tdmodel.Suite#getModel <em>Model</em>}</li>
 *   <li>{@link tdmodel.Suite#getInitialModelInstance <em>Initial Model Instance</em>}</li>
 *   <li>{@link tdmodel.Suite#getTestNameDefinition <em>Test Name Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see tdmodel.TdmodelPackage#getSuite()
 * @model
 * @generated
 */
public interface Suite extends EObject {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see tdmodel.TdmodelPackage#getSuite_Name()
	 * @model dataType="tdmodel.String" ordered="false"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Uid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uid</em>' attribute.
	 * @see #setUid(String)
	 * @see tdmodel.TdmodelPackage#getSuite_Uid()
	 * @model dataType="tdmodel.String" ordered="false"
	 * @generated
	 */
    String getUid();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getUid <em>Uid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uid</em>' attribute.
	 * @see #getUid()
	 * @generated
	 */
    void setUid(String value);

    /**
	 * Returns the value of the '<em><b>Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' attribute.
	 * @see #setDefinition(String)
	 * @see tdmodel.TdmodelPackage#getSuite_Definition()
	 * @model dataType="tdmodel.String" ordered="false"
	 * @generated
	 */
    String getDefinition();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getDefinition <em>Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' attribute.
	 * @see #getDefinition()
	 * @generated
	 */
    void setDefinition(String value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see tdmodel.TdmodelPackage#getSuite_Description()
	 * @model dataType="tdmodel.String" ordered="false"
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #isSetModel()
	 * @see tdmodel.TdmodelPackage#getSuite_Model()
	 * @model unsettable="true" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
	 *        annotation="http://www.eclipse.org/ocl/examples/OCL derive='Project.allInstances()->any(p | p.testSuites->includes(self)).model'"
	 * @generated
	 */
    ActionModelProvider getModel();

    /**
	 * Returns whether the value of the '{@link tdmodel.Suite#getModel <em>Model</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Model</em>' reference is set.
	 * @see #getModel()
	 * @generated
	 */
    boolean isSetModel();

    /**
	 * Returns the value of the '<em><b>Initial Model Instance</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Model Instance</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Model Instance</em>' containment reference.
	 * @see #setInitialModelInstance(ModelInstance)
	 * @see tdmodel.TdmodelPackage#getSuite_InitialModelInstance()
	 * @model containment="true" required="true" ordered="false"
	 * @generated
	 */
    ModelInstance getInitialModelInstance();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getInitialModelInstance <em>Initial Model Instance</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Model Instance</em>' containment reference.
	 * @see #getInitialModelInstance()
	 * @generated
	 */
    void setInitialModelInstance(ModelInstance value);

    /**
	 * Returns the value of the '<em><b>Test Name Definition</b></em>' attribute.
	 * The literals are from the enumeration {@link tdmodel.TestNameDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test Name Definition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Test Name Definition</em>' attribute.
	 * @see tdmodel.TestNameDefinition
	 * @see #setTestNameDefinition(TestNameDefinition)
	 * @see tdmodel.TdmodelPackage#getSuite_TestNameDefinition()
	 * @model ordered="false"
	 * @generated
	 */
    TestNameDefinition getTestNameDefinition();

    /**
	 * Sets the value of the '{@link tdmodel.Suite#getTestNameDefinition <em>Test Name Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Test Name Definition</em>' attribute.
	 * @see tdmodel.TestNameDefinition
	 * @see #getTestNameDefinition()
	 * @generated
	 */
    void setTestNameDefinition(TestNameDefinition value);
}
