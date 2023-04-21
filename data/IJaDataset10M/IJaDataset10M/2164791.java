package de.uniAugsburg.MAF.attrmm.model.attrmm;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.attrmmFactory
 * @model kind="package"
 * @generated
 */
public interface attrmmPackage extends EPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2009  Christian Saad, University of Augsburg, Germany <www.ds-lab.org>";

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "attrmm";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://www.uni-augsburg.de/pvs/MAF/attrmm";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "attrmm";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    attrmmPackage eINSTANCE = de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl.init();

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionElementImpl <em>Attribution Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionElementImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttributionElement()
	 * @generated
	 */
    int ATTRIBUTION_ELEMENT = 0;

    /**
	 * The number of structural features of the '<em>Attribution Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION_ELEMENT_FEATURE_COUNT = 0;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionCollectionImpl <em>Attribution Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionCollectionImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttributionCollection()
	 * @generated
	 */
    int ATTRIBUTION_COLLECTION = 1;

    /**
	 * The feature id for the '<em><b>Attributions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION_COLLECTION__ATTRIBUTIONS = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Attribution Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION_COLLECTION_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrExtensionImpl <em>Attr Extension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrExtensionImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrExtension()
	 * @generated
	 */
    int ATTR_EXTENSION = 3;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrDefinitionImpl <em>Attr Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrDefinitionImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrDefinition()
	 * @generated
	 */
    int ATTR_DEFINITION = 4;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrOccurrenceImpl <em>Attr Occurrence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrOccurrenceImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrOccurrence()
	 * @generated
	 */
    int ATTR_OCCURRENCE = 5;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrSemanticRuleImpl <em>Attr Semantic Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrSemanticRuleImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrSemanticRule()
	 * @generated
	 */
    int ATTR_SEMANTIC_RULE = 8;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrAssignmentImpl <em>Attr Assignment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrAssignmentImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrAssignment()
	 * @generated
	 */
    int ATTR_ASSIGNMENT = 6;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrConstraintImpl <em>Attr Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrConstraintImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrConstraint()
	 * @generated
	 */
    int ATTR_CONSTRAINT = 7;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionImpl <em>Attribution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionImpl
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttribution()
	 * @generated
	 */
    int ATTRIBUTION = 2;

    /**
	 * The feature id for the '<em><b>Attr Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__ATTR_EXTENSIONS = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Attr Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__ATTR_DEFINITIONS = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__ID = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__NAME = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__DESCRIPTION = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION__VERSION = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 5;

    /**
	 * The number of structural features of the '<em>Attribution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTRIBUTION_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_EXTENSION__ATTRIBUTES = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Attributed Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_EXTENSION__ATTRIBUTED_CLASS = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Attr Extension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_EXTENSION_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_DEFINITION__ID = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_DEFINITION__DESCRIPTION = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_DEFINITION__DATA_TYPE = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Start Value Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_DEFINITION__START_VALUE_RULE = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The number of structural features of the '<em>Attr Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_DEFINITION_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Defined By</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_OCCURRENCE__DEFINED_BY = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Calculated By</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_OCCURRENCE__CALCULATED_BY = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Contained In</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_OCCURRENCE__CONTAINED_IN = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The number of structural features of the '<em>Attr Occurrence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_OCCURRENCE_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Rule</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_SEMANTIC_RULE__RULE = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Rule Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_SEMANTIC_RULE__RULE_TYPE = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Attr Semantic Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_SEMANTIC_RULE_FEATURE_COUNT = ATTRIBUTION_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Rule</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_ASSIGNMENT__RULE = ATTR_SEMANTIC_RULE__RULE;

    /**
	 * The feature id for the '<em><b>Rule Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_ASSIGNMENT__RULE_TYPE = ATTR_SEMANTIC_RULE__RULE_TYPE;

    /**
	 * The number of structural features of the '<em>Attr Assignment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_ASSIGNMENT_FEATURE_COUNT = ATTR_SEMANTIC_RULE_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Rule</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_CONSTRAINT__RULE = ATTR_SEMANTIC_RULE__RULE;

    /**
	 * The feature id for the '<em><b>Rule Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_CONSTRAINT__RULE_TYPE = ATTR_SEMANTIC_RULE__RULE_TYPE;

    /**
	 * The feature id for the '<em><b>Violation ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_CONSTRAINT__VIOLATION_ID = ATTR_SEMANTIC_RULE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Attr Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ATTR_CONSTRAINT_FEATURE_COUNT = ATTR_SEMANTIC_RULE_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType <em>Semantic Rule Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getSemanticRuleType()
	 * @generated
	 */
    int SEMANTIC_RULE_TYPE = 9;

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension <em>Attr Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Extension</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension
	 * @generated
	 */
    EClass getAttrExtension();

    /**
	 * Returns the meta object for the containment reference list '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension#getAttributes()
	 * @see #getAttrExtension()
	 * @generated
	 */
    EReference getAttrExtension_Attributes();

    /**
	 * Returns the meta object for the reference '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension#getAttributedClass <em>Attributed Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Attributed Class</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrExtension#getAttributedClass()
	 * @see #getAttrExtension()
	 * @generated
	 */
    EReference getAttrExtension_AttributedClass();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition <em>Attr Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Definition</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition
	 * @generated
	 */
    EClass getAttrDefinition();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getId()
	 * @see #getAttrDefinition()
	 * @generated
	 */
    EAttribute getAttrDefinition_Id();

    /**
	 * Returns the meta object for the containment reference '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getStartValueRule <em>Start Value Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Value Rule</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getStartValueRule()
	 * @see #getAttrDefinition()
	 * @generated
	 */
    EReference getAttrDefinition_StartValueRule();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getDescription()
	 * @see #getAttrDefinition()
	 * @generated
	 */
    EAttribute getAttrDefinition_Description();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Type</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrDefinition#getDataType()
	 * @see #getAttrDefinition()
	 * @generated
	 */
    EAttribute getAttrDefinition_DataType();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence <em>Attr Occurrence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Occurrence</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence
	 * @generated
	 */
    EClass getAttrOccurrence();

    /**
	 * Returns the meta object for the reference '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getDefinedBy <em>Defined By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Defined By</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getDefinedBy()
	 * @see #getAttrOccurrence()
	 * @generated
	 */
    EReference getAttrOccurrence_DefinedBy();

    /**
	 * Returns the meta object for the containment reference '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getCalculatedBy <em>Calculated By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Calculated By</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getCalculatedBy()
	 * @see #getAttrOccurrence()
	 * @generated
	 */
    EReference getAttrOccurrence_CalculatedBy();

    /**
	 * Returns the meta object for the container reference '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getContainedIn <em>Contained In</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Contained In</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrOccurrence#getContainedIn()
	 * @see #getAttrOccurrence()
	 * @generated
	 */
    EReference getAttrOccurrence_ContainedIn();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrAssignment <em>Attr Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Assignment</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrAssignment
	 * @generated
	 */
    EClass getAttrAssignment();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrConstraint <em>Attr Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Constraint</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrConstraint
	 * @generated
	 */
    EClass getAttrConstraint();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrConstraint#getViolationID <em>Violation ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Violation ID</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrConstraint#getViolationID()
	 * @see #getAttrConstraint()
	 * @generated
	 */
    EAttribute getAttrConstraint_ViolationID();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule <em>Attr Semantic Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attr Semantic Rule</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule
	 * @generated
	 */
    EClass getAttrSemanticRule();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule#getRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rule</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule#getRule()
	 * @see #getAttrSemanticRule()
	 * @generated
	 */
    EAttribute getAttrSemanticRule_Rule();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule#getRuleType <em>Rule Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rule Type</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttrSemanticRule#getRuleType()
	 * @see #getAttrSemanticRule()
	 * @generated
	 */
    EAttribute getAttrSemanticRule_RuleType();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution <em>Attribution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribution</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution
	 * @generated
	 */
    EClass getAttribution();

    /**
	 * Returns the meta object for the containment reference list '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getAttrExtensions <em>Attr Extensions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attr Extensions</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getAttrExtensions()
	 * @see #getAttribution()
	 * @generated
	 */
    EReference getAttribution_AttrExtensions();

    /**
	 * Returns the meta object for the containment reference list '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getAttrDefinitions <em>Attr Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attr Definitions</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getAttrDefinitions()
	 * @see #getAttribution()
	 * @generated
	 */
    EReference getAttribution_AttrDefinitions();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getId()
	 * @see #getAttribution()
	 * @generated
	 */
    EAttribute getAttribution_Id();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getName()
	 * @see #getAttribution()
	 * @generated
	 */
    EAttribute getAttribution_Name();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getDescription()
	 * @see #getAttribution()
	 * @generated
	 */
    EAttribute getAttribution_Description();

    /**
	 * Returns the meta object for the attribute '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.Attribution#getVersion()
	 * @see #getAttribution()
	 * @generated
	 */
    EAttribute getAttribution_Version();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionElement <em>Attribution Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribution Element</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionElement
	 * @generated
	 */
    EClass getAttributionElement();

    /**
	 * Returns the meta object for class '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionCollection <em>Attribution Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribution Collection</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionCollection
	 * @generated
	 */
    EClass getAttributionCollection();

    /**
	 * Returns the meta object for the containment reference list '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionCollection#getAttributions <em>Attributions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributions</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.AttributionCollection#getAttributions()
	 * @see #getAttributionCollection()
	 * @generated
	 */
    EReference getAttributionCollection_Attributions();

    /**
	 * Returns the meta object for enum '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType <em>Semantic Rule Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Semantic Rule Type</em>'.
	 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType
	 * @generated
	 */
    EEnum getSemanticRuleType();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    attrmmFactory getattrmmFactory();

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
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrExtensionImpl <em>Attr Extension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrExtensionImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrExtension()
		 * @generated
		 */
        EClass ATTR_EXTENSION = eINSTANCE.getAttrExtension();

        /**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_EXTENSION__ATTRIBUTES = eINSTANCE.getAttrExtension_Attributes();

        /**
		 * The meta object literal for the '<em><b>Attributed Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_EXTENSION__ATTRIBUTED_CLASS = eINSTANCE.getAttrExtension_AttributedClass();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrDefinitionImpl <em>Attr Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrDefinitionImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrDefinition()
		 * @generated
		 */
        EClass ATTR_DEFINITION = eINSTANCE.getAttrDefinition();

        /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_DEFINITION__ID = eINSTANCE.getAttrDefinition_Id();

        /**
		 * The meta object literal for the '<em><b>Start Value Rule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_DEFINITION__START_VALUE_RULE = eINSTANCE.getAttrDefinition_StartValueRule();

        /**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_DEFINITION__DESCRIPTION = eINSTANCE.getAttrDefinition_Description();

        /**
		 * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_DEFINITION__DATA_TYPE = eINSTANCE.getAttrDefinition_DataType();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrOccurrenceImpl <em>Attr Occurrence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrOccurrenceImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrOccurrence()
		 * @generated
		 */
        EClass ATTR_OCCURRENCE = eINSTANCE.getAttrOccurrence();

        /**
		 * The meta object literal for the '<em><b>Defined By</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_OCCURRENCE__DEFINED_BY = eINSTANCE.getAttrOccurrence_DefinedBy();

        /**
		 * The meta object literal for the '<em><b>Calculated By</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_OCCURRENCE__CALCULATED_BY = eINSTANCE.getAttrOccurrence_CalculatedBy();

        /**
		 * The meta object literal for the '<em><b>Contained In</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTR_OCCURRENCE__CONTAINED_IN = eINSTANCE.getAttrOccurrence_ContainedIn();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrAssignmentImpl <em>Attr Assignment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrAssignmentImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrAssignment()
		 * @generated
		 */
        EClass ATTR_ASSIGNMENT = eINSTANCE.getAttrAssignment();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrConstraintImpl <em>Attr Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrConstraintImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrConstraint()
		 * @generated
		 */
        EClass ATTR_CONSTRAINT = eINSTANCE.getAttrConstraint();

        /**
		 * The meta object literal for the '<em><b>Violation ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_CONSTRAINT__VIOLATION_ID = eINSTANCE.getAttrConstraint_ViolationID();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrSemanticRuleImpl <em>Attr Semantic Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttrSemanticRuleImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttrSemanticRule()
		 * @generated
		 */
        EClass ATTR_SEMANTIC_RULE = eINSTANCE.getAttrSemanticRule();

        /**
		 * The meta object literal for the '<em><b>Rule</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_SEMANTIC_RULE__RULE = eINSTANCE.getAttrSemanticRule_Rule();

        /**
		 * The meta object literal for the '<em><b>Rule Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTR_SEMANTIC_RULE__RULE_TYPE = eINSTANCE.getAttrSemanticRule_RuleType();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionImpl <em>Attribution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttribution()
		 * @generated
		 */
        EClass ATTRIBUTION = eINSTANCE.getAttribution();

        /**
		 * The meta object literal for the '<em><b>Attr Extensions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTRIBUTION__ATTR_EXTENSIONS = eINSTANCE.getAttribution_AttrExtensions();

        /**
		 * The meta object literal for the '<em><b>Attr Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTRIBUTION__ATTR_DEFINITIONS = eINSTANCE.getAttribution_AttrDefinitions();

        /**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTRIBUTION__ID = eINSTANCE.getAttribution_Id();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTRIBUTION__NAME = eINSTANCE.getAttribution_Name();

        /**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTRIBUTION__DESCRIPTION = eINSTANCE.getAttribution_Description();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute ATTRIBUTION__VERSION = eINSTANCE.getAttribution_Version();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionElementImpl <em>Attribution Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionElementImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttributionElement()
		 * @generated
		 */
        EClass ATTRIBUTION_ELEMENT = eINSTANCE.getAttributionElement();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionCollectionImpl <em>Attribution Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.AttributionCollectionImpl
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getAttributionCollection()
		 * @generated
		 */
        EClass ATTRIBUTION_COLLECTION = eINSTANCE.getAttributionCollection();

        /**
		 * The meta object literal for the '<em><b>Attributions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference ATTRIBUTION_COLLECTION__ATTRIBUTIONS = eINSTANCE.getAttributionCollection_Attributions();

        /**
		 * The meta object literal for the '{@link de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType <em>Semantic Rule Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.SemanticRuleType
		 * @see de.uniAugsburg.MAF.attrmm.model.attrmm.impl.attrmmPackageImpl#getSemanticRuleType()
		 * @generated
		 */
        EEnum SEMANTIC_RULE_TYPE = eINSTANCE.getSemanticRuleType();
    }
}
