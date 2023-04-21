package org.activebpel.rt.bpel.impl.expr;

import org.activebpel.rt.bpel.function.IAeFunctionFactory;
import org.activebpel.rt.bpel.impl.AeAbstractBpelObject;
import org.activebpel.rt.bpel.impl.IAeFaultFactory;
import org.activebpel.rt.xml.IAeNamespaceContext;

/**
 * This interface is used by expression runners during execution of expressions.  The
 * expression runner can interrogate the expression runner context for various 
 * pieces of information needed during execution of the expression.
 */
public interface IAeExpressionRunnerContext {

    /**
    * Returns the namespace context to use when executing an expression.
    */
    public IAeNamespaceContext getNamespaceContext();

    /**
    * Returns the function context to use when executing an expression.
    */
    public IAeFunctionFactory getFunctionContext();

    /**
    * Returns the object to use as the evaluation context.
    */
    public Object getEvaluationContext();

    /**
    * Returns interface responsible for resolving a context variable by name.
    */
    public IAeExpressionRunnerVariableResolver getVariableResolver();

    public AeAbstractBpelObject getAbstractBpelObject();

    /**
    * Getter for the fault factory
    */
    public IAeFaultFactory getFaultFactory();

    /**
    * Gets the BPEL namespace of the process that this expression is contained within.
    */
    public String getBpelNamespace();

    /**
    * Gets the URI of the language being used to execute the expression.
    */
    public String getLanguageURI();
}
