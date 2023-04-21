package org.javia.arity;

/**
   To evaluate CompiledFunctions from multiple threads in parallel,
   you need to create one EvalContext instance per thread,
   and pass it to the eval() methods of CompiledFunction.
 */
public class EvalContext {

    static final int MAX_STACK_SIZE = 128;

    double stackRe[] = new double[MAX_STACK_SIZE];

    final Complex stackComplex[] = new Complex[MAX_STACK_SIZE];

    int stackBase = 0;

    double args1[] = new double[1];

    double args2[] = new double[2];

    Complex args1c[];

    Complex args2c[];

    /** Constructs a new EvalContext, ready to be used with CompiledFunction.eval().
     */
    public EvalContext() {
        for (int i = 0; i < MAX_STACK_SIZE; ++i) {
            stackComplex[i] = new Complex();
        }
        args1c = new Complex[] { new Complex() };
        args2c = new Complex[] { new Complex(), new Complex() };
    }
}
