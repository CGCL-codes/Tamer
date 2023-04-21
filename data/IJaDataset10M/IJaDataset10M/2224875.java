package eu.medeia.ecore.apmm.aliasModel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import eu.medeia.ecore.apmm.aliasModel.Alias;
import eu.medeia.ecore.apmm.aliasModel.AliasModelPackage;
import eu.medeia.ecore.apmm.aliasModel.DataAlias;
import eu.medeia.ecore.apmm.aliasModel.EventAlias;
import eu.medeia.ecore.apmm.aliasModel.IPortElementAlias;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see eu.medeia.ecore.apmm.aliasModel.AliasModelPackage
 * @generated
 */
public class AliasModelAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static AliasModelPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AliasModelAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = AliasModelPackage.eINSTANCE;
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
    protected AliasModelSwitch<Adapter> modelSwitch = new AliasModelSwitch<Adapter>() {

        @Override
        public Adapter caseAlias(Alias object) {
            return createAliasAdapter();
        }

        @Override
        public Adapter caseEventAlias(EventAlias object) {
            return createEventAliasAdapter();
        }

        @Override
        public Adapter caseDataAlias(DataAlias object) {
            return createDataAliasAdapter();
        }

        @Override
        public Adapter caseIPortElementAlias(IPortElementAlias object) {
            return createIPortElementAliasAdapter();
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
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.aliasModel.Alias <em>Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.aliasModel.Alias
	 * @generated
	 */
    public Adapter createAliasAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.aliasModel.EventAlias <em>Event Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.aliasModel.EventAlias
	 * @generated
	 */
    public Adapter createEventAliasAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.aliasModel.DataAlias <em>Data Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.aliasModel.DataAlias
	 * @generated
	 */
    public Adapter createDataAliasAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link eu.medeia.ecore.apmm.aliasModel.IPortElementAlias <em>IPort Element Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see eu.medeia.ecore.apmm.aliasModel.IPortElementAlias
	 * @generated
	 */
    public Adapter createIPortElementAliasAdapter() {
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
