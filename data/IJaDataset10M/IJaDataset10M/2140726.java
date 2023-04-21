package edu.itba.ia.tp1.problem.binary2bcd.circuittree.component;

import edu.itba.ia.tp1.problem.binary2bcd.circuittree.logicstate.LogicState;

/**
 * This class represents the output of a given circuit
 * @author Jorge Goldman
 * 
 */
public class Output extends Wire {

    @Override
    public void operate() {
    }

    /**
	 * Gets output logic state value.
	 * @return Output logic state value.
	 */
    public LogicState getOutputValue() {
        return input;
    }
}
