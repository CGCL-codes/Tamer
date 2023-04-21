package org.enml.devices.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.enml.devices.Device;
import org.enml.devices.DeviceType;
import org.enml.devices.DevicesFactory;
import org.enml.devices.DevicesPackage;
import org.enml.documents.DocumentsPackage;
import org.enml.documents.impl.DocumentsPackageImpl;
import org.enml.geo.GeoPackage;
import org.enml.geo.impl.GeoPackageImpl;
import org.enml.measures.MeasuresPackage;
import org.enml.measures.impl.MeasuresPackageImpl;
import org.enml.metainfo.MetainfoPackage;
import org.enml.metainfo.impl.MetainfoPackageImpl;
import org.enml.net.NetPackage;
import org.enml.net.impl.NetPackageImpl;
import org.enml.notes.NotesPackage;
import org.enml.notes.impl.NotesPackageImpl;
import org.enml.ops.OpsPackage;
import org.enml.ops.impl.OpsPackageImpl;
import org.enml.sources.SourcesPackage;
import org.enml.sources.impl.SourcesPackageImpl;
import org.enml.subjects.SubjectsPackage;
import org.enml.subjects.impl.SubjectsPackageImpl;
import org.enml.validity.ValidityPackage;
import org.enml.validity.impl.ValidityPackageImpl;
import org.enml.work.WorkPackage;
import org.enml.work.impl.WorkPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DevicesPackageImpl extends EPackageImpl implements DevicesPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "enml.org (C) 2007";

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass deviceEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum deviceTypeEEnum = null;

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
	 * @see org.enml.devices.DevicesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private DevicesPackageImpl() {
        super(eNS_URI, DevicesFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link DevicesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static DevicesPackage init() {
        if (isInited) return (DevicesPackage) EPackage.Registry.INSTANCE.getEPackage(DevicesPackage.eNS_URI);
        DevicesPackageImpl theDevicesPackage = (DevicesPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DevicesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DevicesPackageImpl());
        isInited = true;
        DocumentsPackageImpl theDocumentsPackage = (DocumentsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DocumentsPackage.eNS_URI) instanceof DocumentsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DocumentsPackage.eNS_URI) : DocumentsPackage.eINSTANCE);
        GeoPackageImpl theGeoPackage = (GeoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(GeoPackage.eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(GeoPackage.eNS_URI) : GeoPackage.eINSTANCE);
        MeasuresPackageImpl theMeasuresPackage = (MeasuresPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MeasuresPackage.eNS_URI) instanceof MeasuresPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MeasuresPackage.eNS_URI) : MeasuresPackage.eINSTANCE);
        MetainfoPackageImpl theMetainfoPackage = (MetainfoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MetainfoPackage.eNS_URI) instanceof MetainfoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetainfoPackage.eNS_URI) : MetainfoPackage.eINSTANCE);
        NetPackageImpl theNetPackage = (NetPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(NetPackage.eNS_URI) instanceof NetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(NetPackage.eNS_URI) : NetPackage.eINSTANCE);
        NotesPackageImpl theNotesPackage = (NotesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(NotesPackage.eNS_URI) instanceof NotesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(NotesPackage.eNS_URI) : NotesPackage.eINSTANCE);
        OpsPackageImpl theOpsPackage = (OpsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(OpsPackage.eNS_URI) instanceof OpsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OpsPackage.eNS_URI) : OpsPackage.eINSTANCE);
        SourcesPackageImpl theSourcesPackage = (SourcesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI) instanceof SourcesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI) : SourcesPackage.eINSTANCE);
        SubjectsPackageImpl theSubjectsPackage = (SubjectsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SubjectsPackage.eNS_URI) instanceof SubjectsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SubjectsPackage.eNS_URI) : SubjectsPackage.eINSTANCE);
        ValidityPackageImpl theValidityPackage = (ValidityPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ValidityPackage.eNS_URI) instanceof ValidityPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ValidityPackage.eNS_URI) : ValidityPackage.eINSTANCE);
        WorkPackageImpl theWorkPackage = (WorkPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(WorkPackage.eNS_URI) instanceof WorkPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WorkPackage.eNS_URI) : WorkPackage.eINSTANCE);
        theDevicesPackage.createPackageContents();
        theDocumentsPackage.createPackageContents();
        theGeoPackage.createPackageContents();
        theMeasuresPackage.createPackageContents();
        theMetainfoPackage.createPackageContents();
        theNetPackage.createPackageContents();
        theNotesPackage.createPackageContents();
        theOpsPackage.createPackageContents();
        theSourcesPackage.createPackageContents();
        theSubjectsPackage.createPackageContents();
        theValidityPackage.createPackageContents();
        theWorkPackage.createPackageContents();
        theDevicesPackage.initializePackageContents();
        theDocumentsPackage.initializePackageContents();
        theGeoPackage.initializePackageContents();
        theMeasuresPackage.initializePackageContents();
        theMetainfoPackage.initializePackageContents();
        theNetPackage.initializePackageContents();
        theNotesPackage.initializePackageContents();
        theOpsPackage.initializePackageContents();
        theSourcesPackage.initializePackageContents();
        theSubjectsPackage.initializePackageContents();
        theValidityPackage.initializePackageContents();
        theWorkPackage.initializePackageContents();
        theDevicesPackage.freeze();
        EPackage.Registry.INSTANCE.put(DevicesPackage.eNS_URI, theDevicesPackage);
        return theDevicesPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDevice() {
        return deviceEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDevice_Brand() {
        return (EAttribute) deviceEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDevice_Model() {
        return (EAttribute) deviceEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDevice_Serial() {
        return (EAttribute) deviceEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDevice_Type() {
        return (EAttribute) deviceEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDevice_Certifications() {
        return (EReference) deviceEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDevice_Life() {
        return (EReference) deviceEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getDeviceType() {
        return deviceTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DevicesFactory getDevicesFactory() {
        return (DevicesFactory) getEFactoryInstance();
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
        deviceEClass = createEClass(DEVICE);
        createEAttribute(deviceEClass, DEVICE__BRAND);
        createEAttribute(deviceEClass, DEVICE__MODEL);
        createEAttribute(deviceEClass, DEVICE__SERIAL);
        createEAttribute(deviceEClass, DEVICE__TYPE);
        createEReference(deviceEClass, DEVICE__CERTIFICATIONS);
        createEReference(deviceEClass, DEVICE__LIFE);
        deviceTypeEEnum = createEEnum(DEVICE_TYPE);
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
        DocumentsPackage theDocumentsPackage = (DocumentsPackage) EPackage.Registry.INSTANCE.getEPackage(DocumentsPackage.eNS_URI);
        MetainfoPackage theMetainfoPackage = (MetainfoPackage) EPackage.Registry.INSTANCE.getEPackage(MetainfoPackage.eNS_URI);
        initEClass(deviceEClass, Device.class, "Device", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDevice_Brand(), ecorePackage.getEString(), "brand", null, 1, 1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDevice_Model(), ecorePackage.getEString(), "model", null, 1, 1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDevice_Serial(), ecorePackage.getEString(), "serial", null, 1, 1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDevice_Type(), this.getDeviceType(), "type", null, 1, 1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDevice_Certifications(), theDocumentsPackage.getCertification(), theDocumentsPackage.getCertification_Device(), "certifications", null, 1, -1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDevice_Life(), theMetainfoPackage.getLife(), null, "life", null, 1, 1, Device.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(deviceTypeEEnum, DeviceType.class, "DeviceType");
        addEEnumLiteral(deviceTypeEEnum, DeviceType.NOT_AVAILABLE);
        addEEnumLiteral(deviceTypeEEnum, DeviceType.MICROPHONE);
        addEEnumLiteral(deviceTypeEEnum, DeviceType.ANALYZER);
        createResource(eNS_URI);
    }
}
