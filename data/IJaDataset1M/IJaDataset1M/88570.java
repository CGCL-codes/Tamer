package org.uc3m.verbus.bpa;

import java.util.List;

public interface EntType extends BPABase {

    public void addVariable(Variable variable) throws BPAException;

    public List getVariables();

    public Variable getVariable(String name);
}
