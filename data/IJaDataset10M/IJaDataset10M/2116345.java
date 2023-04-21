package org.fluid.commons.modeling.base.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.fluid.commons.modeling.base.AnnotatedElement;
import org.fluid.commons.modeling.base.BaseElement;
import org.fluid.commons.modeling.base.IDElement;
import org.fluid.commons.modeling.base.ModelRoot;
import org.fluid.commons.modeling.base.ModelingBasePackage;
import org.fluid.commons.modeling.base.NamedElement;
import org.fluid.commons.modeling.base.NamespaceContainer;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.fluid.commons.modeling.base.ModelingBasePackage
 * @generated
 */
public class ModelingBaseAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static ModelingBasePackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelingBaseAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ModelingBasePackage.eINSTANCE;
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
    protected ModelingBaseSwitch<Adapter> modelSwitch = new ModelingBaseSwitch<Adapter>() {

        @Override
        public Adapter caseNamedElement(NamedElement object) {
            return createNamedElementAdapter();
        }

        @Override
        public Adapter caseIDElement(IDElement object) {
            return createIDElementAdapter();
        }

        @Override
        public Adapter caseBaseElement(BaseElement object) {
            return createBaseElementAdapter();
        }

        @Override
        public Adapter caseNamespaceContainer(NamespaceContainer object) {
            return createNamespaceContainerAdapter();
        }

        @Override
        public Adapter caseAnnotatedElement(AnnotatedElement object) {
            return createAnnotatedElementAdapter();
        }

        @Override
        public Adapter caseModelRoot(ModelRoot object) {
            return createModelRootAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.NamedElement
	 * @generated
	 */
    public Adapter createNamedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.IDElement <em>ID Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.IDElement
	 * @generated
	 */
    public Adapter createIDElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.BaseElement <em>Base Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.BaseElement
	 * @generated
	 */
    public Adapter createBaseElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.NamespaceContainer <em>Namespace Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.NamespaceContainer
	 * @generated
	 */
    public Adapter createNamespaceContainerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.AnnotatedElement <em>Annotated Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.AnnotatedElement
	 * @generated
	 */
    public Adapter createAnnotatedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link org.fluid.commons.modeling.base.ModelRoot <em>Model Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.fluid.commons.modeling.base.ModelRoot
	 * @generated
	 */
    public Adapter createModelRootAdapter() {
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
