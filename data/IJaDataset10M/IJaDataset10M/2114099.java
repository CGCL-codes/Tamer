package net.sf.rcpforms.emf.test.impl;

import net.sf.rcpforms.emf.test.AddressModel;
import net.sf.rcpforms.emf.test.ConfigurationModel;
import net.sf.rcpforms.emf.test.Gender;
import net.sf.rcpforms.emf.test.TestFactory;
import net.sf.rcpforms.emf.test.TestModel;
import net.sf.rcpforms.emf.test.TestPackage;
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
public class TestPackageImpl extends EPackageImpl implements TestPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass testModelEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass configurationModelEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass addressModelEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum genderEEnum = null;

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
	 * @see net.sf.rcpforms.emf.test.TestPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private TestPackageImpl() {
        super(eNS_URI, TestFactory.eINSTANCE);
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
    public static TestPackage init() {
        if (isInited) return (TestPackage) EPackage.Registry.INSTANCE.getEPackage(TestPackage.eNS_URI);
        TestPackageImpl theTestPackage = (TestPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof TestPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new TestPackageImpl());
        isInited = true;
        theTestPackage.createPackageContents();
        theTestPackage.initializePackageContents();
        theTestPackage.freeze();
        return theTestPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTestModel() {
        return testModelEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_Name() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_BirthDate() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_Age() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_AccountBalance() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(6);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_IsSelectable() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(7);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTestModel_Address() {
        return (EReference) testModelEClass.getEStructuralFeatures().get(8);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_OverdrawAccount() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_ChildCount() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTestModel_Gender() {
        return (EAttribute) testModelEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getConfigurationModel() {
        return configurationModelEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConfigurationModel_TestModels() {
        return (EReference) configurationModelEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getConfigurationModel_TestModel() {
        return (EReference) configurationModelEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAddressModel() {
        return addressModelEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_ZipCode() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_ValidFrom() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_ValidTo() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_DifferentPostAddress() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_Street() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(4);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAddressModel_HouseNumber() {
        return (EAttribute) addressModelEClass.getEStructuralFeatures().get(5);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getGender() {
        return genderEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestFactory getTestFactory() {
        return (TestFactory) getEFactoryInstance();
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
        testModelEClass = createEClass(TEST_MODEL);
        createEAttribute(testModelEClass, TEST_MODEL__GENDER);
        createEAttribute(testModelEClass, TEST_MODEL__NAME);
        createEAttribute(testModelEClass, TEST_MODEL__BIRTH_DATE);
        createEAttribute(testModelEClass, TEST_MODEL__OVERDRAW_ACCOUNT);
        createEAttribute(testModelEClass, TEST_MODEL__CHILD_COUNT);
        createEAttribute(testModelEClass, TEST_MODEL__AGE);
        createEAttribute(testModelEClass, TEST_MODEL__ACCOUNT_BALANCE);
        createEAttribute(testModelEClass, TEST_MODEL__IS_SELECTABLE);
        createEReference(testModelEClass, TEST_MODEL__ADDRESS);
        configurationModelEClass = createEClass(CONFIGURATION_MODEL);
        createEReference(configurationModelEClass, CONFIGURATION_MODEL__TEST_MODELS);
        createEReference(configurationModelEClass, CONFIGURATION_MODEL__TEST_MODEL);
        addressModelEClass = createEClass(ADDRESS_MODEL);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__ZIP_CODE);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__VALID_FROM);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__VALID_TO);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__DIFFERENT_POST_ADDRESS);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__STREET);
        createEAttribute(addressModelEClass, ADDRESS_MODEL__HOUSE_NUMBER);
        genderEEnum = createEEnum(GENDER);
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
        initEClass(testModelEClass, TestModel.class, "TestModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTestModel_Gender(), this.getGender(), "gender", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_BirthDate(), ecorePackage.getEDate(), "birthDate", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_OverdrawAccount(), ecorePackage.getEBooleanObject(), "overdrawAccount", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_ChildCount(), ecorePackage.getEIntegerObject(), "childCount", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_Age(), ecorePackage.getEInt(), "age", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_AccountBalance(), ecorePackage.getEDoubleObject(), "accountBalance", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getTestModel_IsSelectable(), ecorePackage.getEBooleanObject(), "isSelectable", null, 0, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getTestModel_Address(), this.getAddressModel(), null, "address", null, 1, 1, TestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(configurationModelEClass, ConfigurationModel.class, "ConfigurationModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConfigurationModel_TestModels(), this.getTestModel(), null, "testModels", null, 0, -1, ConfigurationModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConfigurationModel_TestModel(), this.getTestModel(), null, "testModel", null, 1, 1, ConfigurationModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(addressModelEClass, AddressModel.class, "AddressModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAddressModel_ZipCode(), ecorePackage.getEIntegerObject(), "zipCode", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressModel_ValidFrom(), ecorePackage.getEDate(), "validFrom", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressModel_ValidTo(), ecorePackage.getEDate(), "validTo", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressModel_DifferentPostAddress(), ecorePackage.getEBoolean(), "differentPostAddress", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressModel_Street(), ecorePackage.getEString(), "street", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAddressModel_HouseNumber(), ecorePackage.getEIntegerObject(), "houseNumber", null, 0, 1, AddressModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(genderEEnum, Gender.class, "Gender");
        addEEnumLiteral(genderEEnum, Gender.MALE);
        addEEnumLiteral(genderEEnum, Gender.FEMALE);
        addEEnumLiteral(genderEEnum, Gender.UNKNOWN);
        createResource(eNS_URI);
    }
}
