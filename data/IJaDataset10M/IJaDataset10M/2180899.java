package org.enml.geo.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.enml.devices.DevicesPackage;
import org.enml.devices.impl.DevicesPackageImpl;
import org.enml.documents.DocumentsPackage;
import org.enml.documents.impl.DocumentsPackageImpl;
import org.enml.geo.Area;
import org.enml.geo.CRSType;
import org.enml.geo.CSType;
import org.enml.geo.CoordinateReferenceSystem;
import org.enml.geo.CoordinateSystem;
import org.enml.geo.DATUMType;
import org.enml.geo.Datum;
import org.enml.geo.GeoFactory;
import org.enml.geo.GeoPackage;
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
public class GeoPackageImpl extends EPackageImpl implements GeoPackage {

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
    private EClass areaEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass coordinateReferenceSystemEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass coordinateSystemEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass datumEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum crsTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum csTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum datumTypeEEnum = null;

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
	 * @see org.enml.geo.GeoPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private GeoPackageImpl() {
        super(eNS_URI, GeoFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link GeoPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static GeoPackage init() {
        if (isInited) return (GeoPackage) EPackage.Registry.INSTANCE.getEPackage(GeoPackage.eNS_URI);
        GeoPackageImpl theGeoPackage = (GeoPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GeoPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new GeoPackageImpl());
        isInited = true;
        DevicesPackageImpl theDevicesPackage = (DevicesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DevicesPackage.eNS_URI) instanceof DevicesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DevicesPackage.eNS_URI) : DevicesPackage.eINSTANCE);
        DocumentsPackageImpl theDocumentsPackage = (DocumentsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(DocumentsPackage.eNS_URI) instanceof DocumentsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DocumentsPackage.eNS_URI) : DocumentsPackage.eINSTANCE);
        MeasuresPackageImpl theMeasuresPackage = (MeasuresPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MeasuresPackage.eNS_URI) instanceof MeasuresPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MeasuresPackage.eNS_URI) : MeasuresPackage.eINSTANCE);
        MetainfoPackageImpl theMetainfoPackage = (MetainfoPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MetainfoPackage.eNS_URI) instanceof MetainfoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetainfoPackage.eNS_URI) : MetainfoPackage.eINSTANCE);
        NetPackageImpl theNetPackage = (NetPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(NetPackage.eNS_URI) instanceof NetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(NetPackage.eNS_URI) : NetPackage.eINSTANCE);
        NotesPackageImpl theNotesPackage = (NotesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(NotesPackage.eNS_URI) instanceof NotesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(NotesPackage.eNS_URI) : NotesPackage.eINSTANCE);
        OpsPackageImpl theOpsPackage = (OpsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(OpsPackage.eNS_URI) instanceof OpsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OpsPackage.eNS_URI) : OpsPackage.eINSTANCE);
        SourcesPackageImpl theSourcesPackage = (SourcesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI) instanceof SourcesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI) : SourcesPackage.eINSTANCE);
        SubjectsPackageImpl theSubjectsPackage = (SubjectsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(SubjectsPackage.eNS_URI) instanceof SubjectsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SubjectsPackage.eNS_URI) : SubjectsPackage.eINSTANCE);
        ValidityPackageImpl theValidityPackage = (ValidityPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(ValidityPackage.eNS_URI) instanceof ValidityPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ValidityPackage.eNS_URI) : ValidityPackage.eINSTANCE);
        WorkPackageImpl theWorkPackage = (WorkPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(WorkPackage.eNS_URI) instanceof WorkPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(WorkPackage.eNS_URI) : WorkPackage.eINSTANCE);
        theGeoPackage.createPackageContents();
        theDevicesPackage.createPackageContents();
        theDocumentsPackage.createPackageContents();
        theMeasuresPackage.createPackageContents();
        theMetainfoPackage.createPackageContents();
        theNetPackage.createPackageContents();
        theNotesPackage.createPackageContents();
        theOpsPackage.createPackageContents();
        theSourcesPackage.createPackageContents();
        theSubjectsPackage.createPackageContents();
        theValidityPackage.createPackageContents();
        theWorkPackage.createPackageContents();
        theGeoPackage.initializePackageContents();
        theDevicesPackage.initializePackageContents();
        theDocumentsPackage.initializePackageContents();
        theMeasuresPackage.initializePackageContents();
        theMetainfoPackage.initializePackageContents();
        theNetPackage.initializePackageContents();
        theNotesPackage.initializePackageContents();
        theOpsPackage.initializePackageContents();
        theSourcesPackage.initializePackageContents();
        theSubjectsPackage.initializePackageContents();
        theValidityPackage.initializePackageContents();
        theWorkPackage.initializePackageContents();
        theGeoPackage.freeze();
        EPackage.Registry.INSTANCE.put(GeoPackage.eNS_URI, theGeoPackage);
        return theGeoPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getArea() {
        return areaEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_Code() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_Name() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_AreaOfUse() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_SouthLatitude() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_NorthLatitude() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_LeftLongitude() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_RightLongitude() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_IsoA2() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_IsoA3() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(8);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArea_IsoNumericoCode() {
        return (EAttribute) areaEClass.getEStructuralFeatures().get(9);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCoordinateReferenceSystem() {
        return coordinateReferenceSystemEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateReferenceSystem_Code() {
        return (EAttribute) coordinateReferenceSystemEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateReferenceSystem_Name() {
        return (EAttribute) coordinateReferenceSystemEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateReferenceSystem_Type() {
        return (EAttribute) coordinateReferenceSystemEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoordinateReferenceSystem_Datum() {
        return (EReference) coordinateReferenceSystemEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoordinateReferenceSystem_Area() {
        return (EReference) coordinateReferenceSystemEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCoordinateReferenceSystem_CoordinateSystem() {
        return (EReference) coordinateReferenceSystemEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCoordinateSystem() {
        return coordinateSystemEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateSystem_Dimension() {
        return (EAttribute) coordinateSystemEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateSystem_Code() {
        return (EAttribute) coordinateSystemEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateSystem_Name() {
        return (EAttribute) coordinateSystemEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCoordinateSystem_Type() {
        return (EAttribute) coordinateSystemEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDatum() {
        return datumEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDatum_Code() {
        return (EAttribute) datumEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDatum_Name() {
        return (EAttribute) datumEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getDatum_Type() {
        return (EAttribute) datumEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getCRSType() {
        return crsTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getCSType() {
        return csTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getDATUMType() {
        return datumTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public GeoFactory getGeoFactory() {
        return (GeoFactory) getEFactoryInstance();
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
        areaEClass = createEClass(AREA);
        createEAttribute(areaEClass, AREA__CODE);
        createEAttribute(areaEClass, AREA__NAME);
        createEAttribute(areaEClass, AREA__AREA_OF_USE);
        createEAttribute(areaEClass, AREA__SOUTH_LATITUDE);
        createEAttribute(areaEClass, AREA__NORTH_LATITUDE);
        createEAttribute(areaEClass, AREA__LEFT_LONGITUDE);
        createEAttribute(areaEClass, AREA__RIGHT_LONGITUDE);
        createEAttribute(areaEClass, AREA__ISO_A2);
        createEAttribute(areaEClass, AREA__ISO_A3);
        createEAttribute(areaEClass, AREA__ISO_NUMERICO_CODE);
        coordinateReferenceSystemEClass = createEClass(COORDINATE_REFERENCE_SYSTEM);
        createEAttribute(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__CODE);
        createEAttribute(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__NAME);
        createEAttribute(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__TYPE);
        createEReference(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__DATUM);
        createEReference(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__AREA);
        createEReference(coordinateReferenceSystemEClass, COORDINATE_REFERENCE_SYSTEM__COORDINATE_SYSTEM);
        coordinateSystemEClass = createEClass(COORDINATE_SYSTEM);
        createEAttribute(coordinateSystemEClass, COORDINATE_SYSTEM__DIMENSION);
        createEAttribute(coordinateSystemEClass, COORDINATE_SYSTEM__CODE);
        createEAttribute(coordinateSystemEClass, COORDINATE_SYSTEM__NAME);
        createEAttribute(coordinateSystemEClass, COORDINATE_SYSTEM__TYPE);
        datumEClass = createEClass(DATUM);
        createEAttribute(datumEClass, DATUM__CODE);
        createEAttribute(datumEClass, DATUM__NAME);
        createEAttribute(datumEClass, DATUM__TYPE);
        crsTypeEEnum = createEEnum(CRS_TYPE);
        csTypeEEnum = createEEnum(CS_TYPE);
        datumTypeEEnum = createEEnum(DATUM_TYPE);
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
        initEClass(areaEClass, Area.class, "Area", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getArea_Code(), ecorePackage.getELong(), "code", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_Name(), ecorePackage.getEString(), "name", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_AreaOfUse(), ecorePackage.getEString(), "areaOfUse", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_SouthLatitude(), ecorePackage.getEDouble(), "southLatitude", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_NorthLatitude(), ecorePackage.getEDouble(), "northLatitude", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_LeftLongitude(), ecorePackage.getEDouble(), "leftLongitude", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_RightLongitude(), ecorePackage.getEDouble(), "rightLongitude", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_IsoA2(), ecorePackage.getEString(), "isoA2", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_IsoA3(), ecorePackage.getEString(), "isoA3", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArea_IsoNumericoCode(), ecorePackage.getELong(), "isoNumericoCode", null, 0, 1, Area.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(coordinateReferenceSystemEClass, CoordinateReferenceSystem.class, "CoordinateReferenceSystem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoordinateReferenceSystem_Code(), ecorePackage.getELong(), "code", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoordinateReferenceSystem_Name(), ecorePackage.getEString(), "name", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoordinateReferenceSystem_Type(), this.getCRSType(), "type", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoordinateReferenceSystem_Datum(), this.getDatum(), null, "datum", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoordinateReferenceSystem_Area(), this.getArea(), null, "area", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCoordinateReferenceSystem_CoordinateSystem(), this.getCoordinateSystem(), null, "coordinateSystem", null, 1, 1, CoordinateReferenceSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(coordinateSystemEClass, CoordinateSystem.class, "CoordinateSystem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCoordinateSystem_Dimension(), ecorePackage.getEInt(), "dimension", null, 0, 1, CoordinateSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoordinateSystem_Code(), ecorePackage.getELong(), "code", null, 0, 1, CoordinateSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoordinateSystem_Name(), ecorePackage.getEString(), "name", null, 0, 1, CoordinateSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCoordinateSystem_Type(), this.getCSType(), "type", null, 0, 1, CoordinateSystem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(datumEClass, Datum.class, "Datum", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDatum_Code(), ecorePackage.getELong(), "code", null, 0, 1, Datum.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatum_Name(), ecorePackage.getEString(), "name", null, 0, 1, Datum.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDatum_Type(), this.getDATUMType(), "type", null, 0, 1, Datum.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(crsTypeEEnum, CRSType.class, "CRSType");
        addEEnumLiteral(crsTypeEEnum, CRSType.PROJECTED);
        addEEnumLiteral(crsTypeEEnum, CRSType.GEOGRAPHIC2D);
        addEEnumLiteral(crsTypeEEnum, CRSType.GEOGRAPHIC3D);
        addEEnumLiteral(crsTypeEEnum, CRSType.GEOCENTRIC);
        addEEnumLiteral(crsTypeEEnum, CRSType.VERTICAL);
        addEEnumLiteral(crsTypeEEnum, CRSType.ENGINEERING);
        addEEnumLiteral(crsTypeEEnum, CRSType.COMPOUND);
        initEEnum(csTypeEEnum, CSType.class, "CSType");
        addEEnumLiteral(csTypeEEnum, CSType.CARTESIAN);
        addEEnumLiteral(csTypeEEnum, CSType.ELLIPSOIDAL);
        addEEnumLiteral(csTypeEEnum, CSType.VERTICAL);
        initEEnum(datumTypeEEnum, DATUMType.class, "DATUMType");
        addEEnumLiteral(datumTypeEEnum, DATUMType.GEODETIC);
        addEEnumLiteral(datumTypeEEnum, DATUMType.VERTICAL);
        addEEnumLiteral(datumTypeEEnum, DATUMType.ENGINEERING);
        createResource(eNS_URI);
    }
}
