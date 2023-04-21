package org.musicnotation.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.musicnotation.model.Duration;
import org.musicnotation.model.MusicNotationPackage;
import org.musicnotation.model.TimeSignature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Signature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.musicnotation.model.impl.TimeSignatureImpl#getNumberOfBeats <em>Number Of Beats</em>}</li>
 *   <li>{@link org.musicnotation.model.impl.TimeSignatureImpl#getBeatDuration <em>Beat Duration</em>}</li>
 *   <li>{@link org.musicnotation.model.impl.TimeSignatureImpl#isAbbreviated <em>Abbreviated</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TimeSignatureImpl extends MarkImpl implements TimeSignature {

    /**
	 * The default value of the '{@link #getNumberOfBeats() <em>Number Of Beats</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfBeats()
	 * @generated
	 * @ordered
	 */
    protected static final int NUMBER_OF_BEATS_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getNumberOfBeats() <em>Number Of Beats</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfBeats()
	 * @generated
	 * @ordered
	 */
    protected int numberOfBeats = NUMBER_OF_BEATS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getBeatDuration() <em>Beat Duration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeatDuration()
	 * @generated
	 * @ordered
	 */
    protected Duration beatDuration;

    /**
	 * The default value of the '{@link #isAbbreviated() <em>Abbreviated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbbreviated()
	 * @generated
	 * @ordered
	 */
    protected static final boolean ABBREVIATED_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isAbbreviated() <em>Abbreviated</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbbreviated()
	 * @generated
	 * @ordered
	 */
    protected boolean abbreviated = ABBREVIATED_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected TimeSignatureImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return MusicNotationPackage.Literals.TIME_SIGNATURE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getNumberOfBeats() {
        return numberOfBeats;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNumberOfBeats(int newNumberOfBeats) {
        int oldNumberOfBeats = numberOfBeats;
        numberOfBeats = newNumberOfBeats;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MusicNotationPackage.TIME_SIGNATURE__NUMBER_OF_BEATS, oldNumberOfBeats, numberOfBeats));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Duration getBeatDuration() {
        return beatDuration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetBeatDuration(Duration newBeatDuration, NotificationChain msgs) {
        Duration oldBeatDuration = beatDuration;
        beatDuration = newBeatDuration;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION, oldBeatDuration, newBeatDuration);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBeatDuration(Duration newBeatDuration) {
        if (newBeatDuration != beatDuration) {
            NotificationChain msgs = null;
            if (beatDuration != null) msgs = ((InternalEObject) beatDuration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION, null, msgs);
            if (newBeatDuration != null) msgs = ((InternalEObject) newBeatDuration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION, null, msgs);
            msgs = basicSetBeatDuration(newBeatDuration, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION, newBeatDuration, newBeatDuration));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isAbbreviated() {
        return abbreviated;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAbbreviated(boolean newAbbreviated) {
        boolean oldAbbreviated = abbreviated;
        abbreviated = newAbbreviated;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MusicNotationPackage.TIME_SIGNATURE__ABBREVIATED, oldAbbreviated, abbreviated));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION:
                return basicSetBeatDuration(null, msgs);
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
            case MusicNotationPackage.TIME_SIGNATURE__NUMBER_OF_BEATS:
                return getNumberOfBeats();
            case MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION:
                return getBeatDuration();
            case MusicNotationPackage.TIME_SIGNATURE__ABBREVIATED:
                return isAbbreviated();
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
            case MusicNotationPackage.TIME_SIGNATURE__NUMBER_OF_BEATS:
                setNumberOfBeats((Integer) newValue);
                return;
            case MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION:
                setBeatDuration((Duration) newValue);
                return;
            case MusicNotationPackage.TIME_SIGNATURE__ABBREVIATED:
                setAbbreviated((Boolean) newValue);
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
            case MusicNotationPackage.TIME_SIGNATURE__NUMBER_OF_BEATS:
                setNumberOfBeats(NUMBER_OF_BEATS_EDEFAULT);
                return;
            case MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION:
                setBeatDuration((Duration) null);
                return;
            case MusicNotationPackage.TIME_SIGNATURE__ABBREVIATED:
                setAbbreviated(ABBREVIATED_EDEFAULT);
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
            case MusicNotationPackage.TIME_SIGNATURE__NUMBER_OF_BEATS:
                return numberOfBeats != NUMBER_OF_BEATS_EDEFAULT;
            case MusicNotationPackage.TIME_SIGNATURE__BEAT_DURATION:
                return beatDuration != null;
            case MusicNotationPackage.TIME_SIGNATURE__ABBREVIATED:
                return abbreviated != ABBREVIATED_EDEFAULT;
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
        result.append(" (numberOfBeats: ");
        result.append(numberOfBeats);
        result.append(", abbreviated: ");
        result.append(abbreviated);
        result.append(')');
        return result.toString();
    }
}
