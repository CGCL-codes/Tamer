package net.sf.smbt.i2c.devices.smart.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import net.sf.smbt.i2c.I2cPackage;
import net.sf.smbt.i2c.devices.smart.SmartFactory;
import net.sf.smbt.i2c.devices.smart.SmartModule;
import net.sf.smbt.i2c.devices.smart.SmartObject;
import net.sf.smbt.i2c.devices.smart.SmartPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmartPackageImpl extends EPackageImpl implements SmartPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass smartObjectEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass smartModuleEClass = null;

    /**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see net.sf.smbt.i2c.devices.smart.SmartPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private SmartPackageImpl() {
        super(eNS_URI, SmartFactory.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link SmartPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static SmartPackage init() {
        if (isInited) return (SmartPackage) EPackage.Registry.INSTANCE.getEPackage(SmartPackage.eNS_URI);
        SmartPackageImpl theSmartPackage = (SmartPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SmartPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SmartPackageImpl());
        isInited = true;
        I2cPackage.eINSTANCE.eClass();
        theSmartPackage.createPackageContents();
        theSmartPackage.initializePackageContents();
        theSmartPackage.freeze();
        EPackage.Registry.INSTANCE.put(SmartPackage.eNS_URI, theSmartPackage);
        return theSmartPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSmartObject() {
        return smartObjectEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSmartObject_Name() {
        return (EAttribute) smartObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSmartObject_Id() {
        return (EAttribute) smartObjectEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartObject_Modules() {
        return (EReference) smartObjectEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartObject_Bus() {
        return (EReference) smartObjectEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartObject_Devices() {
        return (EReference) smartObjectEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSmartModule() {
        return smartModuleEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSmartModule_Addr() {
        return (EAttribute) smartModuleEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSmartModule_Name() {
        return (EAttribute) smartModuleEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartModule_SmartObject() {
        return (EReference) smartModuleEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartModule_Segment() {
        return (EReference) smartModuleEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartModule_Devices() {
        return (EReference) smartModuleEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartModule_Children() {
        return (EReference) smartModuleEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSmartModule_Parent() {
        return (EReference) smartModuleEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSmartModule_Id() {
        return (EAttribute) smartModuleEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SmartFactory getSmartFactory() {
        return (SmartFactory) getEFactoryInstance();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isCreated = false;

    /**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;
        smartObjectEClass = createEClass(SMART_OBJECT);
        createEAttribute(smartObjectEClass, SMART_OBJECT__NAME);
        createEAttribute(smartObjectEClass, SMART_OBJECT__ID);
        createEReference(smartObjectEClass, SMART_OBJECT__DEVICES);
        createEReference(smartObjectEClass, SMART_OBJECT__MODULES);
        createEReference(smartObjectEClass, SMART_OBJECT__BUS);
        smartModuleEClass = createEClass(SMART_MODULE);
        createEAttribute(smartModuleEClass, SMART_MODULE__ADDR);
        createEAttribute(smartModuleEClass, SMART_MODULE__NAME);
        createEReference(smartModuleEClass, SMART_MODULE__DEVICES);
        createEReference(smartModuleEClass, SMART_MODULE__CHILDREN);
        createEReference(smartModuleEClass, SMART_MODULE__PARENT);
        createEAttribute(smartModuleEClass, SMART_MODULE__ID);
        createEReference(smartModuleEClass, SMART_MODULE__SMART_OBJECT);
        createEReference(smartModuleEClass, SMART_MODULE__SEGMENT);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isInitialized = false;

    /**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);
        I2cPackage theI2cPackage = (I2cPackage) EPackage.Registry.INSTANCE.getEPackage(I2cPackage.eNS_URI);
        initEClass(smartObjectEClass, SmartObject.class, "SmartObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSmartObject_Name(), ecorePackage.getEString(), "name", null, 0, 1, SmartObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSmartObject_Id(), ecorePackage.getEString(), "id", null, 0, 1, SmartObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartObject_Devices(), theI2cPackage.getI2CDevice(), null, "devices", null, 0, -1, SmartObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartObject_Modules(), this.getSmartModule(), this.getSmartModule_SmartObject(), "modules", null, 0, -1, SmartObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartObject_Bus(), theI2cPackage.getI2CBus(), null, "bus", null, 1, 1, SmartObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(smartModuleEClass, SmartModule.class, "SmartModule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSmartModule_Addr(), ecorePackage.getEInt(), "addr", null, 0, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSmartModule_Name(), ecorePackage.getEString(), "name", null, 0, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartModule_Devices(), theI2cPackage.getI2CDevice(), null, "devices", null, 0, -1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartModule_Children(), this.getSmartModule(), this.getSmartModule_Parent(), "children", null, 0, -1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartModule_Parent(), this.getSmartModule(), this.getSmartModule_Children(), "parent", null, 1, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSmartModule_Id(), ecorePackage.getEString(), "id", null, 0, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartModule_SmartObject(), this.getSmartObject(), this.getSmartObject_Modules(), "smartObject", null, 0, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSmartModule_Segment(), theI2cPackage.getI2CBusSegment(), null, "segment", null, 1, 1, SmartModule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
