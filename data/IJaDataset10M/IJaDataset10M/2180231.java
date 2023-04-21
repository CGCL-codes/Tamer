package org.nakedobjects.object;

/**
 * Details the intial states of, and the labels for, the parameters for an
 * action method.
 */
public interface ActionParameterSet {

    public Object[] getDefaultParameterValues();

    public String[] getParameterLabels();

    public boolean[] getRequiredParameters();

    public void checkParameters(String name, NakedObjectSpecification requiredTypes[]);
}
