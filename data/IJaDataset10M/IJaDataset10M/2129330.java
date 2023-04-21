package hub.metrik.lang.dtmfcontrol.util;

import hub.metrik.lang.dtmfcontrol.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage
 * @generated
 */
public class DtmfcontrolAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static DtmfcontrolPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DtmfcontrolAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = DtmfcontrolPackage.eINSTANCE;
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
    protected DtmfcontrolSwitch<Adapter> modelSwitch = new DtmfcontrolSwitch<Adapter>() {

        @Override
        public Adapter caseApplication(Application object) {
            return createApplicationAdapter();
        }

        @Override
        public Adapter caseBlock(Block object) {
            return createBlockAdapter();
        }

        @Override
        public Adapter caseSayText(SayText object) {
            return createSayTextAdapter();
        }

        @Override
        public Adapter caseCommand(Command object) {
            return createCommandAdapter();
        }

        @Override
        public Adapter caseSayExternal(SayExternal object) {
            return createSayExternalAdapter();
        }

        @Override
        public Adapter caseListen(Listen object) {
            return createListenAdapter();
        }

        @Override
        public Adapter caseListenOption(ListenOption object) {
            return createListenOptionAdapter();
        }

        @Override
        public Adapter caseExternal(External object) {
            return createExternalAdapter();
        }

        @Override
        public Adapter caseGoto(Goto object) {
            return createGotoAdapter();
        }

        @Override
        public Adapter caseKeyPress(KeyPress object) {
            return createKeyPressAdapter();
        }

        @Override
        public Adapter caseOutput(Output object) {
            return createOutputAdapter();
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
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Application <em>Application</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Application
	 * @generated
	 */
    public Adapter createApplicationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Block <em>Block</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Block
	 * @generated
	 */
    public Adapter createBlockAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.SayText <em>Say Text</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.SayText
	 * @generated
	 */
    public Adapter createSayTextAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Command <em>Command</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Command
	 * @generated
	 */
    public Adapter createCommandAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.SayExternal <em>Say External</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.SayExternal
	 * @generated
	 */
    public Adapter createSayExternalAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Listen <em>Listen</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Listen
	 * @generated
	 */
    public Adapter createListenAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.ListenOption <em>Listen Option</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.ListenOption
	 * @generated
	 */
    public Adapter createListenOptionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.External <em>External</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.External
	 * @generated
	 */
    public Adapter createExternalAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Goto <em>Goto</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Goto
	 * @generated
	 */
    public Adapter createGotoAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.KeyPress <em>Key Press</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.KeyPress
	 * @generated
	 */
    public Adapter createKeyPressAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.dtmfcontrol.Output <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.dtmfcontrol.Output
	 * @generated
	 */
    public Adapter createOutputAdapter() {
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
