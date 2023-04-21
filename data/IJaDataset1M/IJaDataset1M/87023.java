package org.ceno.model.util;

import org.ceno.model.*;
import org.ceno.model.BroadcastMessage;
import org.ceno.model.Developer;
import org.ceno.model.DeveloperResourceState;
import org.ceno.model.DeveloperResourceStates;
import org.ceno.model.Message;
import org.ceno.model.Messages;
import org.ceno.model.ModelPackage;
import org.ceno.model.MulticastMessage;
import org.ceno.model.Resource;
import org.ceno.model.UnicastMessage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.ceno.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static ModelPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ModelPackage.eINSTANCE;
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
    protected ModelSwitch<Adapter> modelSwitch = new ModelSwitch<Adapter>() {

        @Override
        public Adapter caseDeveloper(Developer object) {
            return createDeveloperAdapter();
        }

        @Override
        public Adapter caseResource(Resource object) {
            return createResourceAdapter();
        }

        @Override
        public Adapter caseDeveloperResourceState(DeveloperResourceState object) {
            return createDeveloperResourceStateAdapter();
        }

        @Override
        public Adapter caseDeveloperResourceStates(DeveloperResourceStates object) {
            return createDeveloperResourceStatesAdapter();
        }

        @Override
        public Adapter caseMessage(Message object) {
            return createMessageAdapter();
        }

        @Override
        public Adapter caseUnicastMessage(UnicastMessage object) {
            return createUnicastMessageAdapter();
        }

        @Override
        public Adapter caseMulticastMessage(MulticastMessage object) {
            return createMulticastMessageAdapter();
        }

        @Override
        public Adapter caseBroadcastMessage(BroadcastMessage object) {
            return createBroadcastMessageAdapter();
        }

        @Override
        public Adapter caseMessages(Messages object) {
            return createMessagesAdapter();
        }

        @Override
        public Adapter caseWorkbenchMessage(WorkbenchMessage object) {
            return createWorkbenchMessageAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.ceno.model.Developer <em>Developer</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.Developer
	 * @generated
	 */
    public Adapter createDeveloperAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.Resource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.Resource
	 * @generated
	 */
    public Adapter createResourceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.DeveloperResourceState <em>Developer Resource State</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.DeveloperResourceState
	 * @generated
	 */
    public Adapter createDeveloperResourceStateAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.DeveloperResourceStates <em>Developer Resource States</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.DeveloperResourceStates
	 * @generated
	 */
    public Adapter createDeveloperResourceStatesAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.Message <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.Message
	 * @generated
	 */
    public Adapter createMessageAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.UnicastMessage <em>Unicast Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.UnicastMessage
	 * @generated
	 */
    public Adapter createUnicastMessageAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.MulticastMessage <em>Multicast Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.MulticastMessage
	 * @generated
	 */
    public Adapter createMulticastMessageAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.BroadcastMessage <em>Broadcast Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.BroadcastMessage
	 * @generated
	 */
    public Adapter createBroadcastMessageAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.Messages <em>Messages</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.Messages
	 * @generated
	 */
    public Adapter createMessagesAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.ceno.model.WorkbenchMessage <em>Workbench Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.ceno.model.WorkbenchMessage
	 * @generated
	 */
    public Adapter createWorkbenchMessageAdapter() {
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
