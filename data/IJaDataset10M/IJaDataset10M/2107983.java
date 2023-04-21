package ru.ifmo.rain.astrans.trace.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import ru.ifmo.rain.astrans.trace.ReferenceMapping;
import ru.ifmo.rain.astrans.trace.ReferenceMappingType;
import ru.ifmo.rain.astrans.trace.TracePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ru.ifmo.rain.astrans.trace.impl.ReferenceMappingImpl#getProto <em>Proto</em>}</li>
 *   <li>{@link ru.ifmo.rain.astrans.trace.impl.ReferenceMappingImpl#getImage <em>Image</em>}</li>
 *   <li>{@link ru.ifmo.rain.astrans.trace.impl.ReferenceMappingImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceMappingImpl extends EObjectImpl implements ReferenceMapping {

    /**
	 * The cached value of the '{@link #getProto() <em>Proto</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProto()
	 * @generated
	 * @ordered
	 */
    protected EReference proto = null;

    /**
	 * The cached value of the '{@link #getImage() <em>Image</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImage()
	 * @generated
	 * @ordered
	 */
    protected EStructuralFeature image = null;

    /**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final ReferenceMappingType TYPE_EDEFAULT = ReferenceMappingType.NONE_LITERAL;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected ReferenceMappingType type = TYPE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ReferenceMappingImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return TracePackage.Literals.REFERENCE_MAPPING;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getProto() {
        if (proto != null && proto.eIsProxy()) {
            InternalEObject oldProto = (InternalEObject) proto;
            proto = (EReference) eResolveProxy(oldProto);
            if (proto != oldProto) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.REFERENCE_MAPPING__PROTO, oldProto, proto));
            }
        }
        return proto;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference basicGetProto() {
        return proto;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProto(EReference newProto) {
        EReference oldProto = proto;
        proto = newProto;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.REFERENCE_MAPPING__PROTO, oldProto, proto));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EStructuralFeature getImage() {
        if (image != null && image.eIsProxy()) {
            InternalEObject oldImage = (InternalEObject) image;
            image = (EStructuralFeature) eResolveProxy(oldImage);
            if (image != oldImage) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.REFERENCE_MAPPING__IMAGE, oldImage, image));
            }
        }
        return image;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EStructuralFeature basicGetImage() {
        return image;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setImage(EStructuralFeature newImage) {
        EStructuralFeature oldImage = image;
        image = newImage;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.REFERENCE_MAPPING__IMAGE, oldImage, image));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ReferenceMappingType getType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(ReferenceMappingType newType) {
        ReferenceMappingType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.REFERENCE_MAPPING__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TracePackage.REFERENCE_MAPPING__PROTO:
                if (resolve) return getProto();
                return basicGetProto();
            case TracePackage.REFERENCE_MAPPING__IMAGE:
                if (resolve) return getImage();
                return basicGetImage();
            case TracePackage.REFERENCE_MAPPING__TYPE:
                return getType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case TracePackage.REFERENCE_MAPPING__PROTO:
                setProto((EReference) newValue);
                return;
            case TracePackage.REFERENCE_MAPPING__IMAGE:
                setImage((EStructuralFeature) newValue);
                return;
            case TracePackage.REFERENCE_MAPPING__TYPE:
                setType((ReferenceMappingType) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case TracePackage.REFERENCE_MAPPING__PROTO:
                setProto((EReference) null);
                return;
            case TracePackage.REFERENCE_MAPPING__IMAGE:
                setImage((EStructuralFeature) null);
                return;
            case TracePackage.REFERENCE_MAPPING__TYPE:
                setType(TYPE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case TracePackage.REFERENCE_MAPPING__PROTO:
                return proto != null;
            case TracePackage.REFERENCE_MAPPING__IMAGE:
                return image != null;
            case TracePackage.REFERENCE_MAPPING__TYPE:
                return type != TYPE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }
}
