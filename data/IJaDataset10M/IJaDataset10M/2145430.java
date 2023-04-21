package UMLModelMM;

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
 * @see UMLModelMM.UMLModelMMFactory
 * @model kind="package"
 * @generated
 */
public interface UMLModelMMPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "UMLModelMM";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///UMLModelMM.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "UMLModelMM";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    UMLModelMMPackage eINSTANCE = UMLModelMM.impl.UMLModelMMPackageImpl.init();

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.ElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.ElementImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getElement()
	 * @generated
	 */
    int ELEMENT = 0;

    /**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ELEMENT_FEATURE_COUNT = 0;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.NamedElementImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getNamedElement()
	 * @generated
	 */
    int NAMED_ELEMENT = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAMED_ELEMENT__NAME = ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAMED_ELEMENT__QUALIFIED_NAME = ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAMED_ELEMENT_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.PackageableElementImpl <em>Packageable Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.PackageableElementImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPackageableElement()
	 * @generated
	 */
    int PACKAGEABLE_ELEMENT = 3;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGEABLE_ELEMENT__NAME = NAMED_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGEABLE_ELEMENT__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGEABLE_ELEMENT__OWNER = NAMED_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Packageable Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGEABLE_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.PackageImpl <em>Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.PackageImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPackage()
	 * @generated
	 */
    int PACKAGE = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGE__NAME = PACKAGEABLE_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGE__QUALIFIED_NAME = PACKAGEABLE_ELEMENT__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGE__OWNER = PACKAGEABLE_ELEMENT__OWNER;

    /**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGE__ELEMENTS = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PACKAGE_FEATURE_COUNT = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.ClassifierImpl <em>Classifier</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.ClassifierImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getClassifier()
	 * @generated
	 */
    int CLASSIFIER = 5;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__NAME = PACKAGEABLE_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__QUALIFIED_NAME = PACKAGEABLE_ELEMENT__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__OWNER = PACKAGEABLE_ELEMENT__OWNER;

    /**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__METHODS = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__VISIBILITY = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Is Compilation Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__IS_COMPILATION_UNIT = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Nestedclassifier</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__NESTEDCLASSIFIER = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__NAMESPACE = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Is Extern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__IS_EXTERN = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER__PROPERTIES = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 6;

    /**
	 * The number of structural features of the '<em>Classifier</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASSIFIER_FEATURE_COUNT = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 7;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.ClassImpl <em>Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.ClassImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getClass_()
	 * @generated
	 */
    int CLASS = 4;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__NAME = CLASSIFIER__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__QUALIFIED_NAME = CLASSIFIER__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__OWNER = CLASSIFIER__OWNER;

    /**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__METHODS = CLASSIFIER__METHODS;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__VISIBILITY = CLASSIFIER__VISIBILITY;

    /**
	 * The feature id for the '<em><b>Is Compilation Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__IS_COMPILATION_UNIT = CLASSIFIER__IS_COMPILATION_UNIT;

    /**
	 * The feature id for the '<em><b>Nestedclassifier</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__NESTEDCLASSIFIER = CLASSIFIER__NESTEDCLASSIFIER;

    /**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__NAMESPACE = CLASSIFIER__NAMESPACE;

    /**
	 * The feature id for the '<em><b>Is Extern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__IS_EXTERN = CLASSIFIER__IS_EXTERN;

    /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__PROPERTIES = CLASSIFIER__PROPERTIES;

    /**
	 * The feature id for the '<em><b>Is Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS__IS_FINAL = CLASSIFIER_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CLASS_FEATURE_COUNT = CLASSIFIER_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.TypeImpl <em>Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.TypeImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getType()
	 * @generated
	 */
    int TYPE = 6;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPE__NAME = PACKAGEABLE_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPE__QUALIFIED_NAME = PACKAGEABLE_ELEMENT__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPE__OWNER = PACKAGEABLE_ELEMENT__OWNER;

    /**
	 * The number of structural features of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TYPE_FEATURE_COUNT = PACKAGEABLE_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.MethodImpl <em>Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.MethodImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getMethod()
	 * @generated
	 */
    int METHOD = 7;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__NAME = NAMED_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Is Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__IS_FINAL = NAMED_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Is Static</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__IS_STATIC = NAMED_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Param</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__PARAM = NAMED_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Paramtype</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__PARAMTYPE = NAMED_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Returntype</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__RETURNTYPE = NAMED_ELEMENT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD__VISIBILITY = NAMED_ELEMENT_FEATURE_COUNT + 5;

    /**
	 * The number of structural features of the '<em>Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int METHOD_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 6;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.PropertyImpl <em>Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.PropertyImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getProperty()
	 * @generated
	 */
    int PROPERTY = 8;

    /**
	 * The feature id for the '<em><b>Is Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY__IS_FINAL = 0;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY__VISIBILITY = 1;

    /**
	 * The feature id for the '<em><b>Is Static</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY__IS_STATIC = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY__NAME = 3;

    /**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY__TYPE = 4;

    /**
	 * The number of structural features of the '<em>Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PROPERTY_FEATURE_COUNT = 5;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.InterfaceImpl <em>Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.InterfaceImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getInterface()
	 * @generated
	 */
    int INTERFACE = 9;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__NAME = CLASSIFIER__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__QUALIFIED_NAME = CLASSIFIER__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__OWNER = CLASSIFIER__OWNER;

    /**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__METHODS = CLASSIFIER__METHODS;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__VISIBILITY = CLASSIFIER__VISIBILITY;

    /**
	 * The feature id for the '<em><b>Is Compilation Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__IS_COMPILATION_UNIT = CLASSIFIER__IS_COMPILATION_UNIT;

    /**
	 * The feature id for the '<em><b>Nestedclassifier</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__NESTEDCLASSIFIER = CLASSIFIER__NESTEDCLASSIFIER;

    /**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__NAMESPACE = CLASSIFIER__NAMESPACE;

    /**
	 * The feature id for the '<em><b>Is Extern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__IS_EXTERN = CLASSIFIER__IS_EXTERN;

    /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE__PROPERTIES = CLASSIFIER__PROPERTIES;

    /**
	 * The number of structural features of the '<em>Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTERFACE_FEATURE_COUNT = CLASSIFIER_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.EnumerationImpl <em>Enumeration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.EnumerationImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getEnumeration()
	 * @generated
	 */
    int ENUMERATION = 10;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__NAME = CLASSIFIER__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__QUALIFIED_NAME = CLASSIFIER__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__OWNER = CLASSIFIER__OWNER;

    /**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__METHODS = CLASSIFIER__METHODS;

    /**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__VISIBILITY = CLASSIFIER__VISIBILITY;

    /**
	 * The feature id for the '<em><b>Is Compilation Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__IS_COMPILATION_UNIT = CLASSIFIER__IS_COMPILATION_UNIT;

    /**
	 * The feature id for the '<em><b>Nestedclassifier</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__NESTEDCLASSIFIER = CLASSIFIER__NESTEDCLASSIFIER;

    /**
	 * The feature id for the '<em><b>Namespace</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__NAMESPACE = CLASSIFIER__NAMESPACE;

    /**
	 * The feature id for the '<em><b>Is Extern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__IS_EXTERN = CLASSIFIER__IS_EXTERN;

    /**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION__PROPERTIES = CLASSIFIER__PROPERTIES;

    /**
	 * The number of structural features of the '<em>Enumeration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int ENUMERATION_FEATURE_COUNT = CLASSIFIER_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link UMLModelMM.impl.PrimitiveTypeImpl <em>Primitive Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.impl.PrimitiveTypeImpl
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPrimitiveType()
	 * @generated
	 */
    int PRIMITIVE_TYPE = 11;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PRIMITIVE_TYPE__NAME = TYPE__NAME;

    /**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PRIMITIVE_TYPE__QUALIFIED_NAME = TYPE__QUALIFIED_NAME;

    /**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PRIMITIVE_TYPE__OWNER = TYPE__OWNER;

    /**
	 * The number of structural features of the '<em>Primitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PRIMITIVE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link UMLModelMM.VisibilityKind <em>Visibility Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see UMLModelMM.VisibilityKind
	 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getVisibilityKind()
	 * @generated
	 */
    int VISIBILITY_KIND = 12;

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Element <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see UMLModelMM.Element
	 * @generated
	 */
    EClass getElement();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see UMLModelMM.NamedElement
	 * @generated
	 */
    EClass getNamedElement();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see UMLModelMM.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
    EAttribute getNamedElement_Name();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.NamedElement#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see UMLModelMM.NamedElement#getQualifiedName()
	 * @see #getNamedElement()
	 * @generated
	 */
    EAttribute getNamedElement_QualifiedName();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Package <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Package</em>'.
	 * @see UMLModelMM.Package
	 * @generated
	 */
    EClass getPackage();

    /**
	 * Returns the meta object for the containment reference list '{@link UMLModelMM.Package#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see UMLModelMM.Package#getElements()
	 * @see #getPackage()
	 * @generated
	 */
    EReference getPackage_Elements();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.PackageableElement <em>Packageable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Packageable Element</em>'.
	 * @see UMLModelMM.PackageableElement
	 * @generated
	 */
    EClass getPackageableElement();

    /**
	 * Returns the meta object for the container reference '{@link UMLModelMM.PackageableElement#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see UMLModelMM.PackageableElement#getOwner()
	 * @see #getPackageableElement()
	 * @generated
	 */
    EReference getPackageableElement_Owner();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Class <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class</em>'.
	 * @see UMLModelMM.Class
	 * @generated
	 */
    EClass getClass_();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Class#isFinal <em>Is Final</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Final</em>'.
	 * @see UMLModelMM.Class#isFinal()
	 * @see #getClass_()
	 * @generated
	 */
    EAttribute getClass_IsFinal();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Classifier <em>Classifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Classifier</em>'.
	 * @see UMLModelMM.Classifier
	 * @generated
	 */
    EClass getClassifier();

    /**
	 * Returns the meta object for the containment reference list '{@link UMLModelMM.Classifier#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see UMLModelMM.Classifier#getMethods()
	 * @see #getClassifier()
	 * @generated
	 */
    EReference getClassifier_Methods();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Classifier#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see UMLModelMM.Classifier#getVisibility()
	 * @see #getClassifier()
	 * @generated
	 */
    EAttribute getClassifier_Visibility();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Classifier#isCompilationUnit <em>Is Compilation Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Compilation Unit</em>'.
	 * @see UMLModelMM.Classifier#isCompilationUnit()
	 * @see #getClassifier()
	 * @generated
	 */
    EAttribute getClassifier_IsCompilationUnit();

    /**
	 * Returns the meta object for the containment reference list '{@link UMLModelMM.Classifier#getNestedclassifier <em>Nestedclassifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nestedclassifier</em>'.
	 * @see UMLModelMM.Classifier#getNestedclassifier()
	 * @see #getClassifier()
	 * @generated
	 */
    EReference getClassifier_Nestedclassifier();

    /**
	 * Returns the meta object for the container reference '{@link UMLModelMM.Classifier#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Namespace</em>'.
	 * @see UMLModelMM.Classifier#getNamespace()
	 * @see #getClassifier()
	 * @generated
	 */
    EReference getClassifier_Namespace();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Classifier#isExtern <em>Is Extern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Extern</em>'.
	 * @see UMLModelMM.Classifier#isExtern()
	 * @see #getClassifier()
	 * @generated
	 */
    EAttribute getClassifier_IsExtern();

    /**
	 * Returns the meta object for the containment reference list '{@link UMLModelMM.Classifier#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see UMLModelMM.Classifier#getProperties()
	 * @see #getClassifier()
	 * @generated
	 */
    EReference getClassifier_Properties();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Type <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type</em>'.
	 * @see UMLModelMM.Type
	 * @generated
	 */
    EClass getType();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Method <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Method</em>'.
	 * @see UMLModelMM.Method
	 * @generated
	 */
    EClass getMethod();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Method#isFinal <em>Is Final</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Final</em>'.
	 * @see UMLModelMM.Method#isFinal()
	 * @see #getMethod()
	 * @generated
	 */
    EAttribute getMethod_IsFinal();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Method#isStatic <em>Is Static</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Static</em>'.
	 * @see UMLModelMM.Method#isStatic()
	 * @see #getMethod()
	 * @generated
	 */
    EAttribute getMethod_IsStatic();

    /**
	 * Returns the meta object for the attribute list '{@link UMLModelMM.Method#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Param</em>'.
	 * @see UMLModelMM.Method#getParam()
	 * @see #getMethod()
	 * @generated
	 */
    EAttribute getMethod_Param();

    /**
	 * Returns the meta object for the reference list '{@link UMLModelMM.Method#getParamtype <em>Paramtype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Paramtype</em>'.
	 * @see UMLModelMM.Method#getParamtype()
	 * @see #getMethod()
	 * @generated
	 */
    EReference getMethod_Paramtype();

    /**
	 * Returns the meta object for the reference '{@link UMLModelMM.Method#getReturntype <em>Returntype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Returntype</em>'.
	 * @see UMLModelMM.Method#getReturntype()
	 * @see #getMethod()
	 * @generated
	 */
    EReference getMethod_Returntype();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Method#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see UMLModelMM.Method#getVisibility()
	 * @see #getMethod()
	 * @generated
	 */
    EAttribute getMethod_Visibility();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Property <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property</em>'.
	 * @see UMLModelMM.Property
	 * @generated
	 */
    EClass getProperty();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Property#isFinal <em>Is Final</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Final</em>'.
	 * @see UMLModelMM.Property#isFinal()
	 * @see #getProperty()
	 * @generated
	 */
    EAttribute getProperty_IsFinal();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Property#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see UMLModelMM.Property#getVisibility()
	 * @see #getProperty()
	 * @generated
	 */
    EAttribute getProperty_Visibility();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Property#isStatic <em>Is Static</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Static</em>'.
	 * @see UMLModelMM.Property#isStatic()
	 * @see #getProperty()
	 * @generated
	 */
    EAttribute getProperty_IsStatic();

    /**
	 * Returns the meta object for the attribute '{@link UMLModelMM.Property#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see UMLModelMM.Property#getName()
	 * @see #getProperty()
	 * @generated
	 */
    EAttribute getProperty_Name();

    /**
	 * Returns the meta object for the reference '{@link UMLModelMM.Property#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see UMLModelMM.Property#getType()
	 * @see #getProperty()
	 * @generated
	 */
    EReference getProperty_Type();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Interface <em>Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Interface</em>'.
	 * @see UMLModelMM.Interface
	 * @generated
	 */
    EClass getInterface();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.Enumeration <em>Enumeration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enumeration</em>'.
	 * @see UMLModelMM.Enumeration
	 * @generated
	 */
    EClass getEnumeration();

    /**
	 * Returns the meta object for class '{@link UMLModelMM.PrimitiveType <em>Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Type</em>'.
	 * @see UMLModelMM.PrimitiveType
	 * @generated
	 */
    EClass getPrimitiveType();

    /**
	 * Returns the meta object for enum '{@link UMLModelMM.VisibilityKind <em>Visibility Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Visibility Kind</em>'.
	 * @see UMLModelMM.VisibilityKind
	 * @generated
	 */
    EEnum getVisibilityKind();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    UMLModelMMFactory getUMLModelMMFactory();

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
		 * The meta object literal for the '{@link UMLModelMM.impl.ElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.ElementImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getElement()
		 * @generated
		 */
        EClass ELEMENT = eINSTANCE.getElement();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.NamedElementImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getNamedElement()
		 * @generated
		 */
        EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

        /**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute NAMED_ELEMENT__QUALIFIED_NAME = eINSTANCE.getNamedElement_QualifiedName();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.PackageImpl <em>Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.PackageImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPackage()
		 * @generated
		 */
        EClass PACKAGE = eINSTANCE.getPackage();

        /**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PACKAGE__ELEMENTS = eINSTANCE.getPackage_Elements();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.PackageableElementImpl <em>Packageable Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.PackageableElementImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPackageableElement()
		 * @generated
		 */
        EClass PACKAGEABLE_ELEMENT = eINSTANCE.getPackageableElement();

        /**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PACKAGEABLE_ELEMENT__OWNER = eINSTANCE.getPackageableElement_Owner();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.ClassImpl <em>Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.ClassImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getClass_()
		 * @generated
		 */
        EClass CLASS = eINSTANCE.getClass_();

        /**
		 * The meta object literal for the '<em><b>Is Final</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLASS__IS_FINAL = eINSTANCE.getClass_IsFinal();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.ClassifierImpl <em>Classifier</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.ClassifierImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getClassifier()
		 * @generated
		 */
        EClass CLASSIFIER = eINSTANCE.getClassifier();

        /**
		 * The meta object literal for the '<em><b>Methods</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASSIFIER__METHODS = eINSTANCE.getClassifier_Methods();

        /**
		 * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLASSIFIER__VISIBILITY = eINSTANCE.getClassifier_Visibility();

        /**
		 * The meta object literal for the '<em><b>Is Compilation Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLASSIFIER__IS_COMPILATION_UNIT = eINSTANCE.getClassifier_IsCompilationUnit();

        /**
		 * The meta object literal for the '<em><b>Nestedclassifier</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASSIFIER__NESTEDCLASSIFIER = eINSTANCE.getClassifier_Nestedclassifier();

        /**
		 * The meta object literal for the '<em><b>Namespace</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASSIFIER__NAMESPACE = eINSTANCE.getClassifier_Namespace();

        /**
		 * The meta object literal for the '<em><b>Is Extern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CLASSIFIER__IS_EXTERN = eINSTANCE.getClassifier_IsExtern();

        /**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CLASSIFIER__PROPERTIES = eINSTANCE.getClassifier_Properties();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.TypeImpl <em>Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.TypeImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getType()
		 * @generated
		 */
        EClass TYPE = eINSTANCE.getType();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.MethodImpl <em>Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.MethodImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getMethod()
		 * @generated
		 */
        EClass METHOD = eINSTANCE.getMethod();

        /**
		 * The meta object literal for the '<em><b>Is Final</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute METHOD__IS_FINAL = eINSTANCE.getMethod_IsFinal();

        /**
		 * The meta object literal for the '<em><b>Is Static</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute METHOD__IS_STATIC = eINSTANCE.getMethod_IsStatic();

        /**
		 * The meta object literal for the '<em><b>Param</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute METHOD__PARAM = eINSTANCE.getMethod_Param();

        /**
		 * The meta object literal for the '<em><b>Paramtype</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference METHOD__PARAMTYPE = eINSTANCE.getMethod_Paramtype();

        /**
		 * The meta object literal for the '<em><b>Returntype</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference METHOD__RETURNTYPE = eINSTANCE.getMethod_Returntype();

        /**
		 * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute METHOD__VISIBILITY = eINSTANCE.getMethod_Visibility();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.PropertyImpl <em>Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.PropertyImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getProperty()
		 * @generated
		 */
        EClass PROPERTY = eINSTANCE.getProperty();

        /**
		 * The meta object literal for the '<em><b>Is Final</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PROPERTY__IS_FINAL = eINSTANCE.getProperty_IsFinal();

        /**
		 * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PROPERTY__VISIBILITY = eINSTANCE.getProperty_Visibility();

        /**
		 * The meta object literal for the '<em><b>Is Static</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PROPERTY__IS_STATIC = eINSTANCE.getProperty_IsStatic();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PROPERTY__NAME = eINSTANCE.getProperty_Name();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PROPERTY__TYPE = eINSTANCE.getProperty_Type();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.InterfaceImpl <em>Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.InterfaceImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getInterface()
		 * @generated
		 */
        EClass INTERFACE = eINSTANCE.getInterface();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.EnumerationImpl <em>Enumeration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.EnumerationImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getEnumeration()
		 * @generated
		 */
        EClass ENUMERATION = eINSTANCE.getEnumeration();

        /**
		 * The meta object literal for the '{@link UMLModelMM.impl.PrimitiveTypeImpl <em>Primitive Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.impl.PrimitiveTypeImpl
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getPrimitiveType()
		 * @generated
		 */
        EClass PRIMITIVE_TYPE = eINSTANCE.getPrimitiveType();

        /**
		 * The meta object literal for the '{@link UMLModelMM.VisibilityKind <em>Visibility Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see UMLModelMM.VisibilityKind
		 * @see UMLModelMM.impl.UMLModelMMPackageImpl#getVisibilityKind()
		 * @generated
		 */
        EEnum VISIBILITY_KIND = eINSTANCE.getVisibilityKind();
    }
}
