package de.fraunhofer.isst.eastadl.featuremodeling.impl;

import de.fraunhofer.isst.eastadl.featuremodeling.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class FeaturemodelingFactoryImpl extends EFactoryImpl implements FeaturemodelingFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static FeaturemodelingFactory init() {
        try {
            FeaturemodelingFactory theFeaturemodelingFactory = (FeaturemodelingFactory) EPackage.Registry.INSTANCE.getEFactory("http:///de/fraunhofer/isst/eastadl/featuremodeling.ecore");
            if (theFeaturemodelingFactory != null) {
                return theFeaturemodelingFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new FeaturemodelingFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeaturemodelingFactoryImpl() {
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
            case FeaturemodelingPackage.BINDING_TIME:
                return createBindingTime();
            case FeaturemodelingPackage.FEATURE:
                return createFeature();
            case FeaturemodelingPackage.FEATURE_CONSTRAINT:
                return createFeatureConstraint();
            case FeaturemodelingPackage.FEATURE_GROUP:
                return createFeatureGroup();
            case FeaturemodelingPackage.FEATURE_LINK:
                return createFeatureLink();
            case FeaturemodelingPackage.FEATURE_MODEL:
                return createFeatureModel();
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
            case FeaturemodelingPackage.BINDING_TIME_KIND:
                return createBindingTimeKindFromString(eDataType, initialValue);
            case FeaturemodelingPackage.VARIABILITY_DEPENDENCY_KIND:
                return createVariabilityDependencyKindFromString(eDataType, initialValue);
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
            case FeaturemodelingPackage.BINDING_TIME_KIND:
                return convertBindingTimeKindToString(eDataType, instanceValue);
            case FeaturemodelingPackage.VARIABILITY_DEPENDENCY_KIND:
                return convertVariabilityDependencyKindToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BindingTime createBindingTime() {
        BindingTimeImpl bindingTime = new BindingTimeImpl();
        return bindingTime;
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
    public FeatureConstraint createFeatureConstraint() {
        FeatureConstraintImpl featureConstraint = new FeatureConstraintImpl();
        return featureConstraint;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureGroup createFeatureGroup() {
        FeatureGroupImpl featureGroup = new FeatureGroupImpl();
        return featureGroup;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureLink createFeatureLink() {
        FeatureLinkImpl featureLink = new FeatureLinkImpl();
        return featureLink;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureModel createFeatureModel() {
        FeatureModelImpl featureModel = new FeatureModelImpl();
        return featureModel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BindingTimeKind createBindingTimeKindFromString(EDataType eDataType, String initialValue) {
        BindingTimeKind result = BindingTimeKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertBindingTimeKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VariabilityDependencyKind createVariabilityDependencyKindFromString(EDataType eDataType, String initialValue) {
        VariabilityDependencyKind result = VariabilityDependencyKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertVariabilityDependencyKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeaturemodelingPackage getFeaturemodelingPackage() {
        return (FeaturemodelingPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static FeaturemodelingPackage getPackage() {
        return FeaturemodelingPackage.eINSTANCE;
    }
}
