package de.fraunhofer.fokus.cttcn.util;

import de.fraunhofer.fokus.cttcn.*;
import hub.metrik.lang.eprovide.debuggingstate.LabeledElement;
import hub.metrik.lang.eprovide.debuggingstate.MActivationFrame;
import hub.metrik.lang.eprovide.debuggingstate.MConcurrencyContext;
import hub.metrik.lang.eprovide.debuggingstate.MProgramContext;
import hub.metrik.lang.eprovide.debuggingstate.MVariable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import de.fraunhofer.fokus.cttcn.Assignment;
import de.fraunhofer.fokus.cttcn.Blck;
import de.fraunhofer.fokus.cttcn.BlckContainer;
import de.fraunhofer.fokus.cttcn.BooleanStreamValue;
import de.fraunhofer.fokus.cttcn.Cont;
import de.fraunhofer.fokus.cttcn.CttcnPackage;
import de.fraunhofer.fokus.cttcn.DefaultTrans;
import de.fraunhofer.fokus.cttcn.DottedTrans;
import de.fraunhofer.fokus.cttcn.Guard;
import de.fraunhofer.fokus.cttcn.GuardedTrans;
import de.fraunhofer.fokus.cttcn.InputStreamVar;
import de.fraunhofer.fokus.cttcn.IntStreamValue;
import de.fraunhofer.fokus.cttcn.Inv;
import de.fraunhofer.fokus.cttcn.OutputStreamVar;
import de.fraunhofer.fokus.cttcn.Par;
import de.fraunhofer.fokus.cttcn.Predicate;
import de.fraunhofer.fokus.cttcn.Seq;
import de.fraunhofer.fokus.cttcn.SimpleDataType;
import de.fraunhofer.fokus.cttcn.StreamValue;
import de.fraunhofer.fokus.cttcn.StreamVar;
import de.fraunhofer.fokus.cttcn.Test;
import de.fraunhofer.fokus.cttcn.Trans;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.fraunhofer.fokus.cttcn.CttcnPackage
 * @generated
 */
