package hub.metrik.lang.eprovide.trace.impl;

import hub.metrik.lang.eprovide.trace.MultipleFeatureAdd;
import hub.metrik.lang.eprovide.trace.TracePackage;
import hub.metrik.lang.eprovide.trace.Value;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Multiple Feature Add</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.metrik.lang.eprovide.trace.impl.MultipleFeatureAddImpl#getValue <em>Value</em>}</li>
 *   <li>{@link hub.metrik.lang.eprovide.trace.impl.MultipleFeatureAddImpl#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleFeatureAddImpl extends MultipleFeatureChangeImpl implements MultipleFeatureAdd {

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected Value value;

    /**
	 * The default value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
    protected static final int TO_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
    protected int to = TO_EDEFAULT;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected MultipleFeatureAddImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TracePackage.Literals.MULTIPLE_FEATURE_ADD;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public Value getValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValue(Value newValue, NotificationChain msgs) {
        Value oldValue = value;
        value = newValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.MULTIPLE_FEATURE_ADD__VALUE, oldValue, newValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(Value newValue) {
        if (newValue != value) {
            NotificationChain msgs = null;
            if (value != null) msgs = ((InternalEObject) value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.MULTIPLE_FEATURE_ADD__VALUE, null, msgs);
            if (newValue != null) msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.MULTIPLE_FEATURE_ADD__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.MULTIPLE_FEATURE_ADD__VALUE, newValue, newValue));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public int getTo() {
        return to;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setTo(int newTo) {
        int oldTo = to;
        to = newTo;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.MULTIPLE_FEATURE_ADD__TO, oldTo, to));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TracePackage.MULTIPLE_FEATURE_ADD__VALUE:
                return basicSetValue(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TracePackage.MULTIPLE_FEATURE_ADD__VALUE:
                return getValue();
            case TracePackage.MULTIPLE_FEATURE_ADD__TO:
                return new Integer(getTo());
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case TracePackage.MULTIPLE_FEATURE_ADD__VALUE:
                setValue((Value) newValue);
                return;
            case TracePackage.MULTIPLE_FEATURE_ADD__TO:
                setTo(((Integer) newValue).intValue());
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
            case TracePackage.MULTIPLE_FEATURE_ADD__VALUE:
                setValue((Value) null);
                return;
            case TracePackage.MULTIPLE_FEATURE_ADD__TO:
                setTo(TO_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case TracePackage.MULTIPLE_FEATURE_ADD__VALUE:
                return value != null;
            case TracePackage.MULTIPLE_FEATURE_ADD__TO:
                return to != TO_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (to: ");
        result.append(to);
        result.append(')');
        return result.toString();
    }

    /**
	 * @see hub.metrik.lang.eprovide.trace.impl.ChangeImpl#replay(java.util.Map)
	 * @generated NOT
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void replay(Map<EObject, EObject> correspondences) {
        EList list = (EList) correspondences.get(getTargetObject()).eGet(getFeature());
        list.add(getTo(), resolveValue(correspondences, getValue()));
    }
}
