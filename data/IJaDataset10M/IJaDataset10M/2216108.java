package mytest.impl;

import mytest.MytestFactory;
import mytest.MytestPackage;
import mytest.WithClassAttribute;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MytestPackageImpl extends EPackageImpl implements MytestPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass withClassAttributeEClass = null;

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
	 * @see mytest.MytestPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private MytestPackageImpl() {
        super(eNS_URI, MytestFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link MytestPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static MytestPackage init() {
        if (isInited) return (MytestPackage) EPackage.Registry.INSTANCE.getEPackage(MytestPackage.eNS_URI);
        MytestPackageImpl theMytestPackage = (MytestPackageImpl) (EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MytestPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MytestPackageImpl());
        isInited = true;
        theMytestPackage.createPackageContents();
        theMytestPackage.initializePackageContents();
        theMytestPackage.freeze();
        EPackage.Registry.INSTANCE.put(MytestPackage.eNS_URI, theMytestPackage);
        return theMytestPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getWithClassAttribute() {
        return withClassAttributeEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getWithClassAttribute_Class() {
        return (EAttribute) withClassAttributeEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MytestFactory getMytestFactory() {
        return (MytestFactory) getEFactoryInstance();
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
        withClassAttributeEClass = createEClass(WITH_CLASS_ATTRIBUTE);
        createEAttribute(withClassAttributeEClass, WITH_CLASS_ATTRIBUTE__CLASS);
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
        initEClass(withClassAttributeEClass, WithClassAttribute.class, "WithClassAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWithClassAttribute_Class(), ecorePackage.getEString(), "class", null, 0, 1, WithClassAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        createResource(eNS_URI);
    }
}
