package se.mdh.mrtc.saveccm.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import se.mdh.mrtc.saveccm.Delay;
import se.mdh.mrtc.saveccm.SaveccmPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delay</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link se.mdh.mrtc.saveccm.impl.DelayImpl#getPrecision <em>Precision</em>}</li>
 *   <li>{@link se.mdh.mrtc.saveccm.impl.DelayImpl#getDelay <em>Delay</em>}</li>
 *   <li>{@link se.mdh.mrtc.saveccm.impl.DelayImpl#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DelayImpl extends ElementImpl implements Delay {

    /**
	 * The default value of the '{@link #getPrecision() <em>Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrecision()
	 * @generated
	 * @ordered
	 */
    protected static final float PRECISION_EDEFAULT = 0.0F;

    /**
	 * The cached value of the '{@link #getPrecision() <em>Precision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrecision()
	 * @generated
	 * @ordered
	 */
    protected float precision = PRECISION_EDEFAULT;

    /**
	 * The default value of the '{@link #getDelay() <em>Delay</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelay()
	 * @generated
	 * @ordered
	 */
    protected static final float DELAY_EDEFAULT = 0.0F;

    /**
	 * The cached value of the '{@link #getDelay() <em>Delay</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDelay()
	 * @generated
	 * @ordered
	 */
    protected float delay = DELAY_EDEFAULT;

    /**
	 * The default value of the '{@link #getBody() <em>Body</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBody()
	 * @generated
	 * @ordered
	 */
    protected static final String BODY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getBody() <em>Body</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBody()
	 * @generated
	 * @ordered
	 */
    protected String body = BODY_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DelayImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SaveccmPackage.Literals.DELAY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public float getPrecision() {
        return precision;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPrecision(float newPrecision) {
        float oldPrecision = precision;
        precision = newPrecision;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SaveccmPackage.DELAY__PRECISION, oldPrecision, precision));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public float getDelay() {
        return delay;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDelay(float newDelay) {
        float oldDelay = delay;
        delay = newDelay;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SaveccmPackage.DELAY__DELAY, oldDelay, delay));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getBody() {
        return body;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBody(String newBody) {
        String oldBody = body;
        body = newBody;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SaveccmPackage.DELAY__BODY, oldBody, body));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SaveccmPackage.DELAY__PRECISION:
                return new Float(getPrecision());
            case SaveccmPackage.DELAY__DELAY:
                return new Float(getDelay());
            case SaveccmPackage.DELAY__BODY:
                return getBody();
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
            case SaveccmPackage.DELAY__PRECISION:
                setPrecision(((Float) newValue).floatValue());
                return;
            case SaveccmPackage.DELAY__DELAY:
                setDelay(((Float) newValue).floatValue());
                return;
            case SaveccmPackage.DELAY__BODY:
                setBody((String) newValue);
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
            case SaveccmPackage.DELAY__PRECISION:
                setPrecision(PRECISION_EDEFAULT);
                return;
            case SaveccmPackage.DELAY__DELAY:
                setDelay(DELAY_EDEFAULT);
                return;
            case SaveccmPackage.DELAY__BODY:
                setBody(BODY_EDEFAULT);
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
            case SaveccmPackage.DELAY__PRECISION:
                return precision != PRECISION_EDEFAULT;
            case SaveccmPackage.DELAY__DELAY:
                return delay != DELAY_EDEFAULT;
            case SaveccmPackage.DELAY__BODY:
                return BODY_EDEFAULT == null ? body != null : !BODY_EDEFAULT.equals(body);
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
        result.append(" (precision: ");
        result.append(precision);
        result.append(", delay: ");
        result.append(delay);
        result.append(", body: ");
        result.append(body);
        result.append(')');
        return result.toString();
    }
}
