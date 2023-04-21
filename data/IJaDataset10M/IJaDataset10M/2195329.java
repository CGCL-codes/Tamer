package org.echarts.edt.editor.eCharts.impl;

import org.echarts.edt.editor.eCharts.CompilationUnit;
import org.echarts.edt.editor.eCharts.EChartsFactory;
import org.echarts.edt.editor.eCharts.EChartsPackage;
import org.echarts.edt.editor.eCharts.MachineBody;
import org.echarts.edt.editor.eCharts.MachineFeature;
import org.echarts.edt.editor.eCharts.MachineModifier;
import org.echarts.edt.editor.eCharts.NamedMachine;
import org.echarts.edt.editor.eCharts.PackageDecl;
import org.echarts.edt.editor.eCharts.ReflectedStateMachineInvocation;
import org.echarts.edt.editor.eCharts.State;
import org.echarts.edt.editor.eCharts.StateMachineInvocation;
import org.echarts.edt.editor.eCharts.StateModifier;
import org.echarts.edt.editor.eCharts.StateRef;
import org.echarts.edt.editor.eCharts.TransitionDeclaration;
import org.echarts.edt.editor.eCharts.TransitionModifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.xbase.XbasePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EChartsPackageImpl extends EPackageImpl implements EChartsPackage {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass compilationUnitEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass packageDeclEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass namedMachineEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass machineBodyEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass machineFeatureEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass transitionDeclarationEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass stateRefEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass transitionModifierEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass stateEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass stateMachineInvocationEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass reflectedStateMachineInvocationEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EEnum stateModifierEEnum = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EEnum machineModifierEEnum = null;

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
   * @see org.echarts.edt.editor.eCharts.EChartsPackage#eNS_URI
   * @see #init()
   * @generated
   */
    private EChartsPackageImpl() {
        super(eNS_URI, EChartsFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link EChartsPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
    public static EChartsPackage init() {
        if (isInited) return (EChartsPackage) EPackage.Registry.INSTANCE.getEPackage(EChartsPackage.eNS_URI);
        EChartsPackageImpl theEChartsPackage = (EChartsPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EChartsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EChartsPackageImpl());
        isInited = true;
        XbasePackage.eINSTANCE.eClass();
        theEChartsPackage.createPackageContents();
        theEChartsPackage.initializePackageContents();
        theEChartsPackage.freeze();
        EPackage.Registry.INSTANCE.put(EChartsPackage.eNS_URI, theEChartsPackage);
        return theEChartsPackage;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getCompilationUnit() {
        return compilationUnitEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getPackageDecl() {
        return packageDeclEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getPackageDecl_Imports() {
        return (EReference) packageDeclEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getPackageDecl_MachineDeclaration() {
        return (EReference) packageDeclEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getPackageDecl_Name() {
        return (EAttribute) packageDeclEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getNamedMachine() {
        return namedMachineEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getNamedMachine_Modifiers() {
        return (EAttribute) namedMachineEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getNamedMachine_Name() {
        return (EAttribute) namedMachineEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getNamedMachine_Interfaces() {
        return (EReference) namedMachineEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getNamedMachine_Body() {
        return (EReference) namedMachineEClass.getEStructuralFeatures().get(3);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getMachineBody() {
        return machineBodyEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getMachineBody_Features() {
        return (EReference) machineBodyEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getMachineFeature() {
        return machineFeatureEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getMachineFeature_Constructor() {
        return (EAttribute) machineFeatureEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getMachineFeature_States() {
        return (EReference) machineFeatureEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getMachineFeature_Transitions() {
        return (EReference) machineFeatureEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getMachineFeature_HostBlocks() {
        return (EAttribute) machineFeatureEClass.getEStructuralFeatures().get(3);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getTransitionDeclaration() {
        return transitionDeclarationEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getTransitionDeclaration_Modifiers() {
        return (EReference) transitionDeclarationEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getTransitionDeclaration_StartStateRef() {
        return (EReference) transitionDeclarationEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getTransitionDeclaration_EndStateRef() {
        return (EReference) transitionDeclarationEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getStateRef() {
        return stateRefEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getStateRef_StartState() {
        return (EReference) stateRefEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getStateRef_SstartState() {
        return (EReference) stateRefEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getTransitionModifier() {
        return transitionModifierEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getTransitionModifier_FOO() {
        return (EAttribute) transitionModifierEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getState() {
        return stateEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getState_Modifiers() {
        return (EAttribute) stateEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getState_Name() {
        return (EAttribute) stateEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getState_EntryExp() {
        return (EReference) stateEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getState_ExitExp() {
        return (EReference) stateEClass.getEStructuralFeatures().get(3);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getState_ReflectedInvocation() {
        return (EReference) stateEClass.getEStructuralFeatures().get(4);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getState_StateMachineInvocation() {
        return (EReference) stateEClass.getEStructuralFeatures().get(5);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getState_MachineBody() {
        return (EReference) stateEClass.getEStructuralFeatures().get(6);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getStateMachineInvocation() {
        return stateMachineInvocationEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getStateMachineInvocation_MachineRef() {
        return (EAttribute) stateMachineInvocationEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getReflectedStateMachineInvocation() {
        return reflectedStateMachineInvocationEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getReflectedStateMachineInvocation_MachineName() {
        return (EAttribute) reflectedStateMachineInvocationEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getReflectedStateMachineInvocation_MachineArgumentArray() {
        return (EAttribute) reflectedStateMachineInvocationEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EEnum getStateModifier() {
        return stateModifierEEnum;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EEnum getMachineModifier() {
        return machineModifierEEnum;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EChartsFactory getEChartsFactory() {
        return (EChartsFactory) getEFactoryInstance();
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
        compilationUnitEClass = createEClass(COMPILATION_UNIT);
        packageDeclEClass = createEClass(PACKAGE_DECL);
        createEReference(packageDeclEClass, PACKAGE_DECL__IMPORTS);
        createEReference(packageDeclEClass, PACKAGE_DECL__MACHINE_DECLARATION);
        createEAttribute(packageDeclEClass, PACKAGE_DECL__NAME);
        namedMachineEClass = createEClass(NAMED_MACHINE);
        createEAttribute(namedMachineEClass, NAMED_MACHINE__MODIFIERS);
        createEAttribute(namedMachineEClass, NAMED_MACHINE__NAME);
        createEReference(namedMachineEClass, NAMED_MACHINE__INTERFACES);
        createEReference(namedMachineEClass, NAMED_MACHINE__BODY);
        machineBodyEClass = createEClass(MACHINE_BODY);
        createEReference(machineBodyEClass, MACHINE_BODY__FEATURES);
        machineFeatureEClass = createEClass(MACHINE_FEATURE);
        createEAttribute(machineFeatureEClass, MACHINE_FEATURE__CONSTRUCTOR);
        createEReference(machineFeatureEClass, MACHINE_FEATURE__STATES);
        createEReference(machineFeatureEClass, MACHINE_FEATURE__TRANSITIONS);
        createEAttribute(machineFeatureEClass, MACHINE_FEATURE__HOST_BLOCKS);
        transitionDeclarationEClass = createEClass(TRANSITION_DECLARATION);
        createEReference(transitionDeclarationEClass, TRANSITION_DECLARATION__MODIFIERS);
        createEReference(transitionDeclarationEClass, TRANSITION_DECLARATION__START_STATE_REF);
        createEReference(transitionDeclarationEClass, TRANSITION_DECLARATION__END_STATE_REF);
        stateRefEClass = createEClass(STATE_REF);
        createEReference(stateRefEClass, STATE_REF__START_STATE);
        createEReference(stateRefEClass, STATE_REF__SSTART_STATE);
        transitionModifierEClass = createEClass(TRANSITION_MODIFIER);
        createEAttribute(transitionModifierEClass, TRANSITION_MODIFIER__FOO);
        stateEClass = createEClass(STATE);
        createEAttribute(stateEClass, STATE__MODIFIERS);
        createEAttribute(stateEClass, STATE__NAME);
        createEReference(stateEClass, STATE__ENTRY_EXP);
        createEReference(stateEClass, STATE__EXIT_EXP);
        createEReference(stateEClass, STATE__REFLECTED_INVOCATION);
        createEReference(stateEClass, STATE__STATE_MACHINE_INVOCATION);
        createEReference(stateEClass, STATE__MACHINE_BODY);
        stateMachineInvocationEClass = createEClass(STATE_MACHINE_INVOCATION);
        createEAttribute(stateMachineInvocationEClass, STATE_MACHINE_INVOCATION__MACHINE_REF);
        reflectedStateMachineInvocationEClass = createEClass(REFLECTED_STATE_MACHINE_INVOCATION);
        createEAttribute(reflectedStateMachineInvocationEClass, REFLECTED_STATE_MACHINE_INVOCATION__MACHINE_NAME);
        createEAttribute(reflectedStateMachineInvocationEClass, REFLECTED_STATE_MACHINE_INVOCATION__MACHINE_ARGUMENT_ARRAY);
        stateModifierEEnum = createEEnum(STATE_MODIFIER);
        machineModifierEEnum = createEEnum(MACHINE_MODIFIER);
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
        TypesPackage theTypesPackage = (TypesPackage) EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
        XbasePackage theXbasePackage = (XbasePackage) EPackage.Registry.INSTANCE.getEPackage(XbasePackage.eNS_URI);
        packageDeclEClass.getESuperTypes().add(this.getCompilationUnit());
        initEClass(compilationUnitEClass, CompilationUnit.class, "CompilationUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(packageDeclEClass, PackageDecl.class, "PackageDecl", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPackageDecl_Imports(), theTypesPackage.getJvmWildcardTypeReference(), null, "imports", null, 0, -1, PackageDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPackageDecl_MachineDeclaration(), this.getNamedMachine(), null, "machineDeclaration", null, 0, 1, PackageDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPackageDecl_Name(), ecorePackage.getEString(), "name", null, 0, 1, PackageDecl.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(namedMachineEClass, NamedMachine.class, "NamedMachine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNamedMachine_Modifiers(), this.getMachineModifier(), "modifiers", null, 0, -1, NamedMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getNamedMachine_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNamedMachine_Interfaces(), theTypesPackage.getJvmTypeReference(), null, "interfaces", null, 0, -1, NamedMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNamedMachine_Body(), this.getMachineBody(), null, "body", null, 0, 1, NamedMachine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(machineBodyEClass, MachineBody.class, "MachineBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMachineBody_Features(), this.getMachineFeature(), null, "features", null, 0, -1, MachineBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(machineFeatureEClass, MachineFeature.class, "MachineFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMachineFeature_Constructor(), ecorePackage.getEString(), "constructor", null, 0, 1, MachineFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMachineFeature_States(), this.getState(), null, "states", null, 0, -1, MachineFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMachineFeature_Transitions(), this.getTransitionDeclaration(), null, "transitions", null, 0, -1, MachineFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMachineFeature_HostBlocks(), ecorePackage.getEString(), "hostBlocks", null, 0, -1, MachineFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(transitionDeclarationEClass, TransitionDeclaration.class, "TransitionDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTransitionDeclaration_Modifiers(), this.getTransitionModifier(), null, "modifiers", null, 0, -1, TransitionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransitionDeclaration_StartStateRef(), this.getStateRef(), null, "startStateRef", null, 0, 1, TransitionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTransitionDeclaration_EndStateRef(), this.getStateRef(), null, "endStateRef", null, 0, 1, TransitionDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(stateRefEClass, StateRef.class, "StateRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStateRef_StartState(), this.getState(), null, "startState", null, 0, -1, StateRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getStateRef_SstartState(), this.getStateRef(), null, "sstartState", null, 0, -1, StateRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(transitionModifierEClass, TransitionModifier.class, "TransitionModifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransitionModifier_FOO(), ecorePackage.getEString(), "FOO", null, 0, 1, TransitionModifier.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(stateEClass, State.class, "State", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getState_Modifiers(), this.getStateModifier(), "modifiers", null, 0, -1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getState_Name(), ecorePackage.getEString(), "name", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getState_EntryExp(), theXbasePackage.getXExpression(), null, "entryExp", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getState_ExitExp(), theXbasePackage.getXExpression(), null, "exitExp", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getState_ReflectedInvocation(), this.getReflectedStateMachineInvocation(), null, "reflectedInvocation", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getState_StateMachineInvocation(), this.getStateMachineInvocation(), null, "stateMachineInvocation", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getState_MachineBody(), this.getMachineBody(), null, "machineBody", null, 0, 1, State.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(stateMachineInvocationEClass, StateMachineInvocation.class, "StateMachineInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStateMachineInvocation_MachineRef(), ecorePackage.getEString(), "machineRef", null, 0, 1, StateMachineInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(reflectedStateMachineInvocationEClass, ReflectedStateMachineInvocation.class, "ReflectedStateMachineInvocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getReflectedStateMachineInvocation_MachineName(), ecorePackage.getEString(), "machineName", null, 0, 1, ReflectedStateMachineInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getReflectedStateMachineInvocation_MachineArgumentArray(), ecorePackage.getEString(), "machineArgumentArray", null, 0, 1, ReflectedStateMachineInvocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(stateModifierEEnum, StateModifier.class, "StateModifier");
        addEEnumLiteral(stateModifierEEnum, StateModifier.INITIAL);
        addEEnumLiteral(stateModifierEEnum, StateModifier.CONCURRENT);
        initEEnum(machineModifierEEnum, MachineModifier.class, "MachineModifier");
        addEEnumLiteral(machineModifierEEnum, MachineModifier.PUBLIC);
        addEEnumLiteral(machineModifierEEnum, MachineModifier.CONCURRENT);
        createResource(eNS_URI);
    }
}
