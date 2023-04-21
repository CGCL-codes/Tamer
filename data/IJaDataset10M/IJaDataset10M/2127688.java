package org.remus.infomngmnt.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.remus.infomngmnt.InfomngmntPackage;
import org.remus.infomngmnt.RemoteRepository;
import org.remus.infomngmnt.RepositoryCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Repository Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.impl.RepositoryCollectionImpl#getRepositories <em>Repositories</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RepositoryCollectionImpl extends EObjectImpl implements RepositoryCollection {

    /**
	 * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositories()
	 * @generated
	 * @ordered
	 */
    protected EList<RemoteRepository> repositories;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RepositoryCollectionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return InfomngmntPackage.Literals.REPOSITORY_COLLECTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<RemoteRepository> getRepositories() {
        if (repositories == null) {
            repositories = new EObjectContainmentEList<RemoteRepository>(RemoteRepository.class, this, InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES);
        }
        return repositories;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES:
                return ((InternalEList<?>) getRepositories()).basicRemove(otherEnd, msgs);
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
            case InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES:
                return getRepositories();
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
            case InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES:
                getRepositories().clear();
                getRepositories().addAll((Collection<? extends RemoteRepository>) newValue);
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
            case InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES:
                getRepositories().clear();
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
            case InfomngmntPackage.REPOSITORY_COLLECTION__REPOSITORIES:
                return repositories != null && !repositories.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
