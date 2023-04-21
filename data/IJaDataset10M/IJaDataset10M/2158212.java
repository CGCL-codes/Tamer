package hub.metrik.lang.eprovide.usertrace;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see hub.metrik.lang.eprovide.usertrace.UsertracePackage
 * @generated
 */
public interface UsertraceFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    UsertraceFactory eINSTANCE = hub.metrik.lang.eprovide.usertrace.impl.UsertraceFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>User Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>User Trace</em>'.
	 * @generated
	 */
    UserTrace createUserTrace();

    /**
	 * Returns a new object of class '<em>Step</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Step</em>'.
	 * @generated
	 */
    Step createStep();

    /**
	 * Returns a new object of class '<em>Instance Corresponce</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Instance Corresponce</em>'.
	 * @generated
	 */
    InstanceCorresponce createInstanceCorresponce();

    /**
	 * Returns a new object of class '<em>Attribute Correspondence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute Correspondence</em>'.
	 * @generated
	 */
    AttributeCorrespondence createAttributeCorrespondence();

    /**
	 * Returns a new object of class '<em>Reference Correspondence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reference Correspondence</em>'.
	 * @generated
	 */
    ReferenceCorrespondence createReferenceCorrespondence();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    UsertracePackage getUsertracePackage();
}
