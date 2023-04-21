package de.scheidgen.affirmator.affirmations;

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
 * @see de.scheidgen.affirmator.affirmations.AffirmationsFactory
 * @model kind="package"
 * @generated
 */
public interface AffirmationsPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "affirmations";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://de.scheidgen.affirmator/ns";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "affirmations";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    AffirmationsPackage eINSTANCE = de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl.init();

    /**
	 * The meta object id for the '{@link de.scheidgen.affirmator.affirmations.impl.CategoryImpl <em>Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.scheidgen.affirmator.affirmations.impl.CategoryImpl
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getCategory()
	 * @generated
	 */
    int CATEGORY = 0;

    /**
	 * The feature id for the '<em><b>Affirmations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CATEGORY__AFFIRMATIONS = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CATEGORY__NAME = 1;

    /**
	 * The number of structural features of the '<em>Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CATEGORY_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link de.scheidgen.affirmator.affirmations.impl.AffirmationImpl <em>Affirmation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationImpl
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getAffirmation()
	 * @generated
	 */
    int AFFIRMATION = 1;

    /**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AFFIRMATION__TEXT = 0;

    /**
	 * The number of structural features of the '<em>Affirmation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AFFIRMATION_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link de.scheidgen.affirmator.affirmations.impl.AffirmationsImpl <em>Affirmations</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsImpl
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getAffirmations()
	 * @generated
	 */
    int AFFIRMATIONS = 2;

    /**
	 * The feature id for the '<em><b>Categories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AFFIRMATIONS__CATEGORIES = 0;

    /**
	 * The number of structural features of the '<em>Affirmations</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int AFFIRMATIONS_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link de.scheidgen.affirmator.affirmations.impl.ScheduleImpl <em>Schedule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.scheidgen.affirmator.affirmations.impl.ScheduleImpl
	 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getSchedule()
	 * @generated
	 */
    int SCHEDULE = 3;

    /**
	 * The feature id for the '<em><b>Every</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SCHEDULE__EVERY = 0;

    /**
	 * The feature id for the '<em><b>Number Of Categories</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SCHEDULE__NUMBER_OF_CATEGORIES = 1;

    /**
	 * The feature id for the '<em><b>Categories</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SCHEDULE__CATEGORIES = 2;

    /**
	 * The number of structural features of the '<em>Schedule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int SCHEDULE_FEATURE_COUNT = 3;

    /**
	 * Returns the meta object for class '{@link de.scheidgen.affirmator.affirmations.Category <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Category</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Category
	 * @generated
	 */
    EClass getCategory();

    /**
	 * Returns the meta object for the containment reference list '{@link de.scheidgen.affirmator.affirmations.Category#getAffirmations <em>Affirmations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Affirmations</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Category#getAffirmations()
	 * @see #getCategory()
	 * @generated
	 */
    EReference getCategory_Affirmations();

    /**
	 * Returns the meta object for the attribute '{@link de.scheidgen.affirmator.affirmations.Category#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Category#getName()
	 * @see #getCategory()
	 * @generated
	 */
    EAttribute getCategory_Name();

    /**
	 * Returns the meta object for class '{@link de.scheidgen.affirmator.affirmations.Affirmation <em>Affirmation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Affirmation</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Affirmation
	 * @generated
	 */
    EClass getAffirmation();

    /**
	 * Returns the meta object for the attribute '{@link de.scheidgen.affirmator.affirmations.Affirmation#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Affirmation#getText()
	 * @see #getAffirmation()
	 * @generated
	 */
    EAttribute getAffirmation_Text();

    /**
	 * Returns the meta object for class '{@link de.scheidgen.affirmator.affirmations.Affirmations <em>Affirmations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Affirmations</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Affirmations
	 * @generated
	 */
    EClass getAffirmations();

    /**
	 * Returns the meta object for the containment reference list '{@link de.scheidgen.affirmator.affirmations.Affirmations#getCategories <em>Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Categories</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Affirmations#getCategories()
	 * @see #getAffirmations()
	 * @generated
	 */
    EReference getAffirmations_Categories();

    /**
	 * Returns the meta object for class '{@link de.scheidgen.affirmator.affirmations.Schedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedule</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Schedule
	 * @generated
	 */
    EClass getSchedule();

    /**
	 * Returns the meta object for the attribute '{@link de.scheidgen.affirmator.affirmations.Schedule#getEvery <em>Every</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Every</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Schedule#getEvery()
	 * @see #getSchedule()
	 * @generated
	 */
    EAttribute getSchedule_Every();

    /**
	 * Returns the meta object for the attribute '{@link de.scheidgen.affirmator.affirmations.Schedule#getNumberOfCategories <em>Number Of Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Categories</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Schedule#getNumberOfCategories()
	 * @see #getSchedule()
	 * @generated
	 */
    EAttribute getSchedule_NumberOfCategories();

    /**
	 * Returns the meta object for the reference list '{@link de.scheidgen.affirmator.affirmations.Schedule#getCategories <em>Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Categories</em>'.
	 * @see de.scheidgen.affirmator.affirmations.Schedule#getCategories()
	 * @see #getSchedule()
	 * @generated
	 */
    EReference getSchedule_Categories();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    AffirmationsFactory getAffirmationsFactory();

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
		 * The meta object literal for the '{@link de.scheidgen.affirmator.affirmations.impl.CategoryImpl <em>Category</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.scheidgen.affirmator.affirmations.impl.CategoryImpl
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getCategory()
		 * @generated
		 */
        EClass CATEGORY = eINSTANCE.getCategory();

        /**
		 * The meta object literal for the '<em><b>Affirmations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference CATEGORY__AFFIRMATIONS = eINSTANCE.getCategory_Affirmations();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CATEGORY__NAME = eINSTANCE.getCategory_Name();

        /**
		 * The meta object literal for the '{@link de.scheidgen.affirmator.affirmations.impl.AffirmationImpl <em>Affirmation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationImpl
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getAffirmation()
		 * @generated
		 */
        EClass AFFIRMATION = eINSTANCE.getAffirmation();

        /**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute AFFIRMATION__TEXT = eINSTANCE.getAffirmation_Text();

        /**
		 * The meta object literal for the '{@link de.scheidgen.affirmator.affirmations.impl.AffirmationsImpl <em>Affirmations</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsImpl
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getAffirmations()
		 * @generated
		 */
        EClass AFFIRMATIONS = eINSTANCE.getAffirmations();

        /**
		 * The meta object literal for the '<em><b>Categories</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference AFFIRMATIONS__CATEGORIES = eINSTANCE.getAffirmations_Categories();

        /**
		 * The meta object literal for the '{@link de.scheidgen.affirmator.affirmations.impl.ScheduleImpl <em>Schedule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.scheidgen.affirmator.affirmations.impl.ScheduleImpl
		 * @see de.scheidgen.affirmator.affirmations.impl.AffirmationsPackageImpl#getSchedule()
		 * @generated
		 */
        EClass SCHEDULE = eINSTANCE.getSchedule();

        /**
		 * The meta object literal for the '<em><b>Every</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SCHEDULE__EVERY = eINSTANCE.getSchedule_Every();

        /**
		 * The meta object literal for the '<em><b>Number Of Categories</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute SCHEDULE__NUMBER_OF_CATEGORIES = eINSTANCE.getSchedule_NumberOfCategories();

        /**
		 * The meta object literal for the '<em><b>Categories</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference SCHEDULE__CATEGORIES = eINSTANCE.getSchedule_Categories();
    }
}
