package edu.itba.ia.tp1.problem.binary2bcd.circuittree.logicstate;

/**
 * This class represents a three-state logic state. Used in circuit inputs and
 * outputs.
 * 
 * @author Mart�n A. Heras
 * 
 */
public abstract class LogicState {

    /**
	 * Indicates whether this logic states is on.
	 * @return <code>true</code> if it is on; otherwise <code>false</code>.
	 */
    public abstract boolean isOn();

    /**
	 * Indicates whether this logic states is off.
	 * @return <code>true</code> if it is off; otherwise <code>false</code>.
	 */
    public abstract boolean isOff();

    /**
	 * Indicates whether this logic states is not ready.
	 * @return <code>true</code> if it is not ready; otherwise <code>false</code>.
	 */
    public abstract boolean isNotReady();
}
