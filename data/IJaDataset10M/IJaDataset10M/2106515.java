package hu.cubussapiens.modembed.model.assembly.util;

import hu.cubussapiens.modembed.model.assembly.*;
import hu.cubussapiens.modembed.model.infrastructure.NamedElement;
import hu.cubussapiens.modembed.model.infrastructure.PackageElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see hu.cubussapiens.modembed.model.assembly.AssemblyPackage
 * @generated
 */
public class AssemblyAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static AssemblyPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssemblyAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = AssemblyPackage.eINSTANCE;
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
    protected AssemblySwitch<Adapter> modelSwitch = new AssemblySwitch<Adapter>() {

        @Override
        public Adapter caseInstructionSet(InstructionSet object) {
            return createInstructionSetAdapter();
        }

        @Override
        public Adapter caseInstruction(Instruction object) {
            return createInstructionAdapter();
        }

        @Override
        public Adapter caseField(Field object) {
            return createFieldAdapter();
        }

        @Override
        public Adapter caseSection(Section object) {
            return createSectionAdapter();
        }

        @Override
        public Adapter caseCode(Code object) {
            return createCodeAdapter();
        }

        @Override
        public Adapter caseParameter(Parameter object) {
            return createParameterAdapter();
        }

        @Override
        public Adapter caseNamedElement(NamedElement object) {
            return createNamedElementAdapter();
        }

        @Override
        public Adapter casePackageElement(PackageElement object) {
            return createPackageElementAdapter();
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
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.InstructionSet <em>Instruction Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.InstructionSet
	 * @generated
	 */
    public Adapter createInstructionSetAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.Instruction <em>Instruction</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.Instruction
	 * @generated
	 */
    public Adapter createInstructionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.Field <em>Field</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.Field
	 * @generated
	 */
    public Adapter createFieldAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.Section <em>Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.Section
	 * @generated
	 */
    public Adapter createSectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.Code <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.Code
	 * @generated
	 */
    public Adapter createCodeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.assembly.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.assembly.Parameter
	 * @generated
	 */
    public Adapter createParameterAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.infrastructure.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.infrastructure.NamedElement
	 * @generated
	 */
    public Adapter createNamedElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link hu.cubussapiens.modembed.model.infrastructure.PackageElement <em>Package Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see hu.cubussapiens.modembed.model.infrastructure.PackageElement
	 * @generated
	 */
    public Adapter createPackageElementAdapter() {
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
