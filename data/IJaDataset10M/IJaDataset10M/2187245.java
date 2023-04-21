package seqSamoa.services.rpt2pt;

import framework.PID;

/**
 * The parameters of a response to RPT2PT
 */
public class RPT2PTResponseParameters {

    /**
     * <CODE>pid</CODE> denotes the sender of the message delivered with
     * RPT2PT
     */
    public PID pid;

    /**
     * Constructor
     * 
     * @param pid
     *            the sender of the message delivered with RPT2PT
     */
    public RPT2PTResponseParameters(PID pid) {
        this.pid = pid;
    }
}
