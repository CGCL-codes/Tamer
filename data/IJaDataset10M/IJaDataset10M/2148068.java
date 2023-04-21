package net.sf.rcer.rom.ddic.impl;

import net.sf.rcer.rom.ROMPackage;
import net.sf.rcer.rom.RepositoryObjectCollection;
import net.sf.rcer.rom.RepositoryObjectType;
import net.sf.rcer.rom.ddic.DDICPackage;
import net.sf.rcer.rom.ddic.TableType;
import net.sf.rcer.rom.impl.RepositoryObjectImpl;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Table Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.rcer.rom.ddic.impl.TableTypeImpl#getCollection <em>Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TableTypeImpl extends RepositoryObjectImpl implements TableType {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TableTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DDICPackage.Literals.TABLE_TYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RepositoryObjectCollection getCollection() {
        if (eContainerFeatureID != DDICPackage.TABLE_TYPE__COLLECTION) return null;
        return (RepositoryObjectCollection) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DDICPackage.TABLE_TYPE__COLLECTION:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return eBasicSetContainer(otherEnd, DDICPackage.TABLE_TYPE__COLLECTION, msgs);
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
            case DDICPackage.TABLE_TYPE__COLLECTION:
                return eBasicSetContainer(null, DDICPackage.TABLE_TYPE__COLLECTION, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID) {
            case DDICPackage.TABLE_TYPE__COLLECTION:
                return eInternalContainer().eInverseRemove(this, ROMPackage.REPOSITORY_OBJECT_COLLECTION__TABLE_TYPES, RepositoryObjectCollection.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DDICPackage.TABLE_TYPE__COLLECTION:
                return getCollection();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DDICPackage.TABLE_TYPE__COLLECTION:
                return getCollection() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * @see net.sf.rcer.rom.impl.RepositoryObjectImpl#getObjectType()
	 * @generated no
	 */
    @Override
    public RepositoryObjectType getObjectType() {
        return RepositoryObjectType.TABLE_TYPE;
    }

    /**
	 * @see net.sf.rcer.rom.impl.RepositoryObjectImpl#getProgramID()
	 * @generated no
	 */
    @Override
    public String getProgramID() {
        return "R3TR";
    }

    /**
	 * @generated no
	 * @see net.sf.rcer.rom.impl.RepositoryObjectImpl#getObjectTypeID()
	 */
    @Override
    public String getObjectTypeID() {
        return "TTYP";
    }
}
