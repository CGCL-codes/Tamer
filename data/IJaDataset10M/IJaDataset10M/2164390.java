package net.sf.smbt.i2c.devices.smart.impl;

import java.util.Collection;
import net.sf.smbt.i2c.I2CBusSegment;
import net.sf.smbt.i2c.I2CDevice;
import net.sf.smbt.i2c.devices.smart.SmartModule;
import net.sf.smbt.i2c.devices.smart.SmartObject;
import net.sf.smbt.i2c.devices.smart.SmartPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getAddr <em>Addr</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getDevices <em>Devices</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getId <em>Id</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getSmartObject <em>Smart Object</em>}</li>
 *   <li>{@link net.sf.smbt.i2c.devices.smart.impl.SmartModuleImpl#getSegment <em>Segment</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SmartModuleImpl extends CDOObjectImpl implements SmartModule {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SmartModuleImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SmartPackage.Literals.SMART_MODULE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getAddr() {
        return (Integer) eGet(SmartPackage.Literals.SMART_MODULE__ADDR, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAddr(int newAddr) {
        eSet(SmartPackage.Literals.SMART_MODULE__ADDR, newAddr);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return (String) eGet(SmartPackage.Literals.SMART_MODULE__NAME, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        eSet(SmartPackage.Literals.SMART_MODULE__NAME, newName);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SmartObject getSmartObject() {
        return (SmartObject) eGet(SmartPackage.Literals.SMART_MODULE__SMART_OBJECT, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSmartObject(SmartObject newSmartObject) {
        eSet(SmartPackage.Literals.SMART_MODULE__SMART_OBJECT, newSmartObject);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public I2CBusSegment getSegment() {
        return (I2CBusSegment) eGet(SmartPackage.Literals.SMART_MODULE__SEGMENT, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSegment(I2CBusSegment newSegment) {
        eSet(SmartPackage.Literals.SMART_MODULE__SEGMENT, newSegment);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public EList<I2CDevice> getDevices() {
        return (EList<I2CDevice>) eGet(SmartPackage.Literals.SMART_MODULE__DEVICES, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public EList<SmartModule> getChildren() {
        return (EList<SmartModule>) eGet(SmartPackage.Literals.SMART_MODULE__CHILDREN, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SmartModule getParent() {
        return (SmartModule) eGet(SmartPackage.Literals.SMART_MODULE__PARENT, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setParent(SmartModule newParent) {
        eSet(SmartPackage.Literals.SMART_MODULE__PARENT, newParent);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
        return (String) eGet(SmartPackage.Literals.SMART_MODULE__ID, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
        eSet(SmartPackage.Literals.SMART_MODULE__ID, newId);
    }
}
