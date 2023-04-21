package net.randomice.gengmf.desc.impl;

import net.randomice.gengmf.desc.DescPackage;
import net.randomice.gengmf.desc.ElementTypeTransformator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Element Type Transformator</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link net.randomice.gengmf.desc.impl.ElementTypeTransformatorImpl#getElement
 * <em>Element</em>}</li>
 * <li>
 * {@link net.randomice.gengmf.desc.impl.ElementTypeTransformatorImpl#getEType
 * <em>EType</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ElementTypeTransformatorImpl extends AbstractTransformatorImpl implements ElementTypeTransformator {

    /**
	 * The cached value of the '{@link #getElement() <em>Element</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
    protected EObject element;

    /**
	 * The cached value of the '{@link #getEType() <em>EType</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEType()
	 * @generated
	 * @ordered
	 */
    protected EClass eType;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected ElementTypeTransformatorImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DescPackage.Literals.ELEMENT_TYPE_TRANSFORMATOR;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EObject getElement() {
        if (element != null && element.eIsProxy()) {
            InternalEObject oldElement = (InternalEObject) element;
            element = eResolveProxy(oldElement);
            if (element != oldElement) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT, oldElement, element));
            }
        }
        return element;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EObject basicGetElement() {
        return element;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setElement(EObject newElement) {
        EObject oldElement = element;
        element = newElement;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT, oldElement, element));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass getEType() {
        if (eType != null && eType.eIsProxy()) {
            InternalEObject oldEType = (InternalEObject) eType;
            eType = (EClass) eResolveProxy(oldEType);
            if (eType != oldEType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE, oldEType, eType));
            }
        }
        return eType;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass basicGetEType() {
        return eType;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setEType(EClass newEType) {
        EClass oldEType = eType;
        eType = newEType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE, oldEType, eType));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT:
                if (resolve) return getElement();
                return basicGetElement();
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE:
                if (resolve) return getEType();
                return basicGetEType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT:
                setElement((EObject) newValue);
                return;
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE:
                setEType((EClass) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT:
                setElement((EObject) null);
                return;
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE:
                setEType((EClass) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ELEMENT:
                return element != null;
            case DescPackage.ELEMENT_TYPE_TRANSFORMATOR__ETYPE:
                return eType != null;
        }
        return super.eIsSet(featureID);
    }
}
