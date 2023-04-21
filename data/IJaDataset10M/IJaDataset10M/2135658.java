package com.ibm.realtime.flexotask;

/**
 * The interface that all Flexotasks must implement.  Flexotasks must also have a 0-argument constructor
 */
public interface Flexotask extends Stable {

    /**
	 * Provide an opportunity to initialize immediately after construction
	 * @param inputPorts the array of input ports, in declaration order.  May be zero length but will never be
	 * 	 null
	 * @param outputPorts the array of output ports, in declaration order.  May be zero length but will never be
	 * 	 null
	 * @param parameter the instantiation-time parameter for this Flexotask.  If the Flexotask is marked as 
	 *   weakly isolated this parameter is verified to be ref-immutable and is pinned and passed by reference.
	 *   Otherwise, this parameter is deep-cloned into the flexotask's private heap
	 */
    public void initialize(FlexotaskInputPort[] inputPorts, FlexotaskOutputPort[] outputPorts, Object parameter);

    /**
	 * Method that the scheduler calls on each Flexotask execution
	 */
    public void execute();
}
