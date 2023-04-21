package com.safi.asterisk.actionstep.impl;

import org.asteriskjava.fastagi.AgiChannel;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.safi.asterisk.Call;
import com.safi.asterisk.actionstep.ActionstepPackage;
import com.safi.asterisk.actionstep.RecordFile;
import com.safi.core.actionstep.ActionStepException;
import com.safi.core.actionstep.DynamicValue;
import com.safi.core.actionstep.impl.ActionStepImpl;
import com.safi.core.call.CallConsumer1;
import com.safi.core.call.CallPackage;
import com.safi.core.call.SafiCall;
import com.safi.core.saflet.SafletContext;
import com.safi.db.VariableType;
import com.safi.db.util.VariableTranslator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Record File</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getCall1 <em>Call1</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#isBeep <em>Beep</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getEscapeDigits <em>Escape Digits</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getFile <em>File</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getOffset <em>Offset</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getTimeout <em>Timeout</em>}</li>
 *   <li>{@link com.safi.asterisk.actionstep.impl.RecordFileImpl#getMaxSilence <em>Max Silence</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RecordFileImpl extends AsteriskActionStepImpl implements RecordFile {

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
	 * The default value of the '{@link #isBeep() <em>Beep</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isBeep()
	 * @generated
	 * @ordered
	 */
    protected static final boolean BEEP_EDEFAULT = true;

    /**
	 * The cached value of the '{@link #isBeep() <em>Beep</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #isBeep()
	 * @generated
	 * @ordered
	 */
    protected boolean beep = BEEP_EDEFAULT;

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
	 * The cached value of the '{@link #getFile() <em>File</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getFile()
	 * @generated
	 * @ordered
	 */
    protected DynamicValue file;

    /**
	 * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
    protected static final String FORMAT_EDEFAULT = "wav";

    /**
	 * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
    protected String format = FORMAT_EDEFAULT;

    /**
	 * The default value of the '{@link #getOffset() <em>Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getOffset()
	 * @generated
	 * @ordered
	 */
    protected static final int OFFSET_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getOffset() <em>Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getOffset()
	 * @generated
	 * @ordered
	 */
    protected int offset = OFFSET_EDEFAULT;

    /**
	 * The default value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getTimeout()
	 * @generated
	 * @ordered
	 */
    protected static final long TIMEOUT_EDEFAULT = -1L;

    /**
	 * The cached value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getTimeout()
	 * @generated
	 * @ordered
	 */
    protected long timeout = TIMEOUT_EDEFAULT;

    /**
	 * The default value of the '{@link #getMaxSilence() <em>Max Silence</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getMaxSilence()
	 * @generated
	 * @ordered
	 */
    protected static final long MAX_SILENCE_EDEFAULT = 5L;

    /**
	 * The cached value of the '{@link #getMaxSilence() <em>Max Silence</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getMaxSilence()
	 * @generated
	 * @ordered
	 */
    protected long maxSilence = MAX_SILENCE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected RecordFileImpl() {
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
            Object dynValue = resolveDynamicValue(file, context);
            String filenameStr = (String) VariableTranslator.translateValue(VariableType.TEXT, dynValue);
            if (filenameStr == null || filenameStr.trim().equals("")) throw new ActionStepException("Filename was null");
            channel.recordFile(filenameStr, format, escapeDigits, (int) timeout, (int) offset, beep, (int) maxSilence);
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
        return ActionstepPackage.Literals.RECORD_FILE;
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
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActionstepPackage.RECORD_FILE__CALL1, oldCall1, call1));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__CALL1, oldCall1, call1));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isBeep() {
        return beep;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBeep(boolean newBeep) {
        boolean oldBeep = beep;
        beep = newBeep;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__BEEP, oldBeep, beep));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__ESCAPE_DIGITS, oldEscapeDigits, escapeDigits));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public DynamicValue getFile() {
        return file;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFile(DynamicValue newFile, NotificationChain msgs) {
        DynamicValue oldFile = file;
        file = newFile;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__FILE, oldFile, newFile);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFile(DynamicValue newFile) {
        if (newFile != file) {
            NotificationChain msgs = null;
            if (file != null) msgs = ((InternalEObject) file).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.RECORD_FILE__FILE, null, msgs);
            if (newFile != null) msgs = ((InternalEObject) newFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.RECORD_FILE__FILE, null, msgs);
            msgs = basicSetFile(newFile, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__FILE, newFile, newFile));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String getFormat() {
        return format;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__FORMAT, oldFormat, format));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public int getOffset() {
        return offset;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOffset(int newOffset) {
        int oldOffset = offset;
        offset = newOffset;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__OFFSET, oldOffset, offset));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public long getTimeout() {
        return timeout;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTimeout(long newTimeout) {
        long oldTimeout = timeout;
        timeout = newTimeout;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__TIMEOUT, oldTimeout, timeout));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public long getMaxSilence() {
        return maxSilence;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMaxSilence(long newMaxSilence) {
        long oldMaxSilence = maxSilence;
        maxSilence = newMaxSilence;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.RECORD_FILE__MAX_SILENCE, oldMaxSilence, maxSilence));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ActionstepPackage.RECORD_FILE__FILE:
                return basicSetFile(null, msgs);
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
            case ActionstepPackage.RECORD_FILE__CALL1:
                if (resolve) return getCall1();
                return basicGetCall1();
            case ActionstepPackage.RECORD_FILE__BEEP:
                return isBeep();
            case ActionstepPackage.RECORD_FILE__ESCAPE_DIGITS:
                return getEscapeDigits();
            case ActionstepPackage.RECORD_FILE__FILE:
                return getFile();
            case ActionstepPackage.RECORD_FILE__FORMAT:
                return getFormat();
            case ActionstepPackage.RECORD_FILE__OFFSET:
                return getOffset();
            case ActionstepPackage.RECORD_FILE__TIMEOUT:
                return getTimeout();
            case ActionstepPackage.RECORD_FILE__MAX_SILENCE:
                return getMaxSilence();
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
            case ActionstepPackage.RECORD_FILE__CALL1:
                setCall1((SafiCall) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__BEEP:
                setBeep((Boolean) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__ESCAPE_DIGITS:
                setEscapeDigits((String) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__FILE:
                setFile((DynamicValue) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__FORMAT:
                setFormat((String) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__OFFSET:
                setOffset((Integer) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__TIMEOUT:
                setTimeout((Long) newValue);
                return;
            case ActionstepPackage.RECORD_FILE__MAX_SILENCE:
                setMaxSilence((Long) newValue);
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
            case ActionstepPackage.RECORD_FILE__CALL1:
                setCall1((SafiCall) null);
                return;
            case ActionstepPackage.RECORD_FILE__BEEP:
                setBeep(BEEP_EDEFAULT);
                return;
            case ActionstepPackage.RECORD_FILE__ESCAPE_DIGITS:
                setEscapeDigits(ESCAPE_DIGITS_EDEFAULT);
                return;
            case ActionstepPackage.RECORD_FILE__FILE:
                setFile((DynamicValue) null);
                return;
            case ActionstepPackage.RECORD_FILE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case ActionstepPackage.RECORD_FILE__OFFSET:
                setOffset(OFFSET_EDEFAULT);
                return;
            case ActionstepPackage.RECORD_FILE__TIMEOUT:
                setTimeout(TIMEOUT_EDEFAULT);
                return;
            case ActionstepPackage.RECORD_FILE__MAX_SILENCE:
                setMaxSilence(MAX_SILENCE_EDEFAULT);
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
            case ActionstepPackage.RECORD_FILE__CALL1:
                return call1 != null;
            case ActionstepPackage.RECORD_FILE__BEEP:
                return beep != BEEP_EDEFAULT;
            case ActionstepPackage.RECORD_FILE__ESCAPE_DIGITS:
                return ESCAPE_DIGITS_EDEFAULT == null ? escapeDigits != null : !ESCAPE_DIGITS_EDEFAULT.equals(escapeDigits);
            case ActionstepPackage.RECORD_FILE__FILE:
                return file != null;
            case ActionstepPackage.RECORD_FILE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case ActionstepPackage.RECORD_FILE__OFFSET:
                return offset != OFFSET_EDEFAULT;
            case ActionstepPackage.RECORD_FILE__TIMEOUT:
                return timeout != TIMEOUT_EDEFAULT;
            case ActionstepPackage.RECORD_FILE__MAX_SILENCE:
                return maxSilence != MAX_SILENCE_EDEFAULT;
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
                case ActionstepPackage.RECORD_FILE__CALL1:
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
                    return ActionstepPackage.RECORD_FILE__CALL1;
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
        result.append(" (beep: ");
        result.append(beep);
        result.append(", escapeDigits: ");
        result.append(escapeDigits);
        result.append(", format: ");
        result.append(format);
        result.append(", offset: ");
        result.append(offset);
        result.append(", timeout: ");
        result.append(timeout);
        result.append(", maxSilence: ");
        result.append(maxSilence);
        result.append(')');
        return result.toString();
    }
}
