package net.sf.smbt.i2c.devices.smart;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see net.sf.smbt.i2c.devices.smart.SmartFactory
 * @model kind="package"
 * @generated
 */
public interface SmartPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "smart";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://www.eclipse.org/smartobjects/smart/1.0.0";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "smart";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    SmartPackage eINSTANCE = net.sf.smbt.i2c.devices.smart.impl.SmartPackageImpl.init();

    /**
	 * The meta object id for the '{@link net.sf.smbt.i2c.devices.smart.impl.SmartObjectImpl <em>Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.i2c.devices.smart.impl.SmartObjectImpl
	 * @see net.sf.smbt.i2c.devices.smart.impl.SmartPackageImpl#getSmartObject()
	 * @generated
	 */
    int SMART_OBJECT = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT__NAME = 0;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT__ID = 1;

    /**
	 * The feature id for the '<em><b>Devices</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT__DEVICES = 2;

    /**
	 * The feature id for the '<em><b>Modules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT__MODULES = 3;

    /**
	 * The feature id for the '<em><b>Bus</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT__BUS = 4;

    /**
	 * The number of structural features of the '<em>Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_OBJECT_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl <em>Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl
	 * @see net.sf.smbt.i2c.devices.smart.impl.SmartPackageImpl#getSmartModule()
	 * @generated
	 */
    int SMART_MODULE = 1;

    /**
	 * The feature id for the '<em><b>Addr</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__ADDR = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__NAME = 1;

    /**
	 * The feature id for the '<em><b>Devices</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__DEVICES = 2;

    /**
	 * The feature id for the '<em><b>Children</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__CHILDREN = 3;

    /**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__PARENT = 4;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__ID = 5;

    /**
	 * The feature id for the '<em><b>Smart Object</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__SMART_OBJECT = 6;

    /**
	 * The feature id for the '<em><b>Segment</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE__SEGMENT = 7;

    /**
	 * The number of structural features of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SMART_MODULE_FEATURE_COUNT = 8;

    /**
	 * Returns the meta object for class '{@link net.sf.smbt.i2c.devices.smart.SmartObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject
	 * @generated
	 */
    EClass getSmartObject();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.i2c.devices.smart.SmartObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject#getName()
	 * @see #getSmartObject()
	 * @generated
	 */
    EAttribute getSmartObject_Name();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.i2c.devices.smart.SmartObject#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject#getId()
	 * @see #getSmartObject()
	 * @generated
	 */
    EAttribute getSmartObject_Id();

    /**
	 * Returns the meta object for the containment reference list '{@link net.sf.smbt.i2c.devices.smart.SmartObject#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modules</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject#getModules()
	 * @see #getSmartObject()
	 * @generated
	 */
    EReference getSmartObject_Modules();

    /**
	 * Returns the meta object for the containment reference '{@link net.sf.smbt.i2c.devices.smart.SmartObject#getBus <em>Bus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bus</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject#getBus()
	 * @see #getSmartObject()
	 * @generated
	 */
    EReference getSmartObject_Bus();

    /**
	 * Returns the meta object for the reference list '{@link net.sf.smbt.i2c.devices.smart.SmartObject#getDevices <em>Devices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Devices</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartObject#getDevices()
	 * @see #getSmartObject()
	 * @generated
	 */
    EReference getSmartObject_Devices();

    /**
	 * Returns the meta object for class '{@link net.sf.smbt.i2c.devices.smart.SmartModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule
	 * @generated
	 */
    EClass getSmartModule();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getAddr <em>Addr</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Addr</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getAddr()
	 * @see #getSmartModule()
	 * @generated
	 */
    EAttribute getSmartModule_Addr();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getName()
	 * @see #getSmartModule()
	 * @generated
	 */
    EAttribute getSmartModule_Name();

    /**
	 * Returns the meta object for the container reference '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getSmartObject <em>Smart Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Smart Object</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getSmartObject()
	 * @see #getSmartModule()
	 * @generated
	 */
    EReference getSmartModule_SmartObject();

    /**
	 * Returns the meta object for the reference '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getSegment <em>Segment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Segment</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getSegment()
	 * @see #getSmartModule()
	 * @generated
	 */
    EReference getSmartModule_Segment();

    /**
	 * Returns the meta object for the reference list '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getDevices <em>Devices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Devices</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getDevices()
	 * @see #getSmartModule()
	 * @generated
	 */
    EReference getSmartModule_Devices();

    /**
	 * Returns the meta object for the reference list '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Children</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getChildren()
	 * @see #getSmartModule()
	 * @generated
	 */
    EReference getSmartModule_Children();

    /**
	 * Returns the meta object for the reference '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getParent()
	 * @see #getSmartModule()
	 * @generated
	 */
    EReference getSmartModule_Parent();

    /**
	 * Returns the meta object for the attribute '{@link net.sf.smbt.i2c.devices.smart.SmartModule#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see net.sf.smbt.i2c.devices.smart.SmartModule#getId()
	 * @see #getSmartModule()
	 * @generated
	 */
    EAttribute getSmartModule_Id();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    SmartFactory getSmartFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link net.sf.smbt.i2c.devices.smart.impl.SmartObjectImpl <em>Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.i2c.devices.smart.impl.SmartObjectImpl
		 * @see net.sf.smbt.i2c.devices.smart.impl.SmartPackageImpl#getSmartObject()
		 * @generated
		 */
        EClass SMART_OBJECT = eINSTANCE.getSmartObject();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SMART_OBJECT__NAME = eINSTANCE.getSmartObject_Name();

        /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SMART_OBJECT__ID = eINSTANCE.getSmartObject_Id();

        /**
		 * The meta object literal for the '<em><b>Modules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_OBJECT__MODULES = eINSTANCE.getSmartObject_Modules();

        /**
		 * The meta object literal for the '<em><b>Bus</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_OBJECT__BUS = eINSTANCE.getSmartObject_Bus();

        /**
		 * The meta object literal for the '<em><b>Devices</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_OBJECT__DEVICES = eINSTANCE.getSmartObject_Devices();

        /**
		 * The meta object literal for the '{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl <em>Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl
		 * @see net.sf.smbt.i2c.devices.smart.impl.SmartPackageImpl#getSmartModule()
		 * @generated
		 */
        EClass SMART_MODULE = eINSTANCE.getSmartModule();

        /**
		 * The meta object literal for the '<em><b>Addr</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SMART_MODULE__ADDR = eINSTANCE.getSmartModule_Addr();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SMART_MODULE__NAME = eINSTANCE.getSmartModule_Name();

        /**
		 * The meta object literal for the '<em><b>Smart Object</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_MODULE__SMART_OBJECT = eINSTANCE.getSmartModule_SmartObject();

        /**
		 * The meta object literal for the '<em><b>Segment</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_MODULE__SEGMENT = eINSTANCE.getSmartModule_Segment();

        /**
		 * The meta object literal for the '<em><b>Devices</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_MODULE__DEVICES = eINSTANCE.getSmartModule_Devices();

        /**
		 * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_MODULE__CHILDREN = eINSTANCE.getSmartModule_Children();

        /**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SMART_MODULE__PARENT = eINSTANCE.getSmartModule_Parent();

        /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SMART_MODULE__ID = eINSTANCE.getSmartModule_Id();
    }
}
