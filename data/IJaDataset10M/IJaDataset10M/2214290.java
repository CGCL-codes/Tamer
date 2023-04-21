package tefkat.engine.executiontrace.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import tefkat.engine.executiontrace.model.ExecutiontraceFactory;
import tefkat.engine.executiontrace.model.ExecutiontracePackage;
import tefkat.engine.executiontrace.model.Trace;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExecutiontraceFactoryImpl extends EFactoryImpl implements ExecutiontraceFactory {

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ExecutiontraceFactory init() {
        try {
            ExecutiontraceFactory theExecutiontraceFactory = (ExecutiontraceFactory) EPackage.Registry.INSTANCE.getEFactory("http:///com/dstc/tefkat/TefkatTrace.ecore");
            if (theExecutiontraceFactory != null) {
                return theExecutiontraceFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ExecutiontraceFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutiontraceFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case ExecutiontracePackage.TRACE:
                return createTrace();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Trace createTrace() {
        TraceImpl trace = new TraceImpl();
        return trace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutiontracePackage getExecutiontracePackage() {
        return (ExecutiontracePackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ExecutiontracePackage getPackage() {
        return ExecutiontracePackage.eINSTANCE;
    }
}
