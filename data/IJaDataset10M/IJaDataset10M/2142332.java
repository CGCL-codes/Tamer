package net.sf.povclipsetextur.imagemeta.core.model.files.impl;

import java.util.Collection;
import net.sf.povclipsetextur.imagemeta.core.model.files.FilesPackage;
import net.sf.povclipsetextur.imagemeta.core.model.files.IMetaFile;
import net.sf.povclipsetextur.imagemeta.core.model.files.IMetaFolder;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IMeta Folder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.povclipsetextur.imagemeta.core.model.files.impl.IMetaFolderImpl#getFilesList <em>Files</em>}</li>
 *   <li>{@link net.sf.povclipsetextur.imagemeta.core.model.files.impl.IMetaFolderImpl#getSubfoldersList <em>Subfolders</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IMetaFolderImpl extends IFSObjectImpl implements IMetaFolder {

    /**
	 * The cached value of the '{@link #getFilesList() <em>Files</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilesList()
	 * @generated
	 * @ordered
	 */
    protected EList<IMetaFile> files;

    /**
	 * The empty value for the '{@link #getFiles() <em>Files</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFiles()
	 * @generated
	 * @ordered
	 */
    protected static final IMetaFile[] FILES_EEMPTY_ARRAY = new IMetaFile[0];

    /**
	 * The cached value of the '{@link #getSubfoldersList() <em>Subfolders</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubfoldersList()
	 * @generated
	 * @ordered
	 */
    protected EList<IMetaFolder> subfolders;

    /**
	 * The empty value for the '{@link #getSubfolders() <em>Subfolders</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubfolders()
	 * @generated
	 * @ordered
	 */
    protected static final IMetaFolder[] SUBFOLDERS_EEMPTY_ARRAY = new IMetaFolder[0];

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IMetaFolderImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return FilesPackage.Literals.IMETA_FOLDER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public IMetaFile[] getFiles() {
        if (files == null || files.isEmpty()) return FILES_EEMPTY_ARRAY;
        BasicEList<IMetaFile> list = (BasicEList<IMetaFile>) files;
        list.shrink();
        return (IMetaFile[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IMetaFile getFiles(int index) {
        return getFilesList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getFilesLength() {
        return files == null ? 0 : files.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFiles(IMetaFile[] newFiles) {
        ((BasicEList<IMetaFile>) getFilesList()).setData(newFiles.length, newFiles);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFiles(int index, IMetaFile element) {
        getFilesList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<IMetaFile> getFilesList() {
        if (files == null) {
            files = new EObjectContainmentWithInverseEList<IMetaFile>(IMetaFile.class, this, FilesPackage.IMETA_FOLDER__FILES, FilesPackage.IMETA_FILE__PARENT);
        }
        return files;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public IMetaFolder[] getSubfolders() {
        if (subfolders == null || subfolders.isEmpty()) return SUBFOLDERS_EEMPTY_ARRAY;
        BasicEList<IMetaFolder> list = (BasicEList<IMetaFolder>) subfolders;
        list.shrink();
        return (IMetaFolder[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IMetaFolder getSubfolders(int index) {
        return getSubfoldersList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getSubfoldersLength() {
        return subfolders == null ? 0 : subfolders.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSubfolders(IMetaFolder[] newSubfolders) {
        ((BasicEList<IMetaFolder>) getSubfoldersList()).setData(newSubfolders.length, newSubfolders);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSubfolders(int index, IMetaFolder element) {
        getSubfoldersList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<IMetaFolder> getSubfoldersList() {
        if (subfolders == null) {
            subfolders = new EObjectContainmentEList<IMetaFolder>(IMetaFolder.class, this, FilesPackage.IMETA_FOLDER__SUBFOLDERS);
        }
        return subfolders;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getFilesList()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                return ((InternalEList<?>) getFilesList()).basicRemove(otherEnd, msgs);
            case FilesPackage.IMETA_FOLDER__SUBFOLDERS:
                return ((InternalEList<?>) getSubfoldersList()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                return getFilesList();
            case FilesPackage.IMETA_FOLDER__SUBFOLDERS:
                return getSubfoldersList();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                getFilesList().clear();
                getFilesList().addAll((Collection<? extends IMetaFile>) newValue);
                return;
            case FilesPackage.IMETA_FOLDER__SUBFOLDERS:
                getSubfoldersList().clear();
                getSubfoldersList().addAll((Collection<? extends IMetaFolder>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                getFilesList().clear();
                return;
            case FilesPackage.IMETA_FOLDER__SUBFOLDERS:
                getSubfoldersList().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case FilesPackage.IMETA_FOLDER__FILES:
                return files != null && !files.isEmpty();
            case FilesPackage.IMETA_FOLDER__SUBFOLDERS:
                return subfolders != null && !subfolders.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
