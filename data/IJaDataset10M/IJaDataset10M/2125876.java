package seqSamoa.services.order;

import framework.PID;

/**
 * The parameters of a response from FIFO
 */
public class CausalOrderResponseParameters extends FIFOResponseParameters {

    /**
     * Constructor
     * 
     * @param source
     *            the process that sends the message
     */
    public CausalOrderResponseParameters(PID source) {
        super(source);
    }
}
