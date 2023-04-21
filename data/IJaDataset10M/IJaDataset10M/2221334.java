package org.openarchitectureware.recipe2.dsl.detector.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.openarchitectureware.recipe2.dsl.detector.Detector;
import org.openarchitectureware.recipe2.dsl.detector.DetectorFactory;
import org.openarchitectureware.recipe2.dsl.detector.DetectorPackage;
import org.openarchitectureware.recipe2.dsl.detector.Parameter;
import org.openarchitectureware.recipe2.dsl.detector.RecipeDetectorModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DetectorPackageImpl extends EPackageImpl implements DetectorPackage {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass recipeDetectorModelEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass detectorEClass = null;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    private EClass parameterEClass = null;

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
   * @see org.openarchitectureware.recipe2.dsl.detector.DetectorPackage#eNS_URI
   * @see #init()
   * @generated
   */
    private DetectorPackageImpl() {
        super(eNS_URI, DetectorFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link DetectorPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
    public static DetectorPackage init() {
        if (isInited) return (DetectorPackage) EPackage.Registry.INSTANCE.getEPackage(DetectorPackage.eNS_URI);
        DetectorPackageImpl theDetectorPackage = (DetectorPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DetectorPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DetectorPackageImpl());
        isInited = true;
        theDetectorPackage.createPackageContents();
        theDetectorPackage.initializePackageContents();
        theDetectorPackage.freeze();
        EPackage.Registry.INSTANCE.put(DetectorPackage.eNS_URI, theDetectorPackage);
        return theDetectorPackage;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getRecipeDetectorModel() {
        return recipeDetectorModelEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getRecipeDetectorModel_Name() {
        return (EAttribute) recipeDetectorModelEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getRecipeDetectorModel_BugDetectors() {
        return (EReference) recipeDetectorModelEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getDetector() {
        return detectorEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getDetector_Predef() {
        return (EAttribute) detectorEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getDetector_Message() {
        return (EAttribute) detectorEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EReference getDetector_Parameters() {
        return (EReference) detectorEClass.getEStructuralFeatures().get(2);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EClass getParameter() {
        return parameterEClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getParameter_Name() {
        return (EAttribute) parameterEClass.getEStructuralFeatures().get(0);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EAttribute getParameter_Value() {
        return (EAttribute) parameterEClass.getEStructuralFeatures().get(1);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DetectorFactory getDetectorFactory() {
        return (DetectorFactory) getEFactoryInstance();
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
        recipeDetectorModelEClass = createEClass(RECIPE_DETECTOR_MODEL);
        createEAttribute(recipeDetectorModelEClass, RECIPE_DETECTOR_MODEL__NAME);
        createEReference(recipeDetectorModelEClass, RECIPE_DETECTOR_MODEL__BUG_DETECTORS);
        detectorEClass = createEClass(DETECTOR);
        createEAttribute(detectorEClass, DETECTOR__PREDEF);
        createEAttribute(detectorEClass, DETECTOR__MESSAGE);
        createEReference(detectorEClass, DETECTOR__PARAMETERS);
        parameterEClass = createEClass(PARAMETER);
        createEAttribute(parameterEClass, PARAMETER__NAME);
        createEAttribute(parameterEClass, PARAMETER__VALUE);
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
        initEClass(recipeDetectorModelEClass, RecipeDetectorModel.class, "RecipeDetectorModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRecipeDetectorModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, RecipeDetectorModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRecipeDetectorModel_BugDetectors(), this.getDetector(), null, "bugDetectors", null, 0, -1, RecipeDetectorModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(detectorEClass, Detector.class, "Detector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDetector_Predef(), ecorePackage.getEString(), "predef", null, 0, 1, Detector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDetector_Message(), ecorePackage.getEString(), "message", null, 0, 1, Detector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDetector_Parameters(), this.getParameter(), null, "parameters", null, 0, -1, Detector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getParameter_Name(), ecorePackage.getEString(), "name", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getParameter_Value(), ecorePackage.getEString(), "value", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
