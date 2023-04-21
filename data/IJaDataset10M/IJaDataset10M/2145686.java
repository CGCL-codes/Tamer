package de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecGenerator;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCC;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCGC;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCMC;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestspecgenPackage;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionNotifier;
import de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionSubscriber;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestspecgenPackage
 * @generated
 */
public class TestspecgenAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static TestspecgenPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestspecgenAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = TestspecgenPackage.eINSTANCE;
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
    protected TestspecgenSwitch<Adapter> modelSwitch = new TestspecgenSwitch<Adapter>() {

        @Override
        public Adapter caseTestSpecGenerator(TestSpecGenerator object) {
            return createTestSpecGeneratorAdapter();
        }

        @Override
        public Adapter caseTestSpecsForCC(TestSpecsForCC object) {
            return createTestSpecsForCCAdapter();
        }

        @Override
        public Adapter caseTestSpecsForCGC(TestSpecsForCGC object) {
            return createTestSpecsForCGCAdapter();
        }

        @Override
        public Adapter caseTestSpecsForCMC(TestSpecsForCMC object) {
            return createTestSpecsForCMCAdapter();
        }

        @Override
        public Adapter caseTraversionSubscriber(TraversionSubscriber object) {
            return createTraversionSubscriberAdapter();
        }

        @Override
        public Adapter caseTraversionNotifier(TraversionNotifier object) {
            return createTraversionNotifierAdapter();
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
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecGenerator <em>Test Spec Generator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecGenerator
	 * @generated
	 */
    public Adapter createTestSpecGeneratorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCC <em>Test Specs For CC</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCC
	 * @generated
	 */
    public Adapter createTestSpecsForCCAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCGC <em>Test Specs For CGC</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCGC
	 * @generated
	 */
    public Adapter createTestSpecsForCGCAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCMC <em>Test Specs For CMC</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TestSpecsForCMC
	 * @generated
	 */
    public Adapter createTestSpecsForCMCAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionSubscriber <em>Traversion Subscriber</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionSubscriber
	 * @generated
	 */
    public Adapter createTraversionSubscriberAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionNotifier <em>Traversion Notifier</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.hu_berlin.sam.mmunit.coverage.mutation_analysis.testspecgen.TraversionNotifier
	 * @generated
	 */
    public Adapter createTraversionNotifierAdapter() {
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
