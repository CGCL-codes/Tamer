package jfb.examples.gmf.filesystem;

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
 * @see jfb.examples.gmf.filesystem.FilesystemFactory
 * @model kind="package"
 * @generated
 */
public interface FilesystemPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "filesystem";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///jfb/examples/gmf/filesystem.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "jfb.examples.gmf.filesystem";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    FilesystemPackage eINSTANCE = jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl.init();

    /**
	 * The meta object id for the '{@link jfb.examples.gmf.filesystem.impl.FileImpl <em>File</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see jfb.examples.gmf.filesystem.impl.FileImpl
	 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFile()
	 * @generated
	 */
    int FILE = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FILE__NAME = 0;

    /**
	 * The number of structural features of the '<em>File</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FILE_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link jfb.examples.gmf.filesystem.impl.FilesystemImpl <em>Filesystem</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see jfb.examples.gmf.filesystem.impl.FilesystemImpl
	 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFilesystem()
	 * @generated
	 */
    int FILESYSTEM = 1;

    /**
	 * The feature id for the '<em><b>Folders</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FILESYSTEM__FOLDERS = 0;

    /**
	 * The feature id for the '<em><b>Files</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FILESYSTEM__FILES = 1;

    /**
	 * The number of structural features of the '<em>Filesystem</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FILESYSTEM_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link jfb.examples.gmf.filesystem.impl.FolderImpl <em>Folder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see jfb.examples.gmf.filesystem.impl.FolderImpl
	 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFolder()
	 * @generated
	 */
    int FOLDER = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FOLDER__NAME = 0;

    /**
	 * The feature id for the '<em><b>Folders</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FOLDER__FOLDERS = 1;

    /**
	 * The feature id for the '<em><b>Files</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FOLDER__FILES = 2;

    /**
	 * The number of structural features of the '<em>Folder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int FOLDER_FEATURE_COUNT = 3;

    /**
	 * Returns the meta object for class '{@link jfb.examples.gmf.filesystem.File <em>File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File</em>'.
	 * @see jfb.examples.gmf.filesystem.File
	 * @generated
	 */
    EClass getFile();

    /**
	 * Returns the meta object for the attribute '{@link jfb.examples.gmf.filesystem.File#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see jfb.examples.gmf.filesystem.File#getName()
	 * @see #getFile()
	 * @generated
	 */
    EAttribute getFile_Name();

    /**
	 * Returns the meta object for class '{@link jfb.examples.gmf.filesystem.Filesystem <em>Filesystem</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Filesystem</em>'.
	 * @see jfb.examples.gmf.filesystem.Filesystem
	 * @generated
	 */
    EClass getFilesystem();

    /**
	 * Returns the meta object for the containment reference list '{@link jfb.examples.gmf.filesystem.Filesystem#getFolders <em>Folders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Folders</em>'.
	 * @see jfb.examples.gmf.filesystem.Filesystem#getFolders()
	 * @see #getFilesystem()
	 * @generated
	 */
    EReference getFilesystem_Folders();

    /**
	 * Returns the meta object for the containment reference list '{@link jfb.examples.gmf.filesystem.Filesystem#getFiles <em>Files</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Files</em>'.
	 * @see jfb.examples.gmf.filesystem.Filesystem#getFiles()
	 * @see #getFilesystem()
	 * @generated
	 */
    EReference getFilesystem_Files();

    /**
	 * Returns the meta object for class '{@link jfb.examples.gmf.filesystem.Folder <em>Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Folder</em>'.
	 * @see jfb.examples.gmf.filesystem.Folder
	 * @generated
	 */
    EClass getFolder();

    /**
	 * Returns the meta object for the attribute '{@link jfb.examples.gmf.filesystem.Folder#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see jfb.examples.gmf.filesystem.Folder#getName()
	 * @see #getFolder()
	 * @generated
	 */
    EAttribute getFolder_Name();

    /**
	 * Returns the meta object for the reference list '{@link jfb.examples.gmf.filesystem.Folder#getFolders <em>Folders</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Folders</em>'.
	 * @see jfb.examples.gmf.filesystem.Folder#getFolders()
	 * @see #getFolder()
	 * @generated
	 */
    EReference getFolder_Folders();

    /**
	 * Returns the meta object for the reference list '{@link jfb.examples.gmf.filesystem.Folder#getFiles <em>Files</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Files</em>'.
	 * @see jfb.examples.gmf.filesystem.Folder#getFiles()
	 * @see #getFolder()
	 * @generated
	 */
    EReference getFolder_Files();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    FilesystemFactory getFilesystemFactory();

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
		 * The meta object literal for the '{@link jfb.examples.gmf.filesystem.impl.FileImpl <em>File</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see jfb.examples.gmf.filesystem.impl.FileImpl
		 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFile()
		 * @generated
		 */
        EClass FILE = eINSTANCE.getFile();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute FILE__NAME = eINSTANCE.getFile_Name();

        /**
		 * The meta object literal for the '{@link jfb.examples.gmf.filesystem.impl.FilesystemImpl <em>Filesystem</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see jfb.examples.gmf.filesystem.impl.FilesystemImpl
		 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFilesystem()
		 * @generated
		 */
        EClass FILESYSTEM = eINSTANCE.getFilesystem();

        /**
		 * The meta object literal for the '<em><b>Folders</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference FILESYSTEM__FOLDERS = eINSTANCE.getFilesystem_Folders();

        /**
		 * The meta object literal for the '<em><b>Files</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference FILESYSTEM__FILES = eINSTANCE.getFilesystem_Files();

        /**
		 * The meta object literal for the '{@link jfb.examples.gmf.filesystem.impl.FolderImpl <em>Folder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see jfb.examples.gmf.filesystem.impl.FolderImpl
		 * @see jfb.examples.gmf.filesystem.impl.FilesystemPackageImpl#getFolder()
		 * @generated
		 */
        EClass FOLDER = eINSTANCE.getFolder();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute FOLDER__NAME = eINSTANCE.getFolder_Name();

        /**
		 * The meta object literal for the '<em><b>Folders</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference FOLDER__FOLDERS = eINSTANCE.getFolder_Folders();

        /**
		 * The meta object literal for the '<em><b>Files</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference FOLDER__FILES = eINSTANCE.getFolder_Files();
    }
}
