package org.openarchitectureware.example.dsl.entityModel;

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
 * @see org.openarchitectureware.example.dsl.entityModel.EntityModelFactory
 * @model kind="package"
 * @generated
 */
public interface EntityModelPackage extends EPackage {

    /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNAME = "entityModel";

    /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNS_URI = "http://www.openarchitectureware.org/example/dsl/EntityModel";

    /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNS_PREFIX = "entityModel";

    /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    EntityModelPackage eINSTANCE = org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl.init();

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.ModelImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getModel()
   * @generated
   */
    int MODEL = 0;

    /**
   * The feature id for the '<em><b>Imports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int MODEL__IMPORTS = 0;

    /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int MODEL__ELEMENTS = 1;

    /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int MODEL_FEATURE_COUNT = 2;

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.ImportImpl <em>Import</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.ImportImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getImport()
   * @generated
   */
    int IMPORT = 1;

    /**
   * The feature id for the '<em><b>Import URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int IMPORT__IMPORT_URI = 0;

    /**
   * The number of structural features of the '<em>Import</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int IMPORT_FEATURE_COUNT = 1;

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.TypeImpl <em>Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.TypeImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getType()
   * @generated
   */
    int TYPE = 2;

    /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int TYPE__NAME = 0;

    /**
   * The number of structural features of the '<em>Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int TYPE_FEATURE_COUNT = 1;

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.SimpleTypeImpl <em>Simple Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.SimpleTypeImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getSimpleType()
   * @generated
   */
    int SIMPLE_TYPE = 3;

    /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int SIMPLE_TYPE__NAME = TYPE__NAME;

    /**
   * The number of structural features of the '<em>Simple Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int SIMPLE_TYPE_FEATURE_COUNT = TYPE_FEATURE_COUNT + 0;

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.EntityImpl <em>Entity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getEntity()
   * @generated
   */
    int ENTITY = 4;

    /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ENTITY__NAME = TYPE__NAME;

    /**
   * The feature id for the '<em><b>Extends</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ENTITY__EXTENDS = TYPE_FEATURE_COUNT + 0;

    /**
   * The feature id for the '<em><b>Properties</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ENTITY__PROPERTIES = TYPE_FEATURE_COUNT + 1;

    /**
   * The number of structural features of the '<em>Entity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ENTITY_FEATURE_COUNT = TYPE_FEATURE_COUNT + 2;

    /**
   * The meta object id for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.PropertyImpl <em>Property</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openarchitectureware.example.dsl.entityModel.impl.PropertyImpl
   * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getProperty()
   * @generated
   */
    int PROPERTY = 5;

    /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int PROPERTY__NAME = 0;

    /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int PROPERTY__TYPE = 1;

    /**
   * The feature id for the '<em><b>Many</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int PROPERTY__MANY = 2;

    /**
   * The number of structural features of the '<em>Property</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int PROPERTY_FEATURE_COUNT = 3;

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Model
   * @generated
   */
    EClass getModel();

    /**
   * Returns the meta object for the containment reference list '{@link org.openarchitectureware.example.dsl.entityModel.Model#getImports <em>Imports</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Imports</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Model#getImports()
   * @see #getModel()
   * @generated
   */
    EReference getModel_Imports();

    /**
   * Returns the meta object for the containment reference list '{@link org.openarchitectureware.example.dsl.entityModel.Model#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Model#getElements()
   * @see #getModel()
   * @generated
   */
    EReference getModel_Elements();

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Import
   * @generated
   */
    EClass getImport();

    /**
   * Returns the meta object for the attribute '{@link org.openarchitectureware.example.dsl.entityModel.Import#getImportURI <em>Import URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Import URI</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Import#getImportURI()
   * @see #getImport()
   * @generated
   */
    EAttribute getImport_ImportURI();

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.Type <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Type
   * @generated
   */
    EClass getType();

