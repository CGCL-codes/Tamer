package eu.medeia.ecore.apmm.mm.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import eu.medeia.ecore.apmm.mm.ACDeviceMapping;
import eu.medeia.ecore.apmm.mm.ACHardwareMapping;
import eu.medeia.ecore.apmm.mm.InputIOsToPlantPort;
import eu.medeia.ecore.apmm.mm.MmPackage;
import eu.medeia.ecore.apmm.mm.OutputIOsToPlantPort;
import eu.medeia.ecore.apmm.mm.PlantPortComPortMapping;
import eu.medeia.ecore.apmm.mm.PlantPortIOPortMapping;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see eu.medeia.ecore.apmm.mm.MmPackage
 * @generated
 */
public class MmAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static MmPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MmAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = MmPackage.eINSTANCE;
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
    protected MmSwitch<Adapter> modelSwitch = new MmSwitch<Adapter>() {

        @Override
        public Adapter casePlantPortComPortMapping(PlantPortComPortMapping object) {
            return createPlantPortComPortMappingAdapter();
        }

        @Override
        public Adapter casePlantPortIOPortMapping(PlantPortIOPortMapping object) {
            return createPlantPortIOPortMappingAdapter();
        }

        @Override
        public Adapter caseACHardwareMapping(ACHardwareMapping object) {
            return createACHardwareMappingAdapter();
        }

        @Override
        public Adapter caseACDeviceMapping(ACDeviceMapping object) {
            return createACDeviceMappingAdapter();
        }

        @Override
        public Adapter caseInputIOsToPlantPort(InputIOsToPlantPort object) {
            return createInputIOsToPlantPortAdapter();
        }

        @Override
        public Adapter caseOutputIOsToPlantPort(OutputIOsToPlantPort object) {
            return createOutputIOsToPlantPortAdapter();
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
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.PlantPortComPortMapping <em>Plant Port Com Port Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.PlantPortComPortMapping
	 * @generated
	 */
    public Adapter createPlantPortComPortMappingAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.PlantPortIOPortMapping <em>Plant Port IO Port Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.PlantPortIOPortMapping
	 * @generated
	 */
    public Adapter createPlantPortIOPortMappingAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.ACHardwareMapping <em>AC Hardware Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.ACHardwareMapping
	 * @generated
	 */
    public Adapter createACHardwareMappingAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.ACDeviceMapping <em>AC Device Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.ACDeviceMapping
	 * @generated
	 */
    public Adapter createACDeviceMappingAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.InputIOsToPlantPort <em>Input IOs To Plant Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.InputIOsToPlantPort
	 * @generated
	 */
    public Adapter createInputIOsToPlantPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.mm.OutputIOsToPlantPort <em>Output IOs To Plant Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.mm.OutputIOsToPlantPort
	 * @generated
	 */
    public Adapter createOutputIOsToPlantPortAdapter() {
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
