package com.maschinenstuermer.profiler.trace.impl;

import com.maschinenstuermer.profiler.trace.Metrics;
import com.maschinenstuermer.profiler.trace.ProfiledMethod;
import com.maschinenstuermer.profiler.trace.TracePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Profiled Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.maschinenstuermer.profiler.trace.impl.ProfiledMethodImpl#getMetrics <em>Metrics</em>}</li>
 *   <li>{@link com.maschinenstuermer.profiler.trace.impl.ProfiledMethodImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfiledMethodImpl extends EObjectImpl implements ProfiledMethod {

    /**
	 * The cached value of the '{@link #getMetrics() <em>Metrics</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetrics()
	 * @generated
	 * @ordered
	 */
    protected Metrics metrics;

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ProfiledMethodImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TracePackage.Literals.PROFILED_METHOD;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMetrics(Metrics newMetrics, NotificationChain msgs) {
        Metrics oldMetrics = metrics;
        metrics = newMetrics;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.PROFILED_METHOD__METRICS, oldMetrics, newMetrics);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetrics(Metrics newMetrics) {
        if (newMetrics != metrics) {
            NotificationChain msgs = null;
            if (metrics != null) msgs = ((InternalEObject) metrics).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.PROFILED_METHOD__METRICS, null, msgs);
            if (newMetrics != null) msgs = ((InternalEObject) newMetrics).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.PROFILED_METHOD__METRICS, null, msgs);
            msgs = basicSetMetrics(newMetrics, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.PROFILED_METHOD__METRICS, newMetrics, newMetrics));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.PROFILED_METHOD__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TracePackage.PROFILED_METHOD__METRICS:
                return basicSetMetrics(null, msgs);
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
            case TracePackage.PROFILED_METHOD__METRICS:
                return getMetrics();
            case TracePackage.PROFILED_METHOD__NAME:
                return getName();
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
            case TracePackage.PROFILED_METHOD__METRICS:
                setMetrics((Metrics) newValue);
                return;
            case TracePackage.PROFILED_METHOD__NAME:
                setName((String) newValue);
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
            case TracePackage.PROFILED_METHOD__METRICS:
                setMetrics((Metrics) null);
                return;
            case TracePackage.PROFILED_METHOD__NAME:
                setName(NAME_EDEFAULT);
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
            case TracePackage.PROFILED_METHOD__METRICS:
                return metrics != null;
            case TracePackage.PROFILED_METHOD__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }
}
