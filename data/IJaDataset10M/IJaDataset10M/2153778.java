package de.fraunhofer.isst.eastadl.elements.impl;

import de.fraunhofer.isst.eastadl.autosar.impl.IdentifiableImpl;
import de.fraunhofer.isst.eastadl.elements.Comment;
import de.fraunhofer.isst.eastadl.elements.EAElement;
import de.fraunhofer.isst.eastadl.elements.ElementsPackage;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeElementType;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeValue;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeableElement;
import de.fraunhofer.isst.eastadl.userattributes.UserattributesPackage;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EA Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.EAElementImpl#getUaValue <em>Ua Value</em>}</li>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.EAElementImpl#getUaType <em>Ua Type</em>}</li>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.EAElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.fraunhofer.isst.eastadl.elements.impl.EAElementImpl#getOwnedComment <em>Owned Comment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EAElementImpl extends IdentifiableImpl implements EAElement {

    /**
	 * The cached value of the '{@link #getUaValue() <em>Ua Value</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUaValue()
	 * @generated
	 * @ordered
	 */
    protected EList<UserAttributeValue> uaValue;

    /**
	 * The cached value of the '{@link #getUaType() <em>Ua Type</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUaType()
	 * @generated
	 * @ordered
	 */
    protected EList<UserAttributeElementType> uaType;

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * This is true if the Name attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean nameESet;

    /**
	 * The cached value of the '{@link #getOwnedComment() <em>Owned Comment</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedComment()
	 * @generated
	 * @ordered
	 */
    protected EList<Comment> ownedComment;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EAElementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ElementsPackage.Literals.EA_ELEMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UserAttributeValue> getUaValue() {
        if (uaValue == null) {
            uaValue = new EObjectContainmentEList<UserAttributeValue>(UserAttributeValue.class, this, ElementsPackage.EA_ELEMENT__UA_VALUE);
        }
        return uaValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<UserAttributeElementType> getUaType() {
        if (uaType == null) {
            uaType = new EObjectResolvingEList<UserAttributeElementType>(UserAttributeElementType.class, this, ElementsPackage.EA_ELEMENT__UA_TYPE);
        }
        return uaType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        boolean oldNameESet = nameESet;
        nameESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ElementsPackage.EA_ELEMENT__NAME, oldName, name, !oldNameESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetName() {
        String oldName = name;
        boolean oldNameESet = nameESet;
        name = NAME_EDEFAULT;
        nameESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, ElementsPackage.EA_ELEMENT__NAME, oldName, NAME_EDEFAULT, oldNameESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetName() {
        return nameESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Comment> getOwnedComment() {
        if (ownedComment == null) {
            ownedComment = new EObjectContainmentEList<Comment>(Comment.class, this, ElementsPackage.EA_ELEMENT__OWNED_COMMENT);
        }
        return ownedComment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ElementsPackage.EA_ELEMENT__UA_VALUE:
                return ((InternalEList<?>) getUaValue()).basicRemove(otherEnd, msgs);
            case ElementsPackage.EA_ELEMENT__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComment()).basicRemove(otherEnd, msgs);
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
            case ElementsPackage.EA_ELEMENT__UA_VALUE:
                return getUaValue();
            case ElementsPackage.EA_ELEMENT__UA_TYPE:
                return getUaType();
            case ElementsPackage.EA_ELEMENT__NAME:
                return getName();
            case ElementsPackage.EA_ELEMENT__OWNED_COMMENT:
                return getOwnedComment();
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
            case ElementsPackage.EA_ELEMENT__UA_VALUE:
                getUaValue().clear();
                getUaValue().addAll((Collection<? extends UserAttributeValue>) newValue);
                return;
            case ElementsPackage.EA_ELEMENT__UA_TYPE:
                getUaType().clear();
                getUaType().addAll((Collection<? extends UserAttributeElementType>) newValue);
                return;
            case ElementsPackage.EA_ELEMENT__NAME:
                setName((String) newValue);
                return;
            case ElementsPackage.EA_ELEMENT__OWNED_COMMENT:
                getOwnedComment().clear();
                getOwnedComment().addAll((Collection<? extends Comment>) newValue);
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
            case ElementsPackage.EA_ELEMENT__UA_VALUE:
                getUaValue().clear();
                return;
            case ElementsPackage.EA_ELEMENT__UA_TYPE:
                getUaType().clear();
                return;
            case ElementsPackage.EA_ELEMENT__NAME:
                unsetName();
                return;
            case ElementsPackage.EA_ELEMENT__OWNED_COMMENT:
                getOwnedComment().clear();
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
            case ElementsPackage.EA_ELEMENT__UA_VALUE:
                return uaValue != null && !uaValue.isEmpty();
            case ElementsPackage.EA_ELEMENT__UA_TYPE:
                return uaType != null && !uaType.isEmpty();
            case ElementsPackage.EA_ELEMENT__NAME:
                return isSetName();
            case ElementsPackage.EA_ELEMENT__OWNED_COMMENT:
                return ownedComment != null && !ownedComment.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == UserAttributeableElement.class) {
            switch(derivedFeatureID) {
                case ElementsPackage.EA_ELEMENT__UA_VALUE:
                    return UserattributesPackage.USER_ATTRIBUTEABLE_ELEMENT__UA_VALUE;
                case ElementsPackage.EA_ELEMENT__UA_TYPE:
                    return UserattributesPackage.USER_ATTRIBUTEABLE_ELEMENT__UA_TYPE;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == UserAttributeableElement.class) {
            switch(baseFeatureID) {
                case UserattributesPackage.USER_ATTRIBUTEABLE_ELEMENT__UA_VALUE:
                    return ElementsPackage.EA_ELEMENT__UA_VALUE;
                case UserattributesPackage.USER_ATTRIBUTEABLE_ELEMENT__UA_TYPE:
                    return ElementsPackage.EA_ELEMENT__UA_TYPE;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (name: ");
        if (nameESet) result.append(name); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
