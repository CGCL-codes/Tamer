package de.abg.jreichert.serviceqos.predicate;

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
 * @see de.abg.jreichert.serviceqos.predicate.PredicateFactory
 * @model kind="package"
 * @generated
 */
public interface PredicatePackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "predicate";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://www.abg.de/jreichert/serviceqos/Predicate";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "predicate";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    PredicatePackage eINSTANCE = de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl.init();

    /**
	 * The meta object id for the '{@link de.abg.jreichert.serviceqos.predicate.impl.PredicatesImpl <em>Predicates</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatesImpl
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getPredicates()
	 * @generated
	 */
    int PREDICATES = 0;

    /**
	 * The feature id for the '<em><b>Imports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATES__IMPORTS = 0;

    /**
	 * The feature id for the '<em><b>Predicate</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATES__PREDICATE = 1;

    /**
	 * The number of structural features of the '<em>Predicates</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATES_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link de.abg.jreichert.serviceqos.predicate.impl.ImportImpl <em>Import</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.abg.jreichert.serviceqos.predicate.impl.ImportImpl
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getImport()
	 * @generated
	 */
    int IMPORT = 1;

    /**
	 * The feature id for the '<em><b>Imported Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int IMPORT__IMPORTED_NAMESPACE = 0;

    /**
	 * The number of structural features of the '<em>Import</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int IMPORT_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link de.abg.jreichert.serviceqos.predicate.impl.PredicateImpl <em>Predicate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicateImpl
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getPredicate()
	 * @generated
	 */
    int PREDICATE = 2;

    /**
	 * The feature id for the '<em><b>Left</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATE__LEFT = 0;

    /**
	 * The feature id for the '<em><b>Alias</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATE__ALIAS = 1;

    /**
	 * The number of structural features of the '<em>Predicate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PREDICATE_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link de.abg.jreichert.serviceqos.predicate.impl.TerminalPredicateImpl <em>Terminal Predicate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.abg.jreichert.serviceqos.predicate.impl.TerminalPredicateImpl
	 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getTerminalPredicate()
	 * @generated
	 */
    int TERMINAL_PREDICATE = 3;

    /**
	 * The feature id for the '<em><b>Left</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TERMINAL_PREDICATE__LEFT = PREDICATE__LEFT;

    /**
	 * The feature id for the '<em><b>Alias</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TERMINAL_PREDICATE__ALIAS = PREDICATE__ALIAS;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TERMINAL_PREDICATE__NAME = PREDICATE_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Terminal Predicate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TERMINAL_PREDICATE_FEATURE_COUNT = PREDICATE_FEATURE_COUNT + 1;

    /**
	 * Returns the meta object for class '{@link de.abg.jreichert.serviceqos.predicate.Predicates <em>Predicates</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Predicates</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicates
	 * @generated
	 */
    EClass getPredicates();

    /**
	 * Returns the meta object for the containment reference list '{@link de.abg.jreichert.serviceqos.predicate.Predicates#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Imports</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicates#getImports()
	 * @see #getPredicates()
	 * @generated
	 */
    EReference getPredicates_Imports();

    /**
	 * Returns the meta object for the containment reference list '{@link de.abg.jreichert.serviceqos.predicate.Predicates#getPredicate <em>Predicate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Predicate</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicates#getPredicate()
	 * @see #getPredicates()
	 * @generated
	 */
    EReference getPredicates_Predicate();

    /**
	 * Returns the meta object for class '{@link de.abg.jreichert.serviceqos.predicate.Import <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Import</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Import
	 * @generated
	 */
    EClass getImport();

    /**
	 * Returns the meta object for the attribute '{@link de.abg.jreichert.serviceqos.predicate.Import#getImportedNamespace <em>Imported Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Imported Namespace</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Import#getImportedNamespace()
	 * @see #getImport()
	 * @generated
	 */
    EAttribute getImport_ImportedNamespace();

    /**
	 * Returns the meta object for class '{@link de.abg.jreichert.serviceqos.predicate.Predicate <em>Predicate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Predicate</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicate
	 * @generated
	 */
    EClass getPredicate();

    /**
	 * Returns the meta object for the containment reference '{@link de.abg.jreichert.serviceqos.predicate.Predicate#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Left</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicate#getLeft()
	 * @see #getPredicate()
	 * @generated
	 */
    EReference getPredicate_Left();

    /**
	 * Returns the meta object for the containment reference list '{@link de.abg.jreichert.serviceqos.predicate.Predicate#getAlias <em>Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Alias</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.Predicate#getAlias()
	 * @see #getPredicate()
	 * @generated
	 */
    EReference getPredicate_Alias();

    /**
	 * Returns the meta object for class '{@link de.abg.jreichert.serviceqos.predicate.TerminalPredicate <em>Terminal Predicate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminal Predicate</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.TerminalPredicate
	 * @generated
	 */
    EClass getTerminalPredicate();

    /**
	 * Returns the meta object for the attribute '{@link de.abg.jreichert.serviceqos.predicate.TerminalPredicate#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.abg.jreichert.serviceqos.predicate.TerminalPredicate#getName()
	 * @see #getTerminalPredicate()
	 * @generated
	 */
    EAttribute getTerminalPredicate_Name();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    PredicateFactory getPredicateFactory();

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
		 * The meta object literal for the '{@link de.abg.jreichert.serviceqos.predicate.impl.PredicatesImpl <em>Predicates</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatesImpl
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getPredicates()
		 * @generated
		 */
        EClass PREDICATES = eINSTANCE.getPredicates();

        /**
		 * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PREDICATES__IMPORTS = eINSTANCE.getPredicates_Imports();

        /**
		 * The meta object literal for the '<em><b>Predicate</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PREDICATES__PREDICATE = eINSTANCE.getPredicates_Predicate();

        /**
		 * The meta object literal for the '{@link de.abg.jreichert.serviceqos.predicate.impl.ImportImpl <em>Import</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.abg.jreichert.serviceqos.predicate.impl.ImportImpl
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getImport()
		 * @generated
		 */
        EClass IMPORT = eINSTANCE.getImport();

        /**
		 * The meta object literal for the '<em><b>Imported Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute IMPORT__IMPORTED_NAMESPACE = eINSTANCE.getImport_ImportedNamespace();

        /**
		 * The meta object literal for the '{@link de.abg.jreichert.serviceqos.predicate.impl.PredicateImpl <em>Predicate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicateImpl
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getPredicate()
		 * @generated
		 */
        EClass PREDICATE = eINSTANCE.getPredicate();

        /**
		 * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PREDICATE__LEFT = eINSTANCE.getPredicate_Left();

        /**
		 * The meta object literal for the '<em><b>Alias</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PREDICATE__ALIAS = eINSTANCE.getPredicate_Alias();

        /**
		 * The meta object literal for the '{@link de.abg.jreichert.serviceqos.predicate.impl.TerminalPredicateImpl <em>Terminal Predicate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.abg.jreichert.serviceqos.predicate.impl.TerminalPredicateImpl
		 * @see de.abg.jreichert.serviceqos.predicate.impl.PredicatePackageImpl#getTerminalPredicate()
		 * @generated
		 */
        EClass TERMINAL_PREDICATE = eINSTANCE.getTerminalPredicate();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TERMINAL_PREDICATE__NAME = eINSTANCE.getTerminalPredicate_Name();
    }
}
