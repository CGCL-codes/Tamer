package Shop.impl;

import Shop.ShopPackage;
import Shop.UPSCarrier;
import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UPS Carrier</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Shop.impl.UPSCarrierImpl#getDeliveryTime <em>Delivery Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UPSCarrierImpl extends CarrierBaseImpl implements UPSCarrier {

    /**
	 * The default value of the '{@link #getDeliveryTime() <em>Delivery Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryTime()
	 * @generated
	 * @ordered
	 */
    protected static final Date DELIVERY_TIME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDeliveryTime() <em>Delivery Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryTime()
	 * @generated
	 * @ordered
	 */
    protected Date deliveryTime = DELIVERY_TIME_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected UPSCarrierImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ShopPackage.Literals.UPS_CARRIER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDeliveryTime(Date newDeliveryTime) {
        Date oldDeliveryTime = deliveryTime;
        deliveryTime = newDeliveryTime;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ShopPackage.UPS_CARRIER__DELIVERY_TIME, oldDeliveryTime, deliveryTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ShopPackage.UPS_CARRIER__DELIVERY_TIME:
                return getDeliveryTime();
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
            case ShopPackage.UPS_CARRIER__DELIVERY_TIME:
                setDeliveryTime((Date) newValue);
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
            case ShopPackage.UPS_CARRIER__DELIVERY_TIME:
                setDeliveryTime(DELIVERY_TIME_EDEFAULT);
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
            case ShopPackage.UPS_CARRIER__DELIVERY_TIME:
                return DELIVERY_TIME_EDEFAULT == null ? deliveryTime != null : !DELIVERY_TIME_EDEFAULT.equals(deliveryTime);
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
        result.append(" (deliveryTime: ");
        result.append(deliveryTime);
        result.append(')');
        return result.toString();
    }
}
