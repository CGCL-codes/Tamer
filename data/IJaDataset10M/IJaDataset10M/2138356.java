package net.randomice.gengmf.desc.impl;

import net.randomice.gengmf.desc.ClassFigureDesc;
import net.randomice.gengmf.desc.DescPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Class Figure Desc</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link net.randomice.gengmf.desc.impl.ClassFigureDescImpl#getClass_ <em>
 * Class</em>}</li>
 * <li>
 * {@link net.randomice.gengmf.desc.impl.ClassFigureDescImpl#getContainmentFeature
 * <em>Containment Feature</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class ClassFigureDescImpl extends FigureDescImpl implements ClassFigureDesc {

    /**
	 * The cached value of the '{@link #getClass_() <em>Class</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClass_()
	 * @generated
	 * @ordered
	 */
    protected EClass class_;

    /**
	 * The cached value of the '{@link #getContainmentFeature()
	 * <em>Containment Feature</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getContainmentFeature()
	 * @generated
	 * @ordered
	 */
    protected EReference containmentFeature;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected ClassFigureDescImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DescPackage.Literals.CLASS_FIGURE_DESC;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass getClass_() {
        if (class_ != null && class_.eIsProxy()) {
            InternalEObject oldClass = (InternalEObject) class_;
            class_ = (EClass) eResolveProxy(oldClass);
            if (class_ != oldClass) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DescPackage.CLASS_FIGURE_DESC__CLASS, oldClass, class_));
            }
        }
        return class_;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass basicGetClass() {
        return class_;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setClass(EClass newClass) {
        EClass oldClass = class_;
        class_ = newClass;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DescPackage.CLASS_FIGURE_DESC__CLASS, oldClass, class_));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EReference getContainmentFeature() {
        if (containmentFeature != null && containmentFeature.eIsProxy()) {
            InternalEObject oldContainmentFeature = (InternalEObject) containmentFeature;
            containmentFeature = (EReference) eResolveProxy(oldContainmentFeature);
            if (containmentFeature != oldContainmentFeature) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE, oldContainmentFeature, containmentFeature));
            }
        }
        return containmentFeature;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EReference basicGetContainmentFeature() {
        return containmentFeature;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setContainmentFeature(EReference newContainmentFeature) {
        EReference oldContainmentFeature = containmentFeature;
        containmentFeature = newContainmentFeature;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE, oldContainmentFeature, containmentFeature));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DescPackage.CLASS_FIGURE_DESC__CLASS:
                if (resolve) return getClass_();
                return basicGetClass();
            case DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE:
                if (resolve) return getContainmentFeature();
                return basicGetContainmentFeature();
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
            case DescPackage.CLASS_FIGURE_DESC__CLASS:
                setClass((EClass) newValue);
                return;
            case DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE:
                setContainmentFeature((EReference) newValue);
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
            case DescPackage.CLASS_FIGURE_DESC__CLASS:
                setClass((EClass) null);
                return;
            case DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE:
                setContainmentFeature((EReference) null);
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
            case DescPackage.CLASS_FIGURE_DESC__CLASS:
                return class_ != null;
            case DescPackage.CLASS_FIGURE_DESC__CONTAINMENT_FEATURE:
                return containmentFeature != null;
        }
        return super.eIsSet(featureID);
    }
}
