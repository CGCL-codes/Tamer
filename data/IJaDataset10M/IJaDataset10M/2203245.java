package seqSamoa.services.fd;

import framework.libraries.serialization.TSet;

/**
 * The parameters of a call to FD
 */
public class FDCallParameters {

    /**
     * <CODE>startMonitoring</CODE> denotes a group of processes to monitor
     */
    public TSet startMonitoring;

    /**
     * <CODE>stopMonitoring</CODE> denotes a group of processes we do not want
     * to monitor anymore
     */
    public TSet stopMonitoring;

    /**
     * Constructor
     * 
     * @param start
     *            a group of processes to monitor
     * @param stop
     *            a group of processes we do not want to monitor anymore
     */
    public FDCallParameters(TSet start, TSet stop) {
        this.startMonitoring = start;
        this.stopMonitoring = stop;
    }
}
