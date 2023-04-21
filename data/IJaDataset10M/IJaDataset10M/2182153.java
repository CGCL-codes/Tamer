package SensorDataWebGui.impl;

import SensorDataWebGui.AbstractPE;
import SensorDataWebGui.Arc;
import SensorDataWebGui.DiagramTypes;
import SensorDataWebGui.DistributionType;
import SensorDataWebGui.FixedWindow;
import SensorDataWebGui.FunctorTypes;
import SensorDataWebGui.Graph;
import SensorDataWebGui.GraphReference;
import SensorDataWebGui.InputOutputMappingTypes;
import SensorDataWebGui.ProcessingElement;
import SensorDataWebGui.ProcessingElementTypes;
import SensorDataWebGui.SensorDataWebGuiFactory;
import SensorDataWebGui.SensorDataWebGuiPackage;
import SensorDataWebGui.Source;
import SensorDataWebGui.SourceTypes;
import SensorDataWebGui.StandardSensorDataWeb;
import SensorDataWebGui.TimeWindow;
import SensorDataWebGui.TriggerTypes;
import SensorDataWebGui.TupleWindow;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SensorDataWebGuiPackageImpl extends EPackageImpl implements SensorDataWebGuiPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass standardSensorDataWebEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass arcEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass abstractPEEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass tupleWindowEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass timeWindowEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass fixedWindowEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass processingElementEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass sourceEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass graphEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass graphReferenceEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum triggerTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum sourceTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum processingElementTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum functorTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum inputOutputMappingTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum diagramTypesEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum distributionTypeEEnum = null;

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
	 * @see SensorDataWebGui.SensorDataWebGuiPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private SensorDataWebGuiPackageImpl() {
        super(eNS_URI, SensorDataWebGuiFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link SensorDataWebGuiPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static SensorDataWebGuiPackage init() {
        if (isInited) return (SensorDataWebGuiPackage) EPackage.Registry.INSTANCE.getEPackage(SensorDataWebGuiPackage.eNS_URI);
        SensorDataWebGuiPackageImpl theSensorDataWebGuiPackage = (SensorDataWebGuiPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SensorDataWebGuiPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SensorDataWebGuiPackageImpl());
        isInited = true;
        theSensorDataWebGuiPackage.createPackageContents();
        theSensorDataWebGuiPackage.initializePackageContents();
        theSensorDataWebGuiPackage.freeze();
        EPackage.Registry.INSTANCE.put(SensorDataWebGuiPackage.eNS_URI, theSensorDataWebGuiPackage);
        return theSensorDataWebGuiPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStandardSensorDataWeb() {
        return standardSensorDataWebEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getStandardSensorDataWeb_Name() {
        return (EAttribute) standardSensorDataWebEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStandardSensorDataWeb_ContainsPEs() {
        return (EReference) standardSensorDataWebEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStandardSensorDataWeb_ContainsArcs() {
        return (EReference) standardSensorDataWebEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getStandardSensorDataWeb_DiagramType() {
        return (EAttribute) standardSensorDataWebEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStandardSensorDataWeb_ContainsGraph() {
        return (EReference) standardSensorDataWebEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getArc() {
        return arcEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArc_WindowPredicate() {
        return (EAttribute) arcEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArc_Name() {
        return (EAttribute) arcEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getArc_ArcFromPE() {
        return (EReference) arcEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getArc_ArcToPE() {
        return (EReference) arcEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArc_Description() {
        return (EAttribute) arcEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArc_ValidTime() {
        return (EAttribute) arcEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getArc_TransactionTime() {
        return (EAttribute) arcEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAbstractPE() {
        return abstractPEEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_Name() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_View() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_NbrAllowedInputs() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_PersistentView() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_Description() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_ValidTime() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_TransactionTime() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_ViewUsername() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAbstractPE_ViewPassword() {
        return (EAttribute) abstractPEEClass.getEStructuralFeatures().get(8);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTupleWindow() {
        return tupleWindowEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTimeWindow() {
        return timeWindowEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFixedWindow() {
        return fixedWindowEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getProcessingElement() {
        return processingElementEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_TriggerType() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_Triggerpredicate() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_PeType() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_FunctorType() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_InputOutputMappingType() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_ProcessingDelay() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getProcessingElement_ProcessingDelayDistribution() {
        return (EAttribute) processingElementEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProcessingElement_HasGraphReference() {
        return (EReference) processingElementEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSource() {
        return sourceEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSource_Timeout() {
        return (EAttribute) sourceEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSource_Location() {
        return (EAttribute) sourceEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSource_SampleTime() {
        return (EAttribute) sourceEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSource_SampleTimeDistribution() {
        return (EAttribute) sourceEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSource_HasGraphReference() {
        return (EReference) sourceEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSource_SourceType() {
        return (EAttribute) sourceEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGraph() {
        return graphEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_Visible() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_XMin() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_XMax() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_YMin() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_YMax() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_Name() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_Description() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraph_Id() {
        return (EAttribute) graphEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getGraphReference() {
        return graphReferenceEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getGraphReference_Id() {
        return (EAttribute) graphReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getTriggerTypes() {
        return triggerTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getSourceTypes() {
        return sourceTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getProcessingElementTypes() {
        return processingElementTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getFunctorTypes() {
        return functorTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getInputOutputMappingTypes() {
        return inputOutputMappingTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getDiagramTypes() {
        return diagramTypesEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getDistributionType() {
        return distributionTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SensorDataWebGuiFactory getSensorDataWebGuiFactory() {
        return (SensorDataWebGuiFactory) getEFactoryInstance();
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
        standardSensorDataWebEClass = createEClass(STANDARD_SENSOR_DATA_WEB);
        createEAttribute(standardSensorDataWebEClass, STANDARD_SENSOR_DATA_WEB__NAME);
        createEReference(standardSensorDataWebEClass, STANDARD_SENSOR_DATA_WEB__CONTAINS_PES);
        createEReference(standardSensorDataWebEClass, STANDARD_SENSOR_DATA_WEB__CONTAINS_ARCS);
        createEAttribute(standardSensorDataWebEClass, STANDARD_SENSOR_DATA_WEB__DIAGRAM_TYPE);
        createEReference(standardSensorDataWebEClass, STANDARD_SENSOR_DATA_WEB__CONTAINS_GRAPH);
        arcEClass = createEClass(ARC);
        createEAttribute(arcEClass, ARC__WINDOW_PREDICATE);
        createEAttribute(arcEClass, ARC__NAME);
        createEReference(arcEClass, ARC__ARC_FROM_PE);
        createEReference(arcEClass, ARC__ARC_TO_PE);
        createEAttribute(arcEClass, ARC__DESCRIPTION);
        createEAttribute(arcEClass, ARC__VALID_TIME);
        createEAttribute(arcEClass, ARC__TRANSACTION_TIME);
        abstractPEEClass = createEClass(ABSTRACT_PE);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__NAME);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__VIEW);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__NBR_ALLOWED_INPUTS);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__PERSISTENT_VIEW);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__DESCRIPTION);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__VALID_TIME);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__TRANSACTION_TIME);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__VIEW_USERNAME);
        createEAttribute(abstractPEEClass, ABSTRACT_PE__VIEW_PASSWORD);
        tupleWindowEClass = createEClass(TUPLE_WINDOW);
        timeWindowEClass = createEClass(TIME_WINDOW);
        fixedWindowEClass = createEClass(FIXED_WINDOW);
        processingElementEClass = createEClass(PROCESSING_ELEMENT);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__TRIGGER_TYPE);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__TRIGGERPREDICATE);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__PE_TYPE);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__FUNCTOR_TYPE);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__INPUT_OUTPUT_MAPPING_TYPE);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__PROCESSING_DELAY);
        createEAttribute(processingElementEClass, PROCESSING_ELEMENT__PROCESSING_DELAY_DISTRIBUTION);
        createEReference(processingElementEClass, PROCESSING_ELEMENT__HAS_GRAPH_REFERENCE);
        sourceEClass = createEClass(SOURCE);
        createEAttribute(sourceEClass, SOURCE__TIMEOUT);
        createEAttribute(sourceEClass, SOURCE__LOCATION);
        createEAttribute(sourceEClass, SOURCE__SAMPLE_TIME);
        createEAttribute(sourceEClass, SOURCE__SAMPLE_TIME_DISTRIBUTION);
        createEReference(sourceEClass, SOURCE__HAS_GRAPH_REFERENCE);
        createEAttribute(sourceEClass, SOURCE__SOURCE_TYPE);
        graphEClass = createEClass(GRAPH);
        createEAttribute(graphEClass, GRAPH__VISIBLE);
        createEAttribute(graphEClass, GRAPH__XMIN);
        createEAttribute(graphEClass, GRAPH__XMAX);
        createEAttribute(graphEClass, GRAPH__YMIN);
        createEAttribute(graphEClass, GRAPH__YMAX);
        createEAttribute(graphEClass, GRAPH__NAME);
        createEAttribute(graphEClass, GRAPH__DESCRIPTION);
        createEAttribute(graphEClass, GRAPH__ID);
        graphReferenceEClass = createEClass(GRAPH_REFERENCE);
        createEAttribute(graphReferenceEClass, GRAPH_REFERENCE__ID);
        triggerTypesEEnum = createEEnum(TRIGGER_TYPES);
        sourceTypesEEnum = createEEnum(SOURCE_TYPES);
        processingElementTypesEEnum = createEEnum(PROCESSING_ELEMENT_TYPES);
        functorTypesEEnum = createEEnum(FUNCTOR_TYPES);
        inputOutputMappingTypesEEnum = createEEnum(INPUT_OUTPUT_MAPPING_TYPES);
        diagramTypesEEnum = createEEnum(DIAGRAM_TYPES);
        distributionTypeEEnum = createEEnum(DISTRIBUTION_TYPE);
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
        tupleWindowEClass.getESuperTypes().add(this.getArc());
        timeWindowEClass.getESuperTypes().add(this.getArc());
        fixedWindowEClass.getESuperTypes().add(this.getArc());
        processingElementEClass.getESuperTypes().add(this.getAbstractPE());
        sourceEClass.getESuperTypes().add(this.getAbstractPE());
        initEClass(standardSensorDataWebEClass, StandardSensorDataWeb.class, "StandardSensorDataWeb", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStandardSensorDataWeb_Name(), ecorePackage.getEString(), "name", null, 0, 1, StandardSensorDataWeb.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStandardSensorDataWeb_ContainsPEs(), this.getAbstractPE(), null, "containsPEs", null, 0, -1, StandardSensorDataWeb.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStandardSensorDataWeb_ContainsArcs(), this.getArc(), null, "containsArcs", null, 0, -1, StandardSensorDataWeb.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStandardSensorDataWeb_DiagramType(), this.getDiagramTypes(), "diagramType", null, 0, 1, StandardSensorDataWeb.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStandardSensorDataWeb_ContainsGraph(), this.getGraph(), null, "containsGraph", null, 0, -1, StandardSensorDataWeb.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(arcEClass, Arc.class, "Arc", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getArc_WindowPredicate(), ecorePackage.getEString(), "windowPredicate", null, 0, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArc_Name(), ecorePackage.getEString(), "name", null, 0, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getArc_ArcFromPE(), this.getAbstractPE(), null, "arcFromPE", null, 1, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getArc_ArcToPE(), this.getProcessingElement(), null, "arcToPE", null, 1, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArc_Description(), ecorePackage.getEString(), "description", null, 0, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArc_ValidTime(), ecorePackage.getELong(), "validTime", null, 0, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getArc_TransactionTime(), ecorePackage.getELong(), "transactionTime", null, 0, 1, Arc.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(abstractPEEClass, AbstractPE.class, "AbstractPE", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractPE_Name(), ecorePackage.getEString(), "name", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_View(), ecorePackage.getEString(), "view", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_NbrAllowedInputs(), ecorePackage.getEInt(), "nbrAllowedInputs", "0", 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_PersistentView(), ecorePackage.getEBoolean(), "persistentView", "true", 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_Description(), ecorePackage.getEString(), "description", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_ValidTime(), ecorePackage.getELong(), "validTime", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_TransactionTime(), ecorePackage.getELong(), "transactionTime", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_ViewUsername(), ecorePackage.getEString(), "viewUsername", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAbstractPE_ViewPassword(), ecorePackage.getEString(), "viewPassword", null, 0, 1, AbstractPE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(tupleWindowEClass, TupleWindow.class, "TupleWindow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(timeWindowEClass, TimeWindow.class, "TimeWindow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(fixedWindowEClass, FixedWindow.class, "FixedWindow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(processingElementEClass, ProcessingElement.class, "ProcessingElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getProcessingElement_TriggerType(), this.getTriggerTypes(), "triggerType", "0", 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_Triggerpredicate(), ecorePackage.getEString(), "triggerpredicate", "", 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_PeType(), this.getProcessingElementTypes(), "peType", "0", 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_FunctorType(), this.getFunctorTypes(), "functorType", "0", 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_InputOutputMappingType(), this.getInputOutputMappingTypes(), "inputOutputMappingType", "0", 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_ProcessingDelay(), ecorePackage.getEInt(), "processingDelay", null, 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getProcessingElement_ProcessingDelayDistribution(), this.getDistributionType(), "processingDelayDistribution", null, 0, 1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getProcessingElement_HasGraphReference(), this.getGraphReference(), null, "hasGraphReference", null, 0, -1, ProcessingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(sourceEClass, Source.class, "Source", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSource_Timeout(), ecorePackage.getEInt(), "timeout", null, 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSource_Location(), ecorePackage.getEString(), "location", null, 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSource_SampleTime(), ecorePackage.getEInt(), "sampleTime", null, 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSource_SampleTimeDistribution(), this.getDistributionType(), "sampleTimeDistribution", null, 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSource_HasGraphReference(), this.getGraphReference(), null, "hasGraphReference", null, 0, -1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSource_SourceType(), this.getSourceTypes(), "sourceType", "0", 0, 1, Source.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(graphEClass, Graph.class, "Graph", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGraph_Visible(), ecorePackage.getEBoolean(), "visible", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_XMin(), ecorePackage.getEDouble(), "xMin", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_XMax(), ecorePackage.getEDouble(), "xMax", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_YMin(), ecorePackage.getEDouble(), "yMin", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_YMax(), ecorePackage.getEDouble(), "yMax", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_Name(), ecorePackage.getEString(), "name", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_Description(), ecorePackage.getEString(), "description", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getGraph_Id(), ecorePackage.getEInt(), "id", null, 0, 1, Graph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(graphReferenceEClass, GraphReference.class, "GraphReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGraphReference_Id(), ecorePackage.getEInt(), "id", null, 0, 1, GraphReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(triggerTypesEEnum, TriggerTypes.class, "TriggerTypes");
        addEEnumLiteral(triggerTypesEEnum, TriggerTypes.TUPLE);
        addEEnumLiteral(triggerTypesEEnum, TriggerTypes.TIME);
        addEEnumLiteral(triggerTypesEEnum, TriggerTypes.ONCE);
        initEEnum(sourceTypesEEnum, SourceTypes.class, "SourceTypes");
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.MEMORY);
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.GSN);
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.BT_INQUERY);
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.ITOO_HAMMER);
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.FREEHAND);
        addEEnumLiteral(sourceTypesEEnum, SourceTypes.SPARQL);
        initEEnum(processingElementTypesEEnum, ProcessingElementTypes.class, "ProcessingElementTypes");
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.NOOP);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.CROSSPRODUCT);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.DELTA_SELECTOR);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.FUNCTOR);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.INTERVAL_SELECTOR);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.PROJECTOR);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.REPLAY);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.SELECTOR);
        addEEnumLiteral(processingElementTypesEEnum, ProcessingElementTypes.JOIN);
        initEEnum(functorTypesEEnum, FunctorTypes.class, "FunctorTypes");
        addEEnumLiteral(functorTypesEEnum, FunctorTypes.NOTAPPLICABLE);
        addEEnumLiteral(functorTypesEEnum, FunctorTypes.NOOP);
        addEEnumLiteral(functorTypesEEnum, FunctorTypes.AVERAGE);
        initEEnum(inputOutputMappingTypesEEnum, InputOutputMappingTypes.class, "InputOutputMappingTypes");
        addEEnumLiteral(inputOutputMappingTypesEEnum, InputOutputMappingTypes.ONE_TO_ONE);
        addEEnumLiteral(inputOutputMappingTypesEEnum, InputOutputMappingTypes.ONE_TO_MANY);
        addEEnumLiteral(inputOutputMappingTypesEEnum, InputOutputMappingTypes.MANY_TO_ONE);
        addEEnumLiteral(inputOutputMappingTypesEEnum, InputOutputMappingTypes.MANY_TO_MANY);
        initEEnum(diagramTypesEEnum, DiagramTypes.class, "DiagramTypes");
        addEEnumLiteral(diagramTypesEEnum, DiagramTypes.STANDARD);
        addEEnumLiteral(diagramTypesEEnum, DiagramTypes.PROVENANCE);
        addEEnumLiteral(diagramTypesEEnum, DiagramTypes.PROPAGATION);
        initEEnum(distributionTypeEEnum, DistributionType.class, "DistributionType");
        addEEnumLiteral(distributionTypeEEnum, DistributionType.POISSON);
        addEEnumLiteral(distributionTypeEEnum, DistributionType.GAUSSIAN);
        addEEnumLiteral(distributionTypeEEnum, DistributionType.NORMAL);
        addEEnumLiteral(distributionTypeEEnum, DistributionType.EQUI);
        addEEnumLiteral(distributionTypeEEnum, DistributionType.EXPONENTIAL);
        createResource(eNS_URI);
    }
}
