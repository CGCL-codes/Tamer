package net.sf.povclipsetextur.imagemeta.core.model.files.impl;

import net.sf.povclipsetextur.imagemeta.core.model.files.FilesPackage;
import net.sf.povclipsetextur.imagemeta.core.model.files.IFSObject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IFS Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.povclipsetextur.imagemeta.core.model.files.impl.IFSObjectImpl#getResourcePath <em>Resource Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IFSObjectImpl extends EObjectImpl implements IFSObject {

    /**
	 * The default value of the '{@link #getResourcePath() <em>Resource Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourcePath()
	 * @generated
	 * @ordered
	 */
    protected static final IPath RESOURCE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getResourcePath() <em>Resource Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourcePath()
	 * @generated
	 * @ordered
	 */
    protected IPath resourcePath = RESOURCE_PATH_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IFSObjectImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return FilesPackage.Literals.IFS_OBJECT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IPath getResourcePath() {
        return resourcePath;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResourcePath(IPath newResourcePath) {
        IPath oldResourcePath = resourcePath;
        resourcePath = newResourcePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FilesPackage.IFS_OBJECT__RESOURCE_PATH, oldResourcePath, resourcePath));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case FilesPackage.IFS_OBJECT__RESOURCE_PATH:
                return getResourcePath();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case FilesPackage.IFS_OBJECT__RESOURCE_PATH:
                setResourcePath((IPath) newValue);
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
            case FilesPackage.IFS_OBJECT__RESOURCE_PATH:
                setResourcePath(RESOURCE_PATH_EDEFAULT);
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
            case FilesPackage.IFS_OBJECT__RESOURCE_PATH:
                return RESOURCE_PATH_EDEFAULT == null ? resourcePath != null : !RESOURCE_PATH_EDEFAULT.equals(resourcePath);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (resourcePath: ");
        result.append(resourcePath);
        result.append(')');
        return result.toString();
    }
}
