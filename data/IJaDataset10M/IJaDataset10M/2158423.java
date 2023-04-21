package com.rapidminer.operator.learner.functions.kernel.gaussianprocess;

/**
 * Defines the interface for the various RVM-implementations
 * 
 * @author Piotr Kasprzak
 */
public abstract class GPBase {

    protected Problem problem;

    protected Parameter parameter;

    protected Model model = null;

    /** Constructor */
    public GPBase(Problem problem, Parameter parameter) {
        this.problem = problem;
        this.parameter = parameter;
    }

    /** Does the hard work of learning the model from the inputs */
    public abstract Model learn() throws Exception;

    /** Get the learned model */
    public Model getModel() {
        return model;
    }
}
