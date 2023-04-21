package net.sf.povclipsetextur.imagemeta.core.model.internal;

import net.sf.povclipsetextur.imagemeta.core.model.IDisposable;
import net.sf.povclipsetextur.imagemeta.core.model.ImageMetaFactory;
import net.sf.povclipsetextur.imagemeta.core.model.ImageMetaPackage;
import net.sf.povclipsetextur.imagemeta.core.model.files.FilesPackage;
import net.sf.povclipsetextur.imagemeta.core.model.files.impl.FilesPackageImpl;
import net.sf.povclipsetextur.imagemeta.core.model.metadata.MetaDataPackage;
import net.sf.povclipsetextur.imagemeta.core.model.metadata.impl.MetaDataPackageImpl;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ImageMetaPackageImpl extends EPackageImpl implements ImageMetaPackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass iDisposableEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType iPathEDataType = null;

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
	 * @see net.sf.povclipsetextur.imagemeta.core.model.ImageMetaPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private ImageMetaPackageImpl() {
        super(eNS_URI, ImageMetaFactory.eINSTANCE);
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
    public static ImageMetaPackage init() {
        if (isInited) return (ImageMetaPackage) EPackage.Registry.INSTANCE.getEPackage(ImageMetaPackage.eNS_URI);
        ImageMetaPackageImpl theImageMetaPackage = (ImageMetaPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ImageMetaPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ImageMetaPackageImpl());
        isInited = true;
        MetaDataPackageImpl theMetaDataPackage = (MetaDataPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(MetaDataPackage.eNS_URI) instanceof MetaDataPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetaDataPackage.eNS_URI) : MetaDataPackage.eINSTANCE);
        FilesPackageImpl theFilesPackage = (FilesPackageImpl) (EPackage.Registry.INSTANCE.getEPackage(FilesPackage.eNS_URI) instanceof FilesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(FilesPackage.eNS_URI) : FilesPackage.eINSTANCE);
        theImageMetaPackage.createPackageContents();
        theMetaDataPackage.createPackageContents();
        theFilesPackage.createPackageContents();
        theImageMetaPackage.initializePackageContents();
        theMetaDataPackage.initializePackageContents();
        theFilesPackage.initializePackageContents();
        theImageMetaPackage.freeze();
        return theImageMetaPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getIDisposable() {
        return iDisposableEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getIPath() {
        return iPathEDataType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImageMetaFactory getImageMetaFactory() {
        return (ImageMetaFactory) getEFactoryInstance();
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
        iDisposableEClass = createEClass(IDISPOSABLE);
        iPathEDataType = createEDataType(IPATH);
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
        MetaDataPackage theMetaDataPackage = (MetaDataPackage) EPackage.Registry.INSTANCE.getEPackage(MetaDataPackage.eNS_URI);
        FilesPackage theFilesPackage = (FilesPackage) EPackage.Registry.INSTANCE.getEPackage(FilesPackage.eNS_URI);
        getESubpackages().add(theMetaDataPackage);
        getESubpackages().add(theFilesPackage);
        initEClass(iDisposableEClass, IDisposable.class, "IDisposable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        addEOperation(iDisposableEClass, null, "dispose", 0, 1, IS_UNIQUE, IS_ORDERED);
        initEDataType(iPathEDataType, IPath.class, "IPath", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        createResource(eNS_URI);
    }
}
