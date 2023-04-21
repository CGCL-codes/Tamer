package fi.jab.esb.config.xml.impl;

import fi.jab.esb.config.xml.SqlListenerType;
import fi.jab.esb.config.xml.SqlMessageFilterType;
import fi.jab.esb.config.xml.XmlPackage;
import java.math.BigInteger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sql Listener Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlListenerTypeImpl#getSqlMessageFilter <em>Sql Message Filter</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.impl.SqlListenerTypeImpl#getPollFrequencySeconds <em>Poll Frequency Seconds</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SqlListenerTypeImpl extends ListenerImpl implements SqlListenerType {

    /**
   * The cached value of the '{@link #getSqlMessageFilter() <em>Sql Message Filter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSqlMessageFilter()
   * @generated
   * @ordered
   */
    protected SqlMessageFilterType sqlMessageFilter;

    /**
   * The default value of the '{@link #getPollFrequencySeconds() <em>Poll Frequency Seconds</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPollFrequencySeconds()
   * @generated
   * @ordered
   */
    protected static final BigInteger POLL_FREQUENCY_SECONDS_EDEFAULT = new BigInteger("10");

    /**
   * The cached value of the '{@link #getPollFrequencySeconds() <em>Poll Frequency Seconds</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPollFrequencySeconds()
   * @generated
   * @ordered
   */
    protected BigInteger pollFrequencySeconds = POLL_FREQUENCY_SECONDS_EDEFAULT;

    /**
   * This is true if the Poll Frequency Seconds attribute has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    protected boolean pollFrequencySecondsESet;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected SqlListenerTypeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.SQL_LISTENER_TYPE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public SqlMessageFilterType getSqlMessageFilter() {
        return sqlMessageFilter;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetSqlMessageFilter(SqlMessageFilterType newSqlMessageFilter, NotificationChain msgs) {
        SqlMessageFilterType oldSqlMessageFilter = sqlMessageFilter;
        sqlMessageFilter = newSqlMessageFilter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER, oldSqlMessageFilter, newSqlMessageFilter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setSqlMessageFilter(SqlMessageFilterType newSqlMessageFilter) {
        if (newSqlMessageFilter != sqlMessageFilter) {
            NotificationChain msgs = null;
            if (sqlMessageFilter != null) msgs = ((InternalEObject) sqlMessageFilter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER, null, msgs);
            if (newSqlMessageFilter != null) msgs = ((InternalEObject) newSqlMessageFilter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER, null, msgs);
            msgs = basicSetSqlMessageFilter(newSqlMessageFilter, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER, newSqlMessageFilter, newSqlMessageFilter));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public BigInteger getPollFrequencySeconds() {
        return pollFrequencySeconds;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setPollFrequencySeconds(BigInteger newPollFrequencySeconds) {
        BigInteger oldPollFrequencySeconds = pollFrequencySeconds;
        pollFrequencySeconds = newPollFrequencySeconds;
        boolean oldPollFrequencySecondsESet = pollFrequencySecondsESet;
        pollFrequencySecondsESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS, oldPollFrequencySeconds, pollFrequencySeconds, !oldPollFrequencySecondsESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void unsetPollFrequencySeconds() {
        BigInteger oldPollFrequencySeconds = pollFrequencySeconds;
        boolean oldPollFrequencySecondsESet = pollFrequencySecondsESet;
        pollFrequencySeconds = POLL_FREQUENCY_SECONDS_EDEFAULT;
        pollFrequencySecondsESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS, oldPollFrequencySeconds, POLL_FREQUENCY_SECONDS_EDEFAULT, oldPollFrequencySecondsESet));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isSetPollFrequencySeconds() {
        return pollFrequencySecondsESet;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER:
                return basicSetSqlMessageFilter(null, msgs);
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
            case XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER:
                return getSqlMessageFilter();
            case XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS:
                return getPollFrequencySeconds();
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
            case XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER:
                setSqlMessageFilter((SqlMessageFilterType) newValue);
                return;
            case XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS:
                setPollFrequencySeconds((BigInteger) newValue);
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
            case XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER:
                setSqlMessageFilter((SqlMessageFilterType) null);
                return;
            case XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS:
                unsetPollFrequencySeconds();
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
            case XmlPackage.SQL_LISTENER_TYPE__SQL_MESSAGE_FILTER:
                return sqlMessageFilter != null;
            case XmlPackage.SQL_LISTENER_TYPE__POLL_FREQUENCY_SECONDS:
                return isSetPollFrequencySeconds();
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
        result.append(" (pollFrequencySeconds: ");
        if (pollFrequencySecondsESet) result.append(pollFrequencySeconds); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
