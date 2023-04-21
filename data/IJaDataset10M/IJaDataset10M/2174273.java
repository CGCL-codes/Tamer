package com.safi.asterisk.actionstep.impl;

import org.asteriskjava.fastagi.AgiChannel;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.safi.asterisk.Call;
import com.safi.asterisk.actionstep.ActionstepPackage;
import com.safi.asterisk.actionstep.SayPhonetic;
import com.safi.asterisk.saflet.AsteriskSafletContext;
import com.safi.core.actionstep.ActionStepException;
import com.safi.core.actionstep.DynamicValue;
import com.safi.core.actionstep.impl.ActionStepImpl;
import com.safi.core.call.CallConsumer1;
import com.safi.core.call.CallPackage;
import com.safi.core.call.SafiCall;
import com.safi.core.saflet.SafletContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Say Phonetic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.asterisk.actionstep.impl.SayPhoneticImpl#getCall1 <em>Call1</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.SayPhoneticImpl#getEscapeDigits <em>Escape Digits</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.SayPhoneticImpl#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SayPhoneticImpl extends AsteriskActionStepImpl implements SayPhonetic {

    /**
	 * The cached value of the '{@link #getCall1() <em>Call1</em>}' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCall1()
	 * @generated
	 * @ordered
	 */
    protected SafiCall call1;

    /**
	 * The default value of the '{@link #getEscapeDigits() <em>Escape Digits</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getEscapeDigits()
	 * @generated
	 * @ordered
	 */
    protected static final String ESCAPE_DIGITS_EDEFAULT = "#";

    /**
	 * The cached value of the '{@link #getEscapeDigits() <em>Escape Digits</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getEscapeDigits()
	 * @generated
	 * @ordered
	 */
    protected String escapeDigits = ESCAPE_DIGITS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getText() <em>Text</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
    protected DynamicValue text;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected SayPhoneticImpl() {
        super();
    }

    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        if (call1 == null) {
            handleException(context, new ActionStepException("No current call found"));
            return;
        } else if (!(call1 instanceof Call)) {
            handleException(context, new ActionStepException("Call isn't isn't an Asterisk call: " + call1.getClass().getName()));
            return;
        }
        if (((Call) call1).getChannel() == null) {
            handleException(context, new ActionStepException("No channel found in current context"));
            return;
        }
        AgiChannel channel = ((Call) call1).getChannel();
        try {
            Object result = resolveDynamicValue(text, context);
            if (result == null) result = "";
            char c = channel.sayPhonetic(result.toString(), escapeDigits);
            if (c != 0) {
                String digitPressed = String.valueOf(c);
                ((AsteriskSafletContext) context).appendBufferedDigits(digitPressed);
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ActionstepPackage.Literals.SAY_PHONETIC;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafiCall getCall1() {
        if (call1 != null && call1.eIsProxy()) {
            InternalEObject oldCall1 = (InternalEObject) call1;
            call1 = (SafiCall) eResolveProxy(oldCall1);
            if (call1 != oldCall1) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActionstepPackage.SAY_PHONETIC__CALL1, oldCall1, call1));
            }
        }
        return call1;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafiCall basicGetCall1() {
        return call1;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCall1(SafiCall newCall1) {
        SafiCall oldCall1 = call1;
        call1 = newCall1;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SAY_PHONETIC__CALL1, oldCall1, call1));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String getEscapeDigits() {
        return escapeDigits;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEscapeDigits(String newEscapeDigits) {
        String oldEscapeDigits = escapeDigits;
        escapeDigits = newEscapeDigits;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SAY_PHONETIC__ESCAPE_DIGITS, oldEscapeDigits, escapeDigits));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public DynamicValue getText() {
        return text;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetText(DynamicValue newText, NotificationChain msgs) {
        DynamicValue oldText = text;
        text = newText;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.SAY_PHONETIC__TEXT, oldText, newText);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setText(DynamicValue newText) {
        if (newText != text) {
            NotificationChain msgs = null;
            if (text != null) msgs = ((InternalEObject) text).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SAY_PHONETIC__TEXT, null, msgs);
            if (newText != null) msgs = ((InternalEObject) newText).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.SAY_PHONETIC__TEXT, null, msgs);
            msgs = basicSetText(newText, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.SAY_PHONETIC__TEXT, newText, newText));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ActionstepPackage.SAY_PHONETIC__TEXT:
                return basicSetText(null, msgs);
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
            case ActionstepPackage.SAY_PHONETIC__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.SAY_PHONETIC__ESCAPE_DIGITS:
                return getEscapeDigits();
            case ActionstepPackage.SAY_PHONETIC__TEXT:
                return getText();
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
            case ActionstepPackage.SAY_PHONETIC__CALL1:
                setCall1((SafiCall) newValue);
                return;
            case ActionstepPackage.SAY_PHONETIC__ESCAPE_DIGITS:
                setEscapeDigits((String) newValue);
                return;
            case ActionstepPackage.SAY_PHONETIC__TEXT:
                setText((DynamicValue) newValue);
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
            case ActionstepPackage.SAY_PHONETIC__CALL1:
                setCall1((SafiCall) null);
                return;
            case ActionstepPackage.SAY_PHONETIC__ESCAPE_DIGITS:
                setEscapeDigits(ESCAPE_DIGITS_EDEFAULT);
                return;
            case ActionstepPackage.SAY_PHONETIC__TEXT:
                setText((DynamicValue) null);
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
            case ActionstepPackage.SAY_PHONETIC__CALL1:
                return call1 != null;
            case ActionstepPackage.SAY_PHONETIC__ESCAPE_DIGITS:
                return ESCAPE_DIGITS_EDEFAULT == null ? escapeDigits != null : !ESCAPE_DIGITS_EDEFAULT.equals(escapeDigits);
            case ActionstepPackage.SAY_PHONETIC__TEXT:
                return text != null;
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
        if (baseClass == CallConsumer1.class) {
            switch(derivedFeatureID) {
                case ActionstepPackage.SAY_PHONETIC__CALL1:
                    return CallPackage.CALL_CONSUMER1__CALL1;
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
        if (baseClass == CallConsumer1.class) {
            switch(baseFeatureID) {
                case CallPackage.CALL_CONSUMER1__CALL1:
                    return ActionstepPackage.SAY_PHONETIC__CALL1;
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
        result.append(" (escapeDigits: ");
        result.append(escapeDigits);
        result.append(')');
        return result.toString();
    }
}
