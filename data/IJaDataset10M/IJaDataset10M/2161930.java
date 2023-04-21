package ch.hsr.orm.model.impl;

import ch.hsr.orm.model.CollectionType;
import ch.hsr.orm.model.ModelPackage;
import ch.hsr.orm.model.UniOneToMany;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Uni One To Many</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ch.hsr.orm.model.impl.UniOneToManyImpl#getCollectionType <em>Collection Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UniOneToManyImpl extends UnidirectionalRelationImpl implements UniOneToMany {

    /**
	 * The default value of the '{@link #getCollectionType() <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionType()
	 * @generated
	 * @ordered
	 */
    protected static final CollectionType COLLECTION_TYPE_EDEFAULT = CollectionType.SET;

    /**
	 * The cached value of the '{@link #getCollectionType() <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionType()
	 * @generated
	 * @ordered
	 */
    protected CollectionType collectionType = COLLECTION_TYPE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected UniOneToManyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.UNI_ONE_TO_MANY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CollectionType getCollectionType() {
        return collectionType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCollectionType(CollectionType newCollectionType) {
        CollectionType oldCollectionType = collectionType;
        collectionType = newCollectionType == null ? COLLECTION_TYPE_EDEFAULT : newCollectionType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.UNI_ONE_TO_MANY__COLLECTION_TYPE, oldCollectionType, collectionType));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.UNI_ONE_TO_MANY__COLLECTION_TYPE:
                return getCollectionType();
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
            case ModelPackage.UNI_ONE_TO_MANY__COLLECTION_TYPE:
                setCollectionType((CollectionType) newValue);
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
            case ModelPackage.UNI_ONE_TO_MANY__COLLECTION_TYPE:
                setCollectionType(COLLECTION_TYPE_EDEFAULT);
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
            case ModelPackage.UNI_ONE_TO_MANY__COLLECTION_TYPE:
                return collectionType != COLLECTION_TYPE_EDEFAULT;
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
        result.append(" (collectionType: ");
        result.append(collectionType);
        result.append(')');
        return result.toString();
    }
}