    /**
   * Returns the meta object for the attribute '{@link org.openarchitectureware.example.dsl.entityModel.Type#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Type#getName()
   * @see #getType()
   * @generated
   */
    EAttribute getType_Name();

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.SimpleType <em>Simple Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Simple Type</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.SimpleType
   * @generated
   */
    EClass getSimpleType();

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.Entity <em>Entity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Entity</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Entity
   * @generated
   */
    EClass getEntity();

    /**
   * Returns the meta object for the reference '{@link org.openarchitectureware.example.dsl.entityModel.Entity#getExtends <em>Extends</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Extends</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Entity#getExtends()
   * @see #getEntity()
   * @generated
   */
    EReference getEntity_Extends();

    /**
   * Returns the meta object for the containment reference list '{@link org.openarchitectureware.example.dsl.entityModel.Entity#getProperties <em>Properties</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Properties</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Entity#getProperties()
   * @see #getEntity()
   * @generated
   */
    EReference getEntity_Properties();

    /**
   * Returns the meta object for class '{@link org.openarchitectureware.example.dsl.entityModel.Property <em>Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Property
   * @generated
   */
    EClass getProperty();

    /**
   * Returns the meta object for the attribute '{@link org.openarchitectureware.example.dsl.entityModel.Property#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Property#getName()
   * @see #getProperty()
   * @generated
   */
    EAttribute getProperty_Name();

    /**
   * Returns the meta object for the reference '{@link org.openarchitectureware.example.dsl.entityModel.Property#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Property#getType()
   * @see #getProperty()
   * @generated
   */
    EReference getProperty_Type();

    /**
   * Returns the meta object for the attribute '{@link org.openarchitectureware.example.dsl.entityModel.Property#isMany <em>Many</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Many</em>'.
   * @see org.openarchitectureware.example.dsl.entityModel.Property#isMany()
   * @see #getProperty()
   * @generated
   */
    EAttribute getProperty_Many();

    /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
    EntityModelFactory getEntityModelFactory();

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
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.ModelImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getModel()
     * @generated
     */
        EClass MODEL = eINSTANCE.getModel();

        /**
     * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EReference MODEL__IMPORTS = eINSTANCE.getModel_Imports();

        /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EReference MODEL__ELEMENTS = eINSTANCE.getModel_Elements();

        /**
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.ImportImpl <em>Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.ImportImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getImport()
     * @generated
     */
        EClass IMPORT = eINSTANCE.getImport();

        /**
     * The meta object literal for the '<em><b>Import URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EAttribute IMPORT__IMPORT_URI = eINSTANCE.getImport_ImportURI();

        /**
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.TypeImpl <em>Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.TypeImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getType()
     * @generated
     */
        EClass TYPE = eINSTANCE.getType();

        /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EAttribute TYPE__NAME = eINSTANCE.getType_Name();

        /**
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.SimpleTypeImpl <em>Simple Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.SimpleTypeImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getSimpleType()
     * @generated
     */
        EClass SIMPLE_TYPE = eINSTANCE.getSimpleType();

        /**
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.EntityImpl <em>Entity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getEntity()
     * @generated
     */
        EClass ENTITY = eINSTANCE.getEntity();

        /**
     * The meta object literal for the '<em><b>Extends</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EReference ENTITY__EXTENDS = eINSTANCE.getEntity_Extends();

        /**
     * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EReference ENTITY__PROPERTIES = eINSTANCE.getEntity_Properties();

        /**
     * The meta object literal for the '{@link org.openarchitectureware.example.dsl.entityModel.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openarchitectureware.example.dsl.entityModel.impl.PropertyImpl
     * @see org.openarchitectureware.example.dsl.entityModel.impl.EntityModelPackageImpl#getProperty()
     * @generated
     */
        EClass PROPERTY = eINSTANCE.getProperty();

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
     * The meta object literal for the '<em><b>Many</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
        EAttribute PROPERTY__MANY = eINSTANCE.getProperty_Many();
    }
}