public class CttcnAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static CttcnPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CttcnAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = CttcnPackage.eINSTANCE;
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
    protected CttcnSwitch<Adapter> modelSwitch = new CttcnSwitch<Adapter>() {

        @Override
        public Adapter caseBlck(Blck object) {
            return createBlckAdapter();
        }

        @Override
        public Adapter caseTrans(Trans object) {
            return createTransAdapter();
        }

        @Override
        public Adapter caseGuard(Guard object) {
            return createGuardAdapter();
        }

        @Override
        public Adapter caseInv(Inv object) {
            return createInvAdapter();
        }

        @Override
        public Adapter caseAssignment(Assignment object) {
            return createAssignmentAdapter();
        }

        @Override
        public Adapter caseStreamVar(StreamVar object) {
            return createStreamVarAdapter();
        }

        @Override
        public Adapter caseCont(Cont object) {
            return createContAdapter();
        }

        @Override
        public Adapter casePar(Par object) {
            return createParAdapter();
        }

        @Override
        public Adapter caseSeq(Seq object) {
            return createSeqAdapter();
        }

        @Override
        public Adapter caseGuardedTrans(GuardedTrans object) {
            return createGuardedTransAdapter();
        }

        @Override
        public Adapter caseDefaultTrans(DefaultTrans object) {
            return createDefaultTransAdapter();
        }

        @Override
        public Adapter caseDottedTrans(DottedTrans object) {
            return createDottedTransAdapter();
        }

        @Override
        public Adapter caseBlckContainer(BlckContainer object) {
            return createBlckContainerAdapter();
        }

        @Override
        public Adapter caseTest(Test object) {
            return createTestAdapter();
        }

        @Override
        public Adapter casePredicate(Predicate object) {
            return createPredicateAdapter();
        }

        @Override
        public Adapter caseSimpleDataType(SimpleDataType object) {
            return createSimpleDataTypeAdapter();
        }

        @Override
        public Adapter caseIntStreamValue(IntStreamValue object) {
            return createIntStreamValueAdapter();
        }

        @Override
        public Adapter caseBooleanStreamValue(BooleanStreamValue object) {
            return createBooleanStreamValueAdapter();
        }

        @Override
        public Adapter caseStreamValue(StreamValue object) {
            return createStreamValueAdapter();
        }

        @Override
        public Adapter caseInputStreamVar(InputStreamVar object) {
            return createInputStreamVarAdapter();
        }

        @Override
        public Adapter caseOutputStreamVar(OutputStreamVar object) {
            return createOutputStreamVarAdapter();
        }

        @Override
        public Adapter caseLabeledElement(LabeledElement object) {
            return createLabeledElementAdapter();
        }

        @Override
        public Adapter caseMVariable(MVariable object) {
            return createMVariableAdapter();
        }

        @Override
        public Adapter caseMProgramContext(MProgramContext object) {
            return createMProgramContextAdapter();
        }

        @Override
        public Adapter caseMConcurrencyContext(MConcurrencyContext object) {
            return createMConcurrencyContextAdapter();
        }

        @Override
        public Adapter caseMActivationFrame(MActivationFrame object) {
            return createMActivationFrameAdapter();
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
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Blck <em>Blck</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Blck
	 * @generated
	 */
    public Adapter createBlckAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Trans <em>Trans</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Trans
	 * @generated
	 */
    public Adapter createTransAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Guard <em>Guard</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Guard
	 * @generated
	 */
    public Adapter createGuardAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Inv <em>Inv</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Inv
	 * @generated
	 */
    public Adapter createInvAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Assignment <em>Assignment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Assignment
	 * @generated
	 */
    public Adapter createAssignmentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.StreamVar <em>Stream Var</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.StreamVar
	 * @generated
	 */
    public Adapter createStreamVarAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Cont <em>Cont</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Cont
	 * @generated
	 */
    public Adapter createContAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Par <em>Par</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Par
	 * @generated
	 */
    public Adapter createParAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Seq <em>Seq</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Seq
	 * @generated
	 */
    public Adapter createSeqAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.GuardedTrans <em>Guarded Trans</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.GuardedTrans
	 * @generated
	 */
    public Adapter createGuardedTransAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.DefaultTrans <em>Default Trans</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.DefaultTrans
	 * @generated
	 */
    public Adapter createDefaultTransAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.DottedTrans <em>Dotted Trans</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.DottedTrans
	 * @generated
	 */
    public Adapter createDottedTransAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.BlckContainer <em>Blck Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.BlckContainer
	 * @generated
	 */
    public Adapter createBlckContainerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Test <em>Test</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Test
	 * @generated
	 */
    public Adapter createTestAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.Predicate <em>Predicate</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.Predicate
	 * @generated
	 */
    public Adapter createPredicateAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.SimpleDataType <em>Simple Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.SimpleDataType
	 * @generated
	 */
    public Adapter createSimpleDataTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.IntStreamValue <em>Int Stream Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.IntStreamValue
	 * @generated
	 */
    public Adapter createIntStreamValueAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.BooleanStreamValue <em>Boolean Stream Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.BooleanStreamValue
	 * @generated
	 */
    public Adapter createBooleanStreamValueAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.StreamValue <em>Stream Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.StreamValue
	 * @generated
	 */
    public Adapter createStreamValueAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.InputStreamVar <em>Input Stream Var</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.InputStreamVar
	 * @generated
	 */
    public Adapter createInputStreamVarAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.fokus.cttcn.OutputStreamVar <em>Output Stream Var</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.fokus.cttcn.OutputStreamVar
	 * @generated
	 */
    public Adapter createOutputStreamVarAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.eprovide.debuggingstate.LabeledElement <em>Labeled Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.eprovide.debuggingstate.LabeledElement
	 * @generated
	 */
    public Adapter createLabeledElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.eprovide.debuggingstate.MVariable <em>MVariable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.eprovide.debuggingstate.MVariable
	 * @generated
	 */
    public Adapter createMVariableAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.eprovide.debuggingstate.MProgramContext <em>MProgram Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.eprovide.debuggingstate.MProgramContext
	 * @generated
	 */
    public Adapter createMProgramContextAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.eprovide.debuggingstate.MConcurrencyContext <em>MConcurrency Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.eprovide.debuggingstate.MConcurrencyContext
	 * @generated
	 */
    public Adapter createMConcurrencyContextAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hub.metrik.lang.eprovide.debuggingstate.MActivationFrame <em>MActivation Frame</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hub.metrik.lang.eprovide.debuggingstate.MActivationFrame
	 * @generated
	 */
    public Adapter createMActivationFrameAdapter() {
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
