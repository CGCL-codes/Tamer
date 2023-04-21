package org.eclipse.emf.compare.diff.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.GenericDiffElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Generic Diff Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl#getLeftElement <em>Left Element</em>}</li>
 *   <li>{@link org.eclipse.emf.compare.diff.metamodel.impl.GenericDiffElementImpl#getRightElement <em>Right Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GenericDiffElementImpl extends DiffElementImpl implements GenericDiffElement {

    /**
	 * The cached value of the '{@link #getLeftElement() <em>Left Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLeftElement()
	 * @generated
	 * @ordered
	 */
    protected EObject leftElement;

    /**
	 * The cached value of the '{@link #getRightElement() <em>Right Element</em>}' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRightElement()
	 * @generated
	 * @ordered
	 */
    protected EObject rightElement;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected GenericDiffElementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EObject basicGetLeftElement() {
        return leftElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EObject basicGetRightElement() {
        return rightElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT:
                if (resolve) return getLeftElement();
                return basicGetLeftElement();
            case DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT:
                if (resolve) return getRightElement();
                return basicGetRightElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT:
                return leftElement != null;
            case DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT:
                return rightElement != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT:
                setLeftElement((EObject) newValue);
                return;
            case DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT:
                setRightElement((EObject) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT:
                setLeftElement((EObject) null);
                return;
            case DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT:
                setRightElement((EObject) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getLeftElement() {
        if (leftElement != null && leftElement.eIsProxy()) {
            InternalEObject oldLeftElement = (InternalEObject) leftElement;
            leftElement = eResolveProxy(oldLeftElement);
            if (leftElement != oldLeftElement) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT, oldLeftElement, leftElement));
            }
        }
        return leftElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getRightElement() {
        if (rightElement != null && rightElement.eIsProxy()) {
            InternalEObject oldRightElement = (InternalEObject) rightElement;
            rightElement = eResolveProxy(oldRightElement);
            if (rightElement != oldRightElement) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT, oldRightElement, rightElement));
            }
        }
        return rightElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setLeftElement(EObject newLeftElement) {
        EObject oldLeftElement = leftElement;
        leftElement = newLeftElement;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.GENERIC_DIFF_ELEMENT__LEFT_ELEMENT, oldLeftElement, leftElement));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setRightElement(EObject newRightElement) {
        EObject oldRightElement = rightElement;
        rightElement = newRightElement;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DiffPackage.GENERIC_DIFF_ELEMENT__RIGHT_ELEMENT, oldRightElement, rightElement));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DiffPackage.Literals.GENERIC_DIFF_ELEMENT;
    }
}
