package net.sf.smbt.i2c.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import net.sf.smbt.i2c.I2CGRDPin;
import net.sf.smbt.i2c.I2CGRDWire;
import net.sf.smbt.i2c.I2cPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>I2CGRD Pin</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.smbt.i2c.impl.I2CGRDPinImpl#getGrdConnection <em>Grd Connection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class I2CGRDPinImpl extends AbstractI2CConnectorImpl implements I2CGRDPin {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected I2CGRDPinImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return I2cPackage.Literals.I2CGRD_PIN;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public I2CGRDWire getGrdConnection() {
        return (I2CGRDWire) eGet(I2cPackage.Literals.I2CGRD_PIN__GRD_CONNECTION, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGrdConnection(I2CGRDWire newGrdConnection) {
        eSet(I2cPackage.Literals.I2CGRD_PIN__GRD_CONNECTION, newGrdConnection);
    }
}
