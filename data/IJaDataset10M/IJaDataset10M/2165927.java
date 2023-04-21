package hub.metrik.lang.eprovide.trace.impl;

import hub.metrik.lang.eprovide.trace.AttributeValue;
import hub.metrik.lang.eprovide.trace.Change;
import hub.metrik.lang.eprovide.trace.FeatureChange;
import hub.metrik.lang.eprovide.trace.MultipleFeatureAdd;
import hub.metrik.lang.eprovide.trace.MultipleFeatureChange;
import hub.metrik.lang.eprovide.trace.MultipleFeatureMove;
import hub.metrik.lang.eprovide.trace.MultipleFeatureRemove;
import hub.metrik.lang.eprovide.trace.NewObject;
import hub.metrik.lang.eprovide.trace.OrderedMultipleFeatureRemoveAbove;
import hub.metrik.lang.eprovide.trace.ReferenceValue;
import hub.metrik.lang.eprovide.trace.SingularFeatureChange;
import hub.metrik.lang.eprovide.trace.SingularFeatureSetting;
import hub.metrik.lang.eprovide.trace.SingularFeatureUnsetting;
import hub.metrik.lang.eprovide.trace.Step;
import hub.metrik.lang.eprovide.trace.Trace;
import hub.metrik.lang.eprovide.trace.TraceFactory;
import hub.metrik.lang.eprovide.trace.TracePackage;
import hub.metrik.lang.eprovide.trace.Value;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TracePackageImpl extends EPackageImpl implements TracePackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass traceEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass stepEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass changeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass featureChangeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass newObjectEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass singularFeatureChangeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass multipleFeatureChangeEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass valueEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass referenceValueEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass singularFeatureSettingEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass singularFeatureUnsettingEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass multipleFeatureMoveEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass multipleFeatureAddEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass multipleFeatureRemoveEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass attributeValueEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass orderedMultipleFeatureRemoveAboveEClass = null;

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
	 * @see hub.metrik.lang.eprovide.trace.TracePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private TracePackageImpl() {
        super(eNS_URI, TraceFactory.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static TracePackage init() {
        if (isInited) return (TracePackage) EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
        TracePackageImpl theTracePackage = (TracePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof TracePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new TracePackageImpl());
        isInited = true;
        EcorePackage.eINSTANCE.eClass();
        theTracePackage.createPackageContents();
        theTracePackage.initializePackageContents();
        theTracePackage.freeze();
        return theTracePackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTrace() {
        return traceEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTrace_Steps() {
        return (EReference) traceEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTrace_InitialModel() {
        return (EReference) traceEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStep() {
        return stepEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStep_Changes() {
        return (EReference) stepEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getStep_Width() {
        return (EAttribute) stepEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getChange() {
        return changeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFeatureChange() {
        return featureChangeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFeatureChange_TargetObject() {
        return (EReference) featureChangeEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFeatureChange_Feature() {
        return (EReference) featureChangeEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getNewObject() {
        return newObjectEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getNewObject_Object() {
        return (EReference) newObjectEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSingularFeatureChange() {
        return singularFeatureChangeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMultipleFeatureChange() {
        return multipleFeatureChangeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getValue() {
        return valueEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getReferenceValue() {
        return referenceValueEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getReferenceValue_Value() {
        return (EReference) referenceValueEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSingularFeatureSetting() {
        return singularFeatureSettingEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSingularFeatureSetting_Value() {
        return (EReference) singularFeatureSettingEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSingularFeatureUnsetting() {
        return singularFeatureUnsettingEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMultipleFeatureMove() {
        return multipleFeatureMoveEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMultipleFeatureMove_From() {
        return (EAttribute) multipleFeatureMoveEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMultipleFeatureMove_To() {
        return (EAttribute) multipleFeatureMoveEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMultipleFeatureAdd() {
        return multipleFeatureAddEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getMultipleFeatureAdd_Value() {
        return (EReference) multipleFeatureAddEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMultipleFeatureAdd_To() {
        return (EAttribute) multipleFeatureAddEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getMultipleFeatureRemove() {
        return multipleFeatureRemoveEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getMultipleFeatureRemove_From() {
        return (EAttribute) multipleFeatureRemoveEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAttributeValue() {
        return attributeValueEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAttributeValue_ValueLiteral() {
        return (EAttribute) attributeValueEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAttributeValue_DataType() {
        return (EReference) attributeValueEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getOrderedMultipleFeatureRemoveAbove() {
        return orderedMultipleFeatureRemoveAboveEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOrderedMultipleFeatureRemoveAbove_From() {
        return (EAttribute) orderedMultipleFeatureRemoveAboveEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TraceFactory getTraceFactory() {
        return (TraceFactory) getEFactoryInstance();
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
        traceEClass = createEClass(TRACE);
        createEReference(traceEClass, TRACE__STEPS);
        createEReference(traceEClass, TRACE__INITIAL_MODEL);
        stepEClass = createEClass(STEP);
        createEReference(stepEClass, STEP__CHANGES);
        createEAttribute(stepEClass, STEP__WIDTH);
        changeEClass = createEClass(CHANGE);
        featureChangeEClass = createEClass(FEATURE_CHANGE);
        createEReference(featureChangeEClass, FEATURE_CHANGE__TARGET_OBJECT);
        createEReference(featureChangeEClass, FEATURE_CHANGE__FEATURE);
        newObjectEClass = createEClass(NEW_OBJECT);
        createEReference(newObjectEClass, NEW_OBJECT__OBJECT);
        singularFeatureChangeEClass = createEClass(SINGULAR_FEATURE_CHANGE);
        multipleFeatureChangeEClass = createEClass(MULTIPLE_FEATURE_CHANGE);
        valueEClass = createEClass(VALUE);
        referenceValueEClass = createEClass(REFERENCE_VALUE);
        createEReference(referenceValueEClass, REFERENCE_VALUE__VALUE);
        singularFeatureSettingEClass = createEClass(SINGULAR_FEATURE_SETTING);
        createEReference(singularFeatureSettingEClass, SINGULAR_FEATURE_SETTING__VALUE);
        singularFeatureUnsettingEClass = createEClass(SINGULAR_FEATURE_UNSETTING);
        multipleFeatureMoveEClass = createEClass(MULTIPLE_FEATURE_MOVE);
        createEAttribute(multipleFeatureMoveEClass, MULTIPLE_FEATURE_MOVE__FROM);
        createEAttribute(multipleFeatureMoveEClass, MULTIPLE_FEATURE_MOVE__TO);
        multipleFeatureAddEClass = createEClass(MULTIPLE_FEATURE_ADD);
        createEReference(multipleFeatureAddEClass, MULTIPLE_FEATURE_ADD__VALUE);
        createEAttribute(multipleFeatureAddEClass, MULTIPLE_FEATURE_ADD__TO);
        multipleFeatureRemoveEClass = createEClass(MULTIPLE_FEATURE_REMOVE);
        createEAttribute(multipleFeatureRemoveEClass, MULTIPLE_FEATURE_REMOVE__FROM);
        attributeValueEClass = createEClass(ATTRIBUTE_VALUE);
        createEAttribute(attributeValueEClass, ATTRIBUTE_VALUE__VALUE_LITERAL);
        createEReference(attributeValueEClass, ATTRIBUTE_VALUE__DATA_TYPE);
        orderedMultipleFeatureRemoveAboveEClass = createEClass(ORDERED_MULTIPLE_FEATURE_REMOVE_ABOVE);
        createEAttribute(orderedMultipleFeatureRemoveAboveEClass, ORDERED_MULTIPLE_FEATURE_REMOVE_ABOVE__FROM);
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
        EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
        featureChangeEClass.getESuperTypes().add(this.getChange());
        newObjectEClass.getESuperTypes().add(this.getChange());
        singularFeatureChangeEClass.getESuperTypes().add(this.getFeatureChange());
        multipleFeatureChangeEClass.getESuperTypes().add(this.getFeatureChange());
        referenceValueEClass.getESuperTypes().add(this.getValue());
        singularFeatureSettingEClass.getESuperTypes().add(this.getSingularFeatureChange());
        singularFeatureUnsettingEClass.getESuperTypes().add(this.getSingularFeatureChange());
        multipleFeatureMoveEClass.getESuperTypes().add(this.getMultipleFeatureChange());
        multipleFeatureAddEClass.getESuperTypes().add(this.getMultipleFeatureChange());
        multipleFeatureRemoveEClass.getESuperTypes().add(this.getMultipleFeatureChange());
        attributeValueEClass.getESuperTypes().add(this.getValue());
        orderedMultipleFeatureRemoveAboveEClass.getESuperTypes().add(this.getMultipleFeatureChange());
        initEClass(traceEClass, Trace.class, "Trace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTrace_Steps(), this.getStep(), null, "steps", null, 0, -1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTrace_InitialModel(), theEcorePackage.getEObject(), null, "initialModel", null, 1, 1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(stepEClass, Step.class, "Step", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStep_Changes(), this.getChange(), null, "changes", null, 0, -1, Step.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getStep_Width(), theEcorePackage.getEInt(), "width", null, 1, 1, Step.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        EOperation op = addEOperation(stepEClass, null, "replay", 1, 1, IS_UNIQUE, IS_ORDERED);
        EGenericType g1 = createEGenericType(theEcorePackage.getEMap());
        EGenericType g2 = createEGenericType(theEcorePackage.getEObject());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(theEcorePackage.getEObject());
        g1.getETypeArguments().add(g2);
        addEParameter(op, g1, "correspondences", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(changeEClass, Change.class, "Change", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        op = addEOperation(changeEClass, null, "replay", 1, 1, IS_UNIQUE, IS_ORDERED);
        g1 = createEGenericType(theEcorePackage.getEMap());
        g2 = createEGenericType(theEcorePackage.getEObject());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(theEcorePackage.getEObject());
        g1.getETypeArguments().add(g2);
        addEParameter(op, g1, "correspondences", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(featureChangeEClass, FeatureChange.class, "FeatureChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureChange_TargetObject(), theEcorePackage.getEObject(), null, "targetObject", null, 1, 1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getFeatureChange_Feature(), theEcorePackage.getEStructuralFeature(), null, "feature", null, 1, 1, FeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(newObjectEClass, NewObject.class, "NewObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNewObject_Object(), theEcorePackage.getEObject(), null, "object", null, 1, 1, NewObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(singularFeatureChangeEClass, SingularFeatureChange.class, "SingularFeatureChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(multipleFeatureChangeEClass, MultipleFeatureChange.class, "MultipleFeatureChange", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(valueEClass, Value.class, "Value", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(valueEClass, theEcorePackage.getEJavaObject(), "getValue", 1, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(referenceValueEClass, ReferenceValue.class, "ReferenceValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReferenceValue_Value(), theEcorePackage.getEObject(), null, "value", null, 1, 1, ReferenceValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(singularFeatureSettingEClass, SingularFeatureSetting.class, "SingularFeatureSetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSingularFeatureSetting_Value(), this.getValue(), null, "value", null, 1, 1, SingularFeatureSetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(singularFeatureUnsettingEClass, SingularFeatureUnsetting.class, "SingularFeatureUnsetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(multipleFeatureMoveEClass, MultipleFeatureMove.class, "MultipleFeatureMove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMultipleFeatureMove_From(), theEcorePackage.getEInt(), "from", null, 1, 1, MultipleFeatureMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMultipleFeatureMove_To(), theEcorePackage.getEInt(), "to", null, 1, 1, MultipleFeatureMove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(multipleFeatureAddEClass, MultipleFeatureAdd.class, "MultipleFeatureAdd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMultipleFeatureAdd_Value(), this.getValue(), null, "value", null, 1, 1, MultipleFeatureAdd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMultipleFeatureAdd_To(), theEcorePackage.getEInt(), "to", null, 1, 1, MultipleFeatureAdd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(multipleFeatureRemoveEClass, MultipleFeatureRemove.class, "MultipleFeatureRemove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMultipleFeatureRemove_From(), theEcorePackage.getEInt(), "from", null, 1, 1, MultipleFeatureRemove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(attributeValueEClass, AttributeValue.class, "AttributeValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAttributeValue_ValueLiteral(), theEcorePackage.getEString(), "valueLiteral", null, 1, 1, AttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAttributeValue_DataType(), theEcorePackage.getEDataType(), null, "dataType", null, 1, 1, AttributeValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(orderedMultipleFeatureRemoveAboveEClass, OrderedMultipleFeatureRemoveAbove.class, "OrderedMultipleFeatureRemoveAbove", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOrderedMultipleFeatureRemoveAbove_From(), theEcorePackage.getEInt(), "from", null, 1, 1, OrderedMultipleFeatureRemoveAbove.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
