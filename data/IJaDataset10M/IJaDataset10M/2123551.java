package com.keggview.application.model.workflow.workflow.util;

import com.keggview.application.datatypes.Alt;
import com.keggview.application.datatypes.Base;
import com.keggview.application.datatypes.Component;
import com.keggview.application.datatypes.Entry;
import com.keggview.application.datatypes.Graphics;
import com.keggview.application.datatypes.Pathway;
import com.keggview.application.datatypes.Product;
import com.keggview.application.datatypes.Reaction;
import com.keggview.application.datatypes.Relation;
import com.keggview.application.datatypes.Substrate;
import com.keggview.application.model.workflow.workflow.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.keggview.application.model.workflow.workflow.WorkflowPackage
 * @generated
 */
public class WorkflowAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static WorkflowPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public WorkflowAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = WorkflowPackage.eINSTANCE;
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
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WorkflowSwitch<Adapter> modelSwitch = new WorkflowSwitch<Adapter>() {

        @Override
        public Adapter caseBase(Base object) {
            return createBaseAdapter();
        }

        @Override
        public Adapter casePathway(Pathway object) {
            return createPathwayAdapter();
        }

        @Override
        public Adapter caseEntry(Entry object) {
            return createEntryAdapter();
        }

        @Override
        public Adapter caseAlt(Alt object) {
            return createAltAdapter();
        }

        @Override
        public Adapter caseGraphics(Graphics object) {
            return createGraphicsAdapter();
        }

        @Override
        public Adapter caseComponent(Component object) {
            return createComponentAdapter();
        }

        @Override
        public Adapter caseProduct(Product object) {
            return createProductAdapter();
        }

        @Override
        public Adapter caseReaction(Reaction object) {
            return createReactionAdapter();
        }

        @Override
        public Adapter caseSubstrate(Substrate object) {
            return createSubstrateAdapter();
        }

        @Override
        public Adapter caseRelation(Relation object) {
            return createRelationAdapter();
        }

        @Override
        public Adapter caseSubtype(Subtype object) {
            return createSubtypeAdapter();
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
	 * Creates a new adapter for an object of class '{@link Base <em>Base</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Base
	 * @generated
	 */
    public Adapter createBaseAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Pathway <em>Pathway</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Pathway
	 * @generated
	 */
    public Adapter createPathwayAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Entry <em>Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Entry
	 * @generated
	 */
    public Adapter createEntryAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Alt <em>Alt</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Alt
	 * @generated
	 */
    public Adapter createAltAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Graphics <em>Graphics</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Graphics
	 * @generated
	 */
    public Adapter createGraphicsAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Component
	 * @generated
	 */
    public Adapter createComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Product <em>Product</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Product
	 * @generated
	 */
    public Adapter createProductAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Reaction <em>Reaction</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Reaction
	 * @generated
	 */
    public Adapter createReactionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Substrate <em>Substrate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Substrate
	 * @generated
	 */
    public Adapter createSubstrateAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link Relation <em>Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see Relation
	 * @generated
	 */
    public Adapter createRelationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.keggview.application.model.workflow.workflow.Subtype <em>Subtype</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.keggview.application.model.workflow.workflow.Subtype
	 * @generated
	 */
    public Adapter createSubtypeAdapter() {
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
