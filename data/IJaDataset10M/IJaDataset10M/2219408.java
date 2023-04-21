package com.sodad.designer.modelapplier.model.emf.modelapplier.impl;

import com.sodad.designer.modelapplier.model.emf.modelapplier.*;
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
public class ModelapplierFactoryImpl extends EFactoryImpl implements ModelapplierFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static ModelapplierFactory init() {
        try {
            ModelapplierFactory theModelapplierFactory = (ModelapplierFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.sodad.com/modelappliermap");
            if (theModelapplierFactory != null) {
                return theModelapplierFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ModelapplierFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelapplierFactoryImpl() {
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
            case ModelapplierPackage.MODEL_APPLIER_MAP_DATA:
                return createModelApplierMapData();
            case ModelapplierPackage.MODEL_APPLIER_MAP_LINK:
                return createModelApplierMapLink();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelApplierMapData createModelApplierMapData() {
        ModelApplierMapDataImpl modelApplierMapData = new ModelApplierMapDataImpl();
        return modelApplierMapData;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelApplierMapLink createModelApplierMapLink() {
        ModelApplierMapLinkImpl modelApplierMapLink = new ModelApplierMapLinkImpl();
        return modelApplierMapLink;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelapplierPackage getModelapplierPackage() {
        return (ModelapplierPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static ModelapplierPackage getPackage() {
        return ModelapplierPackage.eINSTANCE;
    }
}
