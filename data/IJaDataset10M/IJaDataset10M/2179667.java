package ru.ifmo.rain.astrans.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import ru.ifmo.rain.astrans.AstransPackage;
import ru.ifmo.rain.astrans.EClassifierReference;
import ru.ifmo.rain.astrans.TranslateReferences;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Translate References</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ru.ifmo.rain.astrans.impl.TranslateReferencesImpl#getModelReferenceTypeProto <em>Model Reference Type Proto</em>}</li>
 *   <li>{@link ru.ifmo.rain.astrans.impl.TranslateReferencesImpl#getTextualReferenceType <em>Textual Reference Type</em>}</li>
 *   <li>{@link ru.ifmo.rain.astrans.impl.TranslateReferencesImpl#isCrossReferencesOnly <em>Cross References Only</em>}</li>
 *   <li>{@link ru.ifmo.rain.astrans.impl.TranslateReferencesImpl#isIncludeDescendants <em>Include Descendants</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TranslateReferencesImpl extends ActionImpl implements TranslateReferences {

    /**
	 * The cached value of the '{@link #getModelReferenceTypeProto() <em>Model Reference Type Proto</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelReferenceTypeProto()
	 * @generated
	 * @ordered
	 */
    protected EClass modelReferenceTypeProto = null;

    /**
	 * The cached value of the '{@link #getTextualReferenceType() <em>Textual Reference Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTextualReferenceType()
	 * @generated
	 * @ordered
	 */
    protected EClassifierReference textualReferenceType = null;

    /**
	 * The default value of the '{@link #isCrossReferencesOnly() <em>Cross References Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCrossReferencesOnly()
	 * @generated
	 * @ordered
	 */
    protected static final boolean CROSS_REFERENCES_ONLY_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isCrossReferencesOnly() <em>Cross References Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCrossReferencesOnly()
	 * @generated
	 * @ordered
	 */
    protected boolean crossReferencesOnly = CROSS_REFERENCES_ONLY_EDEFAULT;

    /**
	 * The default value of the '{@link #isIncludeDescendants() <em>Include Descendants</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeDescendants()
	 * @generated
	 * @ordered
	 */
    protected static final boolean INCLUDE_DESCENDANTS_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isIncludeDescendants() <em>Include Descendants</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeDescendants()
	 * @generated
	 * @ordered
	 */
    protected boolean includeDescendants = INCLUDE_DESCENDANTS_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TranslateReferencesImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return AstransPackage.Literals.TRANSLATE_REFERENCES;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getModelReferenceTypeProto() {
        if (modelReferenceTypeProto != null && modelReferenceTypeProto.eIsProxy()) {
            InternalEObject oldModelReferenceTypeProto = (InternalEObject) modelReferenceTypeProto;
            modelReferenceTypeProto = (EClass) eResolveProxy(oldModelReferenceTypeProto);
            if (modelReferenceTypeProto != oldModelReferenceTypeProto) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO, oldModelReferenceTypeProto, modelReferenceTypeProto));
            }
        }
        return modelReferenceTypeProto;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass basicGetModelReferenceTypeProto() {
        return modelReferenceTypeProto;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setModelReferenceTypeProto(EClass newModelReferenceTypeProto) {
        EClass oldModelReferenceTypeProto = modelReferenceTypeProto;
        modelReferenceTypeProto = newModelReferenceTypeProto;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO, oldModelReferenceTypeProto, modelReferenceTypeProto));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClassifierReference getTextualReferenceType() {
        return textualReferenceType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTextualReferenceType(EClassifierReference newTextualReferenceType, NotificationChain msgs) {
        EClassifierReference oldTextualReferenceType = textualReferenceType;
        textualReferenceType = newTextualReferenceType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE, oldTextualReferenceType, newTextualReferenceType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTextualReferenceType(EClassifierReference newTextualReferenceType) {
        if (newTextualReferenceType != textualReferenceType) {
            NotificationChain msgs = null;
            if (textualReferenceType != null) msgs = ((InternalEObject) textualReferenceType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE, null, msgs);
            if (newTextualReferenceType != null) msgs = ((InternalEObject) newTextualReferenceType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE, null, msgs);
            msgs = basicSetTextualReferenceType(newTextualReferenceType, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE, newTextualReferenceType, newTextualReferenceType));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isCrossReferencesOnly() {
        return crossReferencesOnly;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCrossReferencesOnly(boolean newCrossReferencesOnly) {
        boolean oldCrossReferencesOnly = crossReferencesOnly;
        crossReferencesOnly = newCrossReferencesOnly;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AstransPackage.TRANSLATE_REFERENCES__CROSS_REFERENCES_ONLY, oldCrossReferencesOnly, crossReferencesOnly));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isIncludeDescendants() {
        return includeDescendants;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIncludeDescendants(boolean newIncludeDescendants) {
        boolean oldIncludeDescendants = includeDescendants;
        includeDescendants = newIncludeDescendants;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AstransPackage.TRANSLATE_REFERENCES__INCLUDE_DESCENDANTS, oldIncludeDescendants, includeDescendants));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE:
                return basicSetTextualReferenceType(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO:
                if (resolve) return getModelReferenceTypeProto();
                return basicGetModelReferenceTypeProto();
            case AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE:
                return getTextualReferenceType();
            case AstransPackage.TRANSLATE_REFERENCES__CROSS_REFERENCES_ONLY:
                return isCrossReferencesOnly() ? Boolean.TRUE : Boolean.FALSE;
            case AstransPackage.TRANSLATE_REFERENCES__INCLUDE_DESCENDANTS:
                return isIncludeDescendants() ? Boolean.TRUE : Boolean.FALSE;
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
            case AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO:
                setModelReferenceTypeProto((EClass) newValue);
                return;
            case AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE:
                setTextualReferenceType((EClassifierReference) newValue);
                return;
            case AstransPackage.TRANSLATE_REFERENCES__CROSS_REFERENCES_ONLY:
                setCrossReferencesOnly(((Boolean) newValue).booleanValue());
                return;
            case AstransPackage.TRANSLATE_REFERENCES__INCLUDE_DESCENDANTS:
                setIncludeDescendants(((Boolean) newValue).booleanValue());
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
            case AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO:
                setModelReferenceTypeProto((EClass) null);
                return;
            case AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE:
                setTextualReferenceType((EClassifierReference) null);
                return;
            case AstransPackage.TRANSLATE_REFERENCES__CROSS_REFERENCES_ONLY:
                setCrossReferencesOnly(CROSS_REFERENCES_ONLY_EDEFAULT);
                return;
            case AstransPackage.TRANSLATE_REFERENCES__INCLUDE_DESCENDANTS:
                setIncludeDescendants(INCLUDE_DESCENDANTS_EDEFAULT);
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
            case AstransPackage.TRANSLATE_REFERENCES__MODEL_REFERENCE_TYPE_PROTO:
                return modelReferenceTypeProto != null;
            case AstransPackage.TRANSLATE_REFERENCES__TEXTUAL_REFERENCE_TYPE:
                return textualReferenceType != null;
            case AstransPackage.TRANSLATE_REFERENCES__CROSS_REFERENCES_ONLY:
                return crossReferencesOnly != CROSS_REFERENCES_ONLY_EDEFAULT;
            case AstransPackage.TRANSLATE_REFERENCES__INCLUDE_DESCENDANTS:
                return includeDescendants != INCLUDE_DESCENDANTS_EDEFAULT;
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
        result.append(" (crossReferencesOnly: ");
        result.append(crossReferencesOnly);
        result.append(", includeDescendants: ");
        result.append(includeDescendants);
        result.append(')');
        return result.toString();
    }
}
