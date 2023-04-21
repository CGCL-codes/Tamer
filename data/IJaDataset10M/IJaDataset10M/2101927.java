package eu.medeia.ecore.apmm.impl;

import eu.medeia.ecore.apmm.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ApmmFactoryImpl extends EFactoryImpl implements ApmmFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static ApmmFactory init() {
        try {
            ApmmFactory theApmmFactory = (ApmmFactory) EPackage.Registry.INSTANCE.getEFactory("eu.medeia.ecore.apmm");
            if (theApmmFactory != null) {
                return theApmmFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ApmmFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ApmmFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case ApmmPackage.AUTOMATION_PROJECT:
                return createAutomationProject();
            case ApmmPackage.AUTOMATION_APPLICATION:
                return createAutomationApplication();
            case ApmmPackage.MAPPING_CONFIGURATION:
                return createMappingConfiguration();
            case ApmmPackage.SYSTEM_CONFIGURATION:
                return createSystemConfiguration();
            case ApmmPackage.LIBRARY:
                return createLibrary();
            case ApmmPackage.LIBRARYELEMENT:
                return createLibraryelement();
            case ApmmPackage.AUTOMATION_COMPONENT_TYPE:
                return createAutomationComponentType();
            case ApmmPackage.AUTOMATION_COMPONENT_IMPLEMENTATION_TYPE:
                return createAutomationComponentImplementationType();
            case ApmmPackage.EXECUTION_SYSTEM_TYPE:
                return createExecutionSystemType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationProject createAutomationProject() {
        AutomationProjectImpl automationProject = new AutomationProjectImpl();
        return automationProject;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationApplication createAutomationApplication() {
        AutomationApplicationImpl automationApplication = new AutomationApplicationImpl();
        return automationApplication;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MappingConfiguration createMappingConfiguration() {
        MappingConfigurationImpl mappingConfiguration = new MappingConfigurationImpl();
        return mappingConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SystemConfiguration createSystemConfiguration() {
        SystemConfigurationImpl systemConfiguration = new SystemConfigurationImpl();
        return systemConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Library createLibrary() {
        LibraryImpl library = new LibraryImpl();
        return library;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Libraryelement createLibraryelement() {
        LibraryelementImpl libraryelement = new LibraryelementImpl();
        return libraryelement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationComponentType createAutomationComponentType() {
        AutomationComponentTypeImpl automationComponentType = new AutomationComponentTypeImpl();
        return automationComponentType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationComponentImplementationType createAutomationComponentImplementationType() {
        AutomationComponentImplementationTypeImpl automationComponentImplementationType = new AutomationComponentImplementationTypeImpl();
        return automationComponentImplementationType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ExecutionSystemType createExecutionSystemType() {
        ExecutionSystemTypeImpl executionSystemType = new ExecutionSystemTypeImpl();
        return executionSystemType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ApmmPackage getApmmPackage() {
        return (ApmmPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static ApmmPackage getPackage() {
        return ApmmPackage.eINSTANCE;
    }
}
