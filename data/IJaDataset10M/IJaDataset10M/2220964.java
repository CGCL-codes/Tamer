package org.fh.auge.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.fh.auge.AugeFactory;
import org.fh.auge.AugePackage;
import org.fh.auge.Repository;
import org.fh.auge.account.AccountPackage;
import org.fh.auge.account.impl.AccountPackageImpl;
import org.fh.auge.core.CorePackage;
import org.fh.auge.core.impl.CorePackageImpl;
import org.fh.auge.stock.StockPackage;
import org.fh.auge.stock.impl.StockPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AugePackageImpl extends EPackageImpl implements AugePackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass repositoryEClass = null;

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
	 * @see org.fh.auge.AugePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private AugePackageImpl() {
        super(eNS_URI, AugeFactory.eINSTANCE);
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
    public static AugePackage init() {
        if (isInited) return (AugePackage) EPackage.Registry.INSTANCE.getEPackage(AugePackage.eNS_URI);
        AugePackageImpl theAugePackage = (AugePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof AugePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new AugePackageImpl());
        isInited = true;
        StockPackageImpl theStockPackage = (StockPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(StockPackage.eNS_URI) instanceof StockPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(StockPackage.eNS_URI) : StockPackage.eINSTANCE);
        AccountPackageImpl theAccountPackage = (AccountPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(AccountPackage.eNS_URI) instanceof AccountPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AccountPackage.eNS_URI) : AccountPackage.eINSTANCE);
        CorePackageImpl theCorePackage = (CorePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI) instanceof CorePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI) : CorePackage.eINSTANCE);
        theAugePackage.createPackageContents();
        theStockPackage.createPackageContents();
        theAccountPackage.createPackageContents();
        theCorePackage.createPackageContents();
        theAugePackage.initializePackageContents();
        theStockPackage.initializePackageContents();
        theAccountPackage.initializePackageContents();
        theCorePackage.initializePackageContents();
        theAugePackage.freeze();
        return theAugePackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getRepository() {
        return repositoryEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRepository_Stocks() {
        return (EReference) repositoryEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRepository_Exchanges() {
        return (EReference) repositoryEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRepository_Portfolios() {
        return (EReference) repositoryEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getRepository_Accounts() {
        return (EReference) repositoryEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AugeFactory getAugeFactory() {
        return (AugeFactory) getEFactoryInstance();
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
        repositoryEClass = createEClass(REPOSITORY);
        createEReference(repositoryEClass, REPOSITORY__STOCKS);
        createEReference(repositoryEClass, REPOSITORY__EXCHANGES);
        createEReference(repositoryEClass, REPOSITORY__PORTFOLIOS);
        createEReference(repositoryEClass, REPOSITORY__ACCOUNTS);
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
        StockPackage theStockPackage = (StockPackage) EPackage.Registry.INSTANCE.getEPackage(StockPackage.eNS_URI);
        AccountPackage theAccountPackage = (AccountPackage) EPackage.Registry.INSTANCE.getEPackage(AccountPackage.eNS_URI);
        initEClass(repositoryEClass, Repository.class, "Repository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRepository_Stocks(), theStockPackage.getStock(), null, "stocks", null, 0, -1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRepository_Exchanges(), theStockPackage.getExchange(), null, "exchanges", null, 0, -1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRepository_Portfolios(), theStockPackage.getPortfolio(), null, "portfolios", null, 0, -1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRepository_Accounts(), theAccountPackage.getAccounts(), null, "accounts", null, 1, 1, Repository.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
