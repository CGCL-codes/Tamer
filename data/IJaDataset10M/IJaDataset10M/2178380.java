package com.rapidminer.operator;

import com.rapidminer.operator.ports.PortPairExtender;
import com.rapidminer.operator.ports.metadata.SubprocessTransformRule;

/**
 * A simple operator chain which can have an arbitrary number of inner
 * operators. The operators are subsequently applied and their output is used as
 * input for the succeeding operator. The input of the operator chain is used as
 * input for the first inner operator and the output of the last operator is
 * used as the output of the operator chain.
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class SimpleOperatorChain extends OperatorChain {

    protected PortPairExtender inputExtender = new PortPairExtender("in", getInputPorts(), getSubprocess(0).getInnerSources());

    protected PortPairExtender outputExtender = new PortPairExtender("out", getSubprocess(0).getInnerSinks(), getOutputPorts());

    /** Creates an empty operator chain. */
    public SimpleOperatorChain(OperatorDescription description) {
        this(description, "Nested Chain");
    }

    /** This constructor allows subclasses to change the subprocess' name. */
    protected SimpleOperatorChain(OperatorDescription description, String subProcessName) {
        super(description, subProcessName);
        inputExtender.start();
        outputExtender.start();
        getTransformer().addRule(inputExtender.makePassThroughRule());
        getTransformer().addRule(new SubprocessTransformRule(getSubprocess(0)));
        getTransformer().addRule(outputExtender.makePassThroughRule());
    }

    @Override
    public void doWork() throws OperatorException {
        clearAllInnerSinks();
        inputExtender.passDataThrough();
        super.doWork();
        outputExtender.passDataThrough();
    }
}
