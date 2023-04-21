package org.sonify.vm.intermediate.execution;

import java.util.Vector;
import org.sonify.vm.LineInformation;
import org.sonify.vm.execution.ExecutionStep;
import org.sonify.vm.intermediate.symbol.QualifiedNameDescriptor;
import org.sonify.vm.intermediate.symbol.TypeDescriptor;

/**
 *  A support class for helping to pass information to the StepFactory, which
 * will ultimately generate the CallStep for the execution of the program.
 * 
 * @author stefika
 */
public class CallInfo {

    /**
     * The register where a return value may ultimately be stored.
     */
    public int register;

    /**
     * Information regarding the line in which this is being called from.
     */
    public LineInformation location;

    public String locatedIn;

    /**
     * The registers where the arguments are being stored for this function.
     */
    public Vector<Integer> argumentRegisters;

    /**
     * A set of steps related to the expression being executed.
     */
    public Vector<ExecutionStep> argumentSteps;

    /**
     * The name of the method being called.
     */
    public String methodName;

    /**
     * The object having the method called upon it. If null,
     * a method is being called in the current class.
     */
    public QualifiedNameDescriptor variable;

    /**
     * A list of the types of the arguments.
     */
    public Vector<TypeDescriptor> argumentTypes;

    /**
     * The name of the class that is calling the method.
     */
    public String caller;

    /**
     * The name of the class that is having a method called upon.
     */
    public String callee;

    /**
     * True if this call is related to calling a function on an object
     * as opposed to calling a function from within one's class.
     */
    public boolean isObjectCall = false;

    /**
     * Determines if this was a call explicitly made on the "this" pointer.
     */
    public boolean isThisCall = false;
}
