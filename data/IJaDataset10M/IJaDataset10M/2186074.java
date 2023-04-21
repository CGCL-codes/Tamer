package tudresden.ocl20.pivot.examples.pml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import tudresden.ocl20.pivot.examples.pml.*;
import tudresden.ocl20.pivot.examples.pml.ExtensionPoint;
import tudresden.ocl20.pivot.examples.pml.Feature;
import tudresden.ocl20.pivot.examples.pml.JavaType;
import tudresden.ocl20.pivot.examples.pml.PMLFactory;
import tudresden.ocl20.pivot.examples.pml.PMLPackage;
import tudresden.ocl20.pivot.examples.pml.Plugin;
import tudresden.ocl20.pivot.examples.pml.Service;
import tudresden.ocl20.pivot.examples.pml.ServiceParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PMLFactoryImpl extends EFactoryImpl implements PMLFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static PMLFactory init() {
        try {
            PMLFactory thePMLFactory = (PMLFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.tu-dresden.de/ocl20/pivot/2007/pml");
            if (thePMLFactory != null) {
                return thePMLFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PMLFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PMLFactoryImpl() {
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
            case PMLPackage.PLUGIN:
                return createPlugin();
            case PMLPackage.FEATURE:
                return createFeature();
            case PMLPackage.EXTENSION_POINT:
                return createExtensionPoint();
            case PMLPackage.JAVA_TYPE:
                return createJavaType();
            case PMLPackage.SERVICE:
                return createService();
            case PMLPackage.SERVICE_PARAMETER:
                return createServiceParameter();
            case PMLPackage.PLUGIN_PACKAGE:
                return createPluginPackage();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case PMLPackage.SAMPLE_ENUM:
                return createSampleEnumFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case PMLPackage.SAMPLE_ENUM:
                return convertSampleEnumToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Plugin createPlugin() {
        PluginImpl plugin = new PluginImpl();
        return plugin;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Feature createFeature() {
        FeatureImpl feature = new FeatureImpl();
        return feature;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ExtensionPoint createExtensionPoint() {
        ExtensionPointImpl extensionPoint = new ExtensionPointImpl();
        return extensionPoint;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public JavaType createJavaType() {
        JavaTypeImpl javaType = new JavaTypeImpl();
        return javaType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Service createService() {
        ServiceImpl service = new ServiceImpl();
        return service;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceParameter createServiceParameter() {
        ServiceParameterImpl serviceParameter = new ServiceParameterImpl();
        return serviceParameter;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PluginPackage createPluginPackage() {
        PluginPackageImpl pluginPackage = new PluginPackageImpl();
        return pluginPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SampleEnum createSampleEnumFromString(EDataType eDataType, String initialValue) {
        SampleEnum result = SampleEnum.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertSampleEnumToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PMLPackage getPMLPackage() {
        return (PMLPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static PMLPackage getPackage() {
        return PMLPackage.eINSTANCE;
    }
}
