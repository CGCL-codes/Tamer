package de.scheidgen.affirmator.affirmations.impl;

import de.scheidgen.affirmator.affirmations.Affirmation;
import de.scheidgen.affirmator.affirmations.Affirmations;
import de.scheidgen.affirmator.affirmations.AffirmationsFactory;
import de.scheidgen.affirmator.affirmations.AffirmationsPackage;
import de.scheidgen.affirmator.affirmations.Category;
import de.scheidgen.affirmator.affirmations.Schedule;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AffirmationsPackageImpl extends EPackageImpl implements AffirmationsPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass categoryEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass affirmationEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass affirmationsEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass scheduleEClass = null;

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
	 * @see de.scheidgen.affirmator.affirmations.AffirmationsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private AffirmationsPackageImpl() {
        super(eNS_URI, AffirmationsFactory.eINSTANCE);
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
    public static AffirmationsPackage init() {
        if (isInited) return (AffirmationsPackage) EPackage.Registry.INSTANCE.getEPackage(AffirmationsPackage.eNS_URI);
        AffirmationsPackageImpl theAffirmationsPackage = (AffirmationsPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof AffirmationsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new AffirmationsPackageImpl());
        isInited = true;
        theAffirmationsPackage.createPackageContents();
        theAffirmationsPackage.initializePackageContents();
        theAffirmationsPackage.freeze();
        return theAffirmationsPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCategory() {
        return categoryEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getCategory_Affirmations() {
        return (EReference) categoryEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCategory_Name() {
        return (EAttribute) categoryEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAffirmation() {
        return affirmationEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getAffirmation_Text() {
        return (EAttribute) affirmationEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getAffirmations() {
        return affirmationsEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getAffirmations_Categories() {
        return (EReference) affirmationsEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSchedule() {
        return scheduleEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSchedule_Every() {
        return (EAttribute) scheduleEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getSchedule_NumberOfCategories() {
        return (EAttribute) scheduleEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSchedule_Categories() {
        return (EReference) scheduleEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AffirmationsFactory getAffirmationsFactory() {
        return (AffirmationsFactory) getEFactoryInstance();
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
        categoryEClass = createEClass(CATEGORY);
        createEReference(categoryEClass, CATEGORY__AFFIRMATIONS);
        createEAttribute(categoryEClass, CATEGORY__NAME);
        affirmationEClass = createEClass(AFFIRMATION);
        createEAttribute(affirmationEClass, AFFIRMATION__TEXT);
        affirmationsEClass = createEClass(AFFIRMATIONS);
        createEReference(affirmationsEClass, AFFIRMATIONS__CATEGORIES);
        scheduleEClass = createEClass(SCHEDULE);
        createEAttribute(scheduleEClass, SCHEDULE__EVERY);
        createEAttribute(scheduleEClass, SCHEDULE__NUMBER_OF_CATEGORIES);
        createEReference(scheduleEClass, SCHEDULE__CATEGORIES);
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
        initEClass(categoryEClass, Category.class, "Category", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCategory_Affirmations(), this.getAffirmation(), null, "affirmations", null, 0, -1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCategory_Name(), ecorePackage.getEString(), "name", null, 0, 1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(affirmationEClass, Affirmation.class, "Affirmation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAffirmation_Text(), ecorePackage.getEString(), "text", null, 0, 1, Affirmation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(affirmationsEClass, Affirmations.class, "Affirmations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAffirmations_Categories(), this.getCategory(), null, "categories", null, 0, -1, Affirmations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(scheduleEClass, Schedule.class, "Schedule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getSchedule_Every(), ecorePackage.getEInt(), "every", null, 0, 1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSchedule_NumberOfCategories(), ecorePackage.getEInt(), "numberOfCategories", null, 0, 1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSchedule_Categories(), this.getCategory(), null, "categories", null, 0, -1, Schedule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
