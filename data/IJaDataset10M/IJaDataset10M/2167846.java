package siseor.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import siseor.Author;
import siseor.Container;
import siseor.Datatype;
import siseor.Description;
import siseor.DtComplex;
import siseor.DtInteger;
import siseor.DtString;
import siseor.Input;
import siseor.Link;
import siseor.Metadata;
import siseor.Operation;
import siseor.Output;
import siseor.Port;
import siseor.SiseorPackage;
import siseor.Tag;
import siseor.Visitable;
import siseor.Visitor;
import siseor.Wsdl;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see siseor.SiseorPackage
 * @generated
 */
public class SiseorAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static SiseorPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SiseorAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = SiseorPackage.eINSTANCE;
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
    protected SiseorSwitch<Adapter> modelSwitch = new SiseorSwitch<Adapter>() {

        @Override
        public Adapter caseContainer(Container object) {
            return createContainerAdapter();
        }

        @Override
        public Adapter caseOperation(Operation object) {
            return createOperationAdapter();
        }

        @Override
        public Adapter casePort(Port object) {
            return createPortAdapter();
        }

        @Override
        public Adapter caseOutput(Output object) {
            return createOutputAdapter();
        }

        @Override
        public Adapter caseInput(Input object) {
            return createInputAdapter();
        }

        @Override
        public Adapter caseLink(Link object) {
            return createLinkAdapter();
        }

        @Override
        public Adapter caseMetadata(Metadata object) {
            return createMetadataAdapter();
        }

        @Override
        public Adapter caseDescription(Description object) {
            return createDescriptionAdapter();
        }

        @Override
        public Adapter caseTag(Tag object) {
            return createTagAdapter();
        }

        @Override
        public Adapter caseAuthor(Author object) {
            return createAuthorAdapter();
        }

        @Override
        public Adapter caseWsdl(Wsdl object) {
            return createWsdlAdapter();
        }

        @Override
        public Adapter caseDatatype(Datatype object) {
            return createDatatypeAdapter();
        }

        @Override
        public Adapter caseDtString(DtString object) {
            return createDtStringAdapter();
        }

        @Override
        public Adapter caseDtInteger(DtInteger object) {
            return createDtIntegerAdapter();
        }

        @Override
        public Adapter caseDtComplex(DtComplex object) {
            return createDtComplexAdapter();
        }

        @Override
        public Adapter caseVisitor(Visitor object) {
            return createVisitorAdapter();
        }

        @Override
        public Adapter caseVisitable(Visitable object) {
            return createVisitableAdapter();
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
	 * Creates a new adapter for an object of class '{@link siseor.Container <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Container
	 * @generated
	 */
    public Adapter createContainerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Operation
	 * @generated
	 */
    public Adapter createOperationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Port
	 * @generated
	 */
    public Adapter createPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Output <em>Output</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Output
	 * @generated
	 */
    public Adapter createOutputAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Input <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Input
	 * @generated
	 */
    public Adapter createInputAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Link <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Link
	 * @generated
	 */
    public Adapter createLinkAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Metadata
	 * @generated
	 */
    public Adapter createMetadataAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Description <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Description
	 * @generated
	 */
    public Adapter createDescriptionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Tag <em>Tag</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Tag
	 * @generated
	 */
    public Adapter createTagAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Author <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Author
	 * @generated
	 */
    public Adapter createAuthorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Wsdl <em>Wsdl</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Wsdl
	 * @generated
	 */
    public Adapter createWsdlAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Datatype <em>Datatype</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Datatype
	 * @generated
	 */
    public Adapter createDatatypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.DtString <em>Dt String</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.DtString
	 * @generated
	 */
    public Adapter createDtStringAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.DtInteger <em>Dt Integer</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.DtInteger
	 * @generated
	 */
    public Adapter createDtIntegerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.DtComplex <em>Dt Complex</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.DtComplex
	 * @generated
	 */
    public Adapter createDtComplexAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Visitor <em>Visitor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Visitor
	 * @generated
	 */
    public Adapter createVisitorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link siseor.Visitable <em>Visitable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see siseor.Visitable
	 * @generated
	 */
    public Adapter createVisitableAdapter() {
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
