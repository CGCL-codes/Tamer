package org.isistan.flabot.edit.editormodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.isistan.flabot.edit.editormodel.EditormodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EString To EObject Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.isistan.flabot.edit.editormodel.impl.EStringToEObjectMapEntryImpl#getTypedKey <em>Key</em>}</li>
 *   <li>{@link org.isistan.flabot.edit.editormodel.impl.EStringToEObjectMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EStringToEObjectMapEntryImpl extends EObjectImpl implements BasicEMap.Entry {

    /**
	 * The default value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
    protected static final String KEY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
    protected String key = KEY_EDEFAULT;

    /**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
    protected EObject value = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EStringToEObjectMapEntryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return EditormodelPackage.eINSTANCE.getEStringToEObjectMapEntry();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTypedKey() {
        return key;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypedKey(String newKey) {
        String oldKey = key;
        key = newKey;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__KEY, oldKey, key));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getTypedValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTypedValue(EObject newValue, NotificationChain msgs) {
        EObject oldValue = value;
        value = newValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE, oldValue, newValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypedValue(EObject newValue) {
        if (newValue != value) {
            NotificationChain msgs = null;
            if (value != null) msgs = ((InternalEObject) value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE, null, msgs);
            if (newValue != null) msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE, null, msgs);
            msgs = basicSetTypedValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE, newValue, newValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
        if (featureID >= 0) {
            switch(eDerivedStructuralFeatureID(featureID, baseClass)) {
                case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE:
                    return basicSetTypedValue(null, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(EStructuralFeature eFeature, boolean resolve) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__KEY:
                return getTypedKey();
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE:
                return getTypedValue();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(EStructuralFeature eFeature, Object newValue) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__KEY:
                setTypedKey((String) newValue);
                return;
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE:
                setTypedValue((EObject) newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__KEY:
                setTypedKey(KEY_EDEFAULT);
                return;
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE:
                setTypedValue((EObject) null);
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(EStructuralFeature eFeature) {
        switch(eDerivedStructuralFeatureID(eFeature)) {
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__KEY:
                return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
            case EditormodelPackage.ESTRING_TO_EOBJECT_MAP_ENTRY__VALUE:
                return value != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (key: ");
        result.append(key);
        result.append(')');
        return result.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected int hash = -1;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getHash() {
        if (hash == -1) {
            Object theKey = getKey();
            hash = (theKey == null ? 0 : theKey.hashCode());
        }
        return hash;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHash(int hash) {
        this.hash = hash;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getKey() {
        return getTypedKey();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setKey(Object key) {
        setTypedKey((String) key);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getValue() {
        return getTypedValue();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object setValue(Object value) {
        Object oldValue = getValue();
        setTypedValue((EObject) value);
        return oldValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EMap getEMap() {
        EObject container = eContainer();
        return container == null ? null : (EMap) container.eGet(eContainmentFeature());
    }
}
