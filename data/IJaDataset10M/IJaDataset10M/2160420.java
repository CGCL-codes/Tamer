package org.slasoi.monitoring.common.features.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.slasoi.monitoring.common.features.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.slasoi.monitoring.common.features.FeaturesPackage
 * @generated
 */
public class FeaturesAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static FeaturesPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeaturesAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = FeaturesPackage.eINSTANCE;
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
    protected FeaturesSwitch<Adapter> modelSwitch = new FeaturesSwitch<Adapter>() {

        @Override
        public Adapter caseMonitoringFeature(MonitoringFeature object) {
            return createMonitoringFeatureAdapter();
        }

        @Override
        public Adapter caseBasic(Basic object) {
            return createBasicAdapter();
        }

        @Override
        public Adapter caseFunction(Function object) {
            return createFunctionAdapter();
        }

        @Override
        public Adapter caseComponentMonitoringFeatures(ComponentMonitoringFeatures object) {
            return createComponentMonitoringFeaturesAdapter();
        }

        @Override
        public Adapter casePrimitive(Primitive object) {
            return createPrimitiveAdapter();
        }

        @Override
        public Adapter caseEvent(Event object) {
            return createEventAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.MonitoringFeature <em>Monitoring Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.MonitoringFeature
	 * @generated
	 */
    public Adapter createMonitoringFeatureAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.Basic <em>Basic</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.Basic
	 * @generated
	 */
    public Adapter createBasicAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.Function <em>Function</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.Function
	 * @generated
	 */
    public Adapter createFunctionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.ComponentMonitoringFeatures <em>Component Monitoring Features</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.ComponentMonitoringFeatures
	 * @generated
	 */
    public Adapter createComponentMonitoringFeaturesAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.Primitive <em>Primitive</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.Primitive
	 * @generated
	 */
    public Adapter createPrimitiveAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.slasoi.monitoring.common.features.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.slasoi.monitoring.common.features.Event
	 * @generated
	 */
    public Adapter createEventAdapter() {
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
