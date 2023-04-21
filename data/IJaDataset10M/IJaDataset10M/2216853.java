package net.sf.smbt.i2c.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import net.sf.smbt.i2c.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.sf.smbt.i2c.I2cPackage
 * @generated
 */
public class I2cAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static I2cPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public I2cAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = I2cPackage.eINSTANCE;
        }
    }

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected I2cSwitch<Adapter> modelSwitch = new I2cSwitch<Adapter>() {

        @Override
        public Adapter caseI2CBusSegment(I2CBusSegment object) {
            return createI2CBusSegmentAdapter();
        }

        @Override
        public Adapter caseAbstractI2CWire(AbstractI2CWire object) {
            return createAbstractI2CWireAdapter();
        }

        @Override
        public Adapter caseI2CSCLWire(I2CSCLWire object) {
            return createI2CSCLWireAdapter();
        }

        @Override
        public Adapter caseI2CSDAWire(I2CSDAWire object) {
            return createI2CSDAWireAdapter();
        }

        @Override
        public Adapter caseI2CGRDWire(I2CGRDWire object) {
            return createI2CGRDWireAdapter();
        }

        @Override
        public Adapter caseI2CPWRWire(I2CPWRWire object) {
            return createI2CPWRWireAdapter();
        }

        @Override
        public Adapter caseI2CDevice(I2CDevice object) {
            return createI2CDeviceAdapter();
        }

        @Override
        public Adapter caseAbstractI2CConnector(AbstractI2CConnector object) {
            return createAbstractI2CConnectorAdapter();
        }

        @Override
        public Adapter caseI2CBus(I2CBus object) {
            return createI2CBusAdapter();
        }

        @Override
        public Adapter caseI2CSCLPin(I2CSCLPin object) {
            return createI2CSCLPinAdapter();
        }

        @Override
        public Adapter caseI2CSDAPin(I2CSDAPin object) {
            return createI2CSDAPinAdapter();
        }

        @Override
        public Adapter caseI2CGRDPin(I2CGRDPin object) {
            return createI2CGRDPinAdapter();
        }

        @Override
        public Adapter caseI2CPWRPin(I2CPWRPin object) {
            return createI2CPWRPinAdapter();
        }

        @Override
        public Adapter caseII2CUIDElement(II2CUIDElement object) {
            return createII2CUIDElementAdapter();
        }

        @Override
        public Adapter caseII2CNamedElement(II2CNamedElement object) {
            return createII2CNamedElementAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CBusSegment <em>I2C Bus Segment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CBusSegment
	 * @generated
	 */
    public Adapter createI2CBusSegmentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.AbstractI2CWire <em>Abstract I2C Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.AbstractI2CWire
	 * @generated
	 */
    public Adapter createAbstractI2CWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CSCLWire <em>I2CSCL Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CSCLWire
	 * @generated
	 */
    public Adapter createI2CSCLWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CSDAWire <em>I2CSDA Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CSDAWire
	 * @generated
	 */
    public Adapter createI2CSDAWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CGRDWire <em>I2CGRD Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CGRDWire
	 * @generated
	 */
    public Adapter createI2CGRDWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CPWRWire <em>I2CPWR Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CPWRWire
	 * @generated
	 */
    public Adapter createI2CPWRWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CDevice <em>I2C Device</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CDevice
	 * @generated
	 */
    public Adapter createI2CDeviceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.AbstractI2CConnector <em>Abstract I2C Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.AbstractI2CConnector
	 * @generated
	 */
    public Adapter createAbstractI2CConnectorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CBus <em>I2C Bus</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CBus
	 * @generated
	 */
    public Adapter createI2CBusAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CSCLPin <em>I2CSCL Pin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CSCLPin
	 * @generated
	 */
    public Adapter createI2CSCLPinAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CSDAPin <em>I2CSDA Pin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CSDAPin
	 * @generated
	 */
    public Adapter createI2CSDAPinAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CGRDPin <em>I2CGRD Pin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CGRDPin
	 * @generated
	 */
    public Adapter createI2CGRDPinAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.I2CPWRPin <em>I2CPWR Pin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.I2CPWRPin
	 * @generated
	 */
    public Adapter createI2CPWRPinAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.II2CUIDElement <em>II2CUID Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.II2CUIDElement
	 * @generated
	 */
    public Adapter createII2CUIDElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link net.sf.smbt.i2c.II2CNamedElement <em>II2C Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see net.sf.smbt.i2c.II2CNamedElement
	 * @generated
	 */
    public Adapter createII2CNamedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
