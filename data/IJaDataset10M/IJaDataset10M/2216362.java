package org.modelversioning.core.conditions.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.modelversioning.core.conditions.AttributeCorrespondenceCondition;
import org.modelversioning.core.conditions.Condition;
import org.modelversioning.core.conditions.ConditionType;
import org.modelversioning.core.conditions.ConditionsFactory;
import org.modelversioning.core.conditions.ConditionsModel;
import org.modelversioning.core.conditions.ConditionsPackage;
import org.modelversioning.core.conditions.CustomCondition;
import org.modelversioning.core.conditions.EvaluationResult;
import org.modelversioning.core.conditions.EvaluationStatus;
import org.modelversioning.core.conditions.FeatureCondition;
import org.modelversioning.core.conditions.Model;
import org.modelversioning.core.conditions.NonExistenceGroup;
import org.modelversioning.core.conditions.OptionGroup;
import org.modelversioning.core.conditions.RefinementTemplate;
import org.modelversioning.core.conditions.State;
import org.modelversioning.core.conditions.Template;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConditionsPackageImpl extends EPackageImpl implements ConditionsPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass templateEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass conditionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass featureConditionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass attributeCorrespondenceConditionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass customConditionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass conditionsModelEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass evaluationResultEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass refinementTemplateEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass optionGroupEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass nonExistenceGroupEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum conditionTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum stateEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum modelEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum evaluationStatusEEnum = null;

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
	 * @see org.modelversioning.core.conditions.ConditionsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private ConditionsPackageImpl() {
        super(eNS_URI, ConditionsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ConditionsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static ConditionsPackage init() {
        if (isInited) return (ConditionsPackage) EPackage.Registry.INSTANCE.getEPackage(ConditionsPackage.eNS_URI);
        ConditionsPackageImpl theConditionsPackage = (ConditionsPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ConditionsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ConditionsPackageImpl());
        isInited = true;
        EcorePackage.eINSTANCE.eClass();
        theConditionsPackage.createPackageContents();
        theConditionsPackage.initializePackageContents();
        theConditionsPackage.freeze();
        EPackage.Registry.INSTANCE.put(ConditionsPackage.eNS_URI, theConditionsPackage);
        return theConditionsPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTemplate() {
        return templateEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_Name() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_SubTemplates() {
        return (EReference) templateEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_Specifications() {
        return (EReference) templateEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_State() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_Title() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_Representative() {
        return (EReference) templateEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_Model() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_ParentTemplate() {
        return (EReference) templateEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_ParentsFeature() {
        return (EReference) templateEClass.getEStructuralFeatures().get(8);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_ContainingModel() {
        return (EReference) templateEClass.getEStructuralFeatures().get(9);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_Active() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(10);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTemplate_Parameter() {
        return (EAttribute) templateEClass.getEStructuralFeatures().get(11);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_OptionGroups() {
        return (EReference) templateEClass.getEStructuralFeatures().get(12);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTemplate_NonExistenceGroups() {
        return (EReference) templateEClass.getEStructuralFeatures().get(13);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCondition() {
        return conditionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCondition_Type() {
        return (EAttribute) conditionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCondition_State() {
        return (EAttribute) conditionEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCondition_Active() {
        return (EAttribute) conditionEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCondition_Template() {
        return (EReference) conditionEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCondition_InvolvesTemplate() {
        return (EAttribute) conditionEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getFeatureCondition() {
        return featureConditionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getFeatureCondition_Feature() {
        return (EReference) featureConditionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getFeatureCondition_Expression() {
        return (EAttribute) featureConditionEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAttributeCorrespondenceCondition() {
        return attributeCorrespondenceConditionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAttributeCorrespondenceCondition_CorrespondingTemplates() {
        return (EReference) attributeCorrespondenceConditionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAttributeCorrespondenceCondition_CorrespondingFeatures() {
        return (EReference) attributeCorrespondenceConditionEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCustomCondition() {
        return customConditionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCustomCondition_Expression() {
        return (EAttribute) customConditionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getConditionsModel() {
        return conditionsModelEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConditionsModel_RootTemplate() {
        return (EReference) conditionsModelEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getConditionsModel_CreatedAt() {
        return (EAttribute) conditionsModelEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getConditionsModel_ModelName() {
        return (EAttribute) conditionsModelEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getConditionsModel_Language() {
        return (EAttribute) conditionsModelEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConditionsModel_OptionGroups() {
        return (EReference) conditionsModelEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConditionsModel_NonExistenceGroups() {
        return (EReference) conditionsModelEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getEvaluationResult() {
        return evaluationResultEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getEvaluationResult_Message() {
        return (EAttribute) evaluationResultEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getEvaluationResult_Status() {
        return (EAttribute) evaluationResultEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getEvaluationResult_Exception() {
        return (EAttribute) evaluationResultEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getEvaluationResult_Evaluator() {
        return (EAttribute) evaluationResultEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getEvaluationResult_SubResults() {
        return (EReference) evaluationResultEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getEvaluationResult_ParentResult() {
        return (EReference) evaluationResultEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getEvaluationResult_FailedCondition() {
        return (EReference) evaluationResultEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getEvaluationResult_FailedCandidate() {
        return (EReference) evaluationResultEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRefinementTemplate() {
        return refinementTemplateEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRefinementTemplate_RefinedTemplate() {
        return (EReference) refinementTemplateEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getOptionGroup() {
        return optionGroupEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOptionGroup_Name() {
        return (EAttribute) optionGroupEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getOptionGroup_Templates() {
        return (EReference) optionGroupEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getOptionGroup_Replace() {
        return (EAttribute) optionGroupEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getNonExistenceGroup() {
        return nonExistenceGroupEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getNonExistenceGroup_Name() {
        return (EAttribute) nonExistenceGroupEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getNonExistenceGroup_Templates() {
        return (EReference) nonExistenceGroupEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getConditionType() {
        return conditionTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getState() {
        return stateEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getModel() {
        return modelEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getEvaluationStatus() {
        return evaluationStatusEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ConditionsFactory getConditionsFactory() {
        return (ConditionsFactory) getEFactoryInstance();
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
        templateEClass = createEClass(TEMPLATE);
        createEAttribute(templateEClass, TEMPLATE__NAME);
        createEReference(templateEClass, TEMPLATE__SUB_TEMPLATES);
        createEReference(templateEClass, TEMPLATE__SPECIFICATIONS);
        createEAttribute(templateEClass, TEMPLATE__STATE);
        createEAttribute(templateEClass, TEMPLATE__TITLE);
        createEReference(templateEClass, TEMPLATE__REPRESENTATIVE);
        createEAttribute(templateEClass, TEMPLATE__MODEL);
        createEReference(templateEClass, TEMPLATE__PARENT_TEMPLATE);
        createEReference(templateEClass, TEMPLATE__PARENTS_FEATURE);
        createEReference(templateEClass, TEMPLATE__CONTAINING_MODEL);
        createEAttribute(templateEClass, TEMPLATE__ACTIVE);
        createEAttribute(templateEClass, TEMPLATE__PARAMETER);
        createEReference(templateEClass, TEMPLATE__OPTION_GROUPS);
        createEReference(templateEClass, TEMPLATE__NON_EXISTENCE_GROUPS);
        conditionEClass = createEClass(CONDITION);
        createEAttribute(conditionEClass, CONDITION__TYPE);
        createEAttribute(conditionEClass, CONDITION__STATE);
        createEAttribute(conditionEClass, CONDITION__ACTIVE);
        createEReference(conditionEClass, CONDITION__TEMPLATE);
        createEAttribute(conditionEClass, CONDITION__INVOLVES_TEMPLATE);
        featureConditionEClass = createEClass(FEATURE_CONDITION);
        createEReference(featureConditionEClass, FEATURE_CONDITION__FEATURE);
        createEAttribute(featureConditionEClass, FEATURE_CONDITION__EXPRESSION);
        attributeCorrespondenceConditionEClass = createEClass(ATTRIBUTE_CORRESPONDENCE_CONDITION);
        createEReference(attributeCorrespondenceConditionEClass, ATTRIBUTE_CORRESPONDENCE_CONDITION__CORRESPONDING_TEMPLATES);
        createEReference(attributeCorrespondenceConditionEClass, ATTRIBUTE_CORRESPONDENCE_CONDITION__CORRESPONDING_FEATURES);
        customConditionEClass = createEClass(CUSTOM_CONDITION);
        createEAttribute(customConditionEClass, CUSTOM_CONDITION__EXPRESSION);
        conditionsModelEClass = createEClass(CONDITIONS_MODEL);
        createEReference(conditionsModelEClass, CONDITIONS_MODEL__ROOT_TEMPLATE);
        createEAttribute(conditionsModelEClass, CONDITIONS_MODEL__CREATED_AT);
        createEAttribute(conditionsModelEClass, CONDITIONS_MODEL__MODEL_NAME);
        createEAttribute(conditionsModelEClass, CONDITIONS_MODEL__LANGUAGE);
        createEReference(conditionsModelEClass, CONDITIONS_MODEL__OPTION_GROUPS);
        createEReference(conditionsModelEClass, CONDITIONS_MODEL__NON_EXISTENCE_GROUPS);
        evaluationResultEClass = createEClass(EVALUATION_RESULT);
        createEAttribute(evaluationResultEClass, EVALUATION_RESULT__MESSAGE);
        createEAttribute(evaluationResultEClass, EVALUATION_RESULT__STATUS);
        createEAttribute(evaluationResultEClass, EVALUATION_RESULT__EXCEPTION);
        createEAttribute(evaluationResultEClass, EVALUATION_RESULT__EVALUATOR);
        createEReference(evaluationResultEClass, EVALUATION_RESULT__SUB_RESULTS);
        createEReference(evaluationResultEClass, EVALUATION_RESULT__PARENT_RESULT);
        createEReference(evaluationResultEClass, EVALUATION_RESULT__FAILED_CONDITION);
        createEReference(evaluationResultEClass, EVALUATION_RESULT__FAILED_CANDIDATE);
        refinementTemplateEClass = createEClass(REFINEMENT_TEMPLATE);
        createEReference(refinementTemplateEClass, REFINEMENT_TEMPLATE__REFINED_TEMPLATE);
        optionGroupEClass = createEClass(OPTION_GROUP);
        createEAttribute(optionGroupEClass, OPTION_GROUP__NAME);
        createEReference(optionGroupEClass, OPTION_GROUP__TEMPLATES);
        createEAttribute(optionGroupEClass, OPTION_GROUP__REPLACE);
        nonExistenceGroupEClass = createEClass(NON_EXISTENCE_GROUP);
        createEAttribute(nonExistenceGroupEClass, NON_EXISTENCE_GROUP__NAME);
        createEReference(nonExistenceGroupEClass, NON_EXISTENCE_GROUP__TEMPLATES);
        conditionTypeEEnum = createEEnum(CONDITION_TYPE);
        stateEEnum = createEEnum(STATE);
        modelEEnum = createEEnum(MODEL);
        evaluationStatusEEnum = createEEnum(EVALUATION_STATUS);
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
        featureConditionEClass.getESuperTypes().add(this.getCondition());
        attributeCorrespondenceConditionEClass.getESuperTypes().add(this.getFeatureCondition());
        customConditionEClass.getESuperTypes().add(this.getCondition());
        refinementTemplateEClass.getESuperTypes().add(this.getTemplate());
        initEClass(templateEClass, Template.class, "Template", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTemplate_Name(), ecorePackage.getEString(), "name", null, 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_SubTemplates(), this.getTemplate(), this.getTemplate_ParentTemplate(), "subTemplates", null, 0, -1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_Specifications(), this.getCondition(), this.getCondition_Template(), "specifications", null, 0, -1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemplate_State(), this.getState(), "state", null, 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemplate_Title(), ecorePackage.getEString(), "title", "", 0, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_Representative(), theEcorePackage.getEObject(), null, "representative", null, 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemplate_Model(), this.getModel(), "model", null, 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_ParentTemplate(), this.getTemplate(), this.getTemplate_SubTemplates(), "parentTemplate", null, 0, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_ParentsFeature(), theEcorePackage.getEStructuralFeature(), null, "parentsFeature", null, 0, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_ContainingModel(), this.getConditionsModel(), this.getConditionsModel_RootTemplate(), "containingModel", null, 0, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemplate_Active(), ecorePackage.getEBoolean(), "active", "true", 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTemplate_Parameter(), theEcorePackage.getEBoolean(), "parameter", "true", 1, 1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_OptionGroups(), this.getOptionGroup(), this.getOptionGroup_Templates(), "optionGroups", null, 0, -1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTemplate_NonExistenceGroups(), this.getNonExistenceGroup(), this.getNonExistenceGroup_Templates(), "nonExistenceGroups", null, 0, -1, Template.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        addEOperation(templateEClass, ecorePackage.getEBoolean(), "isMandatory", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEOperation(templateEClass, theEcorePackage.getEBoolean(), "isExistence", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(conditionEClass, Condition.class, "Condition", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCondition_Type(), this.getConditionType(), "type", null, 1, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCondition_State(), this.getState(), "state", null, 1, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCondition_Active(), ecorePackage.getEBoolean(), "active", "true", 1, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getCondition_Template(), this.getTemplate(), this.getTemplate_Specifications(), "template", null, 1, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCondition_InvolvesTemplate(), ecorePackage.getEBoolean(), "involvesTemplate", "true", 0, 1, Condition.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(featureConditionEClass, FeatureCondition.class, "FeatureCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureCondition_Feature(), theEcorePackage.getEStructuralFeature(), null, "feature", null, 1, 1, FeatureCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getFeatureCondition_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, FeatureCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(attributeCorrespondenceConditionEClass, AttributeCorrespondenceCondition.class, "AttributeCorrespondenceCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAttributeCorrespondenceCondition_CorrespondingTemplates(), this.getTemplate(), null, "correspondingTemplates", null, 0, -1, AttributeCorrespondenceCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAttributeCorrespondenceCondition_CorrespondingFeatures(), theEcorePackage.getEStructuralFeature(), null, "correspondingFeatures", null, 0, -1, AttributeCorrespondenceCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(customConditionEClass, CustomCondition.class, "CustomCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCustomCondition_Expression(), ecorePackage.getEString(), "expression", null, 1, 1, CustomCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(conditionsModelEClass, ConditionsModel.class, "ConditionsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConditionsModel_RootTemplate(), this.getTemplate(), this.getTemplate_ContainingModel(), "rootTemplate", null, 1, 1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionsModel_CreatedAt(), ecorePackage.getEDate(), "createdAt", null, 0, 1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionsModel_ModelName(), ecorePackage.getEString(), "modelName", null, 0, 1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConditionsModel_Language(), ecorePackage.getEString(), "language", "OCL", 0, 1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConditionsModel_OptionGroups(), this.getOptionGroup(), null, "optionGroups", null, 0, -1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConditionsModel_NonExistenceGroups(), this.getNonExistenceGroup(), null, "nonExistenceGroups", null, 0, -1, ConditionsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(evaluationResultEClass, EvaluationResult.class, "EvaluationResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEvaluationResult_Message(), ecorePackage.getEString(), "message", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEvaluationResult_Status(), this.getEvaluationStatus(), "status", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEvaluationResult_Exception(), ecorePackage.getEJavaObject(), "exception", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getEvaluationResult_Evaluator(), ecorePackage.getEString(), "evaluator", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEvaluationResult_SubResults(), this.getEvaluationResult(), this.getEvaluationResult_ParentResult(), "subResults", null, 0, -1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEvaluationResult_ParentResult(), this.getEvaluationResult(), this.getEvaluationResult_SubResults(), "parentResult", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEvaluationResult_FailedCondition(), this.getCondition(), null, "failedCondition", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEvaluationResult_FailedCandidate(), theEcorePackage.getEObject(), null, "failedCandidate", null, 0, 1, EvaluationResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        addEOperation(evaluationResultEClass, ecorePackage.getEBoolean(), "isMultiResult", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEOperation(evaluationResultEClass, ecorePackage.getEBoolean(), "isOK", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEClass(refinementTemplateEClass, RefinementTemplate.class, "RefinementTemplate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRefinementTemplate_RefinedTemplate(), this.getTemplate(), null, "refinedTemplate", null, 0, 1, RefinementTemplate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(optionGroupEClass, OptionGroup.class, "OptionGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOptionGroup_Name(), ecorePackage.getEString(), "name", null, 0, 1, OptionGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOptionGroup_Templates(), this.getTemplate(), this.getTemplate_OptionGroups(), "templates", null, 0, -1, OptionGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getOptionGroup_Replace(), theEcorePackage.getEBoolean(), "replace", "true", 0, 1, OptionGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(nonExistenceGroupEClass, NonExistenceGroup.class, "NonExistenceGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getNonExistenceGroup_Name(), theEcorePackage.getEString(), "name", null, 0, 1, NonExistenceGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNonExistenceGroup_Templates(), this.getTemplate(), this.getTemplate_NonExistenceGroups(), "templates", null, 0, -1, NonExistenceGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(conditionTypeEEnum, ConditionType.class, "ConditionType");
        addEEnumLiteral(conditionTypeEEnum, ConditionType.ONTOLOGICAL);
        addEEnumLiteral(conditionTypeEEnum, ConditionType.LINGUISTIC);
        initEEnum(stateEEnum, State.class, "State");
        addEEnumLiteral(stateEEnum, State.GENERATED);
        addEEnumLiteral(stateEEnum, State.EDITED);
        addEEnumLiteral(stateEEnum, State.USER_DEFINED);
        initEEnum(modelEEnum, Model.class, "Model");
        addEEnumLiteral(modelEEnum, Model.ORIGIN);
        addEEnumLiteral(modelEEnum, Model.WORKING);
        initEEnum(evaluationStatusEEnum, EvaluationStatus.class, "EvaluationStatus");
        addEEnumLiteral(evaluationStatusEEnum, EvaluationStatus.SATISFIED);
        addEEnumLiteral(evaluationStatusEEnum, EvaluationStatus.UNSATISFIED);
        addEEnumLiteral(evaluationStatusEEnum, EvaluationStatus.ERROR);
        createResource(eNS_URI);
    }
}
