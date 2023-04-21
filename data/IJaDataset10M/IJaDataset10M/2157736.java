package org.remus.infomngmnt.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.remus.infomngmnt.ApplicationRoot;
import org.remus.infomngmnt.AvailableTags;
import org.remus.infomngmnt.Category;
import org.remus.infomngmnt.InfomngmntPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Application Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.impl.ApplicationRootImpl#getRootCategories <em>Root Categories</em>}</li>
 *   <li>{@link org.remus.infomngmnt.impl.ApplicationRootImpl#getAvailableTags <em>Available Tags</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ApplicationRootImpl extends EObjectImpl implements ApplicationRoot {

    /**
	 * The cached value of the '{@link #getRootCategories() <em>Root Categories</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootCategories()
	 * @generated
	 * @ordered
	 */
    protected EList<Category> rootCategories;

    /**
	 * The cached value of the '{@link #getAvailableTags() <em>Available Tags</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableTags()
	 * @generated
	 * @ordered
	 */
    protected AvailableTags availableTags;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ApplicationRootImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return InfomngmntPackage.Literals.APPLICATION_ROOT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Category> getRootCategories() {
        if (rootCategories == null) {
            rootCategories = new EObjectResolvingEList<Category>(Category.class, this, InfomngmntPackage.APPLICATION_ROOT__ROOT_CATEGORIES);
        }
        return rootCategories;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AvailableTags getAvailableTags() {
        if (availableTags != null && availableTags.eIsProxy()) {
            InternalEObject oldAvailableTags = (InternalEObject) availableTags;
            availableTags = (AvailableTags) eResolveProxy(oldAvailableTags);
            if (availableTags != oldAvailableTags) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS, oldAvailableTags, availableTags));
            }
        }
        return availableTags;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AvailableTags basicGetAvailableTags() {
        return availableTags;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAvailableTags(AvailableTags newAvailableTags) {
        AvailableTags oldAvailableTags = availableTags;
        availableTags = newAvailableTags;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS, oldAvailableTags, availableTags));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case InfomngmntPackage.APPLICATION_ROOT__ROOT_CATEGORIES:
                return getRootCategories();
            case InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS:
                if (resolve) return getAvailableTags();
                return basicGetAvailableTags();
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
            case InfomngmntPackage.APPLICATION_ROOT__ROOT_CATEGORIES:
                getRootCategories().clear();
                getRootCategories().addAll((Collection<? extends Category>) newValue);
                return;
            case InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS:
                setAvailableTags((AvailableTags) newValue);
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
            case InfomngmntPackage.APPLICATION_ROOT__ROOT_CATEGORIES:
                getRootCategories().clear();
                return;
            case InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS:
                setAvailableTags((AvailableTags) null);
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
            case InfomngmntPackage.APPLICATION_ROOT__ROOT_CATEGORIES:
                return rootCategories != null && !rootCategories.isEmpty();
            case InfomngmntPackage.APPLICATION_ROOT__AVAILABLE_TAGS:
                return availableTags != null;
        }
        return super.eIsSet(featureID);
    }
}
